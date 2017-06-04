<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <script>
        var countryData = ${requestScope.get('countryStatistics')};
        var groupData = ${requestScope.get('groupsStatistics')};
        var ageStatisticsLabels = ${requestScope.get('ageStatisticsLabels')};
        var menAgeStatistics = ${requestScope.get('menAgeStatistics')};
        var womenAgeStatistics = ${requestScope.get('womenAgeStatistics')};
    </script>
    <title>Личный кабинет</title>
    <link rel="icon" href="<c:url value="/sysImages/logo.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css" />">
    <link rel="stylesheet" href="<c:url value="/css/popup.css" />">
    <link rel="stylesheet" href="<c:url value="/css/actionTooltip.css"/>">

    <script src="<c:url value="/js/cropbox/cropbox.js"/>"></script>
    <script src="<c:url value="/js/addView.js"/>"></script>
    <script src="<c:url value="/js/login.js"/>"></script>
    <script src="<c:url value="/js/user.js"/>"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.bundle.min.js"></script>
    <script src="<c:url value="/js/main.js"/>"></script>
    <script src="<c:url value="/js/statistics.js"/>"></script>
</head>
<body onload="main.showTooltip('${sessionScope.get('tooltip-text')}', '${sessionScope.get('tooltip-type')}');
        statisticsView.drawCharts()">
<c:remove var="tooltip-text" scope="session"/>
<c:remove var="tooltip-type" scope="session"/>
<jsp:include page="header.jsp"/>
<span id="tooltip"></span>

<div class="container">
    <form id="contactForm" onsubmit="return loginView.validatePassword()" action="<c:url value="/contact/user"/>" method="post" enctype="multipart/form-data">
        <div class="jlab-row margin">
            <div class="jlab-cell-12">
                <section>
                    <div class="jlab-row">
                        <div class="jlab-cell-6">
                            <span class="text-large">Редактирование пользователя</span>
                        </div>
                        <div class="jlab-cell-3">
                            <button class="jlab-cell-12 align-center">Сохранить</button>
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

                <section>
                    <div class="jlab-row margin">
                        <button type="button" class="jlab-cell-12 align-center" onclick="location.href='<c:url value="/contact/export"/>'">Экспортировать контакты в Excel</button>
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
                                    <div class="jlab-cell-9 text-small">
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

                                <div class="jlab-row margin">
                                    <div class="jlab-cell-3 center">
                                        <span class="text-small">Повторите пароль</span>
                                    </div>
                                    <div class="jlab-cell-9">
                                        <input id="pass2" type="password" pattern="[\w]{3,20}" title="Буквы, цифры, знак подчеркивания. От 3х до 20 символов">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
                <div class="jlab-row">
                    <section class="jlab-row">
                        <div class="jlab-cell-12 align-center">

                            <div class="text-small">Вы также можете <b><a href="javascript:;" onclick="main.showConfirmDialog('Это действие невозможно отменить. Вы действительно хотите удалить учетную запись?', userView.deleteUser)">Удалить свою страницу</a></b></div>
                        </div>
                    </section>
                </div>
                <div class="jlab-row">
                    <section class="jlab-row">
                        <canvas id="age"></canvas>
                    </section>
                </div>
                <div class="jlab-row">
                    <section class="jlab-row">
                        <canvas id="country"></canvas>
                    </section>
                </div>
                <div class="jlab-row">
                    <section class="jlab-row">
                        <canvas id="contactGroups"></canvas>
                    </section>
                </div>
            </div>

        </div><%-- div.jlab-row.margin end --%>
    </form>
    <form hidden id="deleteUserForm" action="<c:url value="/contact/deleteuser"/>" method="post"></form>
    <%--div container end--%>
</div>
<jsp:include page="confirmPopup.jsp"/>
<jsp:include page="footer.jsp"/>
</body>
</html>
