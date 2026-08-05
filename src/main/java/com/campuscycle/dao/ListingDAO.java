package com.campuscycle.dao;

import com.campuscycle.model.*;
import com.campuscycle.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListingDAO {

    public int create(Listing listing) throws SQLException {
        String sql = """
            INSERT INTO listings (user_id, category_id, title, description, listing_type,
                condition_type, price, barter_wanted, pickup_location, course, semester_tag, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, listing.getUserId());
            ps.setInt(2, listing.getCategoryId());
            ps.setString(3, listing.getTitle());
            ps.setString(4, listing.getDescription());
            ps.setString(5, listing.getListingType().name());
            ps.setString(6, listing.getConditionType().name());
            ps.setBigDecimal(7, listing.getPrice() != null ? listing.getPrice() : BigDecimal.ZERO);
            ps.setString(8, listing.getBarterWanted());
            ps.setString(9, listing.getPickupLocation());
            ps.setString(10, listing.getCourse());
            ps.setString(11, listing.getSemesterTag());
            ps.setString(12, listing.getStatus().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create listing");
    }

    public void addImage(int listingId, String path, boolean primary) throws SQLException {
        String sql = "INSERT INTO listing_images (listing_id, image_path, is_primary) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, listingId);
            ps.setString(2, path);
            ps.setBoolean(3, primary);
            ps.executeUpdate();
        }
    }

    public Optional<Listing> findById(int id) throws SQLException {
        String sql = """
            SELECT l.*, u.name AS seller_name, u.email AS seller_email, u.hostel AS seller_hostel,
                   c.name AS category_name,
                   (SELECT image_path FROM listing_images WHERE listing_id = l.id AND is_primary = TRUE LIMIT 1) AS primary_image
            FROM listings l
            JOIN users u ON l.user_id = u.id
            JOIN categories c ON l.category_id = c.id
            WHERE l.id = ?
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Listing listing = mapRow(rs);
                    listing.setImages(findImages(id));
                    return Optional.of(listing);
                }
            }
        }
        return Optional.empty();
    }

    public List<String> findImages(int listingId) throws SQLException {
        String sql = "SELECT image_path FROM listing_images WHERE listing_id = ? ORDER BY is_primary DESC, id";
        List<String> images = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, listingId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    images.add(rs.getString("image_path"));
                }
            }
        }
        return images;
    }

    public List<Listing> search(ListingFilter filter) throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT l.*, u.name AS seller_name, u.email AS seller_email, u.hostel AS seller_hostel,
                   c.name AS category_name,
                   (SELECT image_path FROM listing_images WHERE listing_id = l.id AND is_primary = TRUE LIMIT 1) AS primary_image
            FROM listings l
            JOIN users u ON l.user_id = u.id
            JOIN categories c ON l.category_id = c.id
            WHERE 1=1
            """);

        List<Object> params = new ArrayList<>();
        appendFilters(sql, params, filter);

        sql.append(" ORDER BY l.created_at DESC LIMIT ? OFFSET ?");
        params.add(filter.getLimit());
        params.add(filter.getOffset());

        List<Listing> listings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            setParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listings.add(mapRow(rs));
                }
            }
        }
        return listings;
    }

    public int countSearch(ListingFilter filter) throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*)
            FROM listings l
            JOIN users u ON l.user_id = u.id
            JOIN categories c ON l.category_id = c.id
            WHERE 1=1
            """);
        List<Object> params = new ArrayList<>();
        appendFilters(sql, params, filter);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            setParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public List<Listing> findByUserId(int userId) throws SQLException {
        ListingFilter filter = new ListingFilter();
        filter.setUserId(userId);
        filter.setLimit(100);
        filter.setOffset(0);
        return search(filter);
    }

    public List<Listing> findActiveByUserId(int userId) throws SQLException {
        ListingFilter filter = new ListingFilter();
        filter.setUserId(userId);
        filter.setStatus(ListingStatus.ACTIVE);
        filter.setLimit(100);
        filter.setOffset(0);
        return search(filter);
    }

    public void updateStatus(int id, ListingStatus status) throws SQLException {
        String sql = "UPDATE listings SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void incrementViews(int id) throws SQLException {
        String sql = "UPDATE listings SET views = views + 1 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int countByStatus(ListingStatus status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM listings WHERE status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public List<Listing> findPending() throws SQLException {
        ListingFilter filter = new ListingFilter();
        filter.setStatus(ListingStatus.PENDING);
        filter.setLimit(100);
        filter.setOffset(0);
        return search(filter);
    }

    private void appendFilters(StringBuilder sql, List<Object> params, ListingFilter filter) {
        if (filter.getStatus() != null) {
            sql.append(" AND l.status = ?");
            params.add(filter.getStatus().name());
        } else if (filter.isPublicOnly()) {
            sql.append(" AND l.status = 'ACTIVE'");
        }

        if (filter.getQuery() != null && !filter.getQuery().isBlank()) {
            sql.append(" AND (l.title LIKE ? OR l.description LIKE ? OR l.course LIKE ?)");
            String q = "%" + filter.getQuery().trim() + "%";
            params.add(q);
            params.add(q);
            params.add(q);
        }

        if (filter.getCategoryId() != null && filter.getCategoryId() > 0) {
            sql.append(" AND (l.category_id = ? OR c.parent_id = ?)");
            params.add(filter.getCategoryId());
            params.add(filter.getCategoryId());
        }

        if (filter.getListingType() != null) {
            sql.append(" AND l.listing_type = ?");
            params.add(filter.getListingType().name());
        }

        if (filter.isFreeOnly()) {
            sql.append(" AND (l.listing_type = 'DONATE' OR l.price = 0)");
        }

        if (filter.isUnder500()) {
            sql.append(" AND l.price > 0 AND l.price <= 500");
        }

        if (filter.getHostel() != null && !filter.getHostel().isBlank()) {
            sql.append(" AND u.hostel LIKE ?");
            params.add("%" + filter.getHostel().trim() + "%");
        }

        if (filter.getCourse() != null && !filter.getCourse().isBlank()) {
            sql.append(" AND l.course LIKE ?");
            params.add("%" + filter.getCourse().trim() + "%");
        }

        if (filter.getSemester() != null && !filter.getSemester().isBlank()) {
            sql.append(" AND l.semester_tag = ?");
            params.add(filter.getSemester().trim());
        }

        if (filter.getUserId() != null) {
            sql.append(" AND l.user_id = ?");
            params.add(filter.getUserId());
        }

        if (filter.getSellerName() != null && !filter.getSellerName().isBlank()) {
            sql.append(" AND u.name LIKE ?");
            params.add("%" + filter.getSellerName().trim() + "%");
        }
    }

    private void setParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            Object p = params.get(i);
            if (p instanceof String s) ps.setString(i + 1, s);
            else if (p instanceof Integer n) ps.setInt(i + 1, n);
            else if (p instanceof ListingStatus ls) ps.setString(i + 1, ls.name());
        }
    }

    private Listing mapRow(ResultSet rs) throws SQLException {
        Listing l = new Listing();
        l.setId(rs.getInt("id"));
        l.setUserId(rs.getInt("user_id"));
        l.setCategoryId(rs.getInt("category_id"));
        l.setTitle(rs.getString("title"));
        l.setDescription(rs.getString("description"));
        l.setListingType(ListingType.valueOf(rs.getString("listing_type")));
        l.setConditionType(ConditionType.valueOf(rs.getString("condition_type")));
        l.setPrice(rs.getBigDecimal("price"));
        l.setBarterWanted(rs.getString("barter_wanted"));
        l.setPickupLocation(rs.getString("pickup_location"));
        l.setCourse(rs.getString("course"));
        l.setSemesterTag(rs.getString("semester_tag"));
        l.setStatus(ListingStatus.valueOf(rs.getString("status")));
        l.setViews(rs.getInt("views"));
        l.setCreatedAt(rs.getTimestamp("created_at"));
        l.setSellerName(rs.getString("seller_name"));
        l.setSellerEmail(rs.getString("seller_email"));
        l.setSellerHostel(rs.getString("seller_hostel"));
        l.setCategoryName(rs.getString("category_name"));
        l.setPrimaryImage(rs.getString("primary_image"));
        return l;
    }
}
