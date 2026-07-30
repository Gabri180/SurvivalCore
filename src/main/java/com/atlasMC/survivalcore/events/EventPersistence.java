package com.atlasMC.survivalcore.events;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import org.bukkit.Bukkit;

public class EventPersistence {
    private final Connection connection;

    public EventPersistence(Connection connection) {
        this.connection = connection;
    }

    public void createEventsTable() {
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS special_events (" +
                    "id VARCHAR(50) PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "type VARCHAR(50) NOT NULL," +
                    "multiplier DOUBLE NOT NULL," +
                    "start_time DATETIME NOT NULL," +
                    "end_time DATETIME NOT NULL," +
                    "active BOOLEAN DEFAULT FALSE," +
                    "creator_uuid VARCHAR(36)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "INDEX idx_event_active (active)," +
                    "INDEX idx_event_type (type)" +
                    ")";
            stmt.execute(sql);
            Bukkit.getLogger().info("§a[Database] Events table initialized");
        } catch (SQLException e) {
            Bukkit.getLogger().warning("§c[Database] Error creating events table: " + e.getMessage());
        }
    }

    public void saveEvent(SpecialEvent event) {
        String sql = "INSERT INTO special_events (id, name, type, multiplier, start_time, end_time, active, creator_uuid) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE name=VALUES(name), multiplier=VALUES(multiplier), active=VALUES(active)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, event.getId());
            pstmt.setString(2, event.getName());
            pstmt.setString(3, event.getType().name());
            pstmt.setDouble(4, event.getMultiplier());
            pstmt.setTimestamp(5, Timestamp.valueOf(event.getStartTime()));
            pstmt.setTimestamp(6, Timestamp.valueOf(event.getEndTime()));
            pstmt.setBoolean(7, event.isActive());
            pstmt.setString(8, event.getCreator() != null ? event.getCreator().toString() : null);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().warning("§c[Database] Error saving event: " + e.getMessage());
        }
    }

    public void deleteEvent(String eventId) {
        String sql = "DELETE FROM special_events WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, eventId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().warning("§c[Database] Error deleting event: " + e.getMessage());
        }
    }

    public List<SpecialEvent> loadAllEvents() {
        List<SpecialEvent> events = new ArrayList<>();
        String sql = "SELECT * FROM special_events";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                SpecialEvent.EventType type = SpecialEvent.EventType.valueOf(rs.getString("type"));
                double multiplier = rs.getDouble("multiplier");
                LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("end_time").toLocalDateTime();
                UUID creator = rs.getString("creator_uuid") != null ? UUID.fromString(rs.getString("creator_uuid")) : null;

                SpecialEvent event = new SpecialEvent(id, name, type, multiplier, startTime, endTime, creator);
                event.setActive(rs.getBoolean("active"));
                events.add(event);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().warning("§c[Database] Error loading events: " + e.getMessage());
        }

        return events;
    }

    public Optional<SpecialEvent> loadEvent(String eventId) {
        String sql = "SELECT * FROM special_events WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, eventId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    SpecialEvent.EventType type = SpecialEvent.EventType.valueOf(rs.getString("type"));
                    double multiplier = rs.getDouble("multiplier");
                    LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
                    LocalDateTime endTime = rs.getTimestamp("end_time").toLocalDateTime();
                    UUID creator = rs.getString("creator_uuid") != null ? UUID.fromString(rs.getString("creator_uuid")) : null;

                    SpecialEvent event = new SpecialEvent(eventId, name, type, multiplier, startTime, endTime, creator);
                    event.setActive(rs.getBoolean("active"));
                    return Optional.of(event);
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().warning("§c[Database] Error loading event: " + e.getMessage());
        }

        return Optional.empty();
    }

    public List<SpecialEvent> loadActiveEvents() {
        String sql = "SELECT * FROM special_events WHERE active = TRUE AND end_time > NOW()";
        List<SpecialEvent> events = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                SpecialEvent.EventType type = SpecialEvent.EventType.valueOf(rs.getString("type"));
                double multiplier = rs.getDouble("multiplier");
                LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("end_time").toLocalDateTime();
                UUID creator = rs.getString("creator_uuid") != null ? UUID.fromString(rs.getString("creator_uuid")) : null;

                SpecialEvent event = new SpecialEvent(id, name, type, multiplier, startTime, endTime, creator);
                event.setActive(true);
                events.add(event);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().warning("§c[Database] Error loading active events: " + e.getMessage());
        }

        return events;
    }
}
