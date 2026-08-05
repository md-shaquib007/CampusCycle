package com.campuscycle.model;

import java.sql.Timestamp;

public class BarterProposal {
    private int id;
    private int proposerId;
    private int targetListingId;
    private int offeredListingId;
    private String message;
    private String status;
    private Timestamp createdAt;
    private Timestamp completedAt;

    private String proposerName;
    private String targetTitle;
    private String offeredTitle;
    private String targetImage;
    private String offeredImage;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProposerId() { return proposerId; }
    public void setProposerId(int proposerId) { this.proposerId = proposerId; }

    public int getTargetListingId() { return targetListingId; }
    public void setTargetListingId(int targetListingId) { this.targetListingId = targetListingId; }

    public int getOfferedListingId() { return offeredListingId; }
    public void setOfferedListingId(int offeredListingId) { this.offeredListingId = offeredListingId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getCompletedAt() { return completedAt; }
    public void setCompletedAt(Timestamp completedAt) { this.completedAt = completedAt; }

    public String getProposerName() { return proposerName; }
    public void setProposerName(String proposerName) { this.proposerName = proposerName; }

    public String getTargetTitle() { return targetTitle; }
    public void setTargetTitle(String targetTitle) { this.targetTitle = targetTitle; }

    public String getOfferedTitle() { return offeredTitle; }
    public void setOfferedTitle(String offeredTitle) { this.offeredTitle = offeredTitle; }

    public String getTargetImage() { return targetImage; }
    public void setTargetImage(String targetImage) { this.targetImage = targetImage; }

    public String getOfferedImage() { return offeredImage; }
    public void setOfferedImage(String offeredImage) { this.offeredImage = offeredImage; }
}
