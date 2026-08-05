package com.campuscycle.dao;

import com.campuscycle.model.ListingStatus;
import com.campuscycle.model.ListingType;

public class ListingFilter {
    private String query;
    private Integer categoryId;
    private ListingType listingType;
    private ListingStatus status;
    private boolean freeOnly;
    private boolean under500;
    private String hostel;
    private String course;
    private String semester;
    private Integer userId;
    private String sellerName;
    private boolean publicOnly = true;
    private int limit = 12;
    private int offset = 0;

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public ListingType getListingType() { return listingType; }
    public void setListingType(ListingType listingType) { this.listingType = listingType; }

    public ListingStatus getStatus() { return status; }
    public void setStatus(ListingStatus status) { this.status = status; }

    public boolean isFreeOnly() { return freeOnly; }
    public void setFreeOnly(boolean freeOnly) { this.freeOnly = freeOnly; }

    public boolean isUnder500() { return under500; }
    public void setUnder500(boolean under500) { this.under500 = under500; }

    public String getHostel() { return hostel; }
    public void setHostel(String hostel) { this.hostel = hostel; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public boolean isPublicOnly() { return publicOnly; }
    public void setPublicOnly(boolean publicOnly) { this.publicOnly = publicOnly; }

    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }

    public int getOffset() { return offset; }
    public void setOffset(int offset) { this.offset = offset; }
}
