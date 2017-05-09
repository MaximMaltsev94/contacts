<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.LocalDate" %>
<% pageContext.setAttribute("year", LocalDate.now().getYear());%>

<c:set var="isAdd" value="${requestScope.get('action') eq 'add'}" scope="page"/>
<!DOCTYPE HTML>
<html>
<head>
    <title>${isAdd eq true ? 'Добавить контакт' : 'Редактировать контакт'}</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css" />">
    <link rel="stylesheet" href="<c:url value="/css/actionTooltip.css" />">
    <link rel="stylesheet" href="<c:url value="/css/popup.css" />">

    <script src="<c:url value="/js/cropbox/cropbox.js"/>"></script>
    <script src="<c:url value="/js/addView.js"/>"></script>
    <script src="<c:url value="/js/main.js"/>"></script>
    <script src="<c:url value="/js/popupPhone.js"/>"></script>
    <script src="<c:url value="/js/popupAttachment.js"/>"></script>
    <script src="<c:url value="/js/popupSocial.js"/>"></script>
</head>
<body onload="addView.selectCountry(${isAdd eq false ? requestScope.get('contact').countryID : 0});
        addView.selectCity(${isAdd eq false ? requestScope.get('contact').cityID : 0});
        addView.selectRelationship(${isAdd eq false ? requestScope.get('contact').relationshipID : 0});
        addView.selectGender(${isAdd eq false ? requestScope.get('contact').gender : 0});
        addView.selectBirthDay(${isAdd eq false ? requestScope.get('contact').birthDay : 0},
${isAdd eq false ? requestScope.get('contact').birthMonth : 0},
${isAdd eq false ? requestScope.get('contact').birthYear : 0});

        popupPhone.setPhoneCount(${isAdd eq false ? requestScope.get('phoneList').size() : 0});
        popupAttachment.setAttachmentCount(${isAdd eq false ? requestScope.get('attachmentList').size() : 0});
        addView.initCropper()">
<jsp:include page="header.jsp"/>
<fmt:setLocale value="ru_RU" scope="session"/>

<span id="tooltip"></span>

