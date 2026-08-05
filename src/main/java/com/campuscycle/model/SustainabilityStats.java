package com.campuscycle.model;

import java.math.BigDecimal;

public class SustainabilityStats {
    private int itemsReused;
    private BigDecimal moneySaved;
    private int wastePreventedKg;
    private int totalDonations;
    private int successfulBarterDeals;
    private int activeListings;
    private int totalUsers;
    private int completedDeals;

    public int getItemsReused() { return itemsReused; }
    public void setItemsReused(int itemsReused) { this.itemsReused = itemsReused; }

    public BigDecimal getMoneySaved() { return moneySaved; }
    public void setMoneySaved(BigDecimal moneySaved) { this.moneySaved = moneySaved; }

    public int getWastePreventedKg() { return wastePreventedKg; }
    public void setWastePreventedKg(int wastePreventedKg) { this.wastePreventedKg = wastePreventedKg; }

    public int getTotalDonations() { return totalDonations; }
    public void setTotalDonations(int totalDonations) { this.totalDonations = totalDonations; }

    public int getSuccessfulBarterDeals() { return successfulBarterDeals; }
    public void setSuccessfulBarterDeals(int successfulBarterDeals) { this.successfulBarterDeals = successfulBarterDeals; }

    public int getActiveListings() { return activeListings; }
    public void setActiveListings(int activeListings) { this.activeListings = activeListings; }

    public int getTotalUsers() { return totalUsers; }
    public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }

    public int getCompletedDeals() { return completedDeals; }
    public void setCompletedDeals(int completedDeals) { this.completedDeals = completedDeals; }
}
