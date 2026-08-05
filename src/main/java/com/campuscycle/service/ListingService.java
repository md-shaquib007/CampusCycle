package com.campuscycle.service;

import com.campuscycle.dao.*;
import com.campuscycle.model.*;
import com.campuscycle.util.FileUploadUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ListingService {
    private final ListingDAO listingDAO = new ListingDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final WishlistDAO wishlistDAO = new WishlistDAO();

    public List<Category> getCategories() throws SQLException {
        return categoryDAO.findAll();
    }

    public List<Listing> search(ListingFilter filter, Integer currentUserId) throws SQLException {
        List<Listing> listings = listingDAO.search(filter);
        if (currentUserId != null) {
            for (Listing listing : listings) {
                listing.setWishlisted(wishlistDAO.exists(currentUserId, listing.getId()));
            }
        }
        return listings;
    }

    public int count(ListingFilter filter) throws SQLException {
        return listingDAO.countSearch(filter);
    }

    public Optional<Listing> getById(int id, Integer currentUserId) throws SQLException {
        Optional<Listing> listingOpt = listingDAO.findById(id);
        if (listingOpt.isPresent() && currentUserId != null) {
            listingOpt.get().setWishlisted(wishlistDAO.exists(currentUserId, id));
        }
        return listingOpt;
    }

    public String createFromRequest(HttpServletRequest request, ServletContext context, int userId) throws Exception {
        FileUploadUtil.UploadResult upload = FileUploadUtil.parse(request, context);

        String title = upload.getField("title");
        String description = upload.getField("description");
        String typeStr = upload.getField("listingType");
        String categoryStr = upload.getField("categoryId");
        String priceStr = upload.getField("price");

        if (title == null || title.isBlank() || description == null || description.isBlank()) {
            return "Title and description are required.";
        }
        if (categoryStr == null || categoryStr.isBlank()) {
            return "Please select a category.";
        }

        Listing listing = new Listing();
        listing.setUserId(userId);
        listing.setTitle(title.trim());
        listing.setDescription(description.trim());
        listing.setListingType(ListingType.fromString(typeStr));
        listing.setConditionType(ConditionType.fromString(upload.getField("conditionType")));
        listing.setCategoryId(Integer.parseInt(categoryStr));
        listing.setBarterWanted(upload.getField("barterWanted"));
        listing.setPickupLocation(upload.getField("pickupLocation"));
        listing.setCourse(upload.getField("course"));
        listing.setSemesterTag(upload.getField("semesterTag"));
        listing.setStatus(ListingStatus.PENDING);

        if (listing.getListingType() == ListingType.DONATE) {
            listing.setPrice(BigDecimal.ZERO);
        } else {
            try {
                listing.setPrice(priceStr != null && !priceStr.isBlank()
                        ? new BigDecimal(priceStr) : BigDecimal.ZERO);
            } catch (NumberFormatException e) {
                return "Invalid price format.";
            }
        }

        int listingId = listingDAO.create(listing);

        String imagePath = upload.getFile("image");
        if (imagePath != null) {
            listingDAO.addImage(listingId, imagePath, true);
        }

        return null;
    }

    public boolean toggleWishlist(int userId, int listingId) throws SQLException {
        if (wishlistDAO.exists(userId, listingId)) {
            wishlistDAO.remove(userId, listingId);
            return false;
        }
        wishlistDAO.add(userId, listingId);
        return true;
    }

    public List<Listing> getUserListings(int userId) throws SQLException {
        return listingDAO.findByUserId(userId);
    }

    public List<Listing> getActiveUserListings(int userId) throws SQLException {
        return listingDAO.findActiveByUserId(userId);
    }

    public void approve(int listingId) throws SQLException {
        listingDAO.updateStatus(listingId, ListingStatus.ACTIVE);
    }

    public void reject(int listingId) throws SQLException {
        listingDAO.updateStatus(listingId, ListingStatus.REJECTED);
    }

    public void view(int listingId) throws SQLException {
        listingDAO.incrementViews(listingId);
    }

    public List<Listing> getPending() throws SQLException {
        return listingDAO.findPending();
    }
}
