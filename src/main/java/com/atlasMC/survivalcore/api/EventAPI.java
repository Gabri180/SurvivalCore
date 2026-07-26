package com.atlasMC.survivalcore.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Bus de eventos interno para comunicacion desacoplada entre modulos
 * (core, jobs/skills/misiones, pvp/clanes/raideo) sin depender de los
 * eventos nativos de Bukkit.
 */
public class EventAPI {

    private final Map<Class<?>, List<Consumer<Object>>> listeners = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void on(Class<T> eventType, Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>())
                .add((Consumer<Object>) listener);
    }

    public <T> void emit(T event) {
        List<Consumer<Object>> eventListeners = listeners.get(event.getClass());
        if (eventListeners == null) {
            return;
        }
        for (Consumer<Object> listener : eventListeners) {
            listener.accept(event);
        }
    }

    public void clear() {
        listeners.clear();
    }
}
