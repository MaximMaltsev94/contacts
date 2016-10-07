<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Добавить контакт</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css" />">

    <script src="<c:url value="/js/addView.js"/>"></script>
</head>
<body onload="addView.selectCountry(0);
        addView.selectCity(0);
        addView.selectRelationship(0);
        addView.selectGender(1);">
<jsp:include page="header.jsp"/>
<fmt:setLocale value="ru_RU" scope="session"/>

<div class="container">
    <section>
        <div class="jlab-row">
            <span class="text-large">Основное</span>
        </div>
        <form action="<c:url value="/contact/?action=add"/>" method="post" enctype="multipart/form-data">

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Фотография</span>

                </div>
                <div class="jlab-cell-3 center">
                    <div class="hiddenFileInputContainter">

                        <img id="blah" class="fileDownload" src="<c:url value="/sysImages/default.png"/>" width="100px"
                             height="100px">
                        <input type="file" name="fileUp" class="hidden" accept="image/*"
                               onchange="addView.readURL(this)">
                    </div>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Имя</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="firstName" required pattern="[A-Za-zА-яа-яЁё]{2,30}" maxlength="30" title="Русские или английские буквы, от 2х до 30 символов"/>
                </div>
            </div>
            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Фамилия</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="lastName" required pattern="[A-Za-zА-яа-я]{2,30}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Отчество</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="patronymic" pattern="[A-Za-zА-яа-я]{2,30}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">День рождения</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="date" name="birthDate"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Пол</span>
                </div>
                <div class="jlab-cell-3">
                    <input class="regular-radio" type="radio" name="gender" value="1" id="r1"/> <label for="r1"></label> Мужской
                    <input class="regular-radio" type="radio" name="gender" value="0" id="r0"/> <label for="r0"></label> Женский
                </div>
            </div>


            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Гражданство</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="citizenship" pattern="[A-Za-zА-яа-я]{2,50}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Семейное положение</span>
                </div>
                <div class="jlab-cell-3">
                    <select name="relationship" id="relationship">
                        <option value="0">Не выбрано</option>
                        <c:forEach var="i" items="${requestScope.get('relationshipList')}">
                            <option value="${i.id}">${i.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Веб сайт</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="url" name="webSite"/>
                </div>
            </div>


            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Эл. почта</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="email" name="email"/>
                </div>
            </div>


            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Место работы</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="companyName" pattern="[A-Za-zА-яа-я]{2,50}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Страна</span>
                </div>
                <div class="jlab-cell-3">
                    <select name="country" id="country" onchange="addView.onChangeCountry(this.value)">
                        <option value="0">Не выбрано</option>
                        <c:forEach var="i" items="${requestScope.get('countryList')}">
                            <option value="${i.id}">${i.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Город</span>
                </div>
                <div class="jlab-cell-3">
                    <select name="city" id="city">

                        <option value="0" data-country="0">Не выбрано</option>
                        <c:forEach var="i" items="${requestScope.get('cityList')}">
                            <option value="${i.id}" data-country="${i.countryID}" hidden>${i.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Адрес</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="street"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Почтовый индекс</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="postcode"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">

                </div>
                <div class="jlab-cell-3">
                    <input type="submit" value="Добавить"/>
                </div>
            </div>

        </form>
    </section>
</div>
</body>
</html>
