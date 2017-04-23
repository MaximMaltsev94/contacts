<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="isAdd" value="${requestScope.get('action') eq 'createList'}" scope="page"/>
<c:set var="isEdit" value="${requestScope.get('action') eq 'editList'}" scope="page"/>
<c:set var="isImport" value="${requestScope.get('action') eq 'importVK'}" scope="page"/>
<c:choose>
    <c:when test="${requestScope.get('action') eq 'createList'}">
        <c:set var="title" value="Создать список" scope="page"/>
        <c:set var="inputGroupName" value="${''}" scope="page"/>
        <c:set var="btnSubmitText" value="Добавить" scope="page"/>
    </c:when>
    <c:when test="${requestScope.get('action') eq 'editList'}">
        <c:set var="title" value="Редактировать список" scope="page"/>
        <c:set var="inputGroupName" value="${requestScope.get('groupName')}" scope="page"/>
        <c:set var="btnSubmitText" value="Сохранить" scope="page"/>

    </c:when>
    <c:when test="${requestScope.get('action') eq 'importVK'}">
        <c:set var="title" value="Импортировать Вконтакте" scope="page"/>
        <c:set var="inputGroupName" value="${'Друзья вконтакте'}" scope="page"/>
        <c:set var="btnSubmitText" value="Импортировать" scope="page"/>

    </c:when>
</c:choose>
<html>
<head>
    <title>${title}</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css" />">
    <link rel="stylesheet" href="<c:url value="/css/popup.css" />">

    <script src="<c:url value="/js/main.js"/>"></script>
    <script src="<c:url value="/js/listManagePopup.js"/>"></script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <form action="<c:url value="/contact/${requestScope.get('action')}"/> " method="post">
        <input type="text" hidden name="id" value="${isAdd or isImport ? '' : requestScope.get('id')}">
        <div class="jlab-row margin">
            <div class="jlab-cell-9">
                <c:if test="${not requestScope.get('contactList').isEmpty()}">
                    <c:forEach var="j" begin="0" end="${requestScope.get('contactList').size() - 1}" step="3">
                        <div class="jlab-row">
                            <c:forEach var="k" begin="${j}" end="${j + 2}">

                                <div class="jlab-cell-4">

                                    <c:if test="${k < requestScope.get('contactList').size()}">
                                        <c:set var="i" value="${requestScope.get('contactList').get(k)}"/>
                                        <c:set var="contains"
                                               value="${isAdd or isImport ? '' : requestScope.get('contactGroups').contains(i.id) ? 'checked' : ''}"/>
                                        <section>
                                            <div class="jlab-row">
                                                <div class="jlab-cell-1 center">
                                                    <input type="checkbox" ${contains} id="${i.id}"name="contact-${i.id}" class="regular-checkbox"/><label for="${i.id}"></label>
                                                </div>

                                                <div class="jlab-cell-2 center">
                                                    <img src="<c:url value="${i.profilePicture}" /> " width="25px" height="25px">
                                                </div>

                                                <div class="jlab-cell-9 center">
                                                    <div class="text-medium">
                                                            ${i.firstName} ${i.lastName}
                                                    </div>

                                                </div>
                                            </div>
                                        </section>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </c:if>
            </div><%--div.jlab-cell-9 end --%>

            <div class="jlab-cell-3">
                <section>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-12">
                            <span class="text-small-bold">
                                ${title}
                            </span>
                        </div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-12 align-center">
                            <input type="text"
                                   name="groupName"
                                   value="${inputGroupName}"
                                   placeholder="Название списка"
                                   pattern="^[A-Za-zА-Яа-яЁё\s0-9_]{1,30}$"
                                   title="Русские, английские буквы, пробелы цифры, знаки подчеркивания от 1 до 30 символов"
                                   required>
                        </div>
                    </div>
                </section>
                <section>
                    <div class="jlab-row margin">
                        <button class="jlab-cell-12 align-center">${btnSubmitText}</button>
                    </div>
                    <c:if test="${isAdd eq false and isImport eq false}">
                        <div class="jlab-row margin">
                            <button type="button"
                                    class="jlab-cell-12 align-center"
                                    onclick="main.showConfirmDialog('Вы действительно хотите удалить группу?', function() {
                                        listManagePopup.onDeleteGroup(${requestScope.get('id')});
                                    })">Удалить</button>
                        </div>
                    </c:if>
                    <div class="jlab-row margin">
                        <c:set var="lastVisitedFilter"
                               value="${sessionScope.containsKey('lastVisitedFilter') ? '&filter='.concat(sessionScope.get('lastVisitedFilter')): ''}"/>
                        <button type="button" class="jlab-cell-12 align-center" onclick="location.href='<c:url value="/contact/show?page=${sessionScope.getOrDefault('lastVisitedPage', 1)}${lastVisitedFilter}" />'">Назад</button>
                    </div>
                </section>
            </div>
        </div><%--info line with save/cancel buttons end--%>
    </form>

</div>
<jsp:include page="confirmPopup.jsp"/>
<jsp:include page="footer.jsp"/>
</body>
</html>
