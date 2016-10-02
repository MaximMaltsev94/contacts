<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Редактировать контакт</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css" />">
    <link rel="stylesheet" href="<c:url value="/css/popup.css" />">
    <script src="<c:url value="/js/addView.js"/>"></script>
    <script src="<c:url value="/js/main.js"/>"></script>
    <script src="<c:url value="/js/popupPhone.js"/>"></script>
    <script src="<c:url value="/js/popupAttachment.js"/>"></script>
</head>
<body onload="addView.selectCountry(${requestScope.get('contact').countryID});
        addView.selectCity(${requestScope.get('contact').cityID});
        addView.selectRelationship(${requestScope.get('contact').relationshipID - 1});
        addView.selectGender(${requestScope.get('contact').gender});

        popupPhone.setPhoneCount(${requestScope.get('phoneList').size()});
        popupAttachment.setAttachmentCount(${requestScope.get('attachmentList').size()})">
<jsp:include page="header.jsp"/>
<fmt:setLocale value="ru_RU" scope="session"/>

<div class="container">
    <form action="<c:url value="/contact/?action=edit"/>" method="post" enctype="multipart/form-data">
        <section>
            <div class="jlab-row">
                <span class="text-large">Основное</span>
            </div>
            <input type="text" name="id" value="${requestScope.get('contact').id}" hidden>
            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Фотография</span>

                </div>
                <div class="jlab-cell-3 center">
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
                <div class="jlab-cell-3">
                    <input type="text" name="firstName" value="${requestScope.get('contact').firstName}" required
                           pattern="[A-Za-zА-яа-я]{2,30}"/>
                </div>
            </div>
            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Фамилия</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="lastName" value="${requestScope.get('contact').lastName}" required
                           pattern="[A-Za-zА-яа-я]{2,30}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Отчество</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="patronymic" value="${requestScope.get('contact').patronymic}"
                           pattern="[A-Za-zА-яа-я]{2,30}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">День рождения</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="date" name="birthDate" value="${requestScope.get('contact').birthDate}"/>
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
                    <input type="text" name="citizenship" value="${requestScope.get('contact').citizenship}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Семейное положение</span>
                </div>
                <div class="jlab-cell-3">
                    <select name="relationship" id="relationship">
                        <c:forEach var="i" items="${requestScope.get('relationshipList')}">
                            <option value="${i.id}" >${i.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Веб сайт</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="webSite" value="${requestScope.get('contact').webSite}"/>
                </div>
            </div>


            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Эл. почта</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="email" name="email" value="${requestScope.get('contact').email}"/>
                </div>
            </div>


            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Место работы</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="companyName" value="${requestScope.get('contact').companyName}"/>
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
                            <option value="${i.id}" data-country="${i.countryID}">${i.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Адрес</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="street" value="${requestScope.get('contact').street}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">
                    <span class="text-small">Почтовый индекс</span>
                </div>
                <div class="jlab-cell-3">
                    <input type="text" name="postcode" value="${requestScope.get('contact').postcode}"/>
                </div>
            </div>

            <div class="jlab-row margin">
                <div class="jlab-cell-3 align-right">

                </div>
                <div class="jlab-cell-3 center">
                    <input type="submit" value="Сохранить"/>
                </div>
            </div>
            <%--main info section end--%>
        </section>

        <section id="phoneSection">
            <div class="jlab-row">
                <div class="jlab-cell-4 center">
                    <span class="text-large">Контактные телефоны</span>
                </div>

                <div class="jlab-cell-2"></div>
                <div class="jlab-cell-1"></div>
                <div class="jlab-cell-1"></div>
                <div class="jlab-cell-1 center">
                    <div class="imageButton add" onclick="popupPhone.showAddPhonePopup()"></div></div>

            </div>
            <div id="phonePopup" class="popupBack">
                <div class="popup">
                    <div class="jlab-row">
                        <div class="jlab-cell-12 align-right">
                            <a class="text-large" href="#phoneSection">&times;</a>
                        </div>
                    </div>
                    <div class="jlab-row margin">
                        <select id="popupPhone_phoneType">
                            <option value="0">Моб.</option>
                            <option value="1">Дом.</option>
                        </select>

                        <select id="popupPhone_countryCode">
                            <c:forEach var="i" items="${requestScope.get('countryList')}">
                                <option value="${i.id}">+${i.phoneCode}</option>
                            </c:forEach>
                        </select>

                        <input id="popupPhone_operatorCode" type="text" pattern="[0-9]{2,5}" placeholder="Код. оп.">

                        <input id="popupPhone_phoneNumber" type="text" pattern="[0-9]{4,9}" placeholder="Номер">
                    </div>

                    <div class="jlab-row margin">
                        <input type="text" id="popupPhone_comment" placeholder="Комментарий">
                    </div>

                    <div class="jlab-row margin">
                        <div class="jlab-cell-12 align-right">
                            <button type="button" id="popupPhone_ok">Добавить</button>
                        </div>
                    </div>

                </div>

                <%--div id phonePopup end--%>
            </div>

            <c:set var="phoneCount" value="0" scope="page"/>
            <c:forEach var="i" items="${requestScope.get('phoneList')}">
                <c:set var="phoneCount" value="${phoneCount + 1}" scope="page"/>
                <div id="phone-${phoneCount}" class="jlab-row margin">
                    <input type="text" name="type_phone-${phoneCount}" id="type_phone-${phoneCount}"
                           value="${i.type == false ? 0 : 1}" hidden>
                    <input type="text" name="country_code_phone-${phoneCount}" id="country_code_phone-${phoneCount}"
                           value="${i.countryID - 1}" hidden>
                    <input type="text" name="op_code_phone-${phoneCount}" id="op_code_phone-${phoneCount}"
                           value="${i.operatorCode}" hidden>
                    <input type="text" name="number_phone-${phoneCount}" id="number_phone-${phoneCount}"
                           value="${i.phoneNumber}" hidden>
                    <input type="text" name="comment_phone-${phoneCount}" id="comment_phone-${phoneCount}"
                           value="${i.comment}" hidden>
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

                    <div class="jlab-cell-1"></div>

                    <div class="jlab-cell-1">
                        <div class="imageButton edit" onclick="popupPhone.showEditPhonePopup(this)"></div>
                    </div>
                    <div class="jlab-cell-1">
                        <div class="imageButton delete" onclick="popupPhone.deletePhoneElement(this)"></div>
                    </div>
                </div>
            </c:forEach>
            <%--contact phones section end--%>
        </section>

        <section id="attachmentSection">
            <div class="jlab-row">
                <div class="jlab-cell-3 center">
                    <span class="text-large">Присоединения</span>
                </div>
                <div class="jlab-cell-3"></div>
                <div class="jlab-cell-1"></div>
                <div class="jlab-cell-1"></div>
                <div class="jlab-cell-1 center">
                    <div class="imageButton addAttachment" onclick="popupAttachment.showAddAttachmentPopup()"></div></div>
            </div>

            <div id="attachmentPopup" class="popupBack">
                <div class="popup">
                    <div class="jlab-row">
                        <div class="jlab-cell-12 align-right">
                            <a class="text-large" href="#attachmentSection">&times;</a>
                        </div>
                    </div>
                    <div class="jlab-row margin">
                        <input id="popupAttachment_fileName" type="text" placeholder="Имя файла">
                    </div>

                    <div class="jlab-row margin">
                        <input type="text" id="popupAttachment_comment" placeholder="Комментарий">
                    </div>

                    <div class="jlab-row margin">
                        <div class="jlab-cell-12 align-right">
                            <button type="button" id="popupAttachment_ok">Добавить</button>
                        </div>
                    </div>

                </div>
                <%--div id attachmentPopup end--%>
            </div>


            <c:set var="attachmentCount" value="0" scope="page"/>
            <c:forEach var="i" items="${requestScope.get('attachmentList')}">
                <c:set var="attachmentCount" value="${attachmentCount + 1}" scope="page"/>
                <div id="attachment-${attachmentCount}" class="jlab-row margin">
                    <input type="text" name="name_attachment-${attachmentCount}" id="name_attachment-${attachmentCount}"
                           value="${i.fileName}" hidden/>
                    <input type="text" name="id_attachment-${attachmentCount}" id="id_attachment-${attachmentCount}"
                            value="${i.id}" hidden>
                    <input type="text" name="path_attachment-${attachmentCount}" id="path_attachment-${attachmentCount}"
                            value="${i.filePath}" hidden>
                    <input type="datetime" name="date_attachment-${attachmentCount}" id="date_attachment-${attachmentCount}"
                           value="${i.uploadDate}" hidden/>
                    <input type="text" name="comment_attachment-${attachmentCount}" id="comment_attachment-${attachmentCount}"
                           value="${i.comment}" hidden>
                    <div id="display_date_attachment-${attachmentCount}" class="jlab-cell-3 align-right text-small">
                        <fmt:formatDate value="${i.uploadDate}" type="both" dateStyle="long" timeStyle="medium"/>
                    </div>

                    <div class="jlab-cell-3">
                        <div id="display_name_attachment-${attachmentCount}" class="jlab-row text-medium">
                            ${i.fileName}
                        </div>
                        <div id="display_comment_attachment-${attachmentCount}" class="jlab-row text text-small">
                                ${i.comment}
                        </div>
                    </div>

                    <div class="jlab-cell-1">
                        <a href="<c:url value="${i.filePath}"/>" target="_blank">
                            <div class="imageButton download"></div></a>
                    </div>

                    <div class="jlab-cell-1">
                        <div class="imageButton edit" onclick="popupAttachment.showEditAttachmentPopup(this)"></div>
                    </div>
                    <div class="jlab-cell-1">
                        <div class="imageButton delete" onclick="popupAttachment.deleteAttachmentElement(this)"></div>
                    </div>
                </div>
            </c:forEach>

            <%--attachment section --%>
        </section>


    </form>
    <%--div container end--%>
</div>
</body>
</html>
