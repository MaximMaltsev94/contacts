<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Написать письмо</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css" />">
    <link rel="stylesheet" href="<c:url value="/css/dropdown.css" />">
    <script src="<c:url value="/js/main.js"/>"></script>
    <script src="<c:url value="/js/email.js"/>"></script>

</head>
<body>
<jsp:include page="header.jsp"/>
<fmt:setLocale value="ru_RU" scope="session"/>

<div class="container">
    <section>
        <form action="<c:url value="/contact/?action=submitEmail"/>" method="post">
        </form>
        <div class="text-large">Отправить письмо</div>
        <div class="jlab-row margin">
            <div class="jlab-cell-1 text-small align-right">Кому</div>
            <div class="jlab-cell-8">
                <input readonly id="receivers" onclick="email.onDropdownShow()" type="text"><div id="myDropdown" class="dropdown-content">
                <c:forEach var="i" items="${requestScope.get('contactList')}">
                    <div class="jlab-row margin">
                        <div class="jlab-cell-1 center">
                            <input type="checkbox" data-fio="${i.firstName} ${i.lastName}" id="${i.id}" class="regular-checkbox" onclick="email.onReceiverSelected()"/>
                            <label for="${i.id}"></label>
                        </div>
                        <div class="jlab-cell-11">
                            <div class="jlab-row text-small-bold"> ${i.firstName} ${i.lastName}</div>
                            <div class="jlab-row text-small"> ${i.email}</div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            </div>

        </div>

        <div class="jlab-row margin">
            <div class="jlab-cell-1 text-small align-right">Тема</div>
            <div class="jlab-cell-8"><input type="text"></div>
        </div>

        <div class="jlab-row margin">
            <div class="jlab-cell-1 align-right text-small">Шаблон</div>
            <div class="jlab-cell-8">
                <select name="" id="">
                    <option value="0" selected>Не выбрано</option>
                </select>
            </div>
        </div>

        <div class="jlab-row margin">
            <div class="jlab-cell-1"></div>
            <div class="jlab-cell-8"><textarea name="" id="" rows="20"></textarea> </div>
        </div>

        <div class="jlab-row margin">
            <div class="jlab-cell-1"></div>
            <div class="jlab-cell-8 align-right">
                <button>Отправить</button>
            </div>
        </div>
    </section>
</div>
</body>
</html>
