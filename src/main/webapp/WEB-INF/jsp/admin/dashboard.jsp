<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Admin Dashboard"/>
<jsp:include page="../includes/header.jsp"/>

<div class="container" style="padding:2rem 0;">
    <h1 class="section-title">Admin Dashboard</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <c:if test="${not empty stats}">
        <div class="stats-row">
            <div class="stat-card"><h3>${stats.totalUsers}</h3><p>Verified Students</p></div>
            <div class="stat-card"><h3>${stats.activeListings}</h3><p>Active Listings</p></div>
            <div class="stat-card"><h3>${stats.completedDeals}</h3><p>Completed Deals</p></div>
            <div class="stat-card"><h3>₹<fmt:formatNumber value="${stats.moneySaved}" groupingUsed="true"/></h3><p>Platform Savings</p></div>
        </div>
    </c:if>

    <div class="admin-section">
        <h2>Pending User Verifications</h2>
        <div class="table-wrap">
            <table>
                <thead><tr><th>Name</th><th>Email</th><th>College</th><th>Action</th></tr></thead>
                <tbody>
                    <c:forEach var="u" items="${pendingUsers}">
                        <tr>
                            <td><c:out value="${u.name}"/></td>
                            <td><c:out value="${u.email}"/></td>
                            <td><c:out value="${u.college}"/></td>
                            <td>
                                <form method="post" class="inline-form">
                                    <input type="hidden" name="action" value="verify">
                                    <input type="hidden" name="userId" value="${u.id}">
                                    <button type="submit" class="btn btn-primary btn-sm">Verify</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty pendingUsers}">
                        <tr><td colspan="4" style="text-align:center;color:var(--muted);">No pending verifications.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <div class="admin-section">
        <h2>Pending Listings</h2>
        <div class="table-wrap">
            <table>
                <thead><tr><th>Title</th><th>Type</th><th>Seller</th><th>Action</th></tr></thead>
                <tbody>
                    <c:forEach var="l" items="${pendingListings}">
                        <tr>
                            <td><c:out value="${l.title}"/></td>
                            <td>${l.listingType.displayName}</td>
                            <td><c:out value="${l.sellerName}"/></td>
                            <td>
                                <form method="post" class="inline-form">
                                    <input type="hidden" name="action" value="approveListing">
                                    <input type="hidden" name="listingId" value="${l.id}">
                                    <button type="submit" class="btn btn-primary btn-sm">Approve</button>
                                </form>
                                <form method="post" class="inline-form">
                                    <input type="hidden" name="action" value="rejectListing">
                                    <input type="hidden" name="listingId" value="${l.id}">
                                    <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty pendingListings}">
                        <tr><td colspan="4" style="text-align:center;color:var(--muted);">No pending listings.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <div class="admin-section">
        <h2>Pending Reports</h2>
        <div class="table-wrap">
            <table>
                <thead><tr><th>Reporter</th><th>Listing</th><th>Reason</th><th>Action</th></tr></thead>
                <tbody>
                    <c:forEach var="r" items="${pendingReports}">
                        <tr>
                            <td><c:out value="${r.reporterName}"/></td>
                            <td><c:out value="${r.listingTitle}"/></td>
                            <td><c:out value="${r.reason}"/></td>
                            <td>
                                <form method="post" class="inline-form">
                                    <input type="hidden" name="action" value="resolveReport">
                                    <input type="hidden" name="reportId" value="${r.id}">
                                    <input type="hidden" name="status" value="RESOLVED">
                                    <input type="hidden" name="adminNote" value="Reviewed and resolved">
                                    <button type="submit" class="btn btn-primary btn-sm">Resolve</button>
                                </form>
                                <form method="post" class="inline-form">
                                    <input type="hidden" name="action" value="resolveReport">
                                    <input type="hidden" name="reportId" value="${r.id}">
                                    <input type="hidden" name="status" value="DISMISSED">
                                    <input type="hidden" name="adminNote" value="Dismissed">
                                    <button type="submit" class="btn btn-outline btn-sm">Dismiss</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty pendingReports}">
                        <tr><td colspan="4" style="text-align:center;color:var(--muted);">No pending reports.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <div class="admin-section">
        <h2>Manage Students</h2>
        <div class="table-wrap">
            <table>
                <thead><tr><th>Name</th><th>Email</th><th>Verified</th><th>Status</th><th>Action</th></tr></thead>
                <tbody>
                    <c:forEach var="u" items="${allStudents}">
                        <tr>
                            <td><c:out value="${u.name}"/></td>
                            <td><c:out value="${u.email}"/></td>
                            <td>${u.verified ? 'Yes' : 'No'}</td>
                            <td>${u.suspended ? 'Suspended' : 'Active'}</td>
                            <td>
                                <c:if test="${!u.suspended}">
                                    <form method="post" class="inline-form">
                                        <input type="hidden" name="action" value="suspend">
                                        <input type="hidden" name="userId" value="${u.id}">
                                        <button type="submit" class="btn btn-danger btn-sm">Suspend</button>
                                    </form>
                                </c:if>
                                <c:if test="${u.suspended}">
                                    <form method="post" class="inline-form">
                                        <input type="hidden" name="action" value="unsuspend">
                                        <input type="hidden" name="userId" value="${u.id}">
                                        <button type="submit" class="btn btn-primary btn-sm">Unsuspend</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp"/>
