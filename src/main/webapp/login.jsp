<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Войти</title>

    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css"/>">

    <script src="<c:url value="/js/main.js"/>"></script>
</head>
<body onload="main.showTooltip('${sessionScope.get('tooltip-text')}', '${sessionScope.get('tooltip-type')}')">
<c:remove var="tooltip-text" scope="session"/>
<c:remove var="tooltip-type" scope="session"/>
<jsp:include page="WEB-INF/view/header.jsp"/>
<div class="container">
    <div class="jlab-row">
        <div class="jlab-cell-9">
            <section>
                <div class="text-large">
                    Добро пожаловать!
                </div>
            </section>
        </div>
        <div class="jlab-cell-3">
            <section>
                <form action="j_security_check" method="post">
                    <div class="jlab-row margin text-small-bold">Имя пользователя</div>
                    <div class="jlab-row margin">
                        <input name="j_username" type="text">
                    </div>
                    <div class="jlab-row margin text-small-bold">Пароль</div>
                    <div class="jlab-row margin">
                        <input name="j_password" type="password">
                    </div>
                    <%--<div class="jlab-row margin">--%>
                        <%--<div class="jlab-cell-4 align-right text-small-bold">Имя пользователя</div>--%>
                        <%--<div class="jlab-cell-8">--%>
                            <%--<input name="j_username" type="text">--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="jlab-row margin">--%>
                        <%--<div class="jlab-cell-4 align-right text-small-bold">Пароль</div>--%>
                        <%--<div class="jlab-cell-8">--%>
                            <%--<input name="j_password" type="password">--%>
                        <%--</div>--%>
                    <%--</div>--%>

                    <div class="jlab-row margin">
                        <input class="jlab-cell-12" type="submit" value="Войти">
                    </div>
                </form>
            </section>
            <br>
            <section>
                <form action="/register" method="post">
                    <div class="jlab-row margin text-medium">Впервые здесь? Зарегистрируйтесь</div>
                    <br>
                    <div class="jlab-row margin text-small-bold">Имя пользователя</div>
                    <div class="jlab-row margin">
                        <input name="username" type="text">
                    </div>
                    <div class="jlab-row margin text-small-bold">Пароль</div>
                    <div class="jlab-row margin">
                        <input id="pass1" name="password" type="password">
                    </div>
                    <div class="jlab-row margin text-small-bold">Подтвердите пароль</div>
                    <div class="jlab-row margin">
                        <input id="pass2" type="password">
                    </div>

                    <div class="jlab-row margin">
                        <input class="jlab-cell-12" type="submit" value="Зарегистрироваться">
                    </div>
                </form>
            </section>
        </div>
    </div>
</div>
<jsp:include page="WEB-INF/view/footer.jsp"/>
</body>
</html>
