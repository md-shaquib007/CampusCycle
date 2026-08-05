package com.campuscycle.service;

import com.campuscycle.dao.ReportDAO;
import com.campuscycle.model.Report;

import java.sql.SQLException;

public class ReportService {
    private final ReportDAO reportDAO = new ReportDAO();

    public String submit(int reporterId, Integer listingId, Integer reportedUserId,
                         String reason, String description) throws SQLException {
        if (reason == null || reason.isBlank()) {
            return "Please select a reason.";
        }
        Report report = new Report();
        report.setReporterId(reporterId);
        report.setListingId(listingId);
        report.setReportedUserId(reportedUserId);
        report.setReason(reason);
        report.setDescription(description);
        reportDAO.create(report);
        return null;
    }
}
