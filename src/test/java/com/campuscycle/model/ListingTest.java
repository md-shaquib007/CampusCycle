package com.campuscycle.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ListingTest {

    @Test
    @DisplayName("isFree returns true for DONATE type or price 0")
    void testIsFree() {
        Listing listing = new Listing();
        listing.setListingType(ListingType.DONATE);
        assertTrue(listing.isFree());

        listing.setListingType(ListingType.SELL);
        listing.setPrice(BigDecimal.ZERO);
        assertTrue(listing.isFree());

        listing.setPrice(new BigDecimal("150.00"));
        assertFalse(listing.isFree());
    }

    @Test
    @DisplayName("Listing properties set and get correctly")
    void testListingProperties() {
        Listing listing = new Listing();
        listing.setId(101);
        listing.setTitle("Engineering Physics Book");
        listing.setSellerName("Jane Student");

        assertEquals(101, listing.getId());
        assertEquals("Engineering Physics Book", listing.getTitle());
        assertEquals("Jane Student", listing.getSellerName());
    }
}
