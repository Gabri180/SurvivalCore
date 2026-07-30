package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.PlayerRepository;
import com.atlasMC.survivalcore.models.PlayerProfile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Carga el {@link PlayerProfile} a cache al entrar y lo persiste/limpia al salir.
 * Columna vertebral que Jobs/Skills/PvP/Clanes consumen via {@link PlayerCache}.
 */
public class PlayerDataListener implements Listener {

    private final PlayerCache playerCache;
    private final PlayerRepository playerRepository;
    private final FileConfiguration config;

    public PlayerDataListener(PlayerCache playerCache, PlayerRepository playerRepository, FileConfiguration config) {
        this.playerCache = playerCache;
        this.playerRepository = playerRepository;
        this.config = config;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        playerCache.getOrLoad(player.getUniqueId(), profile -> {
            if (profile == null) {
                PlayerProfile fresh = PlayerProfile.builder()
                        .uuid(player.getUniqueId())
                        .name(player.getName())
                        .money(config.getLong("economy.starting-money", 1000))
                        .premiumMoney(config.getLong("economy.starting-premium-money", 0))
                        .rank("DEFAULT")
                        .prestige(0)
                        .lastLogin(System.currentTimeMillis())
                        .build();
                playerCache.put(player.getUniqueId(), fresh);
                playerRepository.insertNewPlayer(fresh, id -> fresh.setPlayerId(id));
            } else {
                profile.setName(player.getName());
                profile.setLastLogin(System.currentTimeMillis());
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        var uuid = event.getPlayer().getUniqueId();
        PlayerProfile profile = playerCache.get(uuid);
        if (profile != null) {
            playerRepository.savePlayer(profile);
        }
        playerCache.remove(uuid);
    }
}
