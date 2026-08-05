<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Chat Requests"/>
<jsp:include page="includes/header.jsp"/>

<div class="container" style="padding:2rem 0;">
    <h1 class="section-title">Chat Requests</h1>
    <p style="color:var(--muted);margin-bottom:1.5rem;">After acceptance, contact information becomes visible.</p>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Listing</th>
                    <th>Requester</th>
                    <th>Message</th>
                    <th>Status</th>
                    <th>Contact</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="cr" items="${requests}">
                    <tr>
                        <td><c:out value="${cr.listingTitle}"/></td>
                        <td><c:out value="${cr.requesterName}"/></td>
                        <td><c:out value="${cr.message}"/></td>
                        <td><span class="badge">${cr.status}</span></td>
                        <td>
                            <c:if test="${cr.status == 'ACCEPTED'}">
                                <c:if test="${sessionScope.currentUser.id == cr.sellerId}">
                                    <c:out value="${cr.requesterEmail}"/><br>
                                    <c:out value="${cr.requesterPhone}"/>
                                </c:if>
                                <c:if test="${sessionScope.currentUser.id == cr.requesterId}">
                                    Contact seller via accepted request
                                </c:if>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${cr.status == 'PENDING' && sessionScope.currentUser.id == cr.sellerId}">
                                <form method="post" action="${pageContext.request.contextPath}/chat" class="inline-form">
                                    <input type="hidden" name="action" value="respond">
                                    <input type="hidden" name="requestId" value="${cr.id}">
                                    <input type="hidden" name="decision" value="accept">
                                    <button type="submit" class="btn btn-primary btn-sm">Accept</button>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/chat" class="inline-form">
                                    <input type="hidden" name="action" value="respond">
                                    <input type="hidden" name="requestId" value="${cr.id}">
                                    <input type="hidden" name="decision" value="reject">
                                    <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty requests}">
                    <tr><td colspan="6" style="text-align:center;color:var(--muted);">No chat requests yet.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="includes/footer.jsp"/>
