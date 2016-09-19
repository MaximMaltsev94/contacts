<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Контакты</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/pagination.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css"/>">
</head>
<body>
<nav>
    <span class="text-large">
        Contacts
    </span>
</nav>
<div class="container">

    <div class="jlab-row">
        <div class="jlab-cell-8">
            <c:forEach var="i" items="${requestScope.get('contactList')}">
                <section>
                    <div class="jlab-row">

                        <div class="jlab-cell-1 center">
                            <input type="checkbox" id="${i.id}" class="regular-checkbox"/><label for="${i.id}"></label>
                        </div>

                        <div class="jlab-cell-1 center">
                            <img src="<c:url value="${i.profilePicture}" /> " width="50px" height="50px">
                        </div>

                        <div class="jlab-cell-8 center">

                            <div class="jlab-row">
                                <div class="jlab-cell-12 center">
                                    <a class="text-medium"
                                       href="<c:url value="/contact/?action=edit&id=${i.id}" /> "><c:out
                                            value="${i.firstName} ${i.lastName}"/></a>
                                </div>
                            </div>

                            <div class="jlab-row">
                                <div class="jlab-cell-12 center">
                                <span class="text-small">
                                    <c:out value="${i.birthDate}, ${i.companyName}, ${i.street}"/>
                                </span>
                                </div>
                            </div>

                        </div>

                        <div class="jlab-cell-1 center">
                            <a href="<c:url value="/contact/?action=edit&id=${i.id}"/>">
                                <div id="editImage">
                                </div>
                            </a>
                        </div>
                        <div class="jlab-cell-1 center ">
                            <a href="#">
                                <div id="deleteImage">
                                </div>
                            </a>
                            </a>
                        </div>
                    </div>
                </section>
            </c:forEach>
            <%--pagination--%>

            <c:set var="currentPage" scope="page" value="${param.get('page')}"/>
            <c:set var="lastPage" scope="page" value="${requestScope.get('maxPageNumber')}"/>

            <ul class="pagination">

                <%-- add first page link--%>
                <c:choose>
                    <c:when test="${currentPage == 1}">
                        <li><a class="active" href="<c:url value="/contact/?action=show&page=${1}" /> ">1</a></li>
                    </c:when>

                    <c:when test="${currentPage != 1}">
                        <li><a href="<c:url value="/contact/?action=show&page=${currentPage - 1}" /> ">&Lt;</a></li>
                        <li><a href="<c:url value="/contact/?action=show&page=${1}" /> ">1</a></li>
                    </c:when>
                </c:choose>

                <%--add skip if too away from begining--%>
                <c:if test="${currentPage > 4}">
                    <li><a href="#">...</a></li>
                </c:if>

                <%--add middle links--%>
                <c:forEach var="i" begin="${ currentPage < 4 ? 2 : currentPage - 2}"
                           end="${currentPage > lastPage - 3 ? lastPage - 1 : currentPage + 2}">
                    <c:choose>
                        <c:when test="${currentPage == i}">
                            <li><a class="active" href="<c:url value="/contact/?action=show&page=${i}" /> ">${i}</a>
                            </li>
                        </c:when>
                        <c:when test="${currentPage != i}">
                            <li><a href="<c:url value="/contact/?action=show&page=${i}" /> ">${i}</a></li>
                        </c:when>
                    </c:choose>
                </c:forEach>

                <%--add skip if too away from ending--%>
                <c:if test="${currentPage < (lastPage - 3)}">
                    <li><a href="#">...</a></li>
                </c:if>

                <%--add next page link--%>
                <c:if test="${lastPage != 1}">
                    <c:choose>
                        <c:when test="${currentPage != lastPage}">
                            <li><a href="<c:url value="/contact/?action=show&page=${lastPage}" /> ">${lastPage}</a></li>
                            <li><a href="<c:url value="/contact/?action=show&page=${currentPage + 1}" /> ">&Gt;</a></li>
                        </c:when>

                        <c:when test="${currentPage == lastPage}">
                            <li><a class="active"
                                   href="<c:url value="/contact/?action=show&page=${lastPage}" /> ">${lastPage}</a></li>
                        </c:when>

                    </c:choose>

                </c:if>
            </ul>

            <%--div.jlab-cell-8 end--%>
        </div>
        <div class="jlab-cell-4 fixed">
            <section><span class="text-large">Hello world</span></section>

        </div>

        <%--div.jlab-row end--%>
    </div>

    <%--div.container end--%>
</div>

</body>
</html>
