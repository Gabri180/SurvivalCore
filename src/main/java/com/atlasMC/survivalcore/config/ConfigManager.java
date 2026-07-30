package com.atlasMC.survivalcore.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Carga todos los YAML de resources/ a plugins/SurvivalCore/ y expone
 * acceso unificado a sus valores.
 */
public class ConfigManager {

    private static final String[] CONFIG_FILES = {
            "config.yml",
            "messages_es.yml",
            "jobs.yml",
            "missions.yml",
            "skills.yml",
            "bosses.yml",
            "seasons.yml",
            "claims.yml"
    };

    private final JavaPlugin plugin;
    private final Map<String, FileConfiguration> configs = new LinkedHashMap<>();

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadAll() {
        configs.clear();
        for (String fileName : CONFIG_FILES) {
            configs.put(fileName, loadOrCreate(fileName));
        }
    }

    public void reloadAll() {
        loadAll();
    }

    public void loadRecursive(String dir) {
        File folder = new File(plugin.getDataFolder(), dir);
        if (!folder.exists()) folder.mkdirs();
        loadYamlsFromFolder(folder, "");
    }

    private void loadYamlsFromFolder(File folder, String prefix) {
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                loadYamlsFromFolder(f, prefix + f.getName() + "/");
            } else if (f.getName().endsWith(".yml")) {
                String key = prefix + f.getName();
                try {
                    configs.put(key, YamlConfiguration.loadConfiguration(f));
                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING, "Error cargando " + key, e);
                }
            }
        }
    }

    private FileConfiguration loadOrCreate(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        try (InputStream defaultStream = plugin.getResource(fileName)) {
            if (defaultStream != null) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
                config.setDefaults(defaultConfig);
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "No se pudo cargar el default de " + fileName, e);
        }

        return config;
    }

    /**
     * Busca un path en todos los configs cargados, devolviendo el primer match.
     */
    public Object get(String path) {
        for (FileConfiguration config : configs.values()) {
            if (config.contains(path)) {
                return config.get(path);
            }
        }
        return null;
    }

    public FileConfiguration getConfig(String filename) {
        FileConfiguration config = configs.get(filename);
        if (config == null) {
            throw new IllegalArgumentException("Config no registrado: " + filename);
        }
        return config;
    }

    public int getInt(String path, int def) {
        Object value = get(path);
        return value instanceof Number ? ((Number) value).intValue() : def;
    }

    public String getString(String path, String def) {
        Object value = get(path);
        return value != null ? value.toString() : def;
    }

    public List<?> getList(String path) {
        Object value = get(path);
        return value instanceof List<?> list ? list : List.of();
    }
}
