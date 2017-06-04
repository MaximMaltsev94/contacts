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
    <link rel="stylesheet" href="<c:url value="/css/popup.css" />">
    <link rel="stylesheet" href="<c:url value="/css/hoverableMenu.css" />">

    <script src="<c:url value="/js/main.js"/>"></script>
    <script src="<c:url value="/js/showView.js"/>"></script>
    <script src="<c:url value="/js/listManagePopup.js"/>"></script>
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
                                       href="<c:url value="/contact/edit?id=${i.id}" /> "><c:out
                                            value="${i.firstName} ${i.lastName}"/></a>
                                </div>
                            </div>

                            <c:if test="${requestScope.get('contactGroups').get(i.id).size() > 0}">
                                <div class="jlab-row" style="display: inline">
                                    <c:forEach var="groups" items="${requestScope.get('contactGroups').get(i.id)}">
                                        <span class="label text-tiny" style="cursor: pointer" onclick="location.href='<c:url value="/contact/show?page=1&filter=${groups.id}"/>'">${groups.groupName}</span>
                                    </c:forEach>
                                </div>
                            </c:if>


                            <div class="jlab-row">
                                <div class="jlab-cell-12 center">
                                <span class="text-small">
                                    ${i.birthDay == 0 ? 'xx' : i.birthDay}.${i.birthMonth == 0 ? 'xx' : i.birthMonth}.${i.birthYear == 0 ? 'xxxx' : i.birthYear}
                                    <c:out value=" ${i.companyName} ${i.street}"/>
                                </span>
                                </div>
                            </div>

                        </div>

                        <div class="jlab-cell-1 center">
                            <div class="imageButton" onclick="showView.onEmailContact(${i.id})">
                                <img src="<c:url value="/sysImages/email.png"/>">
                            </div>
                        </div>

                        <div class="jlab-cell-1 center">
                            <div class="imageButton" onclick="location.href='<c:url value="/contact/edit?id=${i.id}"/>'">
                                <img src="<c:url value="/sysImages/edit.png"/>">
                            </div>
                        </div>

                        <div class="jlab-cell-1 center ">
                            <div class="imageButton" onclick="main.showConfirmDialog('Вы действительно хотите удалить контакт ${i.firstName} ${i.lastName}?', function() { showView.onDeleteContact('${i.id}')})">
                                <img src="<c:url value="/sysImages/delete.png"/>" >
                            </div>
                        </div>
                    </div>
                </section>
            </c:forEach>
            <%--pagination--%>

            <c:set var="currentPage" scope="page" value="${requestScope.getOrDefault('page', 1)}"/>
            <c:set var="lastPage" scope="page" value="${requestScope.getOrDefault('maxPageNumber', 1)}"/>
            <c:set var="filter" scope="page" value="${requestScope.containsKey('filter') ? '&filter='.concat(requestScope.get('filter')) : ''}"/>

            <ul class="pagination">

                <%-- add first page link--%>
                <c:choose>
                    <c:when test="${currentPage == 1}">
                        <li><a class="active" href="<c:url value="/contact/show?page=${1}${filter}" /> ">1</a></li>
                    </c:when>

                    <c:when test="${currentPage != 1}">
                        <li><a href="<c:url value="/contact/show?page=${currentPage - 1}${filter}" /> ">&lt;</a></li>
                        <li><a href="<c:url value="/contact/show?page=${1}${filter}" /> ">1</a></li>
                    </c:when>
                </c:choose>

                <%--add skip if too away from begining--%>
                <c:if test="${currentPage > 4}">
                    <li><a href="javascript:;">...</a></li>
                </c:if>

                <%--add middle links--%>
                <c:forEach var="i" begin="${ currentPage < 4 ? 2 : currentPage - 2}"
                           end="${currentPage > lastPage - 3 ? lastPage - 1 : currentPage + 2}">
                    <c:choose>
                        <c:when test="${currentPage == i}">
                            <li><a class="active" href="<c:url value="/contact/show?page=${i}${filter}" /> ">${i}</a>
                            </li>
                        </c:when>
                        <c:when test="${currentPage != i}">
                            <li><a href="<c:url value="/contact/show?page=${i}${filter}" /> ">${i}</a></li>
                        </c:when>
                    </c:choose>
                </c:forEach>

                <%--add skip if too away from ending--%>
                <c:if test="${currentPage < (lastPage - 3)}">
                    <li><a href="javascript:;">...</a></li>
                </c:if>

                <%--add next page link--%>
                <c:if test="${lastPage != 1}">
                    <c:choose>
                        <c:when test="${currentPage != lastPage}">
                            <li><a href="<c:url value="/contact/show?page=${lastPage}${filter}" /> ">${lastPage}</a></li>
                            <li><a href="<c:url value="/contact/show?page=${currentPage + 1}${filter}" /> ">&gt;</a></li>
                        </c:when>

                        <c:when test="${currentPage == lastPage}">
                            <li><a class="active"
                                   href="<c:url value="/contact/show?page=${lastPage}${filter}" /> ">${lastPage}</a></li>
                        </c:when>

                    </c:choose>

                </c:if>
            </ul>
        </div><%--div.jlab-cell-9 end--%>
        <div class="jlab-cell-3">
            <section>
                <div class="jlab-row margin">
                    <div class="jlab-cell-12">
                        <span class="text-medium">
                            Выберите действие
                        </span>
                    </div>
                </div>
                <div class="jlab-row margin">
                    <button class="jlab-cell-10 align-center" onclick="location.href='<c:url value="/contact/add" />'">Создать контакт</button>
                </div>
                <div class="jlab-row margin">
                    <button id="deleteSelected" disabled class="jlab-cell-10 align-center" onclick="main.showConfirmDialog('Вы действительно хотите удалить выбранные контакты?', showView.onDeleteSelectedClick)">Удалить</button>
                </div>
                <div class="jlab-row margin">
                    <button id="sendEmail" disabled class="jlab-cell-10 align-center" onclick="showView.onEmailSelectedClick()">Отправить письмо</button>
                </div>
                <div class="jlab-row margin">
                    <button class="jlab-cell-10 align-center" onclick="location.href='<c:url value="/contact/statistics" />'">Посмотреть статистику</button>
                </div>
            </section>
            <section>
                <div class="jlab-row margin">
                    <div class="jlab-cell-12">
                            <span class="text-medium">Списки контактов</span>
                    </div>
                </div>
                <div class="hover-menu">
                    <div class="hover-menu-item text-small-bold ${requestScope.containsKey('filter') ? "" : "active"}" onclick="location.href='<c:url value="/contact/show?page=1"/>'">Все</div>
                    <c:forEach var="group" items="${requestScope.get('userGroups')}">
                        <div class="hover-menu-item text-small-bold ${requestScope.get('filter') eq group.id ? "active" : ""}"
                             onclick="location.href='<c:url value="/contact/show?page=1&filter=${group.id}" />'">
                                ${group.groupName}
                        </div>
                    </c:forEach>
                </div>
                <div class="jlab-row margin">
                    <button class="jlab-cell-10 align-center" onclick="location.href='<c:url value="/contact/createList" />'">Создать список</button>
                </div>
                <div class="jlab-row margin">
                    <button class="jlab-cell-10 align-center" onclick="listManagePopup.showListManagePopup();">Управление списками</button>
                </div>
            </section>

        </div>

        <%--div.jlab-row end--%>
    </div>

    <%--div.container end--%>
