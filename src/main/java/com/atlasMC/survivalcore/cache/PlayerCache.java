package com.atlasMC.survivalcore.cache;

import com.atlasMC.survivalcore.db.PlayerRepository;
import com.atlasMC.survivalcore.models.PlayerProfile;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Cache en memoria de {@link PlayerProfile}, respaldada por {@link PlayerRepository}.
 */
public class PlayerCache {

    private final Map<UUID, PlayerProfile> cache = new ConcurrentHashMap<>();
    private final PlayerRepository playerRepository;

    public PlayerCache(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public PlayerProfile get(UUID uuid) {
        return cache.get(uuid);
    }

    /**
     * Devuelve el perfil cacheado inmediatamente, o dispara una carga async
     * desde base de datos y entrega el resultado por callback cuando termine.
     */
    public void getOrLoad(UUID uuid, Consumer<PlayerProfile> callback) {
        PlayerProfile cached = cache.get(uuid);
        if (cached != null) {
            callback.accept(cached);
            return;
        }

        playerRepository.loadPlayer(uuid, profile -> {
            if (profile != null) {
                cache.put(uuid, profile);
            }
            callback.accept(profile);
        });
    }

    public void put(UUID uuid, PlayerProfile profile) {
        cache.put(uuid, profile);
    }

    public void remove(UUID uuid) {
        cache.remove(uuid);
    }

    public void clear() {
        cache.clear();
    }

    public boolean isCached(UUID uuid) {
        return cache.containsKey(uuid);
    }

    /**
     * Busca el UUID de un jugador online a partir de su id de BD (players.id).
     * Solo encuentra jugadores actualmente cacheados (online). Util para
     * notificaciones en tiempo real (ej. dueno de un claim bajo ataque).
     */
    public UUID findUuidByPlayerId(long playerId) {
        for (PlayerProfile profile : cache.values()) {
            if (profile.getPlayerId() == playerId) {
                return profile.getUuid();
            }
        }
        return null;
    }
}
