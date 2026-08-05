package com.campuscycle.dao;

import com.campuscycle.model.ChatRequest;
import com.campuscycle.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatRequestDAO {

    public int create(ChatRequest request) throws SQLException {
        String sql = "INSERT INTO chat_requests (listing_id, requester_id, seller_id, message) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, request.getListingId());
            ps.setInt(2, request.getRequesterId());
            ps.setInt(3, request.getSellerId());
            ps.setString(4, request.getMessage());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create chat request");
    }

    public Optional<ChatRequest> findById(int id) throws SQLException {
        String sql = baseSelect() + " WHERE cr.id = ?";
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

    public List<ChatRequest> findForUser(int userId) throws SQLException {
        String sql = baseSelect() + " WHERE cr.requester_id = ? OR cr.seller_id = ? ORDER BY cr.created_at DESC";
        List<ChatRequest> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE chat_requests SET status = ?, responded_at = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    private String baseSelect() {
        return """
            SELECT cr.*, l.title AS listing_title,
                   req.name AS requester_name, req.email AS requester_email, req.phone AS requester_phone,
                   sel.name AS seller_name
            FROM chat_requests cr
            JOIN listings l ON cr.listing_id = l.id
            JOIN users req ON cr.requester_id = req.id
            JOIN users sel ON cr.seller_id = sel.id
            """;
    }

    private ChatRequest mapRow(ResultSet rs) throws SQLException {
        ChatRequest cr = new ChatRequest();
        cr.setId(rs.getInt("id"));
        cr.setListingId(rs.getInt("listing_id"));
        cr.setRequesterId(rs.getInt("requester_id"));
        cr.setSellerId(rs.getInt("seller_id"));
        cr.setMessage(rs.getString("message"));
        cr.setStatus(rs.getString("status"));
        cr.setCreatedAt(rs.getTimestamp("created_at"));
        cr.setListingTitle(rs.getString("listing_title"));
        cr.setRequesterName(rs.getString("requester_name"));
        cr.setRequesterEmail(rs.getString("requester_email"));
        cr.setRequesterPhone(rs.getString("requester_phone"));
        cr.setSellerName(rs.getString("seller_name"));
        return cr;
    }
}
