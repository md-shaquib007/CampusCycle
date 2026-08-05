package com.campuscycle.dao;

import com.campuscycle.model.BarterProposal;
import com.campuscycle.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BarterDAO {

    public int create(BarterProposal proposal) throws SQLException {
        String sql = """
            INSERT INTO barter_proposals (proposer_id, target_listing_id, offered_listing_id, message)
            VALUES (?, ?, ?, ?)
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, proposal.getProposerId());
            ps.setInt(2, proposal.getTargetListingId());
            ps.setInt(3, proposal.getOfferedListingId());
            ps.setString(4, proposal.getMessage());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create barter proposal");
    }

    public Optional<BarterProposal> findById(int id) throws SQLException {
        String sql = baseSelect() + " WHERE bp.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<BarterProposal> findForUser(int userId) throws SQLException {
        String sql = baseSelect() + """
             WHERE bp.proposer_id = ?
                OR tl.user_id = ?
                OR ol.user_id = ?
             ORDER BY bp.created_at DESC
            """;
        List<BarterProposal> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE barter_proposals SET status = ?, completed_at = CASE WHEN ? = 'COMPLETED' THEN NOW() ELSE completed_at END WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, status);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public int countCompleted() throws SQLException {
        String sql = "SELECT COUNT(*) FROM barter_proposals WHERE status = 'COMPLETED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private String baseSelect() {
        return """
            SELECT bp.*, u.name AS proposer_name,
                   tl.title AS target_title, ol.title AS offered_title,
                   (SELECT image_path FROM listing_images WHERE listing_id = tl.id AND is_primary = TRUE LIMIT 1) AS target_image,
                   (SELECT image_path FROM listing_images WHERE listing_id = ol.id AND is_primary = TRUE LIMIT 1) AS offered_image
            FROM barter_proposals bp
            JOIN users u ON bp.proposer_id = u.id
            JOIN listings tl ON bp.target_listing_id = tl.id
            JOIN listings ol ON bp.offered_listing_id = ol.id
            """;
    }

    private BarterProposal mapRow(ResultSet rs) throws SQLException {
        BarterProposal bp = new BarterProposal();
        bp.setId(rs.getInt("id"));
        bp.setProposerId(rs.getInt("proposer_id"));
        bp.setTargetListingId(rs.getInt("target_listing_id"));
        bp.setOfferedListingId(rs.getInt("offered_listing_id"));
        bp.setMessage(rs.getString("message"));
        bp.setStatus(rs.getString("status"));
        bp.setCreatedAt(rs.getTimestamp("created_at"));
        bp.setCompletedAt(rs.getTimestamp("completed_at"));
        bp.setProposerName(rs.getString("proposer_name"));
        bp.setTargetTitle(rs.getString("target_title"));
        bp.setOfferedTitle(rs.getString("offered_title"));
        bp.setTargetImage(rs.getString("target_image"));
        bp.setOfferedImage(rs.getString("offered_image"));
        return bp;
    }
}
