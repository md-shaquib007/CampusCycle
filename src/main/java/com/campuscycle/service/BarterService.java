package com.campuscycle.service;

import com.campuscycle.dao.BarterDAO;
import com.campuscycle.dao.ListingDAO;
import com.campuscycle.dao.SustainabilityDAO;
import com.campuscycle.model.BarterProposal;
import com.campuscycle.model.Listing;
import com.campuscycle.model.ListingStatus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.campuscycle.util.DBConnection;

public class BarterService {
    private final BarterDAO barterDAO = new BarterDAO();
    private final ListingDAO listingDAO = new ListingDAO();
    private final SustainabilityDAO sustainabilityDAO = new SustainabilityDAO();

    public String propose(int proposerId, int targetListingId, int offeredListingId, String message) throws SQLException {
        Optional<Listing> targetOpt = listingDAO.findById(targetListingId);
        Optional<Listing> offeredOpt = listingDAO.findById(offeredListingId);

        if (targetOpt.isEmpty() || offeredOpt.isEmpty()) {
            return "One or both listings not found.";
        }
        Listing target = targetOpt.get();
        Listing offered = offeredOpt.get();

        if (offered.getUserId() != proposerId) {
            return "You can only offer your own listings.";
        }
        if (target.getUserId() == proposerId) {
            return "You cannot barter with your own listing.";
        }
        if (target.getStatus() != ListingStatus.ACTIVE || offered.getStatus() != ListingStatus.ACTIVE) {
            return "Both listings must be active.";
        }

        BarterProposal proposal = new BarterProposal();
        proposal.setProposerId(proposerId);
        proposal.setTargetListingId(targetListingId);
        proposal.setOfferedListingId(offeredListingId);
        proposal.setMessage(message);
        barterDAO.create(proposal);
        return null;
    }

    public List<BarterProposal> getForUser(int userId) throws SQLException {
        return barterDAO.findForUser(userId);
    }

    public String accept(int proposalId, int userId) throws SQLException {
        Optional<BarterProposal> proposalOpt = barterDAO.findById(proposalId);
        if (proposalOpt.isEmpty()) {
            return "Proposal not found.";
        }
        BarterProposal proposal = proposalOpt.get();

        Optional<Listing> targetOpt = listingDAO.findById(proposal.getTargetListingId());
        Optional<Listing> offeredOpt = listingDAO.findById(proposal.getOfferedListingId());
        if (targetOpt.isEmpty() || offeredOpt.isEmpty()) {
            return "Listings no longer available.";
        }

        Listing target = targetOpt.get();
        Listing offered = offeredOpt.get();

        boolean isTargetOwner = target.getUserId() == userId;
        boolean isOfferedOwner = offered.getUserId() == userId;
        if (!isTargetOwner && !isOfferedOwner) {
            return "You are not authorized to accept this proposal.";
        }
        if (!"PENDING".equals(proposal.getStatus())) {
            return "Proposal is no longer pending.";
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                barterDAO.updateStatus(proposalId, "COMPLETED");
                listingDAO.updateStatus(target.getId(), ListingStatus.EXCHANGED);
                listingDAO.updateStatus(offered.getId(), ListingStatus.EXCHANGED);
                sustainabilityDAO.recordTransaction(
                        target.getId(), target.getUserId(), offered.getUserId(),
                        "BARTER", BigDecimal.ZERO, proposalId);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
        return null;
    }

    public String reject(int proposalId, int userId) throws SQLException {
        Optional<BarterProposal> proposalOpt = barterDAO.findById(proposalId);
        if (proposalOpt.isEmpty()) {
            return "Proposal not found.";
        }
        BarterProposal proposal = proposalOpt.get();
        Optional<Listing> targetOpt = listingDAO.findById(proposal.getTargetListingId());
        if (targetOpt.isEmpty() || targetOpt.get().getUserId() != userId) {
            return "You are not authorized to reject this proposal.";
        }
        if (!"PENDING".equals(proposal.getStatus())) {
            return "Proposal is no longer pending.";
        }
        barterDAO.updateStatus(proposalId, "REJECTED");
        return null;
    }
}
