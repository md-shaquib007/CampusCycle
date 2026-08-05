package com.campuscycle.service;

import com.campuscycle.dao.ListingDAO;
import com.campuscycle.dao.ReportDAO;
import com.campuscycle.dao.SustainabilityDAO;
import com.campuscycle.dao.UserDAO;
import com.campuscycle.model.Listing;
import com.campuscycle.model.Report;
import com.campuscycle.model.SustainabilityStats;
import com.campuscycle.model.User;

import java.sql.SQLException;
import java.util.List;

public class AdminService {
    private final UserDAO userDAO = new UserDAO();
    private final ListingDAO listingDAO = new ListingDAO();
    private final ReportDAO reportDAO = new ReportDAO();
    private final SustainabilityDAO sustainabilityDAO = new SustainabilityDAO();
    public List<User> getPendingUsers() throws SQLException {
        return userDAO.findAllPendingVerification();
    }

    public List<User> getAllStudents() throws SQLException {
        return userDAO.findAllStudents();
    }

    public void verifyUser(int userId) throws SQLException {
        userDAO.setVerified(userId, true);
    }

    public void suspendUser(int userId, boolean suspend) throws SQLException {
        userDAO.setSuspended(userId, suspend);
    }

    public List<Listing> getPendingListings() throws SQLException {
        return listingDAO.findPending();
    }

    public List<Report> getPendingReports() throws SQLException {
        return reportDAO.findPending();
    }

    public void resolveReport(int reportId, String status, String note) throws SQLException {
        reportDAO.resolve(reportId, status, note);
    }

    public SustainabilityStats getStats() throws SQLException {
        return sustainabilityDAO.getStats();
    }
}
