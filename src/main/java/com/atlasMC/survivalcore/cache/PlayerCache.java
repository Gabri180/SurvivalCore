package com.atlasMC.survivalcore.cache;

import com.atlasMC.survivalcore.db.PlayerRepository;
import com.atlasMC.survivalcore.models.PlayerProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Cache en memoria de {@link PlayerProfile}, respaldada por {@link PlayerRepository}.
 * v1.0.18+: Incluye expiry automático y operaciones batch.
 */
public class PlayerCache {

    private final Map<UUID, PlayerProfile> cache = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastAccess = new ConcurrentHashMap<>();
    private final PlayerRepository playerRepository;
    private final long expiryTimeMs;

    public PlayerCache(PlayerRepository playerRepository, long expiryTimeMs) {
        this.playerRepository = playerRepository;
        this.expiryTimeMs = expiryTimeMs;
    }

    public PlayerProfile get(UUID uuid) {
        if (cache.containsKey(uuid)) {
            lastAccess.put(uuid, System.currentTimeMillis());
            return cache.get(uuid);
        }
        return null;
    }

    /**
     * Devuelve el perfil cacheado inmediatamente, o dispara una carga async
     * desde base de datos y entrega el resultado por callback cuando termine.
     */
    public void getOrLoad(UUID uuid, Consumer<PlayerProfile> callback) {
        PlayerProfile cached = cache.get(uuid);
        if (cached != null) {
            lastAccess.put(uuid, System.currentTimeMillis());
            callback.accept(cached);
            return;
        }

        playerRepository.loadPlayer(uuid, profile -> {
            if (profile != null) {
                cache.put(uuid, profile);
                lastAccess.put(uuid, System.currentTimeMillis());
            }
            callback.accept(profile);
        });
    }

    /**
     * Carga múltiples jugadores de forma async y devuelve todos via callback.
     * Útil para operaciones batch que requieren datos de varios jugadores.
     */
    public void getOrLoadBatch(List<UUID> uuids, Consumer<List<PlayerProfile>> callback) {
        List<PlayerProfile> results = new ArrayList<>();
        List<UUID> toLoad = new ArrayList<>();

        for (UUID uuid : uuids) {
            PlayerProfile cached = cache.get(uuid);
            if (cached != null) {
                results.add(cached);
                lastAccess.put(uuid, System.currentTimeMillis());
            } else {
                toLoad.add(uuid);
            }
        }

        if (toLoad.isEmpty()) {
            callback.accept(results);
            return;
        }

        playerRepository.getAllPlayers(allPlayers -> {
            for (UUID uuid : toLoad) {
                PlayerProfile profile = allPlayers.stream()
                        .filter(p -> p.getUuid().equals(uuid))
                        .findFirst()
                        .orElse(null);
                if (profile != null) {
                    cache.put(uuid, profile);
                    lastAccess.put(uuid, System.currentTimeMillis());
                    results.add(profile);
                }
            }
            callback.accept(results);
        });
    }

    public void put(UUID uuid, PlayerProfile profile) {
        cache.put(uuid, profile);
        lastAccess.put(uuid, System.currentTimeMillis());
    }

    public void remove(UUID uuid) {
        cache.remove(uuid);
        lastAccess.remove(uuid);
    }

    public void clear() {
        cache.clear();
        lastAccess.clear();
    }

    public boolean isCached(UUID uuid) {
        return cache.containsKey(uuid);
    }

    /**
     * Invalida entradas que no han sido accedidas en expiryTimeMs.
     * Llamar periódicamente (ej. cada 5 minutos) para limpiar caché.
     */
    public void invalidateExpired() {
        long now = System.currentTimeMillis();
        List<UUID> toRemove = new ArrayList<>();

        for (Map.Entry<UUID, Long> entry : lastAccess.entrySet()) {
            if (now - entry.getValue() > expiryTimeMs) {
                toRemove.add(entry.getKey());
            }
        }

        for (UUID uuid : toRemove) {
            remove(uuid);
        }
    }

    /**
     * Busca el UUID de un jugador online a partir de su id de BD (players.id).
     * Solo encuentra jugadores actualmente cacheados (online). Útil para
     * notificaciones en tiempo real (ej. dueño de un claim bajo ataque).
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
