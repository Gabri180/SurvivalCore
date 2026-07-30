package com.atlasMC.survivalcore.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Maneja el pool de conexiones MySQL (HikariCP) y expone operaciones async
 * para que ningun query bloquee el hilo principal del servidor.
 */
public class DatabaseManager {

    private final JavaPlugin plugin;
    private HikariDataSource dataSource;
    private ExecutorService asyncExecutor;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        FileConfiguration cfg = plugin.getConfig();

        HikariConfig hikariConfig = new HikariConfig();
        String host = cfg.getString("database.host", "localhost");
        int port = cfg.getInt("database.port", 3306);
        String database = cfg.getString("database.database", "survivalcore");
        boolean useSSL = cfg.getBoolean("database.useSSL", false);

        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database
                + "?useSSL=" + useSSL + "&autoReconnect=true&characterEncoding=utf8";

        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(cfg.getString("database.user", "root"));
        hikariConfig.setPassword(cfg.getString("database.password", ""));
        hikariConfig.setMaximumPoolSize(cfg.getInt("database.pool.maximum-pool-size", 20));
        hikariConfig.setMinimumIdle(cfg.getInt("database.pool.minimum-idle", 5));
        hikariConfig.setConnectionTimeout(cfg.getLong("database.pool.connection-timeout-ms", 30000));
        hikariConfig.setIdleTimeout(cfg.getLong("database.pool.idle-timeout-ms", 600000));
        hikariConfig.setMaxLifetime(cfg.getLong("database.pool.max-lifetime-ms", 1800000));
        hikariConfig.setPoolName("SurvivalCore-Pool");

        this.dataSource = new HikariDataSource(hikariConfig);
        this.asyncExecutor = Executors.newFixedThreadPool(
                Math.max(2, cfg.getInt("database.pool.maximum-pool-size", 20) / 4));
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Ejecuta el schema inicial (CREATE TABLE IF NOT EXISTS ...) contra la BD.
     */
    public void initializeTables() {
        String schema = readSchemaFile();
        if (schema.isBlank()) {
            plugin.getLogger().warning("No se encontro InitialSchema.sql, saltando creacion de tablas.");
            return;
        }

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            int failures = 0;
            for (String rawStatement : schema.split(";")) {
                String sql = rawStatement.trim();
                if (sql.isEmpty()) {
                    continue;
                }
                try {
                    statement.execute(sql);
                } catch (SQLException e) {
                    failures++;
                    plugin.getLogger().warning("Sentencia de schema fallida (se continua con el resto): " + e.getMessage());
                }
            }
            if (failures == 0) {
                plugin.getLogger().info("Schema de base de datos verificado/creado correctamente.");
            } else {
                plugin.getLogger().warning("Schema inicializado con " + failures + " sentencia(s) fallida(s). Revisa el log.");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error inicializando el schema de base de datos: " + e.getMessage());
        }
    }

    private String readSchemaFile() {
        try (InputStream stream = plugin.getResource("db/migrations/InitialSchema.sql")) {
            if (stream == null) {
                return "";
            }
            StringBuilder builder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().startsWith("--")) {
                        builder.append(line).append('\n');
                    }
                }
            }
            return builder.toString();
        } catch (IOException e) {
            plugin.getLogger().severe("Error leyendo InitialSchema.sql: " + e.getMessage());
            return "";
        }
    }

    /**
     * Ejecuta un UPDATE/INSERT/DELETE de forma asincrona. El callback se
     * invoca en el hilo principal con el numero de filas afectadas.
     */
    public void executeAsync(String sql, Consumer<Integer> callback, Object... params) {
        executeAsync(sql, callback, null, params);
    }

    /**
     * Igual que {@link #executeAsync(String, Consumer, Object...)} pero permite
     * recibir el error en el hilo principal para avisar al jugador con un
     * mensaje claro en vez de dejar solo un stacktrace en consola.
     */
    public void executeAsync(String sql, Consumer<Integer> callback, Consumer<Throwable> onError, Object... params) {
        asyncExecutor.submit(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                bindParams(statement, params);
                int affected = statement.executeUpdate();
                if (callback != null) {
                    runOnMainThread(() -> callback.accept(affected));
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("Error ejecutando query async: " + e.getMessage());
                if (onError != null) {
                    runOnMainThread(() -> onError.accept(e));
                }
            }
        });
    }

    /**
     * INSERT asincrono que devuelve la clave autogenerada (util para crear
     * clanes, claims, subastas, bounties, etc. y obtener su id real de BD).
     */
    public void executeInsertAsync(String sql, Consumer<Long> onGeneratedId, Consumer<Throwable> onError, Object... params) {
        asyncExecutor.submit(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                bindParams(statement, params);
                statement.executeUpdate();
                long generatedId = 0L;
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        generatedId = keys.getLong(1);
                    }
                }
                long finalId = generatedId;
                if (onGeneratedId != null) {
                    runOnMainThread(() -> onGeneratedId.accept(finalId));
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("Error insertando de forma async: " + e.getMessage());
                if (onError != null) {
                    runOnMainThread(() -> onError.accept(e));
                }
            }
        });
    }

    /**
     * Ejecuta un SELECT de forma asincrona, mapeando cada fila con {@code mapper}.
     * El callback se invoca en el hilo principal con la lista de resultados.
     */
    public <T> void queryAsync(String sql, Function<ResultSet, T> mapper, Consumer<List<T>> callback, Object... params) {
        queryAsync(sql, mapper, callback, null, params);
    }

    /**
     * Igual que {@link #queryAsync(String, Function, Consumer, Object...)} pero
     * permite recibir el error en el hilo principal para avisar al jugador.
     */
    public <T> void queryAsync(String sql, Function<ResultSet, T> mapper, Consumer<List<T>> callback,
                                Consumer<Throwable> onError, Object... params) {
        asyncExecutor.submit(() -> {
            List<T> results = new ArrayList<>();
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                bindParams(statement, params);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add(mapper.apply(resultSet));
                    }
                }
                if (callback != null) {
                    runOnMainThread(() -> callback.accept(results));
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("Error consultando BD async: " + e.getMessage());
                if (onError != null) {
                    runOnMainThread(() -> onError.accept(e));
                }
            }
        });
    }

    private void bindParams(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }

    private void runOnMainThread(Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public void close() {
        if (asyncExecutor != null) {
            asyncExecutor.shutdown();
        }
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
