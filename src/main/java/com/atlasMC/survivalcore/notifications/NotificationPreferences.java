package com.atlasMC.survivalcore.notifications;

import java.util.HashMap;
import java.util.Map;

public class NotificationPreferences {

    private boolean arenaNotifications = true;
    private boolean clanNotifications = true;
    private boolean auctionNotifications = true;
    private boolean bountyNotifications = true;
    private boolean jobNotifications = true;

    private boolean soundsEnabled = true;
    private boolean titlesEnabled = true;
    private boolean chatEnabled = true;

    public NotificationPreferences() {
    }

    public boolean isNotificationEnabled(NotificationManager.NotificationType type) {
        return switch (type) {
            case ARENA -> arenaNotifications && chatEnabled;
            case CLAN -> clanNotifications && chatEnabled;
            case AUCTION -> auctionNotifications && chatEnabled;
            case BOUNTY -> bountyNotifications && chatEnabled;
            case JOB -> jobNotifications && chatEnabled;
        };
    }

    public void toggleNotificationType(NotificationManager.NotificationType type) {
        switch (type) {
            case ARENA -> arenaNotifications = !arenaNotifications;
            case CLAN -> clanNotifications = !clanNotifications;
            case AUCTION -> auctionNotifications = !auctionNotifications;
            case BOUNTY -> bountyNotifications = !bountyNotifications;
            case JOB -> jobNotifications = !jobNotifications;
        }
    }

    public void toggleSounds() {
        soundsEnabled = !soundsEnabled;
    }

    public void toggleTitles() {
        titlesEnabled = !titlesEnabled;
    }

    public void toggleChat() {
        chatEnabled = !chatEnabled;
    }

    // Getters
    public boolean isArenaNotifications() { return arenaNotifications; }
    public boolean isClanNotifications() { return clanNotifications; }
    public boolean isAuctionNotifications() { return auctionNotifications; }
    public boolean isBountyNotifications() { return bountyNotifications; }
    public boolean isJobNotifications() { return jobNotifications; }
    public boolean isSoundsEnabled() { return soundsEnabled; }
    public boolean isTitlesEnabled() { return titlesEnabled; }
    public boolean isChatEnabled() { return chatEnabled; }

    // Setters
    public void setArenaNotifications(boolean value) { this.arenaNotifications = value; }
    public void setClanNotifications(boolean value) { this.clanNotifications = value; }
    public void setAuctionNotifications(boolean value) { this.auctionNotifications = value; }
    public void setBountyNotifications(boolean value) { this.bountyNotifications = value; }
    public void setJobNotifications(boolean value) { this.jobNotifications = value; }
    public void setSoundsEnabled(boolean value) { this.soundsEnabled = value; }
    public void setTitlesEnabled(boolean value) { this.titlesEnabled = value; }
    public void setChatEnabled(boolean value) { this.chatEnabled = value; }

    public static NotificationPreferences getDefaults() {
        return new NotificationPreferences();
    }

    public Map<String, Boolean> toMap() {
        Map<String, Boolean> map = new HashMap<>();
        map.put("arena", arenaNotifications);
        map.put("clan", clanNotifications);
        map.put("auction", auctionNotifications);
        map.put("bounty", bountyNotifications);
        map.put("job", jobNotifications);
        map.put("sounds", soundsEnabled);
        map.put("titles", titlesEnabled);
        map.put("chat", chatEnabled);
        return map;
    }
}
