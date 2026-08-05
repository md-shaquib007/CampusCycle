<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Register"/>
<jsp:include page="includes/header.jsp"/>

<div class="form-card" style="max-width:600px;">
    <h2 style="text-align:center;margin-bottom:1.5rem;color:var(--primary);">Join CampusCycle</h2>
    <p style="text-align:center;color:var(--muted);margin-bottom:1.5rem;font-size:0.9rem;">
        Register with your college email. Admin will verify your account.
    </p>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/register">
        <div class="form-group">
            <label>Full Name</label>
            <input type="text" name="name" value="${user.name}" required>
        </div>
        <div class="form-group">
            <label>College Email</label>
            <input type="email" name="email" value="${user.email}" required placeholder="you@college.edu">
        </div>
        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" required minlength="6">
        </div>
        <div class="form-group">
            <label>College Name</label>
            <input type="text" name="college" value="${user.college}" required>
        </div>
        <div class="form-group">
            <label>Department</label>
            <input type="text" name="department" value="${user.department}">
        </div>
        <div class="form-group">
            <label>Semester</label>
            <input type="text" name="semester" value="${user.semester}" placeholder="e.g. 4th">
        </div>
        <div class="form-group">
            <label>Hostel</label>
            <input type="text" name="hostel" value="${user.hostel}" placeholder="e.g. Block A">
        </div>
        <div class="form-group">
            <label>Phone</label>
            <input type="tel" name="phone" value="${user.phone}">
        </div>
        <button type="submit" class="btn btn-primary" style="width:100%;">Register</button>
    </form>

    <p style="text-align:center;margin-top:1rem;font-size:0.9rem;">
        Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a>
    </p>
</div>

<jsp:include page="includes/footer.jsp"/>
