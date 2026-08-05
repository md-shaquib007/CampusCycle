package com.campuscycle.dao;

import com.campuscycle.model.Report;
import com.campuscycle.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public int create(Report report) throws SQLException {
        String sql = """
            INSERT INTO reports (reporter_id, listing_id, reported_user_id, reason, description)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, report.getReporterId());
            if (report.getListingId() != null) ps.setInt(2, report.getListingId());
            else ps.setNull(2, Types.INTEGER);
            if (report.getReportedUserId() != null) ps.setInt(3, report.getReportedUserId());
            else ps.setNull(3, Types.INTEGER);
            ps.setString(4, report.getReason());
            ps.setString(5, report.getDescription());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create report");
    }

    public List<Report> findPending() throws SQLException {
        String sql = """
            SELECT r.*, u.name AS reporter_name, l.title AS listing_title, ru.name AS reported_user_name
            FROM reports r
            JOIN users u ON r.reporter_id = u.id
            LEFT JOIN listings l ON r.listing_id = l.id
            LEFT JOIN users ru ON r.reported_user_id = ru.id
            WHERE r.status = 'PENDING'
            ORDER BY r.created_at DESC
            """;
        List<Report> reports = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                reports.add(mapRow(rs));
            }
        }
        return reports;
    }

    public void resolve(int id, String status, String adminNote) throws SQLException {
        String sql = "UPDATE reports SET status = ?, admin_note = ?, resolved_at = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, adminNote);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    private Report mapRow(ResultSet rs) throws SQLException {
        Report r = new Report();
        r.setId(rs.getInt("id"));
        r.setReporterId(rs.getInt("reporter_id"));
        int listingId = rs.getInt("listing_id");
        r.setListingId(rs.wasNull() ? null : listingId);
        int reportedUserId = rs.getInt("reported_user_id");
        r.setReportedUserId(rs.wasNull() ? null : reportedUserId);
        r.setReason(rs.getString("reason"));
        r.setDescription(rs.getString("description"));
        r.setStatus(rs.getString("status"));
        r.setAdminNote(rs.getString("admin_note"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        r.setReporterName(rs.getString("reporter_name"));
        r.setListingTitle(rs.getString("listing_title"));
        r.setReportedUserName(rs.getString("reported_user_name"));
        return r;
    }
}
