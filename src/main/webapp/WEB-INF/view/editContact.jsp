<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
</head>
<body onload="addView.selectCountry(${isAdd eq false ? requestScope.get('contact').countryID : 0});
        addView.selectCity(${isAdd eq false ? requestScope.get('contact').cityID : 0});
        addView.selectRelationship(${isAdd eq false ? requestScope.get('contact').relationshipID : 0});
        addView.selectGender(${isAdd eq false ? requestScope.get('contact').gender : 1});

        popupPhone.setPhoneCount(${isAdd eq false ? requestScope.get('phoneList').size() : 0});
        popupAttachment.setAttachmentCount(${isAdd eq false ? requestScope.get('attachmentList').size() : 0});
        addView.initCropper()">
<jsp:include page="header.jsp"/>
<fmt:setLocale value="ru_RU" scope="session"/>

<span id="tooltip"></span>

<div class="container-80">
    <form id="contactForm" onsubmit="return addView.validateDate()" action="<c:url value="/contact/${requestScope.get('action')}"/>" method="post" enctype="multipart/form-data">
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

                            <a href="<c:url value="/contact/show?page=${sessionScope.getOrDefault('lastVisitedPage', 1)}" />">
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
                                    <button class="jlab-cell-12"  type="button" id="btnZoomIn" onclick="addView.zoomIn()">Приблизить</button>
                                </div>
                                <div class="jlab-cell-3 align-center">
                                    <button class="jlab-cell-12" type="button" id="btnZoomOut" onclick="addView.zoomOut()">Отдалить</button>
                                </div>
                                <div class="jlab-cell-6">
                                    <button class="jlab-cell-12" type="button" id="btnCrop" onclick="addView.cropImage()">Обрезать</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

                <section>
                    <div class="jlab-row margin">
                        <button type="button" class="jlab-cell-12 align-center" onclick="addView.onLoadImageClick()">Загрузить фотографию</button>
                    </div>
                    <div class="jlab-row margin">
                        <button type="button" class="jlab-cell-12 align-center" onclick="addView.onDeleteImageClick()">Удалить</button>
                    </div>
                </section>

                <section id="phoneSection">
                    <div class="jlab-row">
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

                                <input id="popupPhone_operatorCode" type="text" pattern="[0-9]{2,5}" placeholder="Код. оп." maxlength="5">

                                <input id="popupPhone_phoneNumber" type="text" pattern="[0-9]{4,9}" placeholder="Номер" maxlength="9">
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
                            <input type="text" name="country_code_phone-${phoneCount}" id="country_code_phone-${phoneCount}"
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
            </div> <%-- div.jlab-cell-4 end --%>

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
                                        <input type="text" name="firstName" value="${requestScope.get('contact').firstName}" required
                                               pattern="^[A-Za-zА-Яа-яЁё]{2,30}$" maxlength="30" title="Русские или английские буквы, от 2х до 30 символов"/>
                                    </div>
                                </div>
                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Фамилия*</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="lastName" value="${requestScope.get('contact').lastName}" required
                                               pattern="^[A-Za-zА-Яа-яЁё]{2,30}$" maxlength="30" title="Русские или английские буквы, от 2х до 30 символов"/>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Отчество</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="patronymic" value="${requestScope.get('contact').patronymic}"
                                               pattern="^[A-Za-zА-Яа-яЁё]{2,30}$" maxlength="30" title="Русские или английские буквы, от 2х до 30 символов"/>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">День рождения</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="birthDate" id="birthDate" value="<fmt:formatDate value="${requestScope.get('contact').birthDate}" pattern="dd.MM.yyyy"/>"
                                               placeholder="дд.мм.гггг" pattern="^[0-9]{2}\.[0-9]{2}\.[0-9]{4}$" maxlength="10" title="дд.мм.гггг"/>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Пол</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input class="regular-radio" type="radio" name="gender" value="1" id="r1" onclick="addView.setAvatar('M')"/> <label for="r1"></label> Мужской
                                        <input class="regular-radio" type="radio" name="gender" value="0" id="r0" onclick="addView.setAvatar('W')"/> <label for="r0"></label> Женский
                                    </div>
                                </div>


                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Гражданство</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="citizenship" value="${requestScope.get('contact').citizenship}"
                                               pattern="^[A-Za-zА-Яа-яЁё\s]{2,50}$" maxlength="50" title="Русские или английские буквы, пробелы, от 2х до 50 символов"/>
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
                                                <option value="${i.id}" >${i.name}</option>
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
                                               pattern="^https?:\/\/(?:[-\w]+\.)?([-\w]+)\.\w+(?:\.\w+)?\/?.*$" maxlength="255" title="http://... или https://..."/>
                                    </div>
                                </div>


                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Эл. почта</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="email" value="${requestScope.get('contact').email}"
                                               pattern="^([a-z0-9_\.-]+)@([a-z0-9_\.-]+)\.([a-z\.]{2,6})$" maxlength="255"/>
                                    </div>
                                </div>


                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Место работы</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="companyName" value="${requestScope.get('contact').companyName}"
                                               pattern="[0-9A-Za-zА-Яа-яЁё\s]{2,50}" maxlength="50" title="Русские или английские буквы, цифры, пробелы, от 2х до 50 символов"/>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
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
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Город</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <select name="city" id="city">
                                            <option value="0" data-country="0">Не выбрано</option>
                                        </select>

                                        <%--hidden select with city data--%>
                                        <select hidden id="cityData">
                                            <option value="0" data-country="0">Не выбрано</option>
                                            <c:forEach var="i" items="${requestScope.get('cityList')}">
                                                <option value="${i.id}" data-country="${i.countryID}">${i.name}</option>
                                            </c:forEach>
                                        </select>
                                        <%--hidden select with city data--%>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Адрес</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="street" value="${requestScope.get('contact').street}"
                                               pattern="^[0-9A-Za-zА-Яа-яЁё\s\\.\\,]{2,50}$" maxlength="50" title="Русские или английские буквы, пробелы, точки, запятые, цифры от 2х до 50 символов"/>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Почтовый индекс</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input type="text" name="postcode" value="${requestScope.get('contact').postcode}"
                                               pattern="^[0-9A-Za-z]{2,20}$" maxlength="20" title="Английские буквы, цифрыб от 2х до 20 символов"/>
                                    </div>
                                </div>
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
                                                <a class="text-large" href="javascript:;" onclick="popupAttachment.closeAttachmentPopup()">&times;</a>
                                            </div>
                                        </div>
                                        <div class="jlab-row margin">
                                            <input id="popupAttachment_fileName" type="text" placeholder="Имя файла" maxlength="50">
                                            <input id="popupAttachment_file" type="file"/>
                                        </div>

                                        <div class="jlab-row margin">
                                            <input type="text" id="popupAttachment_comment" placeholder="Комментарий" maxlength="255">
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

                                        <div class="jlab-cell-6">
                                            <div id="display_name_attachment-${attachmentCount}" class="jlab-row text-small-bold">
                                                    ${i.fileName}
                                            </div>
                                            <div id="display_comment_attachment-${attachmentCount}" class="jlab-row text text-small">
                                                    ${i.comment}
                                            </div>
                                        </div>

                                        <div class="jlab-cell-1">
                                            <div class="imageButton" onclick="location.href='<c:url value="${i.filePath}"/>'">
                                                <img src="<c:url value="/sysImages/download.png"/>">
                                            </div>
                                        </div>

                                        <div class="jlab-cell-1">
                                            <div class="imageButton" onclick="popupAttachment.showEditAttachmentPopup(this)">
                                                <img src="<c:url value="/sysImages/edit.png"/>">
                                            </div>
                                        </div>
                                        <div class="jlab-cell-1">
                                            <div class="imageButton" onclick="popupAttachment.deleteAttachmentElement(this)">
                                                <img src="<c:url value="/sysImages/delete.png"/>">
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </section><%--attachment section --%>
                </div>
            </div>

        </div><%-- div.jlab-row.margin end --%>
    </form>
    <%--div container end--%>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
