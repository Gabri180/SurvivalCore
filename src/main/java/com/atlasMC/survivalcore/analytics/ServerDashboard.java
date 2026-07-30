package com.atlasMC.survivalcore.analytics;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;

public class ServerDashboard {
    private final Map<String, DashboardMetric> metrics = new ConcurrentHashMap<>();

    public ServerDashboard() {
        initializeMetrics();
    }

    private void initializeMetrics() {
        metrics.put("online_players", new DashboardMetric("Jugadores Online"));
        metrics.put("total_earnings", new DashboardMetric("Dinero Total en Circulación"));
        metrics.put("arena_fights", new DashboardMetric("Combates en Arena"));
        metrics.put("auctions_active", new DashboardMetric("Subastas Activas"));
        metrics.put("clans_active", new DashboardMetric("Clanes Activos"));
        metrics.put("events_active", new DashboardMetric("Eventos Activos"));
        metrics.put("players_premium", new DashboardMetric("Jugadores Premium"));
    }

    public void updateMetric(String key, double value) {
        DashboardMetric metric = metrics.get(key);
        if (metric != null) {
            metric.updateValue(value);
        }
    }

    public void incrementMetric(String key, double amount) {
        DashboardMetric metric = metrics.get(key);
        if (metric != null) {
            metric.incrementValue(amount);
        }
    }

    public double getMetricValue(String key) {
        DashboardMetric metric = metrics.get(key);
        return metric != null ? metric.getValue() : 0;
    }

    public Map<String, Double> getAllMetrics() {
        Map<String, Double> result = new HashMap<>();
        metrics.forEach((key, metric) -> result.put(key, metric.getValue()));
        return result;
    }

    public String generateDashboardText() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n§6╔════════════════════════════════════════╗\n");
        sb.append("§6║       SERVIDOR DASHBOARD LIVE          ║\n");
        sb.append("§6╠════════════════════════════════════════╣\n");

        metrics.forEach((key, metric) -> {
            String name = String.format("%-30s", metric.getName());
            String value = String.format("%.0f", metric.getValue());
            sb.append("§f").append(name).append(" §8| §e").append(value).append("\n");
        });

        sb.append("§6╠════════════════════════════════════════╣\n");
        sb.append("§7Actualizado: ").append(LocalDateTime.now()).append("\n");
        sb.append("§6╚════════════════════════════════════════╝\n");

        return sb.toString();
    }

    public static class DashboardMetric {
        private final String name;
        private double value;
        private long lastUpdate;
        private final List<Double> history = new ArrayList<>();

        public DashboardMetric(String name) {
            this.name = name;
            this.value = 0;
            this.lastUpdate = System.currentTimeMillis();
        }

        public void updateValue(double newValue) {
            this.value = newValue;
            this.lastUpdate = System.currentTimeMillis();
            history.add(newValue);
            if (history.size() > 100) {
                history.remove(0);
            }
        }

        public void incrementValue(double amount) {
            this.value += amount;
            this.lastUpdate = System.currentTimeMillis();
            history.add(this.value);
            if (history.size() > 100) {
                history.remove(0);
            }
        }

        public String getName() { return name; }
        public double getValue() { return value; }
        public long getLastUpdate() { return lastUpdate; }
        public List<Double> getHistory() { return new ArrayList<>(history); }

        public double getAverageValue() {
            return history.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        }

        public double getMaxValue() {
            return history.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        }
    }
}