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
    <link rel="stylesheet" href="<c:url value="/css/actionTooltip.css"/>">

    <script src="<c:url value="/js/main.js"/>"></script>
    <script src="<c:url value="/js/showView.js"/>"></script>
</head>

<body onload="main.showTooltip('${sessionScope.get('tooltip-text')}', '${sessionScope.get('tooltip-type')}')">
<c:remove var="tooltip-text" scope="session"/>
<c:remove var="tooltip-type" scope="session"/>

<jsp:include page="header.jsp"/>
<fmt:setLocale value="ru_RU" scope="session"/>

<span id="tooltip"></span>

<div class="container">

    <div class="jlab-row">
        <div class="jlab-cell-9">
            <c:if test="${requestScope.get('contactList').size() == 0}">
                <section class="text-medium">
                    Пока нет ни одного контакта
                </section>
            </c:if>
            <c:forEach var="i" items="${requestScope.get('contactList')}">
                <section>
                    <div class="jlab-row">

                        <div class="jlab-cell-1 center">
                            <input type="checkbox" id="${i.id}" class="regular-checkbox" onchange="showView.onCheckBoxChecked(this)"/><label for="${i.id}"></label>
                        </div>

                        <div class="jlab-cell-1 center">
                            <img src="<c:url value="${i.profilePicture}" /> " width="50px" height="50px">
                        </div>

                        <div class="jlab-cell-7 center">

                            <div class="jlab-row">
                                <div class="jlab-cell-12 center">
                                    <a class="text-medium"
                                       href="<c:url value="?action=edit&id=${i.id}" /> "><c:out
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
                            <div class="imageButton email" onclick="showView.onEmailContact(${i.id})">
                            </div>
                        </div>

                        <div class="jlab-cell-1 center">
                            <a href="<c:url value="?action=edit&id=${i.id}"/>">
                                <div class="imageButton edit">
                                </div>
                            </a>
                        </div>

                        <div class="jlab-cell-1 center ">
                            <div class="imageButton delete" onclick="showView.onDeleteContact('${i.firstName}', '${i.lastName}', '${i.id}')">
                            </div>
                        </div>
                    </div>
                </section>
            </c:forEach>
            <%--pagination--%>

            <c:set var="currentPage" scope="page" value="${requestScope.getOrDefault('page', 1)}"/>
            <c:set var="lastPage" scope="page" value="${requestScope.getOrDefault('maxPageNumber', 1)}"/>

            <ul class="pagination">

                <%-- add first page link--%>
                <c:choose>
                    <c:when test="${currentPage == 1}">
                        <li><a class="active" href="<c:url value="?action=show&page=${1}" /> ">1</a></li>
                    </c:when>

                    <c:when test="${currentPage != 1}">
                        <li><a href="<c:url value="?action=show&page=${currentPage - 1}" /> ">&lt;</a></li>
                        <li><a href="<c:url value="?action=show&page=${1}" /> ">1</a></li>
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
                            <li><a class="active" href="<c:url value="?action=show&page=${i}" /> ">${i}</a>
                            </li>
                        </c:when>
                        <c:when test="${currentPage != i}">
                            <li><a href="<c:url value="?action=show&page=${i}" /> ">${i}</a></li>
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
                            <li><a href="<c:url value="?action=show&page=${lastPage}" /> ">${lastPage}</a></li>
                            <li><a href="<c:url value="?action=show&page=${currentPage + 1}" /> ">&gt;</a></li>
                        </c:when>

                        <c:when test="${currentPage == lastPage}">
                            <li><a class="active"
                                   href="<c:url value="?action=show&page=${lastPage}" /> ">${lastPage}</a></li>
                        </c:when>

                    </c:choose>

                </c:if>
            </ul>
        </div><%--div.jlab-cell-9 end--%>
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
                        <a class="jlab-cell-10 align-center" href="<c:url value="?action=add" />">
                            <button class="jlab-cell-12">Создать контакт</button>
                        </a>
                    </div>
                    <div class="jlab-row margin">
                        <button id="deleteSelected" disabled class="jlab-cell-10 align-center" onclick="showView.onDeleteSelectedClick()">Удалить</button>
                    </div>
                    <div class="jlab-row margin">
                        <button id="sendEmail" disabled class="jlab-cell-10 align-center" onclick="showView.onEmailSelectedClick()">Отправить письмо</button>
                    </div>
            </section>

        </div>

        <%--div.jlab-row end--%>
    </div>

    <%--div.container end--%>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
