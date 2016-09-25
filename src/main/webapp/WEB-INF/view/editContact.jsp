<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Редактировать контакт</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css" />">
    <link rel="stylesheet" href="<c:url value="/css/popup.css" />">
    <script src="<c:url value="/js/addView.js"/>"></script>
</head>
<body onload="addView.selectCountryAndCity(${requestScope.get('contact').countryID}, ${requestScope.get('contact').cityID});
                addView.setPhoneCount(${requestScope.get('phoneList').size()})">
<jsp:include page="header.jsp"/>

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
        <%--main info section end--%>
    </section>

    <section id="phoneSection">
        <div class="jlab-row">
            <div class="jlab-cell-3 center">
                <span class="text-large">Контактные телефоны</span>
            </div>

            <div class="jlab-cell-9 center">
                <div class="imageButton add" onclick="addView.showAddPhonePopup()"></div>
            </div>

        </div>
        <div id="phonePopup" class="popupBack">
            <div class="popup">
                <div class="jlab-row">
                    <div class="jlab-cell-12 align-right">
                        <a class="text-large" href="#phoneSection">&times;</a>
                    </div>
                </div>
                <div class="jlab-row margin">
                    <select id="popup_phoneType">
                        <option value="0">Моб.</option>
                        <option value="1">Дом.</option>
                    </select>

                    <select id="popup_countryCode">
                        <c:forEach var="i" items="${requestScope.get('countryList')}">
                            <option value="${i.id}">+${i.phoneCode}</option>
                        </c:forEach>
                    </select>

                    <input id="popup_operatorCode" type="text" pattern="[0-9]{2,5}" placeholder="Код. оп.">

                    <input id="popup_phoneNumber" type="text" pattern="[0-9]{4,9}" placeholder="Номер">
                </div>

                <div class="jlab-row margin">
                    <input type="text" id="popup_comment" placeholder="Комментарий">
                </div>

                <div class="jlab-row margin">
                    <div class="jlab-cell-12 align-right"><button id="popup_submit">Добавить</button></div>
                </div>

            </div>

            <%--div id popup end--%>
        </div>

        <c:set var="phoneCount" value="0" scope="page"/>
        <c:forEach var="i" items="${requestScope.get('phoneList')}">
            <c:set var="phoneCount" value="${phoneCount + 1}" scope="page"/>
            <div id="phone-${phoneCount}" class="jlab-row margin">
                <input type="text" name="type_phone-${phoneCount}" id="type_phone-${phoneCount}" value="${i.type == false ? 0 : 1}" hidden>
                <input type="text" name="country_code_phone-${phoneCount}" id="country_code_phone-${phoneCount}" value="${i.countryID - 1}" hidden>
                <input type="text" name="op_code_phone-${phoneCount}" id="op_code_phone-${phoneCount}" value="${i.operatorCode}" hidden>
                <input type="text" name="number_phone-${phoneCount}" id="number_phone-${phoneCount}" value="${i.phoneNumber}" hidden>
                <input type="text" name="comment_phone-${phoneCount}" id="comment_phone-${phoneCount}" value="${i.comment}" hidden>
                <div id="display_type_phone-${phoneCount}" class="jlab-cell-3 align-right text-small">
                    <c:choose>
                        <c:when test="${i.type == true}">
                            Дом.
                        </c:when>
                        <c:otherwise>
                            Моб.
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="jlab-cell-3">
                    <div id="display_number_phone-${phoneCount}" class="jlab-row text-medium">
                        <c:forEach var="country" items="${requestScope.get('countryList')}">
                            <c:if test="${country.id == i.countryID}">
                                +${country.phoneCode}${i.operatorCode}${i.phoneNumber}
                            </c:if>
                        </c:forEach>
                    </div>
                    <div id="display_comment_phone-${phoneCount}" class="jlab-row text text-small">
                        ${i.comment}
                    </div>
                </div>

                <div class="jlab-cell-1">
                    <div class="imageButton edit" onclick="addView.showEditPhonePopup(this)"></div>
                </div>
                <div class="jlab-cell-1">
                    <div class="imageButton delete" onclick="addView.deletePhoneElement(this)"></div>
                </div>
            </div>
        </c:forEach>


    <%--contact phones section end--%>
    </section>
    <%--div container end--%>
</div>
</body>
</html>
