<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Поиск</title>

    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/pagination.css"/>">
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

                        <div class="jlab-cell-8 center">

                            <div class="jlab-row">
                                <div class="jlab-cell-12 center">
                                    <a class="text-medium"
                                       href="<c:url value="/contact/edit?id=${i.id}" /> "><c:out
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
                            <div class="imageButton" onclick="showView.onEmailContact(${i.id})">
                                <img src="<c:url value="/sysImages/email.png"/>">
                            </div>
                        </div>

                        <div class="jlab-cell-1 center">
                            <div class="imageButton edit" onclick="location.href='<c:url value="/contact/edit?id=${i.id}"/>'">
                                <img src="<c:url value="/sysImages/edit.png"/>">
                            </div>
                        </div>
                        <div class="jlab-cell-1 center ">
                            <input type="text" name="profilePicture" value="${i.profilePicture}" hidden>
                            <div class="imageButton" onclick="showView.onDeleteContact('${i.firstName}', '${i.lastName}', '${i.id}')">
                                <img src="<c:url value="/sysImages/delete.png"/>">
                            </div>
                        </div>
                    </div>
                </section>
            </c:forEach>


            <c:set var="currentPage" scope="page" value="${requestScope.getOrDefault('page', 1)}"/>
            <c:set var="lastPage" scope="page" value="${requestScope.getOrDefault('maxPageNumber', 1)}"/>

            <ul class="pagination">

                <%-- add first page link--%>
                <c:choose>
                    <c:when test="${currentPage == 1}">
                        <li><a class="active" href="javascript:;" onclick="searchView.submitForm(1)">1</a></li>
                    </c:when>

                    <c:when test="${currentPage != 1}">
                        <li><a href="javascript:;" onclick="searchView.submitForm(${currentPage - 1})">&lt;</a></li>
                        <li><a href="javascript:;" onclick="searchView.submitForm(1)">1</a></li>
                    </c:when>
                </c:choose>

                <%--add skip if too away from begining--%>
                <c:if test="${currentPage > 4}">
                    <li><a href="javascript:;">...</a></li>
                </c:if>

                <%--add middle links--%>
                <c:forEach var="i" begin="${ currentPage < 4 ? 2 : currentPage - 2}"
                           end="${currentPage > lastPage - 3 ? lastPage - 1 : currentPage + 2}">
                    <c:choose>
                        <c:when test="${currentPage == i}">
                            <li><a href="javascript:;" class="active" onclick="searchView.submitForm(${i})">${i}</a>
                            </li>
                        </c:when>
                        <c:when test="${currentPage != i}">
                            <li><a href="javascript:;" onclick="searchView.submitForm(${i})">${i}</a></li>
                        </c:when>
                    </c:choose>
                </c:forEach>


                <%--add skip if too away from ending--%>
                <c:if test="${currentPage < (lastPage - 3)}">
                    <li><a href="javascript:;">...</a></li>
                </c:if>

                <%--add next page link--%>
                <c:if test="${lastPage != 1}">
                    <c:choose>
                        <c:when test="${currentPage != lastPage}">
                            <li><a href="javascript:;" onclick="searchView.submitForm(${lastPage})">${lastPage}</a></li>
                            <li><a href="javascript:;" onclick="searchView.submitForm(${currentPage + 1})">&gt;</a></li>
                        </c:when>

                        <c:when test="${currentPage == lastPage}">
                            <li><a href="javascript:;" class="active" onclick="searchView.submitForm(${lastPage})">${lastPage}</a></li>
                        </c:when>

                    </c:choose>

                </c:if>
            </ul>

            <%--div.jlab-cell-8 end--%>
        </div>
        <div class="jlab-cell-3">
            <div id="sectionAction">
                <section>
                    <div class="jlab-row margin">
                        <button id="deleteSelected" class="jlab-cell-10 align-center" onclick="showView.onDeleteSelectedClick()">Удалить</button>
                    </div>
                    <div class="jlab-row margin">
                        <button id="sendEmail" class="jlab-cell-10 align-center" onclick="showView.onEmailSelectedClick()">Отправить письмо</button>
                    </div>
                </section>
            </div>
            <section>
                <form id="searchForm" action="<c:url value="/contact/search"/>" method="post">
                    <input type="text" name="page" id="page" value="1" hidden/>

                    <div class="text-medium">Введите параметры поиска</div>
                    <div class="text-small-bold">Имя</div>
                    <div class="jlab-row margin">
                        <input type="text" name="firstName" value="${requestScope.get('firstName')}"
                               pattern="^[A-Za-zА-Яа-яЁё]{2,30}$" maxlength="30" title="Русские или английские буквы, от 2х до 30 символов"/>
                    </div>

                    <div class="text-small-bold">Фамилия</div>
                    <div class="jlab-row margin">
                        <input type="text" name="lastName" value="${requestScope.get('lastName')}"
                               pattern="^[A-Za-zА-Яа-яЁё]{2,30}$" maxlength="30" title="Русские или английские буквы, от 2х до 30 символов"/>
                    </div>

                    <div class="text-small-bold">Отчество</div>
                    <div class="jlab-row margin">
                        <input type="text" name="patronymic" value="${requestScope.get('patronymic')}"
                               pattern="^[A-Za-zА-Яа-яЁё]{2,30}$" maxlength="30" title="Русские или английские буквы, от 2х до 30 символов"/>
                    </div>

                    <div class="text-small-bold">Возраст</div>
                    <div class="jlab-row margin">
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
                    <div class="jlab-row margin text-small">
                        <div class="jlab-cell-12">
                            <input class="regular-radio" type="radio" name="gender" value="2" id="r2"/> <label for="r2"></label>Любой
                        </div>
                   </div>
                    <div class="jlab-row text-small">
                        <div class="jlab-cell-12">
                            <input class="regular-radio" type="radio" name="gender" value="1" id="r1"/> <label for="r1"></label>Мужской
                        </div>
                   </div>
                    <div class="jlab-row margin text-small">
                        <div class="jlab-cell-12">
                            <input class="regular-radio" type="radio" name="gender" value="0" id="r0"/> <label for="r0"></label>Женский
                        </div>
                   </div>

                    <div class="text-small-bold">Гражданство</div>
                    <div class="jlab-row margin">
                        <input type="text" name="citizenship" value="${requestScope.get('citizenship')}"
                               pattern="^[A-Za-zА-Яа-яЁё\s]{2,50}$" maxlength="50" title="Русские или английские буквы, пробелы, от 2х до 50 символов"/>
                    </div>

                    <div class="text-small-bold">Семейное положение</div>
                    <div class="jlab-row margin">
                        <select name="relationship" id="relationship">
                            <option value="0">Не выбрано</option>
                            <c:forEach var="i" items="${requestScope.get('relationshipList')}">
                                <option value="${i.id}">${i.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="text-small-bold">Место работы</div>
                    <div class="jlab-row margin">
                        <input type="text" name="companyName" value="${requestScope.get('companyName')}"
                               pattern="[0-9A-Za-zА-Яа-яЁё\s]{2,50}" maxlength="50" title="Русские или английские буквы, цифры, пробелы, от 2х до 50 символов"/>
                    </div>

                    <div class="text-small-bold">Страна</div>
                    <div class="jlab-row margin">
                        <select name="country" id="country" onchange="addView.onChangeCountry(this.value)">
                            <option value="0">Не выбрано</option>
                            <c:forEach var="i" items="${requestScope.get('countryList')}">
                                <option value="${i.id}">${i.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="text-small-bold">Город</div>
                    <div class="jlab-row margin">
                        <input id="cityInput" type="text" onkeyup="addView.onCityKeyDown(this.value)">
                        <select id="city" name="city">
                            <option value="0">Не выбрано</option>
                        </select>
                    </div>

                    <div class="text-small-bold">Адрес</div>
                    <div class="jlab-row margin">
                        <input type="text" name="street" value="${requestScope.get('street')}"
                               pattern="^[0-9A-Za-zА-Яа-яЁё\s\\.\\,]{2,50}$" maxlength="50" title="Русские или английские буквы, пробелы, точки, запятые, цифры от 2х до 50 символов"/>
                    </div>

                    <div class="text-small-bold">Почтовый индекс</div>
                    <div class="jlab-row margin">
                        <input type="text" name="postcode" value="${requestScope.get('postcode')}"
                               pattern="^[0-9A-Za-z]{2,20}$" maxlength="20" title="Английские буквы, цифрыб от 2х до 20 символов"/>
                    </div>

                    <div class="jlab-row margin margin">
                        <button class="jlab-cell-12 align-center" type="button" onclick="searchView.submitForm(1)" >Поиск</button>
                    </div>
                </form>
            </section>

        </div>

        <%--div.jlab-row end--%>
    </div>

    <%--div.container end--%>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
