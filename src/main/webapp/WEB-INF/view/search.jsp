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
    <script src="<c:url value="/js/showView.js"/>"></script>
    <script src="<c:url value="/js/search.js"/>"></script>

</head>
<body onload="addView.selectCountry(${requestScope.get('country')});
        addView.selectCity(${requestScope.get('city')});
        addView.selectRelationship(${requestScope.get('relationship')});
        addView.selectGender(${requestScope.get('gender')});
        addView.selectAge(${requestScope.get('age1')}, ${requestScope.get('age2')});">
<jsp:include page="header.jsp"/>

<fmt:setLocale value="ru_RU" scope="session"/>

<div class="container">

    <div class="jlab-row">
        <div class="jlab-cell-9">
            <c:if test="${requestScope.get('contactList') == null || requestScope.get('contactList').size() == 0}">
                <section><span class="text-medium">По вашему запросу ничего не найдено</span></section>
            </c:if>
            <c:forEach var="i" items="${requestScope.get('contactList')}">
                <section>
                    <div class="jlab-row">

                        <div class="jlab-cell-1 center">
                            <input type="checkbox" id="${i.id}" class="regular-checkbox" onchange="searchView.onCheckBoxChecked(this)"/><label for="${i.id}"></label>
                        </div>

                        <div class="jlab-cell-1 center">
                            <img src="<c:url value="${i.profilePicture}" /> " width="50px" height="50px">
                        </div>

                        <div class="jlab-cell-9 center">

                            <div class="jlab-row">
                                <div class="jlab-cell-12 center">
                                    <a class="text-medium"
                                       href="<c:url value="?action=edit&id=${i.id}" /> "><c:out
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
                            <a href="<c:url value="?action=edit&id=${i.id}"/>">
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
            <section id="sectionAction" hidden>
                <div class="jlab-row margin">
                    <button id="deleteSelected" class="jlab-cell-10 align-center" onclick="showView.onDeleteSelectedClick()">Удалить</button>
                </div>
            </section>
            <section>
                <form action="<c:url value="?action=search"/>" method="post">

                    <div class="text-medium">Введите параметры поиска</div>
                    <div class="text-small-bold">Имя</div>
                    <div class="jlab-row">
                        <input type="text" name="firstName" value="${requestScope.get('firstName')}" pattern="[A-Za-zА-яа-я]{2,30}"/>
                    </div>

                    <div class="text-small-bold">Фамилия</div>
                    <div class="jlab-row">
                        <input type="text" name="lastName" value="${requestScope.get('lastName')}" pattern="[A-Za-zА-яа-я]{2,30}"/>
                    </div>

                    <div class="text-small-bold">Отчество</div>
                    <div class="jlab-row">
                        <input type="text" name="patronymic" value="${requestScope.get('patronymic')}" pattern="[A-Za-zА-яа-я]{2,30}"/>
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
                            <option value="0">До</option>
                            <c:forEach var="i" begin="1" end="100">
                                <option value="${i}">${i}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="text-small-bold">Пол</div>
                    <div class="jlab-row text-small">
                        <div class="jlab-cell-12">
                            <input class="regular-radio" type="radio" name="gender" value="2" id="r2"/> <label for="r2"></label>Любой
                        </div>
                   </div>
                    <div class="jlab-row text-small">
                        <div class="jlab-cell-12">
                            <input class="regular-radio" type="radio" name="gender" value="1" id="r1"/> <label for="r1"></label>Мужской
                        </div>
                   </div>
                    <div class="jlab-row text-small">
                        <div class="jlab-cell-12">
                            <input class="regular-radio" type="radio" name="gender" value="0" id="r0"/> <label for="r0"></label>Женский
                        </div>
                   </div>

                    <div class="text-small-bold">Гражданство</div>
                    <div class="jlab-row">
                        <input type="text" name="citizenship" value="${requestScope.get('citizenship')}" pattern="[A-Za-zА-яа-я]{2,50}"/>
                    </div>

                    <div class="text-small-bold">Семейное положение</div>
                    <div class="jlab-row">
                        <select name="relationship" id="relationship">
                            <option value="0">Не выбрано</option>
                            <c:forEach var="i" items="${requestScope.get('relationshipList')}">
                                <option value="${i.id}">${i.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="text-small-bold">Место работы</div>
                    <div class="jlab-row">
                        <input type="text" name="companyName" value="${requestScope.get('companyName')}" pattern="[A-Za-zА-яа-я]{2,50}"/>
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
                        <input type="text" name="street" value="${requestScope.get('street')}"/>
                    </div>

                    <div class="text-small-bold">Почтовый индекс</div>
                    <div class="jlab-row">
                        <input type="text" name="postcode" value="${requestScope.get('postcode')}"/>
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
