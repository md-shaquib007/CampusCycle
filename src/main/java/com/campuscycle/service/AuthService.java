package com.campuscycle.service;

import com.campuscycle.dao.UserDAO;
import com.campuscycle.model.User;
import com.campuscycle.model.UserRole;
import com.campuscycle.util.AppConfig;
import com.campuscycle.util.PasswordUtil;
import com.campuscycle.util.TokenUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public String register(User user, String plainPassword) throws SQLException {
        if (user.getName() == null || user.getName().isBlank()) {
            return "Name is required.";
        }
        if (user.getCollege() == null || user.getCollege().isBlank()) {
            return "College name is required.";
        }
        String normalizedEmail = user.getEmail() == null ? null : user.getEmail().trim().toLowerCase();
        if (!isValidCollegeEmail(normalizedEmail)) {
            return "Please use a valid college email address.";
        }
        if (plainPassword == null || plainPassword.length() < 6) {
            return "Password must be at least 6 characters.";
        }
        if (userDAO.findByEmail(user.getEmail()).isPresent()) {
            return "Email already registered.";
        }

        user.setEmail(normalizedEmail);
        user.setPasswordHash(PasswordUtil.hash(plainPassword));
        user.setRole(UserRole.STUDENT);
        user.setVerified(false);
        userDAO.create(user);
        return null;
    }

    public Optional<User> login(String email, String password) throws SQLException {
        Optional<User> userOpt = userDAO.findByEmail(email);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        User user = userOpt.get();
        if (user.isSuspended()) {
            return Optional.empty();
        }
        if (!PasswordUtil.verify(password, user.getPasswordHash())) {
            return Optional.empty();
        }
        if (!user.isAdmin() && !user.isVerified()) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public String requestPasswordReset(String email) throws SQLException {
        Optional<User> userOpt = userDAO.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "If the email exists, a reset link has been generated.";
        }
        String token = TokenUtil.generateToken();
        Timestamp expires = Timestamp.from(Instant.now().plus(1, ChronoUnit.HOURS));
        userDAO.saveResetToken(userOpt.get().getId(), token, expires);
        return token;
    }

    public String resetPassword(String token, String newPassword) throws SQLException {
        if (newPassword == null || newPassword.length() < 6) {
            return "Password must be at least 6 characters.";
        }
        Optional<Integer> userIdOpt = userDAO.findUserIdByValidToken(token);
        if (userIdOpt.isEmpty()) {
            return "Invalid or expired reset token.";
        }
        userDAO.updatePassword(userIdOpt.get(), PasswordUtil.hash(newPassword));
        userDAO.markTokenUsed(token);
        return null;
    }

    public void updateProfile(User user) throws SQLException {
        userDAO.updateProfile(user);
    }

    private boolean isValidCollegeEmail(String email) {
        if (email == null || !email.contains("@")) {
            return false;
        }
        String configuredDomains = AppConfig.get(
                "college.email.domains",
                AppConfig.get("college.email.domain", "*"));
        if ("*".equals(configuredDomains.trim())) {
            return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
        }
        return Arrays.stream(configuredDomains.split(","))
                .map(String::trim)
                .filter(domain -> !domain.isBlank())
                .map(domain -> domain.startsWith("@") ? domain : "@" + domain)
                .map(String::toLowerCase)
                .anyMatch(email.toLowerCase()::endsWith);
    }
}
