<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Forgot Password"/>
<jsp:include page="includes/header.jsp"/>

<div class="form-card">
    <h2 style="text-align:center;margin-bottom:1.5rem;color:var(--primary);">Forgot Password</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert alert-success"><c:out value="${success}"/></div>
        <c:if test="${not empty resetLink}">
            <p style="font-size:0.85rem;word-break:break-all;"><a href="${resetLink}">Reset Link</a></p>
        </c:if>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/forgot-password">
        <div class="form-group">
            <label>College Email</label>
            <input type="email" name="email" required>
        </div>
        <button type="submit" class="btn btn-primary" style="width:100%;">Generate Reset Token</button>
    </form>

    <p style="text-align:center;margin-top:1rem;font-size:0.9rem;">
        <a href="${pageContext.request.contextPath}/login">Back to Login</a>
    </p>
</div>

<jsp:include page="includes/footer.jsp"/>
