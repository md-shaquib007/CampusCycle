<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Login"/>
<jsp:include page="includes/header.jsp"/>

<div class="form-card">
    <h2 style="text-align:center;margin-bottom:1.5rem;color:var(--primary);">Welcome Back</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert alert-success"><c:out value="${success}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="form-group">
            <label>College Email</label>
            <input type="email" name="email" value="${email}" required placeholder="you@college.edu">
        </div>
        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" required>
        </div>
        <button type="submit" class="btn btn-primary" style="width:100%;">Login</button>
    </form>

    <p style="text-align:center;margin-top:1rem;font-size:0.9rem;">
        <a href="${pageContext.request.contextPath}/forgot-password">Forgot password?</a><br>
        Don't have an account? <a href="${pageContext.request.contextPath}/register">Register</a>
    </p>
</div>

<jsp:include page="includes/footer.jsp"/>
