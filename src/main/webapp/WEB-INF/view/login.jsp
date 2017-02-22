<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Войти</title>

    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/actionTooltip.css"/>">

    <script src="<c:url value="/js/main.js"/>"></script>
    <script src="<c:url value="/js/login.js"/>"></script>
</head>
<body onload="main.showTooltip('${sessionScope.get('tooltip-text')}', '${sessionScope.get('tooltip-type')}')">
<c:remove var="tooltip-text" scope="session"/>
<c:remove var="tooltip-type" scope="session"/>
<jsp:include page="header.jsp"/>
<span id="tooltip"></span>

<div class="container">
    <div class="jlab-row">
        <div class="jlab-cell-9">
            <section>
                <div class="jlab-row margin">
                    <div class="jlab-cell-1"></div>
                    <div class="jlab-cell-10">
                        <div class="jlab-row margin">
                            <div class="jlab-cell-12 text-large align-center">
                                Добро пожаловать!
                            </div>
                        </div>
                        <div class="jlab-row margin">
                            <div class="jlab-cell-12 text-medium align-center">
                                Вы находитесь на платформе управления контактами.
                            </div>
                        </div>

                        <div class="jlab-row margin">
                            <div class="jlab-cell-3">

                            </div>
                            <div class="jlab-cell-6 align-center">
                                <img src="<c:url value="/sysImages/Networking.png"/>" width="100%">
                            </div>
                        </div>

                        <div class="jlab-row margin text-medium">
                            <ul class="regular">
                                <li>Управляйте своими контактами.</li>
                                <li>Импортируйте контакты из популярных социальных сетей.</li>
                                <li>Получайте уведомления о днях рождения.</li>
                                <li>Производите рассылки электронных писем.</li>
                            </ul>
                        </div>
                    </div>
                </div>

            </section>
        </div>
        <div class="jlab-cell-3">
            <section>
                <form action="j_security_check" method="post">
                    <div class="jlab-row margin text-small-bold">Имя пользователя</div>
                    <div class="jlab-row margin">
                        <input name="j_username" type="text" value="${sessionScope.getOrDefault('j_username', '')}">
                        <c:remove var="j_username" scope="session"/>
                    </div>
                    <div class="jlab-row margin text-small-bold">Пароль</div>
                    <div class="jlab-row margin">
                        <input name="j_password" type="password">
                    </div>

                    <div class="jlab-row margin">
                        <input class="jlab-cell-12" type="submit" value="Войти">
                    </div>

                    <div class="jlab-row margin">
                        <div class="jlab-cell-12 align-center text-small-bold">
                            <a href="#">Забыли пароль?</a></div>
                        </div>
                </form>
            </section>
            <br>
            <section>
                <form onsubmit="return loginView.validateRegistrationForm()" action="<c:url value="/contact/register"/>" method="post">
                    <div class="jlab-row">
                        <div class="jlab-cell-12 align-center text-small-bold">
                            Впервые здесь?
                        </div>
                    </div>
                    <div class="jlab-row">
                        <div class="jlab-cell-12 align-center text-medium">
                            Зарегистрируйтесь
                        </div>
                    </div>
                    <div class="jlab-row margin text-small-bold">Имя пользователя</div>
                    <div class="jlab-row margin">
                        <input name="username" type="text" pattern="[\w]{3,15}" required title="Буквы, цифры, знак подчеркивания. От 3х до 15 символов">
                    </div>
                    <div class="jlab-row margin text-small-bold">E-mail</div>
                    <div class="jlab-row margin">
                        <input name="email" type="text" pattern="^([a-z0-9_\.-]+)@([a-z0-9_\.-]+)\.([a-z\.]{2,6})$" maxlength="255" required>
                    </div>
                    <input name="bdate_notify" type="checkbox" checked hidden>
                    <div class="jlab-row margin text-small-bold">Пароль</div>
                    <div class="jlab-row margin">
                        <input id="pass1" name="password" type="password" pattern="[\w]{3,20}" required title="Буквы, цифры, знак подчеркивания. От 3х до 20 символов">
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
<jsp:include page="footer.jsp"/>
</body>
</html>
