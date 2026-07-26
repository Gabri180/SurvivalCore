package com.atlasMC.survivalcore.db;

import com.atlasMC.survivalcore.enums.AuctionStatus;
import com.atlasMC.survivalcore.models.Auction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Persistencia de {@code auctions}. El estado ({@link AuctionStatus}) se
 * infiere en memoria (no hay columna dedicada en el schema base): activa si
 * {@code end_time} aun no paso, si no se considera expirada/cerrada.
 */
public class AuctionRepository {

    private static final Logger LOGGER = Logger.getLogger(AuctionRepository.class.getName());

    private final DatabaseManager databaseManager;

    public AuctionRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void createAuction(Auction auction, Consumer<Long> onId) {
        String sql = """
                INSERT INTO auctions (seller_id, item_name, quantity, start_price, current_price, current_bidder_id, end_time)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        databaseManager.executeInsertAsync(sql, onId, error ->
                        LOGGER.severe("Error creando subasta: " + error.getMessage()),
                auction.getSellerId(),
                auction.getItemName(),
                auction.getQuantity(),
                auction.getStartPrice(),
                auction.getCurrentPrice(),
                auction.getCurrentBidderId(),
                Timestamp.from(auction.getEndTime())
        );
    }

    public void loadActiveAuctions(Consumer<List<Auction>> callback) {
        databaseManager.queryAsync(
                "SELECT * FROM auctions WHERE end_time > NOW()", this::mapRow, callback);
    }

    public void loadExpiredUnprocessed(Consumer<List<Auction>> callback) {
        // El closedAuctions set en memoria evita reprocesar; aca solo traemos las vencidas.
        databaseManager.queryAsync(
                "SELECT * FROM auctions WHERE end_time <= NOW()", this::mapRow, callback);
    }

    public void placeBid(long auctionId, long bidderId, long amount) {
        databaseManager.executeAsync(
                "UPDATE auctions SET current_price = ?, current_bidder_id = ? WHERE id = ?",
                null, amount, bidderId, auctionId);
    }

    public void deleteAuction(long auctionId) {
        databaseManager.executeAsync("DELETE FROM auctions WHERE id = ?", null, auctionId);
    }

    private Auction mapRow(ResultSet rs) {
        try {
            Timestamp endTime = rs.getTimestamp("end_time");
            long bidderId = rs.getLong("current_bidder_id");
            boolean bidderNull = rs.wasNull();
            return Auction.builder()
                    .id(rs.getLong("id"))
                    .sellerId(rs.getLong("seller_id"))
                    .itemName(rs.getString("item_name"))
                    .quantity(rs.getInt("quantity"))
                    .startPrice(rs.getLong("start_price"))
                    .currentPrice(rs.getLong("current_price"))
                    .currentBidderId(bidderNull ? null : bidderId)
                    .endTime(endTime != null ? endTime.toInstant() : null)
                    .build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de auctions", e);
            throw new IllegalStateException(e);
        }
    }
}
