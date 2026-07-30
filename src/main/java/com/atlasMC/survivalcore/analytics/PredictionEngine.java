package com.atlasMC.survivalcore.analytics;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;

public class PredictionEngine {
    private final ServerDashboard dashboard;
    private final Map<String, List<Double>> historicalData = new HashMap<>();

    public PredictionEngine(ServerDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public void recordMetric(String metricKey, double value) {
        historicalData.computeIfAbsent(metricKey, k -> new ArrayList<>()).add(value);

        List<Double> data = historicalData.get(metricKey);
        if (data.size() > 168) {
            data.remove(0);
        }
    }

    public double predictNextValue(String metricKey) {
        List<Double> data = historicalData.getOrDefault(metricKey, new ArrayList<>());
        if (data.size() < 3) {
            return data.isEmpty() ? 0 : data.get(data.size() - 1);
        }

        double trend = calculateTrend(data);
        double average = data.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        return average + trend;
    }

    public Map<String, Double> predictAllMetrics() {
        Map<String, Double> predictions = new HashMap<>();
        historicalData.keySet().forEach(key -> {
            predictions.put(key, predictNextValue(key));
        });
        return predictions;
    }

    private double calculateTrend(List<Double> data) {
        if (data.size() < 2) return 0;

        double sum = 0;
        for (int i = 1; i < data.size(); i++) {
            sum += data.get(i) - data.get(i - 1);
        }

        return sum / (data.size() - 1);
    }

    public String getServerHealthStatus() {
        List<Double> onlinePlayerPrediction = historicalData.getOrDefault("online_players", new ArrayList<>());

        if (onlinePlayerPrediction.isEmpty()) {
            return "§6Estado: Recopilando datos...";
        }

        double average = onlinePlayerPrediction.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        if (average > 30) {
            return "§a✓ Servidor: Excelente (Muy activo)";
        } else if (average > 15) {
            return "§e⚠ Servidor: Bueno (Activo)";
        } else if (average > 5) {
            return "§6⚠ Servidor: Regular (Poco activo)";
        } else {
            return "§c✗ Servidor: Bajo (Muy poco activo)";
        }
    }

    public String generatePredictionReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n§6╔════════════════════════════════════════╗\n");
        sb.append("§6║     PREDICCIÓN DEL SIGUIENTE PERIODO   ║\n");
        sb.append("§6╠════════════════════════════════════════╣\n");

        Map<String, Double> predictions = predictAllMetrics();
        predictions.forEach((key, value) -> {
            sb.append("§e").append(key).append("§7: ");
            sb.append("§a").append(String.format("%.0f", value)).append("\n");
        });

        sb.append("§6╠════════════════════════════════════════╣\n");
        sb.append(getServerHealthStatus()).append("\n");
        sb.append("§6╚════════════════════════════════════════╝\n");

        return sb.toString();
    }

    public void analyzePlayerBehavior() {
        Bukkit.getLogger().info("§b[Analytics] Analyzing player behavior patterns...");

        List<Double> trends = new ArrayList<>();
        historicalData.forEach((key, data) -> {
            trends.add(calculateTrend(data));
        });

        double avgTrend = trends.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        if (avgTrend > 2) {
            Bukkit.getLogger().info("§a[Analytics] Trend: INCREASING - Servidor en crecimiento");
        } else if (avgTrend < -2) {
            Bukkit.getLogger().warning("§c[Analytics] Trend: DECREASING - Revisar actividad del servidor");
        } else {
            Bukkit.getLogger().info("§e[Analytics] Trend: STABLE - Servidor estable");
        }
    }

    public boolean shouldRecommendEvent() {
        List<Double> playerData = historicalData.getOrDefault("online_players", new ArrayList<>());
        if (playerData.isEmpty()) return false;

        double average = playerData.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        return average < 10;
    }

    public String getRecommendation() {
        if (shouldRecommendEvent()) {
            return "§6[Recomendación] Actividad baja. Considera crear un evento especial.";
        }

        List<Double> auctionData = historicalData.getOrDefault("auctions_active", new ArrayList<>());
        if (!auctionData.isEmpty() && auctionData.get(auctionData.size() - 1) < 5) {
            return "§6[Recomendación] Pocas subastas activas. Incentiva la economía.";
        }

        return "§a[Recomendación] Servidor funcionando óptimamente.";
    }
}
