package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.EventAPI;
import com.atlasMC.survivalcore.api.IClanWarManager;
import com.atlasMC.survivalcore.events.ClanWarDeclaredEvent;

/**
 * Dev3 - estructura sin logica de BD. Escucha eventos custom via {@link EventAPI}
 * en lugar de eventos nativos de Bukkit. Se conecta a {@link IClanWarManager} manana.
 */
public class ClanWarListener {

    private final IClanWarManager clanWarManager;

    public ClanWarListener(EventAPI eventAPI, IClanWarManager clanWarManager) {
        this.clanWarManager = clanWarManager;
        eventAPI.on(ClanWarDeclaredEvent.class, this::onClanWarDeclared);
    }

    private void onClanWarDeclared(ClanWarDeclaredEvent event) {
        if (clanWarManager == null) {
            return;
        }
        // Logica de inicio de guerra (territorio, notificaciones) pendiente de IClanWarManager.
    }
}
