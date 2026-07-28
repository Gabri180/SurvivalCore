package com.atlasMC.survivalcore.license;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class LicenseConfig {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    public LicenseConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "license.yml");
        loadConfig();
    }

    private void loadConfig() {
        if (!configFile.exists()) {
            plugin.saveResource("license.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void reload() {
        loadConfig();
    }

    public String getLicenseKey() {
        return config.getString("license-key", "YOUR_LICENSE_KEY_HERE");
    }

    public String getLicenseServerUrl() {
        return config.getString("license-server-url", "http://localhost:3000/api/licenses/validate");
    }

    public int getValidationIntervalHours() {
        return config.getInt("validation.interval-hours", 24);
    }

    public int getOfflineGracePeriodDays() {
        return config.getInt("validation.offline-grace-period-days", 7);
    }

    public int getConnectionTimeoutSeconds() {
        return config.getInt("validation.connection-timeout", 5);
    }

    public int getMaxPlayersForTier(String tier) {
        return config.getInt("tiers." + tier + ".max-players", 50);
    }

    public int getMaxServersForTier(String tier) {
        return config.getInt("tiers." + tier + ".max-servers", 1);
    }

    public String getMessageInvalidLicense() {
        return config.getString("messages.invalid-license", "§c❌ Licencia inválida o expirada.");
    }

    public String getMessageLicenseExpired() {
        return config.getString("messages.license-expired", "§c❌ Tu licencia expiró.");
    }

    public String getMessageValidationFailed() {
        return config.getString("messages.validation-failed", "§7⚠ No se pudo validar la licencia.");
    }

    public String getMessageFeatureLocked() {
        return config.getString("messages.feature-locked", "§c❌ Esta feature está bloqueada.");
    }
}
