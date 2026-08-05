<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Sustainability Impact"/>
<jsp:include page="includes/header.jsp"/>

<section class="hero">
    <div class="container">
        <h1>🌱 Sustainability Dashboard</h1>
        <p>See how CampusCycle is building a circular campus economy.</p>
    </div>
</section>

<div class="container" style="padding:2rem 0;">
    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <c:if test="${not empty stats}">
        <div class="stats-row">
            <div class="stat-card">
                <h3><fmt:formatNumber value="${stats.itemsReused}" groupingUsed="true"/></h3>
                <p>Items Reused</p>
            </div>
            <div class="stat-card">
                <h3>₹<fmt:formatNumber value="${stats.moneySaved}" groupingUsed="true"/></h3>
                <p>Money Saved by Students</p>
            </div>
            <div class="stat-card">
                <h3><fmt:formatNumber value="${stats.wastePreventedKg}" groupingUsed="true"/> kg</h3>
                <p>Estimated Waste Prevented</p>
            </div>
            <div class="stat-card">
                <h3><fmt:formatNumber value="${stats.totalDonations}" groupingUsed="true"/></h3>
                <p>Total Donations</p>
            </div>
            <div class="stat-card">
                <h3><fmt:formatNumber value="${stats.successfulBarterDeals}" groupingUsed="true"/></h3>
                <p>Successful Barter Deals</p>
            </div>
            <div class="stat-card">
                <h3><fmt:formatNumber value="${stats.activeListings}" groupingUsed="true"/></h3>
                <p>Active Listings</p>
            </div>
        </div>

        <div class="card" style="padding:2rem;text-align:center;margin-top:2rem;">
            <h2 style="color:var(--primary);">
                CampusCycle has helped students save ₹<fmt:formatNumber value="${stats.moneySaved}" groupingUsed="true"/>
                and reuse <fmt:formatNumber value="${stats.itemsReused}" groupingUsed="true"/> items.
            </h2>
            <p style="color:var(--muted);margin-top:0.5rem;">${stats.totalUsers} verified students · ${stats.completedDeals} completed deals</p>
        </div>
    </c:if>
</div>

<jsp:include page="includes/footer.jsp"/>
