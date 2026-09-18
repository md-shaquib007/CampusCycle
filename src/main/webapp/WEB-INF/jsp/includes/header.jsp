<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'CampusCycle'}"/> &#8211; Student Marketplace</title>
    <c:url var="styleUrl" value="/css/style.css"/>
    <link rel="stylesheet" href="${styleUrl}?v=5">
</head>
<body>
<nav class="navbar">
    <div class="container nav-inner">
        <a href="${pageContext.request.contextPath}/home" class="logo">&#128279; Campus<span>Cycle</span></a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/listings">Browse</a>
            <a href="${pageContext.request.contextPath}/sustainability">Impact</a>
            <c:choose>
                <c:when test="${sessionScope.currentUser != null}">
                    <a href="${pageContext.request.contextPath}/listing/create">+ Sell</a>
                    <a href="${pageContext.request.contextPath}/barter">Barter</a>
                    <a href="${pageContext.request.contextPath}/chat">Chat</a>
                    <a href="${pageContext.request.contextPath}/profile">Profile</a>
                    <c:if test="${sessionScope.currentUser.admin}">
                        <a href="${pageContext.request.contextPath}/admin/dashboard">Admin</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Login</a>
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">Register</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>

<c:if test="${sessionScope.flash != null}">
    <div class="container" style="margin-top:1rem;">
        <div class="alert alert-success"><c:out value="${sessionScope.flash}"/></div>
    </div>
    <c:remove var="flash" scope="session"/>
</c:if>
<c:if test="${sessionScope.flashError != null}">
    <div class="container" style="margin-top:1rem;">
        <div class="alert alert-error"><c:out value="${sessionScope.flashError}"/></div>
    </div>
    <c:remove var="flashError" scope="session"/>
</c:if>
