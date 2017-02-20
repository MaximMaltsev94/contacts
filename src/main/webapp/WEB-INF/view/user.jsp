<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title></title>

    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css" />">
    <link rel="stylesheet" href="<c:url value="/css/popup.css" />">

    <script src="<c:url value="/js/cropbox/cropbox.js"/>"></script>
    <script src="<c:url value="/js/addView.js"/>"></script>
    <script src="<c:url value="/js/main.js"/>"></script>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container-80">
    <form id="contactForm" onsubmit="return addView.validateDate()" action="<c:url value="/contact/user"/>" method="post" enctype="multipart/form-data">
        <div class="jlab-row margin">
            <div class="jlab-cell-12">
                <section>
                    <div class="jlab-row">
                        <div class="jlab-cell-6">
                            <span class="text-large">Редактирование пользователя</span>
                        </div>
                        <div class="jlab-cell-3">
                            <input type="submit" class="jlab-cell-12 align-center" value="Сохранить">
                        </div>
                        <div class="jlab-cell-3">
                            <a href="<c:url value="/contact/show?page=1" />">
                                <button type="button" class="jlab-cell-12">Назад</button>
                            </a>
                        </div>
                    </div>
                </section>
            </div>
            <%--info line with save/cancel buttons end--%>
        </div>

        <div class="jlab-row margin">
            <div class="jlab-cell-4">
                <section>
                    <div class="jlab-row margin">
                        <div class="jlab-cell-12 hiddenFileInputContainter">

                            <img id="blah" class="fileDownload"
                                 src="<c:url value="${requestScope.get('user').profilePicture}"/>">
                            <input hidden type="file" id="profileImage" class="hidden" accept="image/*"
                                   onchange="addView.readURL(this)">
                            <input type="text" id="imageAction" name="imageAction" value="nothing" hidden>
                            <input type="text" id="profileImageData" name="profileImage" hidden>
                        </div>
                    </div>

                    <div id="cropPopup" class="popupBack">
                        <div class="popup">
                            <div class="jlab-row">
                                <div class="jlab-cell-12 align-right">
                                    <a class="text-large" href="#">&times;</a>
                                </div>
                            </div>
                            <div class="imageBox">
                                <div class="thumbBox"></div>
                                <div class="spinner" style="display: none">Loading...</div>
                            </div>
                            <div class="jlab-row margin">
                                <button class="jlab-cell-12" type="button" id="btnCrop" onclick="addView.cropImage()">Обрезать</button>
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
                                        <span class="text-small">Имя пользователя</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input name="username" readonly type="text" pattern="[\w]{3,15}" value="${requestScope.get('user').login}"
                                               required title="Буквы, цифры, знак подчеркивания. От 3х до 15 символов">
                                    </div>
                                </div>
                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Email</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input name="email" type="text" value="${requestScope.get('user').email}" pattern="^([a-z0-9_\.-]+)@([a-z0-9_\.-]+)\.([a-z\.]{2,6})$" maxlength="255" required>
                                    </div>
                                </div>

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3">
                                        <span class="text-small"></span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <c:choose>
                                            <c:when test="${requestScope.get('user').needBDateNotify}">
                                                <input class="regular-checkbox" type="checkbox" name="bdate_notify" id="chb1" checked/> <label for="chb1"></label> Получать уведомления о днях рождения по почте
                                            </c:when>
                                            <c:otherwise>
                                                <input class="regular-checkbox" type="checkbox" name="bdate_notify" id="chb1"/> <label for="chb1"></label> Получать уведомления о днях рождения по почте
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>


                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Новый пароль</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input id="pass1" name="password" type="password" pattern="[\w]{3,20}" title="Буквы, цифры, знак подчеркивания. От 3х до 20 символов">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            </div>

        </div><%-- div.jlab-row.margin end --%>
    </form>
    <%--div container end--%>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>