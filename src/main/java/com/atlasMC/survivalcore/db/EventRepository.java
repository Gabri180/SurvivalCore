package com.atlasMC.survivalcore.db;

import com.atlasMC.survivalcore.enums.EventType;
import com.atlasMC.survivalcore.events.ServerEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.function.Consumer;

/**
 * Persistencia de eventos especiales en BD.
 */
public class EventRepository {

    private final DatabaseManager databaseManager;

    public EventRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Crea un nuevo evento en BD.
     */
    public void createEvent(EventType eventType, double multiplier, long startTimeMs, long endTimeMs,
                            long createdByPlayerId, Consumer<Long> onGeneratedId) {
        String sql = """
                INSERT INTO events (event_type, multiplier, start_time, end_time, created_by)
                VALUES (?, ?, FROM_UNIXTIME(?/1000), FROM_UNIXTIME(?/1000), ?)
                """;

        databaseManager.executeInsertAsync(sql, onGeneratedId, null,
                eventType.getKey(),
                multiplier,
                startTimeMs,
                endTimeMs,
                createdByPlayerId
        );
    }

    /**
     * Obtiene todos los eventos activos.
     */
    public void getActiveEvents(Consumer<List<ServerEvent>> callback) {
        String sql = """
                SELECT id, event_type, multiplier, UNIX_TIMESTAMP(start_time)*1000, UNIX_TIMESTAMP(end_time)*1000
                FROM events
                WHERE start_time <= NOW() AND end_time > NOW()
                ORDER BY end_time DESC
                """;

        databaseManager.queryAsync(sql, this::mapRow, callback);
    }

    /**
     * Obtiene todos los eventos (activos e inactivos).
     */
    public void getAllEvents(Consumer<List<ServerEvent>> callback) {
        String sql = """
                SELECT id, event_type, multiplier, UNIX_TIMESTAMP(start_time)*1000, UNIX_TIMESTAMP(end_time)*1000
                FROM events
                ORDER BY end_time DESC
                LIMIT 50
                """;

        databaseManager.queryAsync(sql, this::mapRow, callback);
    }

    private ServerEvent mapRow(ResultSet rs) {
        try {
            long id = rs.getLong(1);
            String typeStr = rs.getString(2);
            double multiplier = rs.getDouble(3);
            long startTime = rs.getLong(4);
            long endTime = rs.getLong(5);

            EventType eventType = EventType.fromString(typeStr);
            if (eventType == null) eventType = EventType.DOUBLE_XP;

            return new ServerEvent(id, eventType, multiplier, startTime, endTime);
        } catch (SQLException e) {
            throw new IllegalStateException("Error mapeando evento", e);
        }
    }
}
