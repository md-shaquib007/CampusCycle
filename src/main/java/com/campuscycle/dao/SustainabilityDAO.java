package com.campuscycle.dao;

import com.campuscycle.model.SustainabilityStats;
import com.campuscycle.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;

public class SustainabilityDAO {

    public SustainabilityStats getStats() throws SQLException {
        SustainabilityStats stats = new SustainabilityStats();
        try (Connection conn = DBConnection.getConnection()) {
            stats.setCompletedDeals(count(conn, "SELECT COUNT(*) FROM transactions"));
            stats.setItemsReused(stats.getCompletedDeals());
            stats.setMoneySaved(sumSales(conn));
            stats.setWastePreventedKg(stats.getItemsReused() * 2);
            stats.setTotalDonations(count(conn, "SELECT COUNT(*) FROM transactions WHERE transaction_type = 'DONATION'"));
            stats.setSuccessfulBarterDeals(count(conn, "SELECT COUNT(*) FROM transactions WHERE transaction_type = 'BARTER'"));
            stats.setActiveListings(count(conn, "SELECT COUNT(*) FROM listings WHERE status = 'ACTIVE'"));
            stats.setTotalUsers(count(conn, "SELECT COUNT(*) FROM users WHERE role = 'STUDENT' AND verified = TRUE"));
        }
        return stats;
    }

    private int count(Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private BigDecimal sumSales(Connection conn) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE transaction_type = 'SALE'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
    }

    public void recordTransaction(int listingId, int sellerId, int buyerId,
                                  String type, BigDecimal amount, Integer barterId) throws SQLException {
        String sql = """
            INSERT INTO transactions (listing_id, seller_id, buyer_id, transaction_type, amount, barter_id)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, listingId);
            ps.setInt(2, sellerId);
            ps.setInt(3, buyerId);
            ps.setString(4, type);
            ps.setBigDecimal(5, amount != null ? amount : BigDecimal.ZERO);
            if (barterId != null) ps.setInt(6, barterId);
            else ps.setNull(6, Types.INTEGER);
            ps.executeUpdate();
        }
    }
}
