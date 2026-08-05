package com.campuscycle.service;

import com.campuscycle.dao.ChatRequestDAO;
import com.campuscycle.dao.ListingDAO;
import com.campuscycle.model.ChatRequest;
import com.campuscycle.model.Listing;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ChatService {
    private final ChatRequestDAO chatRequestDAO = new ChatRequestDAO();
    private final ListingDAO listingDAO = new ListingDAO();

    public String sendRequest(int requesterId, int listingId, String message) throws SQLException {
        Optional<Listing> listingOpt = listingDAO.findById(listingId);
        if (listingOpt.isEmpty()) {
            return "Listing not found.";
        }
        Listing listing = listingOpt.get();
        if (listing.getUserId() == requesterId) {
            return "You cannot request chat on your own listing.";
        }

        ChatRequest request = new ChatRequest();
        request.setListingId(listingId);
        request.setRequesterId(requesterId);
        request.setSellerId(listing.getUserId());
        request.setMessage(message);
        chatRequestDAO.create(request);
        return null;
    }

    public List<ChatRequest> getForUser(int userId) throws SQLException {
        return chatRequestDAO.findForUser(userId);
    }

    public void respond(int requestId, int sellerId, boolean accept) throws SQLException {
        Optional<ChatRequest> reqOpt = chatRequestDAO.findById(requestId);
        if (reqOpt.isPresent() && reqOpt.get().getSellerId() == sellerId) {
            chatRequestDAO.updateStatus(requestId, accept ? "ACCEPTED" : "REJECTED");
        }
    }
}