</div>

<div id="listManagePopup" class="popupBack hidden">
    <div class="popup">
        <div class="jlab-row">
            <div class="jlab-cell-11 center text-medium">Управление списками</div>
            <div class="jlab-cell-1 align-right">
                <a class="text-large" href="javascript:;" onclick="listManagePopup.closeListManagePopup()">&times;</a>
            </div>
        </div>
        <div class="box">
            <c:set var="first" value="true" scope="page"/>
            <c:forEach var="group" items="${requestScope.get('userGroups')}">
                <c:if test="${not first eq 'true'}">
                    <hr>
                </c:if>
                <c:set var="first" value="false"/>

                <div class="jlab-row margin">
                    <div class="jlab-cell-1 center">
                        <input type="checkbox" id="manage-group-${group.id}" class="regular-checkbox" onchange="listManagePopup.onCheckBoxChecked(this)"/><label for="manage-group-${group.id}"></label>
                    </div>
                    <div class="jlab-cell-7 center text-small-bold">
                        <a href="<c:url value="/contact/editList?id=${group.id}"/> ">${group.groupName}</a>
                    </div>
                    <div class="jlab-cell-1 center">
                        <div class="imageButton" onclick="listManagePopup.onEmailGroup(${group.id})">
                            <img src="<c:url value="/sysImages/email.png"/>">
                        </div>
                    </div>

                    <div class="jlab-cell-1 center">
                        <div class="imageButton" onclick="location.href='<c:url value="/contact/editList?id=${group.id}"/>'">
                            <img src="<c:url value="/sysImages/edit.png"/>">
                        </div>
                    </div>

                    <div class="jlab-cell-1 center ">
                        <div class="imageButton" onclick="main.showConfirmDialog('Вы действительно хотите удалить группу ${group.groupName}?', function() { listManagePopup.onDeleteGroup('${group.id}')})">
                            <img src="<c:url value="/sysImages/delete.png"/>" >
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="jlab-row">
            <div class="jlab-cell-5"></div>
            <div class="jlab-cell-4">
                <button class="jlab-cell-12 align-center" id="sendEmailGroup" type="button" onclick="listManagePopup.onEmailSelectedClick()" disabled>Отправить письмо</button>
            </div>
            <div class="jlab-cell-3">
                <button class="jlab-cell-12" id="deleteGroup" type="button" onclick="main.showConfirmDialog('Вы действительно хотите удалить выбранные группы?', listManagePopup.onDeleteSelectedClick)" disabled>Удалить</button>
            </div>
        </div>

    </div>

    <%--div id phonePopup end--%>
</div>
<jsp:include page="confirmPopup.jsp"/>
<jsp:include page="footer.jsp"/>
</body>
</html>
