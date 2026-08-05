<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Reset Password"/>
<jsp:include page="includes/header.jsp"/>

<div class="form-card">
    <h2 style="text-align:center;margin-bottom:1.5rem;color:var(--primary);">Reset Password</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/reset-password">
        <input type="hidden" name="token" value="${token}">
        <div class="form-group">
            <label>New Password</label>
            <input type="password" name="password" required minlength="6">
        </div>
        <button type="submit" class="btn btn-primary" style="width:100%;">Reset Password</button>
    </form>
</div>

<jsp:include page="includes/footer.jsp"/>
