package com.campuscycle.model;

public enum ListingType {
    SELL, BUY_REQUEST, BARTER, DONATE;

    public static ListingType fromString(String value) {
        if (value == null) return SELL;
        return ListingType.valueOf(value.toUpperCase());
    }

    public String getDisplayName() {
        return switch (this) {
            case SELL -> "Sell";
            case BUY_REQUEST -> "Buy Request";
            case BARTER -> "Barter";
            case DONATE -> "Donate";
        };
    }
}
