<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Редактировать контакт</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css" />">

    <script src="<c:url value="/js/addView.js"/>"></script>
</head>
<body onload="addView.selectCountryAndCity(${requestScope.get('contact').countryID}, ${requestScope.get('contact').cityID})">
<nav>
    <span class="text-large">
        Contacts
    </span>
</nav>

<div class="container">
    <section>
        <div class="jlab-row">
            <span class="text-large">Основное</span>
        </div>
        <form action="<c:url value="/contact/?action=edit"/>" method="post" enctype="multipart/form-data">
            <input type="text" name="id" value="${requestScope.get('contact').id}" hidden>
            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Фотография</span>

                </div>
                <div class="jlab-cell-9 center">
                    <div class="hiddenFileInputContainter">

                        <img id="blah" class="fileDownload"
                             src="<c:url value="${requestScope.get('contact').profilePicture}"/>" width="100px"
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
                <div class="jlab-cell-9">
                    <input type="text" name="firstName" value="${requestScope.get('contact').firstName}" required
                           pattern="[A-Za-zА-яа-я]{2,30}"/>
                </div>
            </div>
            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Фамилия</span>
                </div>
                <div class="jlab-cell-9">
                    <input type="text" name="lastName" value="${requestScope.get('contact').lastName}" required
                           pattern="[A-Za-zА-яа-я]{2,30}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Отчество</span>
                </div>
                <div class="jlab-cell-9">
                    <input type="text" name="patronymic" value="${requestScope.get('contact').patronymic}"
                           pattern="[A-Za-zА-яа-я]{2,30}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">День рождения</span>
                </div>
                <div class="jlab-cell-9">
                    <input type="date" name="birthDate" value="${requestScope.get('contact').birthDate}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Пол</span>
                </div>
                <div class="jlab-cell-9">
                    <c:choose>
                        <c:when test="${requestScope.get('contact').gender == true}">
                            <input class="regular-radio" type="radio" name="gender" value="1" checked id="r1"/> <label
                                for="r1"></label> Мужской
                            <input class="regular-radio" type="radio" name="gender" value="0" id="r2"/> <label
                                for="r2"></label> Женский
                        </c:when>
                        <c:otherwise>
                            <input class="regular-radio" type="radio" name="gender" value="1" id="r1"/> <label
                                for="r1"></label> Мужской
                            <input class="regular-radio" type="radio" name="gender" value="0" checked id="r2"/> <label
                                for="r2"></label> Женский
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>


            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Гражданство</span>
                </div>
                <div class="jlab-cell-9">
                    <input type="text" name="citizenship" value="${requestScope.get('contact').citizenship}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Семейное положение</span>
                </div>
                <div class="jlab-cell-9">
                    <select name="relationship">
                        <c:forEach var="i" items="${requestScope.get('relationshipList')}">
                            <c:choose>
                                <c:when test="${requestScope.get('contact').relationshipID == i.id}">
                                    <option value="${i.id}" selected>${i.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${i.id}">${i.name}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Веб сайт</span>
                </div>
                <div class="jlab-cell-9">
                    <input type="text" name="webSite" value="${requestScope.get('contact').webSite}"/>
                </div>
            </div>


            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Эл. почта</span>
                </div>
                <div class="jlab-cell-9">
                    <input type="email" name="email" value="${requestScope.get('contact').email}"/>
                </div>
            </div>


            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Место работы</span>
                </div>
                <div class="jlab-cell-9">
                    <input type="text" name="companyName" value="${requestScope.get('contact').companyName}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Страна</span>
                </div>
                <div class="jlab-cell-9">
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
                <div class="jlab-cell-9">
                    <select name="city" id="city">

                        <option value="0" data-country="0">Не выбрано</option>
                        <c:forEach var="i" items="${requestScope.get('cityList')}">
                            <option value="${i.id}" data-country="${i.countryID}">${i.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Адрес</span>
                </div>
                <div class="jlab-cell-9">
                    <input type="text" name="street" value="${requestScope.get('contact').street}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Почтовый индекс</span>
                </div>
                <div class="jlab-cell-9">
                    <input type="text" name="postcode" value="${requestScope.get('contact').postcode}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">

                </div>
                <div class="jlab-cell-9">
                    <input type="submit" value="Сохранить"/>
                </div>
            </div>

        </form>
    </section>
</div>
</body>
</html>
