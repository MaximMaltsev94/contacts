<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Поиск</title>

    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css" />">

    <script src="<c:url value="/js/addView.js"/>"></script>
    <script src="<c:url value="/js/main.js"/>"></script>
</head>
<body onload="addView.selectCountryAndCity(0, 0);">
<jsp:include page="header.jsp"/>

<fmt:setLocale value="ru_RU" scope="session"/>

<div class="container">

    <div class="jlab-row">
        <div class="jlab-cell-9">
            <c:if test="${requestScope.get('contactList') == null}">
                <section><span class="text-medium">По вашему запросу ничего не найдено</span></section>
            </c:if>
            <c:forEach var="i" items="${requestScope.get('contactList')}">
                <section>
                    <div class="jlab-row">

                        <div class="jlab-cell-1 center">
                            <img src="<c:url value="${i.profilePicture}" /> " width="50px" height="50px">
                        </div>

                        <div class="jlab-cell-9 center">

                            <div class="jlab-row">
                                <div class="jlab-cell-12 center">
                                    <a class="text-medium"
                                       href="<c:url value="/contact/?action=edit&id=${i.id}" /> "><c:out
                                            value="${i.firstName} ${i.lastName}"/></a>
                                </div>
                            </div>

                            <div class="jlab-row">
                                <div class="jlab-cell-12 center">
                                <span class="text-small">
                                    <c:if test="${i.birthDate != null}">
                                        <fmt:formatDate value="${i.birthDate}" type="date" dateStyle="long"/>
                                    </c:if>
                                    <c:out value=" ${i.companyName} ${i.street}"/>
                                </span>
                                </div>
                            </div>

                        </div>

                        <div class="jlab-cell-1 center">
                            <a href="<c:url value="/contact/?action=edit&id=${i.id}"/>">
                                <div class="imageButton edit">
                                </div>
                            </a>
                        </div>
                        <div class="jlab-cell-1 center ">
                            <input type="text" name="profilePicture" value="${i.profilePicture}" hidden>
                            <div class="imageButton delete"
                                 onclick="showView.onDeleteContact('${i.firstName}', '${i.lastName}', '${i.id}')">
                            </div>
                        </div>
                    </div>
                </section>
            </c:forEach>

            <%--div.jlab-cell-8 end--%>
        </div>
        <div class="jlab-cell-3">
            <section>
                <form action="<c:url value="/contact/"/>" method="get">
                    <input type="text" name="action" value="search" hidden>

                    <div class="text-small-bold">Имя</div>
                    <div class="jlab-row">
                        <input type="text" name="firstName" pattern="[A-Za-zА-яа-я]{2,30}"/>
                    </div>

                    <div class="text-small-bold">Фамилия</div>
                    <div class="jlab-row">
                        <input type="text" name="lastName" pattern="[A-Za-zА-яа-я]{2,30}"/>
                    </div>

                    <div class="text-small-bold">Отчество</div>
                    <div class="jlab-row">
                        <input type="text" name="patronymic" pattern="[A-Za-zА-яа-я]{2,30}"/>
                    </div>

                    <div class="text-small-bold">Возраст</div>
                    <div class="jlab-row">
                        <select name="age1" id="age1">
                            <option value="0">От</option>
                            <c:forEach var="i" begin="1" end="100">
                                <option value="${i}">${i}</option>
                            </c:forEach>
                        </select>
                        <pre>  -  </pre>
                        <select name="age2" id="age2">
                            <option value="101">До</option>
                            <c:forEach var="i" begin="1" end="100">
                                <option value="${i}">${i}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="text-small-bold">Пол</div>
                    <div class="jlab-row text-small">
                        <div class="jlab-cell-12">
                            <input class="regular-radio" type="radio" name="gender" value="2" checked id="r3"/> <label for="r3"></label>Любой
                        </div>
                   </div>
                    <div class="jlab-row text-small">
                        <div class="jlab-cell-12">
                            <input class="regular-radio" type="radio" name="gender" value="1" id="r1"/> <label for="r1"></label>Мужской
                        </div>
                   </div>
                    <div class="jlab-row text-small">
                        <div class="jlab-cell-12">
                            <input class="regular-radio" type="radio" name="gender" value="0" id="r2"/> <label for="r2"></label>Женский
                        </div>
                   </div>

                    <div class="text-small-bold">Гражданство</div>
                    <div class="jlab-row">
                        <input type="text" name="citizenship"/>
                    </div>

                    <div class="text-small-bold">Семейное положение</div>
                    <div class="jlab-row">
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

                    <div class="text-small-bold">Место работы</div>
                    <div class="jlab-row">
                        <input type="text" name="companyName"/>
                    </div>

                    <div class="text-small-bold">Страна</div>
                    <div class="jlab-row">
                        <select name="country" id="country" onchange="addView.onChangeCountry(this.value)">
                            <option value="0">Не выбрано</option>
                            <c:forEach var="i" items="${requestScope.get('countryList')}">
                                <option value="${i.id}">${i.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="text-small-bold">Город</div>
                    <div class="jlab-row">
                        <select name="city" id="city">
                            <option value="0" data-country="0">Не выбрано</option>
                            <c:forEach var="i" items="${requestScope.get('cityList')}">
                                <option value="${i.id}" data-country="${i.countryID}">${i.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="text-small-bold">Адрес</div>
                    <div class="jlab-row">
                        <input type="text" name="street"/>
                    </div>

                    <div class="text-small-bold">Почтовый индекс</div>
                    <div class="jlab-row">
                        <input type="text" name="postcode"/>
                    </div>

                    <div class="jlab-row margin">
                        <input class="jlab-cell-12 align-center" type="submit" value="Поиск"/>
                    </div>
                </form>
            </section>

        </div>

        <%--div.jlab-row end--%>
    </div>

    <%--div.container end--%>
</div>

</body>
</html>
