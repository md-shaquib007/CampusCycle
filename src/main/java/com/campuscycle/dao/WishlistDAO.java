package com.campuscycle.dao;

import com.campuscycle.util.DBConnection;

import java.sql.*;

public class WishlistDAO {

    public boolean add(int userId, int listingId) throws SQLException {
        String sql = "INSERT INTO wishlist (user_id, listing_id) VALUES (?, ?) ON CONFLICT (user_id, listing_id) DO NOTHING";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, listingId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean remove(int userId, int listingId) throws SQLException {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND listing_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, listingId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean exists(int userId, int listingId) throws SQLException {
        String sql = "SELECT 1 FROM wishlist WHERE user_id = ? AND listing_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, listingId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
