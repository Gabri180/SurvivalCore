package com.atlasMC.survivalcore.managers;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.enums.TournamentStatus;
import com.atlasMC.survivalcore.models.Tournament;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.*;

public class TournamentManager {

    private final Map<Long, Tournament> tournaments = new HashMap<>();
    private final EconomyAPI economyAPI;
    private final PlayerCache playerCache;
    private long nextTournamentId = 1;

    public TournamentManager(EconomyAPI economyAPI, PlayerCache playerCache) {
        this.economyAPI = economyAPI;
        this.playerCache = playerCache;
    }

    public Tournament createTournament(String name, int maxPlayers, long entryFee) {
        Tournament tournament = Tournament.builder()
                .id(nextTournamentId++)
                .name(name)
                .status(TournamentStatus.OPEN)
                .maxPlayers(maxPlayers)
                .prizePool(entryFee * maxPlayers)
                .createdAt(Instant.now())
                .build();

        tournaments.put(tournament.getId(), tournament);
        Bukkit.broadcastMessage("§6§l🏆 §r§6Nuevo torneo: §e" + name + " §6(" + maxPlayers + " jugadores)");
        return tournament;
    }

    public boolean joinTournament(long tournamentId, UUID playerUuid, long entryFee) {
        Tournament tournament = tournaments.get(tournamentId);
        if (tournament == null || !tournament.canJoin()) return false;

        if (!economyAPI.removeBalance(playerUuid, entryFee)) return false;

        tournament.getParticipants().add(playerUuid);

        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            player.sendMessage("§a✓ §6Te uniste al torneo §e" + tournament.getName());
        }

        if (tournament.isFull()) {
            tournament.setStatus(TournamentStatus.IN_PROGRESS);
            tournament.setStartedAt(Instant.now());
            Bukkit.broadcastMessage("§6§l🏆 §r§6Torneo " + tournament.getName() + " iniciado!");
        }

        return true;
    }

    public void finishTournament(long tournamentId, UUID winnerUuid) {
        Tournament tournament = tournaments.get(tournamentId);
        if (tournament == null) return;

        tournament.setWinner(winnerUuid);
        tournament.setStatus(TournamentStatus.FINISHED);
        tournament.setEndedAt(Instant.now());

        economyAPI.addBalance(winnerUuid, tournament.getPrizePool());

        Player winner = Bukkit.getPlayer(winnerUuid);
        if (winner != null) {
            winner.sendMessage("§a✓ §6¡Ganaste el torneo! Prize: §e$" + tournament.getPrizePool());
        }

        Bukkit.broadcastMessage("§6§l🏆 §r§e" + (winner != null ? winner.getName() : "???") + " §6ganó el torneo!");
    }

    public Tournament getTournament(long id) {
        return tournaments.get(id);
    }

    public Collection<Tournament> getActiveTournaments() {
        return tournaments.values().stream()
                .filter(t -> t.getStatus() == TournamentStatus.OPEN || t.getStatus() == TournamentStatus.IN_PROGRESS)
                .toList();
    }

    public double getMatchmakingScore(UUID player1, UUID player2) {
        // Retorna un score de compatibilidad (0-1)
        // 1 = perfecta compatibilidad de ELO
        // Será implementado cuando ArenaManager esté listo
        return 0.8; // Por ahora retorna un score neutral
    }
}
