<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="isAdd" value="${requestScope.get('action') eq 'createList'}" scope="page"/>
<html>
<head>
    <title>${isAdd eq true ? 'Создать список' : 'Редактировать список'}</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css" />">

    <script src="<c:url value="/js/main.js"/>"></script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <form action="<c:url value="/contact/${requestScope.get('action')}"/> " method="post">
        <input type="text" hidden name="id" value="${isAdd ? '' : requestScope.get('id')}">
        <div class="jlab-row margin">
            <div class="jlab-cell-9">

                <c:forEach var="j" begin="0" end="${requestScope.get('contactList').size() - 1}" step="3">
                    <div class="jlab-row">
                        <c:forEach var="k" begin="${j}" end="${j + 2}">

                            <div class="jlab-cell-4">

                                <c:if test="${k < requestScope.get('contactList').size()}">
                                    <c:set var="i" value="${requestScope.get('contactList').get(k)}"/>
                                    <c:set var="contains"
                                           value="${isAdd ? '' : requestScope.get('contactGroups').contains(i.id) ? 'checked' : ''}"/>
                                    <section>
                                        <div class="jlab-row">
                                            <div class="jlab-cell-1 center">
                                                <input type="checkbox" ${contains} id="${i.id}"name="contact-${i.id}" class="regular-checkbox" onchange="showView.onCheckBoxChecked(this)"/><label for="${i.id}"></label>
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
            </div><%--div.jlab-cell-9 end --%>

            <div class="jlab-cell-3">
                <section>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-12">
                            <span class="text-small-bold">
                                ${isAdd eq true ? 'Создание списка' : 'Редактирование списка'}
                            </span>
                        </div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-12 align-center">
                            <input type="text"
                                   name="groupName"
                                   value="${isAdd ? '' : requestScope.get('groupName')}"
                                   placeholder="Название списка"
                                   pattern="^[A-Za-zА-Яа-яЁё\s0-9_]{1,15}$"
                                   title="Русские, английские буквы, пробелы цифры, знаки подчеркивания от 1 до 15 символов"
                                   required>
                        </div>
                    </div>
                </section>
                <section>
                    <div class="jlab-row margin">
                        <button class="jlab-cell-12 align-center">${isAdd eq true ?"Добавить" : "Сохранить"}</button>
                    </div>
                    <c:if test="${isAdd eq false}">
                        <div class="jlab-row margin">
                            <button type="button" class="jlab-cell-12 align-center" onclick="main.showConfirmDialog('Вы действительно хотите удалить список?', showView.onDeleteSelectedClick)">Удалить</button>
                        </div>
                    </c:if>
                    <div class="jlab-row margin">
                        <button type="button" class="jlab-cell-12 align-center" onclick="location.href='<c:url value="/contact/show?page=${sessionScope.getOrDefault('lastVisitedPage', 1)}" />'">Назад</button>
                    </div>
                </section>
            </div>
        </div><%--info line with save/cancel buttons end--%>
    </form>

</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
