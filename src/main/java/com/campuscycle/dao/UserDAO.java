package com.campuscycle.dao;

import com.campuscycle.model.User;
import com.campuscycle.model.UserRole;
import com.campuscycle.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    public Optional<User> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase().trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<User> findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
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

    public int create(User user) throws SQLException {
        String sql = """
            INSERT INTO users (name, email, password_hash, college, department, semester, hostel, phone, role, verified)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail().toLowerCase().trim());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getCollege());
            ps.setString(5, user.getDepartment());
            ps.setString(6, user.getSemester());
            ps.setString(7, user.getHostel());
            ps.setString(8, user.getPhone());
            ps.setString(9, user.getRole().name());
            ps.setBoolean(10, user.isVerified());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create user");
    }

    public void updateProfile(User user) throws SQLException {
        String sql = """
            UPDATE users SET name=?, department=?, semester=?, hostel=?, phone=?
            WHERE id=?
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getDepartment());
            ps.setString(3, user.getSemester());
            ps.setString(4, user.getHostel());
            ps.setString(5, user.getPhone());
            ps.setInt(6, user.getId());
            ps.executeUpdate();
        }
    }

    public void updatePassword(int userId, String hash) throws SQLException {
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hash);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    public void setVerified(int userId, boolean verified) throws SQLException {
        String sql = "UPDATE users SET verified = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, verified);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    public void setSuspended(int userId, boolean suspended) throws SQLException {
        String sql = "UPDATE users SET suspended = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, suspended);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    public List<User> findAllPendingVerification() throws SQLException {
        return findByVerified(false);
    }

    public List<User> findAllStudents() throws SQLException {
        String sql = "SELECT * FROM users WHERE role = 'STUDENT' ORDER BY created_at DESC";
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        }
        return users;
    }

    private List<User> findByVerified(boolean verified) throws SQLException {
        String sql = "SELECT * FROM users WHERE verified = ? AND role = 'STUDENT' ORDER BY created_at DESC";
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, verified);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapRow(rs));
                }
            }
        }
        return users;
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'STUDENT'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public void saveResetToken(int userId, String token, Timestamp expires) throws SQLException {
        String sql = "INSERT INTO password_reset_tokens (user_id, token, expires_at) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.setTimestamp(3, expires);
            ps.executeUpdate();
        }
    }

    public Optional<Integer> findUserIdByValidToken(String token) throws SQLException {
        String sql = """
            SELECT user_id FROM password_reset_tokens
            WHERE token = ? AND used = FALSE AND expires_at > NOW()
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getInt("user_id"));
                }
            }
        }
        return Optional.empty();
    }

    public void markTokenUsed(String token) throws SQLException {
        String sql = "UPDATE password_reset_tokens SET used = TRUE WHERE token = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.executeUpdate();
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setCollege(rs.getString("college"));
        user.setDepartment(rs.getString("department"));
        user.setSemester(rs.getString("semester"));
        user.setHostel(rs.getString("hostel"));
        user.setPhone(rs.getString("phone"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        user.setVerified(rs.getBoolean("verified"));
        user.setSuspended(rs.getBoolean("suspended"));
        user.setRatingAvg(rs.getDouble("rating_avg"));
        user.setRatingCount(rs.getInt("rating_count"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}
