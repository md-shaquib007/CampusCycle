<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Home"/>
<jsp:include page="includes/header.jsp"/>

<section class="hero">
    <div class="container">
        <h1>Reuse. Exchange. Save Money.</h1>
        <p>CampusCycle is your college-exclusive marketplace for buying, selling, bartering, and donating campus essentials.</p>
        <div style="margin-top:1.5rem;">
            <a href="${pageContext.request.contextPath}/listings" class="btn btn-primary">Browse Listings</a>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-outline" style="margin-left:0.5rem;color:white;border-color:white;">Join Now</a>
        </div>
    </div>
</section>

<div class="container">
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
                <p>Money Saved</p>
            </div>
            <div class="stat-card">
                <h3><fmt:formatNumber value="${stats.totalDonations}" groupingUsed="true"/></h3>
                <p>Donations</p>
            </div>
            <div class="stat-card">
                <h3><fmt:formatNumber value="${stats.successfulBarterDeals}" groupingUsed="true"/></h3>
                <p>Barter Deals</p>
            </div>
        </div>
    </c:if>

    <h2 class="section-title">Recent Listings</h2>
    <div class="grid">
        <c:forEach var="item" items="${listings}">
            <a href="${pageContext.request.contextPath}/listing?id=${item.id}" class="card" style="text-decoration:none;color:inherit;">
                <c:choose>
                    <c:when test="${not empty item.primaryImage}">
                        <img src="${pageContext.request.contextPath}/${item.primaryImage}" alt="" class="card-img" style="height:180px;object-fit:cover;">
                    </c:when>
                    <c:otherwise>
                        <div class="card-img">📦</div>
                    </c:otherwise>
                </c:choose>
                <div class="card-body">
                    <span class="badge badge-${item.listingType == 'SELL' ? 'sell' : item.listingType == 'BARTER' ? 'barter' : item.listingType == 'DONATE' ? 'donate' : 'buy'}">
                        ${item.listingType.displayName}
                    </span>
                    <h3 class="card-title"><c:out value="${item.title}"/></h3>
                    <p class="card-meta"><c:out value="${item.categoryName}"/> · <c:out value="${item.sellerName}"/></p>
                    <c:choose>
                        <c:when test="${item.free}">
                            <span class="price price-free">FREE</span>
                        </c:when>
                        <c:otherwise>
                            <span class="price">₹<fmt:formatNumber value="${item.price}"/></span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </a>
        </c:forEach>
        <c:if test="${empty listings}">
            <p>No listings yet. Be the first to post!</p>
        </c:if>
    </div>

    <div style="text-align:center;margin:2rem 0;">
        <a href="${pageContext.request.contextPath}/listings" class="btn btn-outline">View All Listings</a>
    </div>
</div>

<jsp:include page="includes/footer.jsp"/>
