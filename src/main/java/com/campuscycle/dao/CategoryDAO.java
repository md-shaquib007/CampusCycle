package com.campuscycle.dao;

import com.campuscycle.model.Category;
import com.campuscycle.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> findAll() throws SQLException {
        String sql = """
            SELECT c.*, p.name AS parent_name
            FROM categories c
            LEFT JOIN categories p ON c.parent_id = p.id
            ORDER BY COALESCE(c.parent_id, c.id), c.id
            """;
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(mapRow(rs));
            }
        }
        return categories;
    }

    public List<Category> findParents() throws SQLException {
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL ORDER BY name";
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(mapRow(rs));
            }
        }
        return categories;
    }

    public Category findById(int id) throws SQLException {
        String sql = "SELECT c.*, p.name AS parent_name FROM categories c LEFT JOIN categories p ON c.parent_id = p.id WHERE c.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        int parentId = rs.getInt("parent_id");
        c.setParentId(rs.wasNull() ? null : parentId);
        c.setIcon(rs.getString("icon"));
        try {
            c.setParentName(rs.getString("parent_name"));
        } catch (SQLException ignored) {}
        return c;
    }
}
