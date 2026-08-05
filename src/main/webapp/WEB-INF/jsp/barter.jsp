<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Barter Exchange"/>
<jsp:include page="includes/header.jsp"/>

<div class="container" style="padding:2rem 0;">
    <h1 class="section-title">⭐ Barter Engine</h1>
    <p style="color:var(--muted);margin-bottom:1.5rem;">Exchange items directly — no money needed. Both parties accept to complete the deal.</p>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>You Want</th>
                    <th>You Offer</th>
                    <th>From</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${proposals}">
                    <tr>
                        <td><c:out value="${p.targetTitle}"/></td>
                        <td><c:out value="${p.offeredTitle}"/></td>
                        <td><c:out value="${p.proposerName}"/></td>
                        <td><span class="badge">${p.status}</span></td>
                        <td>
                            <c:if test="${p.status == 'PENDING'}">
                                <form method="post" action="${pageContext.request.contextPath}/barter" class="inline-form">
                                    <input type="hidden" name="action" value="accept">
                                    <input type="hidden" name="proposalId" value="${p.id}">
                                    <button type="submit" class="btn btn-primary btn-sm">Accept</button>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/barter" class="inline-form">
                                    <input type="hidden" name="action" value="reject">
                                    <input type="hidden" name="proposalId" value="${p.id}">
                                    <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty proposals}">
                    <tr><td colspan="5" style="text-align:center;color:var(--muted);">No barter proposals yet.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="includes/footer.jsp"/>
