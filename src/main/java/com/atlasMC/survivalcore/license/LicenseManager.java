package com.atlasMC.survivalcore.license;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LicenseManager {

    private final JavaPlugin plugin;
    private String licenseKey;
    private License currentLicense;
    private String serverUuid;
    private String licenseServerUrl = "https://your-vps-ip/api/licenses";
    private boolean validating = false;
    private int validationFailCount = 0;
    private static final int MAX_OFFLINE_DAYS = 7;

    public LicenseManager(JavaPlugin plugin, String licenseKey) {
        this.plugin = plugin;
        this.licenseKey = licenseKey;
        this.serverUuid = generateServerUuid();
    }

    public void setLicenseServerUrl(String url) {
        this.licenseServerUrl = url;
    }

    public void initialize() {
        Bukkit.getLogger().info("🔐 Inicializando sistema de licencias...");

        // Validar licencia al startup
        validateLicense();

        // Validar cada 24 horas
        new BukkitRunnable() {
            @Override
            public void run() {
                validateLicense();
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20 * 60 * 60 * 24); // Cada 24h
    }

    public synchronized void validateLicense() {
        if (validating) return;
        validating = true;

        try {
            // Intentar validar online
            License license = validateOnline();

            if (license != null && license.isValid()) {
                this.currentLicense = license;
                this.validationFailCount = 0;
                Bukkit.getLogger().info("✅ Licencia válida: " + license.getTier());
                return;
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("⚠ No se pudo validar licencia online: " + e.getMessage());
        }

        // Si online falló, usar cache local
        License cachedLicense = loadCachedLicense();
        if (cachedLicense != null && !cachedLicense.isExpired()) {
            this.currentLicense = cachedLicense;
            this.validationFailCount++;

            if (validationFailCount > MAX_OFFLINE_DAYS) {
                Bukkit.getLogger().severe("❌ Licencia expirada. Por favor, reconectar a internet.");
                disablePlugin();
            }
            return;
        }

        // No hay licencia válida
        Bukkit.getLogger().severe("❌ LICENCIA INVÁLIDA O EXPIRADA");
        disablePlugin();
    }

    private License validateOnline() throws Exception {
        URL url = new URL(licenseServerUrl + "/validate");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        String body = String.format(
                "{\"key\":\"%s\",\"serverUuid\":\"%s\",\"playerCount\":%d}",
                licenseKey,
                serverUuid,
                Bukkit.getOnlinePlayers().size()
        );

        try (var os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            // Parse response (JSON simple)
            String response = new String(conn.getInputStream().readAllBytes());
            License license = parseJsonToLicense(response);

            // Cache la licencia
            cacheLicense(license);
            return license;
        }

        return null;
    }

    public boolean isLicenseValid() {
        return currentLicense != null && currentLicense.isValid();
    }

    public License getLicense() {
        return currentLicense;
    }

    public boolean canUseFeature(String tier) {
        if (!isLicenseValid()) return false;
        if (currentLicense.getTier().equals("LIFETIME")) return true;

        return switch (currentLicense.getTier()) {
            case "ENTERPRISE" -> true;
            case "PROFESSIONAL" -> !tier.equals("ENTERPRISE");
            case "STARTER" -> tier.equals("STARTER");
            default -> false;
        };
    }

    private String generateServerUuid() {
        try {
            // Obtener MAC address para hardware locking
            NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] mac = ni.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (byte b : mac) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        } catch (Exception e) {
            // Fallback: usar hostname + timestamp
            return Bukkit.getServer().getMotd() + System.currentTimeMillis();
        }
    }

    private void cacheLicense(License license) {
        File cacheFile = new File(plugin.getDataFolder(), ".license_cache");
        try (FileWriter fw = new FileWriter(cacheFile)) {
            fw.write(licenseToJson(license));
        } catch (IOException e) {
            Bukkit.getLogger().warning("No se pudo cachear licencia: " + e.getMessage());
        }
    }

    private License loadCachedLicense() {
        File cacheFile = new File(plugin.getDataFolder(), ".license_cache");
        if (!cacheFile.exists()) return null;

        try {
            String json = new String(java.nio.file.Files.readAllBytes(cacheFile.toPath()));
            return parseJsonToLicense(json);
        } catch (IOException e) {
            return null;
        }
    }

    private License parseJsonToLicense(String json) {
        // Simple JSON parser (sin dependencias)
        License license = new License();
        license.setKey(extractJsonField(json, "key"));
        license.setTier(extractJsonField(json, "tier"));
        license.setServerUuid(extractJsonField(json, "serverUuid"));
        license.setMaxPlayers(Integer.parseInt(extractJsonField(json, "maxPlayers")));
        license.setMaxServers(Integer.parseInt(extractJsonField(json, "maxServers")));
        license.setActive(Boolean.parseBoolean(extractJsonField(json, "active")));
        return license;
    }

    private String licenseToJson(License license) {
        return String.format(
                "{\"key\":\"%s\",\"tier\":\"%s\",\"serverUuid\":\"%s\",\"maxPlayers\":%d,\"maxServers\":%d,\"active\":%s}",
                license.getKey(),
                license.getTier(),
                license.getServerUuid(),
                license.getMaxPlayers(),
                license.getMaxServers(),
                license.isActive()
        );
    }

    private String extractJsonField(String json, String field) {
        String pattern = "\"" + field + "\":\"?([^,}\"]+)\"?";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(json);
        return matcher.find() ? matcher.group(1) : "";
    }

    private void disablePlugin() {
        Bukkit.getLogger().severe("🔐 Plugin deshabilitado por licencia inválida");
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
}
