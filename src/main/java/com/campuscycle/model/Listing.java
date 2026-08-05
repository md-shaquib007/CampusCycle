package com.campuscycle.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Listing {
    private int id;
    private int userId;
    private int categoryId;
    private String title;
    private String description;
    private ListingType listingType;
    private ConditionType conditionType;
    private BigDecimal price;
    private String barterWanted;
    private String pickupLocation;
    private String course;
    private String semesterTag;
    private ListingStatus status;
    private int views;
    private Timestamp createdAt;

    private String sellerName;
    private String sellerEmail;
    private String sellerHostel;
    private String categoryName;
    private String primaryImage;
    private List<String> images = new ArrayList<>();
    private boolean wishlisted;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ListingType getListingType() { return listingType; }
    public void setListingType(ListingType listingType) { this.listingType = listingType; }

    public ConditionType getConditionType() { return conditionType; }
    public void setConditionType(ConditionType conditionType) { this.conditionType = conditionType; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getBarterWanted() { return barterWanted; }
    public void setBarterWanted(String barterWanted) { this.barterWanted = barterWanted; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getSemesterTag() { return semesterTag; }
    public void setSemesterTag(String semesterTag) { this.semesterTag = semesterTag; }

    public ListingStatus getStatus() { return status; }
    public void setStatus(ListingStatus status) { this.status = status; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public String getSellerEmail() { return sellerEmail; }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }

    public String getSellerHostel() { return sellerHostel; }
    public void setSellerHostel(String sellerHostel) { this.sellerHostel = sellerHostel; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getPrimaryImage() { return primaryImage; }
    public void setPrimaryImage(String primaryImage) { this.primaryImage = primaryImage; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public boolean isWishlisted() { return wishlisted; }
    public void setWishlisted(boolean wishlisted) { this.wishlisted = wishlisted; }

    public boolean isFree() {
        return listingType == ListingType.DONATE
                || (price != null && price.compareTo(BigDecimal.ZERO) == 0);
    }
}
