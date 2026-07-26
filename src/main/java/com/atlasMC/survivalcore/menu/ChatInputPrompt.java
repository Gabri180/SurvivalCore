package com.atlasMC.survivalcore.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChatInputPrompt implements Listener {

    private final Map<Player, Consumer<String>> pending = new HashMap<>();

    public ChatInputPrompt(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void prompt(Player player, Consumer<String> callback) {
        pending.put(player, callback);
        player.sendMessage("§6Escribe en chat:");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Consumer<String> callback = pending.remove(e.getPlayer());
        if (callback == null) return;
        e.setCancelled(true);
        callback.accept(e.getMessage());
    }
}
