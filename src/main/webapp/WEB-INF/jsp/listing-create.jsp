<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Create Listing"/>
<jsp:include page="includes/header.jsp"/>

<div class="container" style="padding:2rem 0;">
    <div class="form-card" style="max-width:700px;">
        <h2 style="color:var(--primary);margin-bottom:1rem;">Create New Listing</h2>

        <c:if test="${not empty error}">
            <div class="alert alert-error"><c:out value="${error}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/listing/create" enctype="multipart/form-data">
            <div class="form-group">
                <label>Listing Type</label>
                <select name="listingType" required>
                    <c:forEach var="t" items="${listingTypes}">
                        <option value="${t.name()}">${t.displayName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Title</label>
                <input type="text" name="title" required placeholder="e.g. Scientific Calculator">
            </div>
            <div class="form-group">
                <label>Description</label>
                <textarea name="description" required placeholder="Describe your item..."></textarea>
            </div>
            <div class="form-group">
                <label>Category</label>
                <select name="categoryId" required>
                    <option value="">Select category</option>
                    <c:forEach var="cat" items="${categories}">
                        <c:if test="${cat.parentId != null}">
                            <option value="${cat.id}"><c:out value="${cat.name}"/></option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Condition</label>
                <select name="conditionType">
                    <option value="NEW">New</option>
                    <option value="LIKE_NEW">Like New</option>
                    <option value="GOOD" selected>Good</option>
                    <option value="FAIR">Fair</option>
                    <option value="POOR">Poor</option>
                </select>
            </div>
            <div class="form-group">
                <label>Price (₹) — leave 0 for barter/donate</label>
                <input type="number" name="price" min="0" step="0.01" value="0">
            </div>
            <div class="form-group">
                <label>Barter Wanted (if exchanging)</label>
                <input type="text" name="barterWanted" placeholder="e.g. Java Books">
            </div>
            <div class="form-group">
                <label>Pickup Location</label>
                <input type="text" name="pickupLocation" placeholder="e.g. Block A Hostel">
            </div>
            <div class="form-group">
                <label>Course (optional)</label>
                <input type="text" name="course" placeholder="e.g. Computer Science">
            </div>
            <div class="form-group">
                <label>Semester (optional)</label>
                <input type="text" name="semesterTag" placeholder="e.g. 3rd">
            </div>
            <div class="form-group">
                <label>Image</label>
                <input type="file" name="image" accept="image/*">
            </div>
            <button type="submit" class="btn btn-primary">Submit for Approval</button>
        </form>
    </div>
</div>

<jsp:include page="includes/footer.jsp"/>
