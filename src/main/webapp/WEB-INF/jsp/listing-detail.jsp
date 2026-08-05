<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="${listing.title}"/>
<jsp:include page="includes/header.jsp"/>

<div class="container" style="padding-top:2rem;padding-bottom:2rem;">
    <c:if test="${empty listing}">
        <div class="alert alert-error">Listing not found.</div>
    </c:if>

    <c:if test="${not empty listing}">
        <div class="detail-grid">
            <div>
                <c:choose>
                    <c:when test="${not empty listing.primaryImage}">
                        <img src="${pageContext.request.contextPath}/${listing.primaryImage}" alt="" class="detail-img">
                    </c:when>
                    <c:otherwise>
                        <div class="detail-img" style="display:flex;align-items:center;justify-content:center;font-size:5rem;background:#e8f0eb;">📦</div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div>
                <span class="badge badge-${listing.listingType == 'SELL' ? 'sell' : listing.listingType == 'BARTER' ? 'barter' : listing.listingType == 'DONATE' ? 'donate' : 'buy'}">${listing.listingType.displayName}</span>
                <h1 style="margin:0.5rem 0;"><c:out value="${listing.title}"/></h1>
                <p style="color:var(--muted);"><c:out value="${listing.categoryName}"/> · Condition: ${listing.conditionType}</p>

                <c:choose>
                    <c:when test="${listing.free}"><p class="price price-free" style="font-size:1.5rem;">FREE / Donation</p></c:when>
                    <c:otherwise><p class="price" style="font-size:1.5rem;">₹<fmt:formatNumber value="${listing.price}"/></p></c:otherwise>
                </c:choose>

                <p style="margin:1rem 0;"><c:out value="${listing.description}"/></p>

                <c:if test="${not empty listing.barterWanted}">
                    <div class="alert alert-success"><strong>Looking for:</strong> <c:out value="${listing.barterWanted}"/></div>
                </c:if>

                <p><strong>Pickup:</strong> <c:out value="${listing.pickupLocation}"/></p>
                <p><strong>Seller:</strong> <c:out value="${listing.sellerName}"/> (<c:out value="${listing.sellerHostel}"/>)</p>
                <p style="font-size:0.85rem;color:var(--muted);">${listing.views} views</p>

                <c:if test="${sessionScope.currentUser != null && sessionScope.currentUser.id != listing.userId}">
                    <div style="margin-top:1.5rem;display:flex;gap:0.5rem;flex-wrap:wrap;">
                        <form method="post" action="${pageContext.request.contextPath}/wishlist" style="display:inline;">
                            <input type="hidden" name="listingId" value="${listing.id}">
                            <button type="submit" class="btn btn-outline btn-sm">${listing.wishlisted ? '♥ Saved' : '♡ Save'}</button>
                        </form>
                    </div>

                    <div style="margin-top:2rem;border-top:1px solid var(--border);padding-top:1.5rem;">
                        <h3>Request Chat</h3>
                        <form method="post" action="${pageContext.request.contextPath}/chat">
                            <input type="hidden" name="action" value="send">
                            <input type="hidden" name="listingId" value="${listing.id}">
                            <div class="form-group">
                                <textarea name="message" placeholder="Hi, I'm interested in this item..." required></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary btn-sm">Send Chat Request</button>
                        </form>
                    </div>

                    <c:if test="${listing.listingType == 'BARTER' && not empty myListings}">
                        <div style="margin-top:2rem;border-top:1px solid var(--border);padding-top:1.5rem;">
                            <h3>⭐ Propose Barter Exchange</h3>
                            <form method="post" action="${pageContext.request.contextPath}/barter">
                                <input type="hidden" name="action" value="propose">
                                <input type="hidden" name="targetListingId" value="${listing.id}">
                                <div class="form-group">
                                    <label>Your item to offer</label>
                                    <select name="offeredListingId" required>
                                        <c:forEach var="mine" items="${myListings}">
                                            <option value="${mine.id}"><c:out value="${mine.title}"/></option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <textarea name="message" placeholder="I'll give you my calculator + ₹300..."></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary btn-sm">Send Barter Proposal</button>
                            </form>
                        </div>
                    </c:if>

                    <div style="margin-top:2rem;border-top:1px solid var(--border);padding-top:1.5rem;">
                        <h3>Report Listing</h3>
                        <form method="post" action="${pageContext.request.contextPath}/report">
                            <input type="hidden" name="listingId" value="${listing.id}">
                            <input type="hidden" name="reportedUserId" value="${listing.userId}">
                            <div class="form-group">
                                <select name="reason" required>
                                    <option value="">Select reason</option>
                                    <option value="FAKE_PRODUCT">Fake Product</option>
                                    <option value="SPAM">Spam</option>
                                    <option value="MISLEADING">Misleading Info</option>
                                    <option value="INAPPROPRIATE">Inappropriate</option>
                                    <option value="OTHER">Other</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <textarea name="description" placeholder="Details..."></textarea>
                            </div>
                            <button type="submit" class="btn btn-danger btn-sm">Report</button>
                        </form>
                    </div>
                </c:if>
            </div>
        </div>
    </c:if>
</div>

<jsp:include page="includes/footer.jsp"/>