<div class="container">
    <form autocomplete="off" id="contactForm" action="<c:url value="/contact/${requestScope.get('action')}"/>"
          method="post" enctype="multipart/form-data">
        <div class="jlab-row margin">
            <div class="jlab-cell-12">
                <section>
                    <div class="jlab-row">
                        <div class="jlab-cell-6">
                            <span class="text-large">${isAdd eq true ? 'Добавление контакта' : 'Редактирование контакта'}</span>
                        </div>
                        <div class="jlab-cell-3">
                            <button class="jlab-cell-12 align-center">${isAdd eq true ?"Добавить" : "Сохранить"}</button>
                        </div>
                        <div class="jlab-cell-3">

                            <c:set var="lastVisitedFilter"
                                   value="${sessionScope.containsKey('lastVisitedFilter') ? '&filter='.concat(sessionScope.get('lastVisitedFilter')): ''}"/>
                            <a href="<c:url value="/contact/show?page=${sessionScope.getOrDefault('lastVisitedPage', 1)}${lastVisitedFilter}" />">
                                <button type="button" class="jlab-cell-12">Назад</button>
                            </a>
                        </div>
                    </div>
                </section>
            </div>
            <%--info line with save/cancel buttons end--%>
        </div>

        <div class="jlab-row margin">
            <input type="text" name="id" value="${requestScope.get('contact').id}" hidden>
            <div class="jlab-cell-4">
                <section>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-12 hiddenFileInputContainter">

                            <img id="blah" class="fileDownload"
                                 src="<c:url value="${isAdd eq true ? '/sysImages/default.png' : requestScope.get('contact').profilePicture}"/>">
                            <input hidden type="file" id="profileImage" class="hidden" accept="image/*"
                                   onchange="addView.readURL(this)">
                            <input type="text" id="imageAction" name="imageAction" value="nothing" hidden>
                            <input type="text" id="profileImageData" name="profileImage" hidden>
                        </div>
                    </div>

                    <div id="cropPopup" class="popupBack hidden">
                        <div class="popup">
                            <div class="jlab-row">
                                <div class="jlab-cell-12 align-right">
                                    <a class="text-large" href="javascript:;" onclick="main.closePopup('cropPopup')">&times;</a>
                                </div>
                            </div>
                            <div class="jlab-row margin">
                                <div class="jlab-cell-12">
                                    <div class="imageBox">
                                        <div class="thumbBox"></div>
                                        <div class="spinner" style="display: none">Loading...</div>
                                    </div>
                                </div>
                            </div>
                            <div class="jlab-row margin">
                                <div class="jlab-cell-3 align-center">
                                    <button class="jlab-cell-12" type="button" id="btnZoomIn"
                                            onclick="addView.zoomIn()">Приблизить
                                    </button>
                                </div>
                                <div class="jlab-cell-3 align-center">
                                    <button class="jlab-cell-12" type="button" id="btnZoomOut"
                                            onclick="addView.zoomOut()">Отдалить
                                    </button>
                                </div>
                                <div class="jlab-cell-6">
                                    <button class="jlab-cell-12" type="button" id="btnCrop"
                                            onclick="addView.cropImage()">Обрезать
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

                <section>
                    <div class="jlab-row margin">
                        <button type="button" class="jlab-cell-12 align-center" onclick="addView.onLoadImageClick()">
                            Загрузить фотографию
                        </button>
                    </div>
                    <div class="jlab-row margin">
                        <button type="button" class="jlab-cell-12 align-center" onclick="addView.onDeleteImageClick()">
                            Удалить
                        </button>
                    </div>
                </section>

                <section id="phoneSection">
                    <div class="jlab-row margin">
                        <div class="jlab-cell-11">
                            <span class="text-medium">Контактные телефоны</span>
                        </div>

                        <div class="jlab-cell-1">
                            <div class="imageButton" onclick="popupPhone.showAddPhonePopup()">
                                <img src="<c:url value="/sysImages/add.png"/>">
                            </div>
                        </div>
                    </div>
                    <div id="phonePopup" class="popupBack hidden">
                        <div class="popup">
                            <div class="jlab-row">
                                <div class="jlab-cell-12 align-right">
                                    <a class="text-large" href="javascript:;" onclick="popupPhone.closePhonePopup()">&times;</a>
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

                                <input id="popupPhone_operatorCode" type="text" pattern="[0-9]{2,5}"
                                       placeholder="Код. оп." maxlength="5">

                                <input id="popupPhone_phoneNumber" type="text" pattern="[0-9]{4,9}" placeholder="Номер"
                                       maxlength="9">
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
                        <hr>
                        <c:set var="phoneCount" value="${phoneCount + 1}" scope="page"/>
                        <div id="phone-${phoneCount}" class="jlab-row margin">
                            <input type="text" name="type_phone-${phoneCount}" id="type_phone-${phoneCount}"
                                   value="${i.type == false ? 0 : 1}" hidden>
                            <input type="text" name="country_code_phone-${phoneCount}"
                                   id="country_code_phone-${phoneCount}"
                                   value="${i.countryID - 1}" hidden>
                            <input type="text" name="op_code_phone-${phoneCount}" id="op_code_phone-${phoneCount}"
                                   value="${i.operatorCode}" hidden>
                            <input type="text" name="number_phone-${phoneCount}" id="number_phone-${phoneCount}"
                                   value="${i.phoneNumber}" hidden>
                            <input type="text" name="comment_phone-${phoneCount}" id="comment_phone-${phoneCount}"
                                   value="${i.comment}" hidden>
                            <div id="display_type_phone-${phoneCount}" class="jlab-cell-2 align-right text-small">
                                <c:choose>
                                    <c:when test="${i.type == true}">
                                        Дом.
                                    </c:when>
                                    <c:otherwise>
                                        Моб.
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="jlab-cell-8">
                                <div id="display_number_phone-${phoneCount}" class="jlab-row text-small-bold">
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
                                <div class="imageButton" onclick="popupPhone.showEditPhonePopup(this)">
                                    <img src="<c:url value="/sysImages/edit.png"/>">
                                </div>
                            </div>
                            <div class="jlab-cell-1">
                                <div class="imageButton" onclick="popupPhone.deletePhoneElement(this)">
                                    <img src="<c:url value="/sysImages/delete.png"/>">
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <%--contact phones section end--%>
                </section>

                <section>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-11">
                            <span class="text-medium">Списки контактов</span>
                        </div>
                    </div>
                    <c:forEach var="group" items="${requestScope.get('userGroups')}">

                        <c:set var="contains"
                               value="${requestScope.get('contactGroups').contains(group.id) ? 'checked' : ''}"
                               scope="page"/>

                        <div class="jlab-row margin">
                            <div class="jlab-cell-1">
                                <input type="checkbox" ${contains} id="${group.id}" name="contactGroup-${group.id}"
                                       class="regular-checkbox"/><label for="${group.id}"></label>
                            </div>
                            <div class="jlab-cell-11">
                                <span class="text-small-bold">${group.groupName}</span>
                            </div>

                        </div>
                    </c:forEach>
                </section>
            </div>
            <%-- div.jlab-cell-4 end --%>

            <div class="jlab-cell-8">
                <div class="jlab-row">
                    <section class="jlab-row">
                        <div class="jlab-row">
                            <div class="jlab-cell-12">
                                <div class="jlab-row margin">
                                    <div class="text-medium">Основная информация</div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Имя*</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="firstName"
                                               value="${requestScope.get('contact').firstName}" required
                                               pattern="^[A-Za-zА-Яа-яЁё]{2,30}$" maxlength="30"
                                               title="Русские или английские буквы, от 2х до 30 символов"/>
                                    </div>
                                </div>
                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Фамилия*</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="lastName"
                                               value="${requestScope.get('contact').lastName}" required
                                               pattern="^[A-Za-zА-Яа-яЁё]{2,30}$" maxlength="30"
                                               title="Русские или английские буквы, от 2х до 30 символов"/>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Отчество</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="patronymic"
                                               value="${requestScope.get('contact').patronymic}"
                                               pattern="^[A-Za-zА-Яа-яЁё]{2,30}$" maxlength="30"
                                               title="Русские или английские буквы, от 2х до 30 символов"/>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">День рождения</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <div class="jlab-row">
                                            <select name="birth_day" id="birth_day">
                                                <option value="0">Не выбрано</option>
                                                <c:forEach var="i" begin="1" end="31">
                                                    <option value="${i}">${i}</option>
                                                </c:forEach>
                                            </select>
                                            <select name="birth_month" id="birth_month"
                                                    onchange="addView.onChangeMonth()">
                                                <option value="0">Не выбрано</option>
                                                <option value="1">Январь</option>
                                                <option value="2">Февраль</option>
                                                <option value="3">Март</option>
                                                <option value="4">Апрель</option>
                                                <option value="5">Май</option>
                                                <option value="6">Июнь</option>
                                                <option value="7">Июль</option>
                                                <option value="8">Август</option>
                                                <option value="9">Сентябрь</option>
                                                <option value="10">Октябрь</option>
                                                <option value="11">Ноябрь</option>
                                                <option value="12">Декабрь</option>
                                            </select>
                                            <select name="birth_year" id="birth_year">
                                                <option value="0">Не выбрано</option>
                                                <c:forEach var="i" begin="${year - 100}" end="${year}">
                                                    <option value="${i}">${i}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Пол</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <select name="gender" id="gender" onchange="addView.setAvatar(this.value)">
                                            <option value="0">Не выбран</option>
                                            <option value="1">Мужской</option>
                                            <option value="2">Женский</option>
                                        </select>
                                    </div>
                                </div>


                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Гражданство</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="citizenship"
                                               value="${requestScope.get('contact').citizenship}"
                                               pattern="^[A-Za-zА-Яа-яЁё\s]{2,50}$" maxlength="50"
                                               title="Русские или английские буквы, пробелы, от 2х до 50 символов"/>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Семейное положение</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <select name="relationship" id="relationship">
                                            <option value="0">Не выбрано</option>
                                            <c:forEach var="i" items="${requestScope.get('relationshipList')}">
                                                <option value="${i.id}">${i.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </section>
                </div>

                <div class="jlab-row">
                    <section class="jlab-row">
                        <div class="jlab-row">
                            <div class="jlab-cell-12">
                                <div class="jlab-row margin">
                                    <div class="text-medium">Контактная информация</div>
                                </div>
                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Веб сайт</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="webSite" value="${requestScope.get('contact').webSite}"
                                               pattern="^https?:\/\/(?:[-\w]+\.)?([-\w]+)\.\w+(?:\.\w+)?\/?.*$"
                                               maxlength="255" title="http://... или https://..."/>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Эл. почта</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="email" value="${requestScope.get('contact').email}"
                                               pattern="^([a-z0-9_\.-]+)@([a-z0-9_\.-]+)\.([a-z\.]{2,6})$"
                                               maxlength="255"/>
                                    </div>
                                </div>


                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Место работы</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="companyName"
                                               value="${requestScope.get('contact').companyName}"
                                               pattern="[0-9A-Za-zА-Яа-яЁё\s]{2,50}" maxlength="50"
                                               title="Русские или английские буквы, цифры, пробелы, от 2х до 50 символов"/>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Страна</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <select name="country" id="country"
                                                onchange="addView.onChangeCountry(this.value)">
                                            <option value="0">Не выбрано</option>
                                            <c:forEach var="i" items="${requestScope.get('countryList')}">
                                                <option value="${i.id}">${i.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Город</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <div class="jlab-row">
                                            <input id="cityInput" type="text"
                                                   onkeyup="addView.onCityKeyDown(this.value)"
                                                   placeholder="Начните вводить название города">

                                            <select id="city" name="city">
                                                <option value="0">Не выбрано</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Адрес</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="street" value="${requestScope.get('contact').street}"
                                               pattern="^[0-9A-Za-zА-Яа-яЁё\s\\.\\,]{2,50}$" maxlength="50"
                                               title="Русские или английские буквы, пробелы, точки, запятые, цифры от 2х до 50 символов"/>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Почтовый индекс</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="postcode"
                                               value="${requestScope.get('contact').postcode}"
                                               pattern="^[0-9A-Za-z]{2,20}$" maxlength="20"
                                               title="Английские буквы, цифрыб от 2х до 20 символов"/>
                                    </div>
                                </div>


                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Социальные сети</span>
                                    </div>
                                    <div class="jlab-cell-8 center">
                                        <c:set var="urls" value="${fn:split('https://vk.com/id,https://ok.ru/profile/,https://www.facebook.com/profile.php?id=,https://www.instagram.com/,https://twitter.com/,https://www.youtube.com/channel/,https://www.linkedin.com/in/,skype:', ',')}"/>
                                        <c:set var="soc" value="${fn:split('vk,ok,facebook,instagram,twitter,youtube,linkedin,skype', ',')}"/>
                                        <script> var urls = [];</script>
                                        <c:forEach var="u" items="${urls}">
                                            <script>urls.push('${u}')</script>
                                        </c:forEach>

                                        <input type="text" id="vkId" name="vkId" value="${requestScope.get('contact').vkId}" hidden>
                                        <input type="text" id="okId" name="okId" value="${requestScope.get('contact').okId}" hidden>
                                        <input type="text" id="facebookId" name="facebookId" value="${requestScope.get('contact').facebookId}" hidden>
                                        <input type="text" id="instagramId" name="instagramId" value="${requestScope.get('contact').instagramId}" hidden>
                                        <input type="text" id="twitterId" name="twitterId" value="${requestScope.get('contact').twitterId}" hidden>
                                        <input type="text" id="youtubeId" name="youtubeId" value="${requestScope.get('contact').youtubeId}" hidden>
                                        <input type="text" id="linkedinId" name="linkedinId" value="${requestScope.get('contact').linkedinId}" hidden>
                                        <input type="text" id="skypeId" name="skypeId" value="${requestScope.get('contact').skypeId}" hidden>

                                        <c:set var="hidden" value="${requestScope.get('contact').vkId == 0 ? 'hidden' : ''}"/>
                                        <img ${hidden} id="img_vk" class="social" src="<c:url value="/sysImages/vk.png"/>" onclick="window.open('${urls[0]}${requestScope.get('contact').vkId}', '_blank')">

                                        <c:set var="hidden" value="${requestScope.get('contact').okId == null ? 'hidden' : ''}"/>
                                        <img ${hidden} id="img_ok" class="social" src="<c:url value="/sysImages/ok.png"/>" onclick="window.open('${urls[1]}${requestScope.get('contact').okId}', '_blank')">

                                        <c:set var="hidden" value="${requestScope.get('contact').facebookId == null ? 'hidden' : ''}"/>
                                        <img ${hidden} id="img_facebook" class="social" src="<c:url value="/sysImages/facebook.png"/>" onclick="window.open('${urls[2]}${requestScope.get('contact').facebookId}', '_blank')">

                                        <c:set var="hidden" value="${requestScope.get('contact').instagramId == null ? 'hidden' : ''}"/>
                                        <img ${hidden} id="img_instagram" class="social" src="<c:url value="/sysImages/instagram.png"/>" onclick="window.open('${urls[3]}${requestScope.get('contact').instagramId}', '_blank')">

                                        <c:set var="hidden" value="${requestScope.get('contact').twitterId == null ? 'hidden' : ''}"/>
                                        <img ${hidden} id="img_twitter" class="social" src="<c:url value="/sysImages/twitter.png"/>" onclick="window.open('${urls[4]}${requestScope.get('contact').twitterId}', '_blank')">

                                        <c:set var="hidden" value="${requestScope.get('contact').youtubeId == null ? 'hidden' : ''}"/>
                                        <img ${hidden} id="img_youtube" class="social" src="<c:url value="/sysImages/youtube.png"/>" onclick="window.open('${urls[5]}${requestScope.get('contact').youtubeId}', '_blank')">

                                        <c:set var="hidden" value="${requestScope.get('contact').linkedinId == null ? 'hidden' : ''}"/>
                                        <img ${hidden} id="img_linkedin" class="social" src="<c:url value="/sysImages/linkedin.png"/>" onclick="window.open('${urls[6]}${requestScope.get('contact').linkedinId}', '_blank')">

                                        <c:set var="hidden" value="${requestScope.get('contact').skypeId == null ? 'hidden' : ''}"/>
                                        <img ${hidden} id="img_skype" class="social" src="<c:url value="/sysImages/skype.png"/>" onclick="window.open('skype:${requestScope.get('contact').skypeId}?chat', '_blank')">
                                    </div>
                                    <div class="jlab-cell-1 center">
                                        <div class="imageButton" onclick="popupSocial.showSocialPopup()">
                                            <img src="<c:url value="/sysImages/edit.png"/>">
                                        </div>
                                    </div>
                                </div> <%-- div social end --%>
                                <div id="socialPopup" class="popupBack hidden">
                                    <div class="popup">
                                        <div class="jlab-row">
                                            <div class="jlab-cell-12 align-right">
                                                <a class="text-large" href="javascript:;" onclick="popupSocial.closeSocialPopup()">&times;</a>
                                            </div>
                                        </div>

                                        <c:forEach var="i" begin="0" end="7">
                                            <div class="jlab-row margin">
                                                <div class="jlab-cell-1">
                                                    <img src="<c:url value="/sysImages/${soc[i]}.png"/>" width="25" height="25">
                                                </div>
                                                <div class="jlab-cell-6 text-small-bold center align-right">${urls[i]}</div>
                                                <div class="jlab-cell-5"><input id="popupSocial_${soc[i]}" type="text"></div>
                                            </div>
                                        </c:forEach>


                                        <div class="jlab-row margin">
                                            <div class="jlab-cell-12 align-right">
                                                <button type="button" onclick="popupSocial.onSaveClick()">Сохранить</button>
                                            </div>
                                        </div>

                                    </div>
                                </div> <%--div id socialPopup end--%>


                            </div>
                        </div>
                    </section>
                </div>

                <div class="jlab-row">

                    <section id="attachmentSection" class="jlab-row">
                        <div class="jlab-row">
                            <div class="jlab-cell-12">
                                <div class="jlab-row">
                                    <div class="jlab-cell-11 center">
                                        <span class="text-medium">Прикрепленные файлы</span>
                                    </div>
                                    <div class="jlab-cell-1 center">
                                        <div class="imageButton" onclick="popupAttachment.showAddAttachmentPopup()">
                                            <img src="<c:url value="/sysImages/add%20attachment.png"/>">
                                        </div>
                                    </div>
                                </div>

                                <div id="attachmentPopup" class="popupBack hidden">
                                    <div class="popup">
                                        <div class="jlab-row">
                                            <div class="jlab-cell-12 align-right">
                                                <a class="text-large" href="javascript:;"
                                                   onclick="popupAttachment.closeAttachmentPopup()">&times;</a>
                                            </div>
                                        </div>
                                        <div class="jlab-row margin">
                                            <input id="popupAttachment_fileName" type="text" placeholder="Имя файла"
                                                   maxlength="50">
                                            <input id="popupAttachment_file" type="file"/>
                                        </div>

                                        <div class="jlab-row margin">
                                            <input type="text" id="popupAttachment_comment" placeholder="Комментарий"
                                                   maxlength="255">
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
                                    <hr>
                                    <c:set var="attachmentCount" value="${attachmentCount + 1}" scope="page"/>
                                    <div id="attachment-${attachmentCount}" class="jlab-row margin">
                                        <input type="text" name="name_attachment-${attachmentCount}"
                                               id="name_attachment-${attachmentCount}"
                                               value="${i.fileName}" hidden/>
                                        <input type="text" name="id_attachment-${attachmentCount}"
                                               id="id_attachment-${attachmentCount}"
                                               value="${i.id}" hidden>
                                        <input type="text" name="path_attachment-${attachmentCount}"
                                               id="path_attachment-${attachmentCount}"
                                               value="${i.filePath}" hidden>
                                        <input type="datetime" name="date_attachment-${attachmentCount}"
                                               id="date_attachment-${attachmentCount}"
                                               value="${i.uploadDate}" hidden/>
                                        <input type="text" name="comment_attachment-${attachmentCount}"
                                               id="comment_attachment-${attachmentCount}"
                                               value="${i.comment}" hidden>
                                        <div id="display_date_attachment-${attachmentCount}"
                                             class="jlab-cell-3 align-right text-small">
                                            <fmt:formatDate value="${i.uploadDate}" type="both" dateStyle="long"
                                                            timeStyle="medium"/>
                                        </div>

                                        <div class="jlab-cell-6">
                                            <div id="display_name_attachment-${attachmentCount}"
                                                 class="jlab-row text-small-bold">
                                                    ${i.fileName}
                                            </div>
                                            <div id="display_comment_attachment-${attachmentCount}"
                                                 class="jlab-row text text-small">
                                                    ${i.comment}
                                            </div>
                                        </div>

                                        <div class="jlab-cell-1">
                                            <div class="imageButton"
                                                 onclick="location.href='<c:url value="${i.filePath}"/>'">
                                                <img src="<c:url value="/sysImages/download.png"/>">
                                            </div>
                                        </div>

                                        <div class="jlab-cell-1">
                                            <div class="imageButton"
                                                 onclick="popupAttachment.showEditAttachmentPopup(this)">
                                                <img src="<c:url value="/sysImages/edit.png"/>">
                                            </div>
                                        </div>
                                        <div class="jlab-cell-1">
                                            <div class="imageButton"
                                                 onclick="popupAttachment.deleteAttachmentElement(this)">
                                                <img src="<c:url value="/sysImages/delete.png"/>">
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </section>
                    <%--attachment section --%>
                </div>
            </div>

        </div>
        <%-- div.jlab-row.margin end --%>
    </form>
    <%--div container end--%>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
