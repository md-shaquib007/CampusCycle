<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="My Profile"/>
<jsp:include page="includes/header.jsp"/>

<div class="container" style="padding:2rem 0;">
    <h1 class="section-title">My Profile</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <div style="display:grid;grid-template-columns:1fr 1fr;gap:2rem;">
        <div class="form-card" style="margin:0;max-width:none;">
            <h3 style="margin-bottom:1rem;">Edit Profile</h3>
            <form method="post" action="${pageContext.request.contextPath}/profile">
                <div class="form-group">
                    <label>Name</label>
                    <input type="text" name="name" value="${sessionScope.currentUser.name}" required>
                </div>
                <div class="form-group">
                    <label>Department</label>
                    <input type="text" name="department" value="${sessionScope.currentUser.department}">
                </div>
                <div class="form-group">
                    <label>Semester</label>
                    <input type="text" name="semester" value="${sessionScope.currentUser.semester}">
                </div>
                <div class="form-group">
                    <label>Hostel</label>
                    <input type="text" name="hostel" value="${sessionScope.currentUser.hostel}">
                </div>
                <div class="form-group">
                    <label>Phone</label>
                    <input type="tel" name="phone" value="${sessionScope.currentUser.phone}">
                </div>
                <button type="submit" class="btn btn-primary">Save</button>
            </form>
        </div>

        <div class="card" style="padding:1.5rem;">
            <h3>Account Info</h3>
            <p><strong>Email:</strong> <c:out value="${sessionScope.currentUser.email}"/></p>
            <p><strong>College:</strong> <c:out value="${sessionScope.currentUser.college}"/></p>
            <p><strong>Rating:</strong> ⭐ <fmt:formatNumber value="${sessionScope.currentUser.ratingAvg}" maxFractionDigits="1"/> (${sessionScope.currentUser.ratingCount} reviews)</p>
            <p><strong>Verified:</strong> ${sessionScope.currentUser.verified ? 'Yes' : 'Pending'}</p>
            <a href="${pageContext.request.contextPath}/listing/create" class="btn btn-primary btn-sm" style="margin-top:1rem;">+ New Listing</a>
        </div>
    </div>

    <h2 class="section-title">My Listings</h2>
    <div class="table-wrap">
        <table>
            <thead>
                <tr><th>Title</th><th>Type</th><th>Price</th><th>Status</th><th>Views</th></tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${myListings}">
                    <tr>
                        <td><a href="${pageContext.request.contextPath}/listing?id=${item.id}"><c:out value="${item.title}"/></a></td>
                        <td>${item.listingType.displayName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${item.free}">FREE</c:when>
                                <c:otherwise>₹<fmt:formatNumber value="${item.price}"/></c:otherwise>
                            </c:choose>
                        </td>
                        <td><span class="badge">${item.status}</span></td>
                        <td>${item.views}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty myListings}">
                    <tr><td colspan="5" style="text-align:center;color:var(--muted);">No listings yet.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="includes/footer.jsp"/>
