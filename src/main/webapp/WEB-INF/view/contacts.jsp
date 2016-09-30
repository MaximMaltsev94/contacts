<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Контакты</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/pagination.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css"/>">

    <script src="<c:url value="/js/showView.js"/>"></script>
</head>
<body>
<jsp:include page="header.jsp"/>
<fmt:setLocale value="ru_RU" scope="session"/>

<div class="container">

    <div class="jlab-row">
        <div class="jlab-cell-9">
            <c:forEach var="i" items="${requestScope.get('contactList')}">
                <section>
                    <div class="jlab-row">

                        <div class="jlab-cell-1 center">
                            <input type="checkbox" id="chB${i.id}" data-form="fDelete${i.id}" class="regular-checkbox" onchange="showView.onCheckBoxChecked(this)"/><label for="chB${i.id}"></label>
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
                                    <c:if test="${i.birthDate != null}">
                                        <fmt:formatDate value="${i.birthDate}" type="date" dateStyle="long" />
                                    </c:if>
                                    <c:out value=" ${i.companyName} ${i.street}"/>
                                </span>
                                </div>
                            </div>

                        </div>

                        <div class="jlab-cell-1 center">
                            <a href="<c:url value="/contact/?action=edit&id=${i.id}"/>">
                                <div class="imageButton edit">
                                </div>
                            </a>
                        </div>
                        <div class="jlab-cell-1 center ">
                            <form id="fDelete${i.id}" action="<c:url value="/contact/?action=delete&id=${i.id}"/>" method="post">
                                <input type="text" name="profilePicture" value="${i.profilePicture}" hidden>
                                <div class="imageButton delete" onclick="showView.onDeleteContact('${i.firstName}', '${i.lastName}', 'fDelete${i.id}')">
                                </div>
                            </form>
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
                        <li><a href="<c:url value="/contact/?action=show&page=${currentPage - 1}" /> ">&lt;</a></li>
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
                            <li><a href="<c:url value="/contact/?action=show&page=${currentPage + 1}" /> ">&gt;</a></li>
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
        <div class="jlab-cell-3 fixed">
            <section>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-12">
                            <span class="text-medium">
                                Выберите действие
                            </span>
                        </div>
                    </div>
                    <div class="jlab-row margin">
                        <a class="jlab-cell-10 align-center" href="<c:url value="/contact/?action=add" />">
                            <button class="jlab-cell-12">Создать контакт</button>
                        </a>
                    </div>
                    <div class="jlab-row margin">
                        <button id="deleteSelected" disabled class="jlab-cell-10 align-center" onclick="showView.onDeleteSelectedClick()">Удалить</button>
                    </div>
            </section>

        </div>

        <%--div.jlab-row end--%>
    </div>

    <%--div.container end--%>
</div>

</body>
</html>
