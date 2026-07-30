package com.atlasMC.survivalcore.events;

import com.atlasMC.survivalcore.api.IEventManager;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class AutomaticEventScheduler {
    private final Plugin plugin;
    private final IEventManager eventManager;
    private BukkitTask schedulerTask;

    public AutomaticEventScheduler(Plugin plugin, IEventManager eventManager) {
        this.plugin = plugin;
        this.eventManager = eventManager;
    }

    public void startScheduler() {
        schedulerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(
                plugin,
                this::checkAndCreateWeeklyEvents,
                0L,
                20 * 60 * 60  // Cada hora
        );
        Bukkit.getLogger().info("§a[Events] Automatic event scheduler started");
    }

    public void stopScheduler() {
        if (schedulerTask != null) {
            schedulerTask.cancel();
            Bukkit.getLogger().info("§a[Events] Automatic event scheduler stopped");
        }
    }

    private void checkAndCreateWeeklyEvents() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = now.getDayOfWeek();

        // Monday: Double XP
        if (today == DayOfWeek.MONDAY && now.getHour() == 0 && now.getMinute() == 0) {
            createEventIfNotExists(
                    "weekly_double_xp",
                    "Doble XP - Lunes",
                    SpecialEvent.EventType.DOUBLE_XP,
                    2.0,
                    24 * 60  // 24 horas
            );
        }

        // Wednesday: Double Money
        if (today == DayOfWeek.WEDNESDAY && now.getHour() == 0 && now.getMinute() == 0) {
            createEventIfNotExists(
                    "weekly_double_money",
                    "Doble Dinero - Miércoles",
                    SpecialEvent.EventType.DOUBLE_MONEY,
                    2.0,
                    24 * 60
            );
        }

        // Friday: Bonus Arena
        if (today == DayOfWeek.FRIDAY && now.getHour() == 0 && now.getMinute() == 0) {
            createEventIfNotExists(
                    "weekly_bonus_arena",
                    "Bonus Arena - Viernes",
                    SpecialEvent.EventType.BONUS_ARENA,
                    1.5,
                    24 * 60
            );
        }

        // Saturday: Triple Combo
        if (today == DayOfWeek.SATURDAY && now.getHour() == 0 && now.getMinute() == 0) {
            createEventIfNotExists(
                    "weekly_triple_combo",
                    "Triple Combo - Sábado (2x XP + 2x Dinero)",
                    SpecialEvent.EventType.SEASONAL,
                    3.0,
                    48 * 60  // 48 horas (fin de semana)
            );
        }
    }

    private void createEventIfNotExists(String id, String name, SpecialEvent.EventType type,
                                       double multiplier, long durationMinutes) {
        if (eventManager.getEvent(id).isEmpty()) {
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.plus(durationMinutes, ChronoUnit.MINUTES);

            eventManager.createEvent(id, name, type, multiplier, start, end, null);
            eventManager.startEvent(id);

            String message = "§6[Evento] ¡" + name + " ha comenzado! " +
                    "Multiplicador: §e" + multiplier + "x§6 por " + durationMinutes + " minutos";
            Bukkit.broadcastMessage(message);

            Bukkit.getLogger().info("§a[Events] Created weekly event: " + name);
        }
    }

    public void createSpecialSeason(String seasonName, int durationDays) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plus(durationDays, ChronoUnit.DAYS);
        String id = "season_" + System.currentTimeMillis();

        eventManager.createEvent(
                id,
                seasonName,
                SpecialEvent.EventType.SEASONAL,
                1.5,
                start,
                end,
                null
        );

        String message = "§6[Evento Estacional] ¡" + seasonName + " ha comenzado! " +
                "Durará " + durationDays + " días.";
        Bukkit.broadcastMessage(message);
        Bukkit.getLogger().info("§a[Events] Created seasonal event: " + seasonName);
    }

    public void createHolidayEvent(String holidayName, double multiplier, int durationHours) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plus(durationHours, ChronoUnit.HOURS);
        String id = "holiday_" + System.currentTimeMillis();

        eventManager.createEvent(
                id,
                "🎉 " + holidayName,
                SpecialEvent.EventType.CUSTOM,
                multiplier,
                start,
                end,
                null
        );

        String message = "§6§l🎉 [Evento Especial] ¡" + holidayName + "! " +
                "§e" + multiplier + "x§6 de recompensas por " + durationHours + " horas";
        Bukkit.broadcastMessage(message);
        Bukkit.getLogger().info("§a[Events] Created holiday event: " + holidayName);
    }
}
