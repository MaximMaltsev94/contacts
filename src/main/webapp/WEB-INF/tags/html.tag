<%@ tag trimDirectiveWhitespaces="true"  pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags"%>

<%@ attribute name="title" required="true"%>
<html>
<head>
    <title>${title}</title>
    <link rel="icon" href="<c:url value="/sysImages/logo.png"/>">
    <link rel="stylesheet" href="<c:url value="/css/font-awesome.min.css" />">
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/pagination.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/actionTooltip.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/popup.css" />">
    <link rel="stylesheet" href="<c:url value="/css/hoverableMenu.css" />">

    <script src="<c:url value="/js/main.js"/>"></script>
    <script src="<c:url value="/js/showView.js"/>"></script>
    <script src="<c:url value="/js/listManagePopup.js"/>"></script>
    <script src="<c:url value="/js/login.js"/>"></script>
</head>

<body onload="main.showTooltip('${sessionScope.get('tooltip-text')}', '${sessionScope.get('tooltip-type')}')">
<c:remove var="tooltip-text" scope="session"/>
<c:remove var="tooltip-type" scope="session"/>

<ui:header/>
<fmt:setLocale value="ru_RU" scope="session"/>

<span id="tooltip"></span>

<div class="container">
    <jsp:doBody/>
</div>

<jsp:include page="confirmPopup.jsp"/>
<ui:footer/>
</body>
</html>
