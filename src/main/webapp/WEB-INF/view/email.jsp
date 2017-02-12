<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
<body onload="email.onReceiverSelected()">
<jsp:include page="header.jsp"/>
<fmt:setLocale value="ru_RU" scope="session"/>

<div class="container">
        <div class="jlab-row">
            <div class="jlab-cell-8">
                <section>
                    <div class="text-large">Отправить письмо</div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-2 text-small align-right">Кому</div>
                        <div class="jlab-cell-10">
                            <input readonly id="receivers" onclick="email.onDropdownShow('myDropdown')" type="text">
                            <div id="myDropdown" class="dropdown-content">
                                <c:if test="${requestScope.get('contactList').size() eq 0}">
                                    <span class="text-medium">Контакты у которых указан email отсутствуют.</span>
                                </c:if>
                                <c:forEach var="i" items="${requestScope.get('contactList')}">
                                    <div class="jlab-row margin">
                                        <div class="jlab-cell-1 center">

                                            <c:set var="checked" value="false" scope="page"/>
                                            <c:forEach var="selectedID" items="${requestScope.get('selectedContacts')}">
                                                <c:if test="${i.id == selectedID}">
                                                    <input checked type="checkbox" data-fio="${i.firstName} ${i.lastName}"
                                                           id="${i.id}" class="regular-checkbox"
                                                           onclick="email.onReceiverSelected()"/>
                                                    <c:set var="checked" value="true" scope="page"/>
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${checked == false}">
                                                <input type="checkbox" data-fio="${i.firstName} ${i.lastName}" id="${i.id}"
                                                       class="regular-checkbox" onclick="email.onReceiverSelected()"/>
                                            </c:if>

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
                        <div class="jlab-cell-2 text-small align-right">Тема</div>
                        <div class="jlab-cell-10"><input name="subject" id="mailSubject" type="text"></div>
                    </div>

                    <div class="jlab-row margin">
                        <div class="jlab-cell-2 align-right text-small">Шаблон</div>
                        <div class="jlab-cell-10">
                            <select id="templateSelect" onchange="email.onTemplateChange()">
                                <option selected>Не выбрано</option>
                                <c:forEach var="i" items="${requestScope.get('templates')}">
                                    <option>${i.key}</option>
                                </c:forEach>
                            </select>

                            <c:set var="templateCount" value="0" scope="page"/>
                            <c:forEach var="i" items="${requestScope.get('templates')}">
                                <c:set var="templateCount" value="${templateCount + 1}" scope="page"/>
                                <textarea hidden id="template-${templateCount}">${i.value}</textarea>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="jlab-row margin">
                        <div class="jlab-cell-2"></div>
                        <div class="jlab-cell-10"><textarea name="mailText" id="mailText" rows="20"></textarea></div>
                    </div>

                    <div class="jlab-row margin">
                        <div class="jlab-cell-2"></div>
                        <div class="jlab-cell-10 align-right">
                            <button onclick="email.onEmailSubmit()">Отправить</button>
                        </div>
                    </div>

                </section>
            </div>
            <div class="jlab-cell-4">
                <section>
                    <div class="jlab-row">
                        <span class="text-medium">Метасимволы </span>
                        <div class="imageButton help"
                             onclick="email.onDropdownShow('helpDropDown')"></div>
                    </div>
                    <div class="jlab-row">

                        <div id="helpDropDown" class="dropdown-content">
                            <span class="text-small">
                                <b>Метасимволы</b> - это специальные конструкции, которые при отправке письма для каждого конкретного контакта заменяются
                                на соответствующую информацию контакта. Например текст письма
                                'Привет &lt;firstName&gt;' для контакта Иван Иванов преобразуется в текст 'Привет Иван'.
                            </span>
                        </div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;firstName&gt;</div>
                        <div class="jlab-cell-6 text-small">Имя</div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;lastName&gt;</div>
                        <div class="jlab-cell-6 text-small">Фамилия</div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;patronymic&gt;</div>
                        <div class="jlab-cell-6 text-small">Отчество</div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;birthDate&gt;</div>
                        <div class="jlab-cell-6 text-small">Дата рождения</div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;gender&gt;</div>
                        <div class="jlab-cell-6 text-small">Пол</div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;citizenship&gt;</div>
                        <div class="jlab-cell-6 text-small">Гражданство</div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;relationship&gt;</div>
                        <div class="jlab-cell-6 text-small">Семейное положение</div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;website&gt;</div>
                        <div class="jlab-cell-6 text-small">Веб сайт</div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;companyName&gt;</div>
                        <div class="jlab-cell-6 text-small">Место работы</div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;country&gt;</div>
                        <div class="jlab-cell-6 text-small">Страна</div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;city&gt;</div>
                        <div class="jlab-cell-6 text-small">Город</div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;street&gt;</div>
                        <div class="jlab-cell-6 text-small">Адрес</div>
                    </div>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-6 text-small-bold">&lt;postcode&gt;</div>
                        <div class="jlab-cell-6 text-small">Почтовый индекс</div>
                    </div>
                </section>
            </div>
        </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
