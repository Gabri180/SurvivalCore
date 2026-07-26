package com.atlasMC.survivalcore.seasons;

import com.atlasMC.survivalcore.db.DatabaseManager;
import com.atlasMC.survivalcore.models.Season;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Gestiona la temporada activa: rankings, recompensas y rotacion.
 */
public class SeasonManager {

    private final DatabaseManager databaseManager;
    private final AtomicReference<Season> currentSeason = new AtomicReference<>();

    public SeasonManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Season currentSeason() {
        return currentSeason.get();
    }

    public void setCurrentSeason(Season season) {
        currentSeason.set(season);
    }

    /**
     * Posicion del jugador en el ranking de la temporada activa.
     * Placeholder: la logica real se conecta a la BD de rankings.
     */
    public int getUserRank(UUID uuid) {
        return -1;
    }

    public void rewardPlayers() {
        Season season = currentSeason.get();
        if (season == null) {
            return;
        }
        // La distribucion real de recompensas se implementa junto al ranking PvP/economico.
    }

    public void resetSeason() {
        Season previous = currentSeason.get();
        int nextNumber = previous != null ? previous.getSeasonNumber() + 1 : 1;

        Season next = Season.builder()
                .seasonNumber(nextNumber)
                .startDate(Instant.now())
                .endDate(Instant.now().plusSeconds(60L * 24 * 60 * 60))
                .rewardPool(0)
                .build();

        currentSeason.set(next);
    }
}
