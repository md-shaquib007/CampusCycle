<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Browse Listings"/>
<jsp:include page="includes/header.jsp"/>

<div class="container" style="padding-top:2rem;padding-bottom:2rem;">
    <h1 class="section-title">Browse Marketplace</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <form method="get" action="${pageContext.request.contextPath}/listings" class="search-bar">
        <input type="text" name="q" value="${filter.query}" placeholder="Search items, courses...">
        <select name="categoryId">
            <option value="">All Categories</option>
            <c:forEach var="cat" items="${categories}">
                <c:if test="${cat.parentId != null}">
                    <option value="${cat.id}" ${filter.categoryId == cat.id ? 'selected' : ''}>
                        <c:out value="${cat.name}"/>
                    </option>
                </c:if>
            </c:forEach>
        </select>
        <select name="type">
            <option value="">All Types</option>
            <c:forEach var="t" items="${listingTypes}">
                <option value="${t.name()}" ${filter.listingType == t ? 'selected' : ''}>${t.displayName}</option>
            </c:forEach>
        </select>
        <button type="submit" class="btn btn-primary">Search</button>
    </form>

    <div class="filters">
        <label><input type="checkbox" name="free" form="filterForm" ${filter.freeOnly ? 'checked' : ''} onchange="document.getElementById('filterForm').submit()"> Free / Donate</label>
        <label><input type="checkbox" name="under500" form="filterForm" ${filter.under500 ? 'checked' : ''} onchange="document.getElementById('filterForm').submit()"> Under ₹500</label>
        <input type="text" name="hostel" form="filterForm" value="${filter.hostel}" placeholder="Hostel filter" style="padding:0.4rem 0.6rem;border:1px solid var(--border);border-radius:6px;">
        <button type="submit" form="filterForm" class="btn btn-sm btn-outline">Apply Filters</button>
    </div>
    <form id="filterForm" method="get" action="${pageContext.request.contextPath}/listings" style="display:none;">
        <input type="hidden" name="q" value="${filter.query}">
        <input type="hidden" name="categoryId" value="${filter.categoryId}">
        <input type="hidden" name="type" value="${filter.listingType != null ? filter.listingType.name() : ''}">
    </form>

    <p style="color:var(--muted);margin-bottom:1rem;">${total} listing(s) found</p>

    <div class="grid">
        <c:forEach var="item" items="${listings}">
            <a href="${pageContext.request.contextPath}/listing?id=${item.id}" class="card" style="text-decoration:none;color:inherit;">
                <c:choose>
                    <c:when test="${not empty item.primaryImage}">
                        <img src="${pageContext.request.contextPath}/${item.primaryImage}" alt="" class="card-img" style="height:180px;object-fit:cover;">
                    </c:when>
                    <c:otherwise><div class="card-img">📦</div></c:otherwise>
                </c:choose>
                <div class="card-body">
                    <span class="badge badge-${item.listingType == 'SELL' ? 'sell' : item.listingType == 'BARTER' ? 'barter' : item.listingType == 'DONATE' ? 'donate' : 'buy'}">${item.listingType.displayName}</span>
                    <h3 class="card-title"><c:out value="${item.title}"/></h3>
                    <p class="card-meta"><c:out value="${item.categoryName}"/> · <c:out value="${item.sellerHostel}"/></p>
                    <c:choose>
                        <c:when test="${item.free}"><span class="price price-free">FREE</span></c:when>
                        <c:otherwise><span class="price">₹<fmt:formatNumber value="${item.price}"/></span></c:otherwise>
                    </c:choose>
                </div>
            </a>
        </c:forEach>
    </div>

    <c:if test="${totalPages > 1}">
        <div style="text-align:center;margin:2rem 0;">
            <c:forEach begin="1" end="${totalPages}" var="p">
                <a href="?page=${p}&q=${filter.query}&categoryId=${filter.categoryId}&type=${filter.listingType != null ? filter.listingType.name() : ''}"
                   class="btn btn-sm ${p == currentPage ? 'btn-primary' : 'btn-outline'}" style="margin:0 0.2rem;">${p}</a>
            </c:forEach>
        </div>
    </c:if>
</div>

<jsp:include page="includes/footer.jsp"/>
