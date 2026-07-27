package com.atlasMC.survivalcore.notifications;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NotificationManager {

    private final Map<UUID, NotificationPreferences> playerPreferences = new HashMap<>();
    private static final String CHAT_PREFIX = "§6[Notificación]§r ";

    public NotificationManager() {
    }

    public void setPlayerPreferences(UUID uuid, NotificationPreferences prefs) {
        playerPreferences.put(uuid, prefs);
    }

    public NotificationPreferences getPlayerPreferences(UUID uuid) {
        return playerPreferences.getOrDefault(uuid, NotificationPreferences.getDefaults());
    }

    public void notifyArenaJoin(Player player, String arenaName, long entryFee, long reward) {
        if (canNotify(player.getUniqueId(), NotificationType.ARENA)) {
            sendChatMessage(player, String.format("§a✓ ¡Te uniste a %s!\n§7Entrada: §6$%d | §7Premio: §a$%d",
                arenaName, entryFee, reward));
            playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
            sendTitle(player, "§a¡Arena!", "§7" + arenaName);
        }
    }

    public void notifyArenaWin(Player player, String arenaName, long reward) {
        if (canNotify(player.getUniqueId(), NotificationType.ARENA)) {
            sendChatMessage(player, String.format("§a✓ ¡Ganaste la arena %s!\n§7Recompensa: §6$%d", arenaName, reward));
            playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
            sendTitle(player, "§a¡Victoria!", "§e" + arenaName);
        }
    }

    public void notifyArenaLoss(Player player, String arenaName) {
        if (canNotify(player.getUniqueId(), NotificationType.ARENA)) {
            sendChatMessage(player, String.format("§cPerdiste la arena %s", arenaName));
            playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT);
        }
    }

    public void notifyClanCreated(Player player, String clanName) {
        if (canNotify(player.getUniqueId(), NotificationType.CLAN)) {
            sendChatMessage(player, String.format("§a✓ ¡Clan creado: %s!", clanName));
            playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
            sendTitle(player, "§a¡Clan Creado!", "§e" + clanName);
        }
    }

    public void notifyClanInvite(Player player, String inviterName, String clanName) {
        if (canNotify(player.getUniqueId(), NotificationType.CLAN)) {
            sendChatMessage(player, String.format("§e%s te invitó al clan: §f%s\n§7Escribe: §f/clan accept",
                inviterName, clanName));
            playSound(player, Sound.ENTITY_VILLAGER_YES);
            sendTitle(player, "§eInvitación", "§7de " + inviterName);
        }
    }

    public void notifyAuctionBidPlaced(Player player, long auctionId, long amount) {
        if (canNotify(player.getUniqueId(), NotificationType.AUCTION)) {
            sendChatMessage(player, String.format("§a✓ Puja de §6$%d colocada en subasta §b#%d", amount, auctionId));
            playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        }
    }

    public void notifyAuctionOutbid(Player player, String itemName, long newBidAmount) {
        if (canNotify(player.getUniqueId(), NotificationType.AUCTION)) {
            sendChatMessage(player, String.format("§cSuperaron tu puja en: §f%s\n§7Nueva puja: §6$%d",
                itemName, newBidAmount));
            playSound(player, Sound.BLOCK_ANVIL_LAND);
        }
    }

    public void notifyAuctionWon(Player player, String itemName, long finalPrice) {
        if (canNotify(player.getUniqueId(), NotificationType.AUCTION)) {
            sendChatMessage(player, String.format("§a✓ ¡Ganaste la subasta!\n§7Item: §f%s §7por §6$%d",
                itemName, finalPrice));
            playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
            sendTitle(player, "§a¡Subasta Ganada!", "§e" + itemName);
        }
    }

    public void notifyBountyCreated(Player player, long amount) {
        if (canNotify(player.getUniqueId(), NotificationType.BOUNTY)) {
            sendChatMessage(player, String.format("§a✓ ¡Recompensa de §6$%d creada!", amount));
            playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        }
    }

    public void notifyBountyOnYou(Player player, String creatorName, long amount) {
        if (canNotify(player.getUniqueId(), NotificationType.BOUNTY)) {
            sendChatMessage(player, String.format("§c⚠ ¡Hay una recompensa sobre ti!\n§7Creada por: §c%s\n§7Precio: §6$%d",
                creatorName, amount));
            playSound(player, Sound.ENTITY_WARDEN_HEARTBEAT);
            sendTitle(player, "§c⚠ ¡Recompensa!", "§6$" + amount);
        }
    }

    public void notifyBountyClaimed(Player player, String targetName, long reward) {
        if (canNotify(player.getUniqueId(), NotificationType.BOUNTY)) {
            sendChatMessage(player, String.format("§a✓ ¡Recompensa reclamada!\n§7Objetivo: §c%s\n§7Ganaste: §6$%d",
                targetName, reward));
            playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
            sendTitle(player, "§a¡Recompensa!", "§6$" + reward);
        }
    }

    public void notifyJobXpGain(Player player, String jobName, long xp) {
        if (canNotify(player.getUniqueId(), NotificationType.JOB)) {
            sendChatMessage(player, String.format("§6+%d XP §7de §e%s", xp, jobName));
        }
    }

    public void notifyJobLevelUp(Player player, String jobName, int level) {
        if (canNotify(player.getUniqueId(), NotificationType.JOB)) {
            sendChatMessage(player, String.format("§a✓ ¡Subiste de nivel!\n§7%s: §bNivel %d", jobName, level));
            playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
            sendTitle(player, "§a¡Nivel Up!", "§e" + jobName);
        }
    }

    private void sendChatMessage(Player player, String message) {
        player.sendMessage(CHAT_PREFIX + message);
    }

    private void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
    }

    private void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(title, subtitle, 10, 40, 10);
    }

    private boolean canNotify(UUID playerUuid, NotificationType type) {
        NotificationPreferences prefs = getPlayerPreferences(playerUuid);
        return prefs.isNotificationEnabled(type);
    }

    public enum NotificationType {
        ARENA("Arena"),
        CLAN("Clan"),
        AUCTION("Subasta"),
        BOUNTY("Recompensa"),
        JOB("Trabajo");

        private final String displayName;

        NotificationType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
