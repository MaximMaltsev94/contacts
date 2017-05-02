<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Восстановление пароля</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/actionTooltip.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/popup.css"/>">

    <script src="<c:url value="/js/main.js"/>"></script>
    <script src="<c:url value="/js/login.js"/>"></script>
</head>
<body>
<c:remove var="tooltip-text" scope="session"/>
<c:remove var="tooltip-type" scope="session"/>
<jsp:include page="header.jsp"/>
<span id="tooltip"></span>
<div class="container">
    <div class="jlab-row">
        <div class="jlab-cell-3"></div>
        <div class="jlab-cell-6">
            <form onsubmit="return loginView.validateRegistrationForm()" action="<c:url value="/contact/resetPassword"/> " method="post">
                <input type="text" name="token" value="${requestScope.get('token')}" hidden>
                <section>
                    <div class="jlab-row margin text-medium">Восстановление пароля</div>
                    <div class="jlab-row margin text-small-bold">Новый пароль</div>
                    <div class="jlab-row margin"><input id="pass1" name="password" type="password" pattern="[\w]{3,20}" required title="Буквы, цифры, знак подчеркивания. От 3х до 20 символов"></div>
                    <div class="jlab-row margin text-small-bold">Повторите пароль</div>
                    <div class="jlab-row margin"><input type="password" id="pass2"></div>
                    <div class="jlab-row margin">
                        <button>Восстановить</button>
                    </div>
                </section>
            </form>
        </div>
        <div class="jlab-cell-3"></div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
