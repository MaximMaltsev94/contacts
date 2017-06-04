<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script>
        var countryData = ${requestScope.get('countryStatistics')};
        var groupData = ${requestScope.get('groupsStatistics')};
        var ageStatisticsLabels = ${requestScope.get('ageStatisticsLabels')};
        var menAgeStatistics = ${requestScope.get('menAgeStatistics')};
        var womenAgeStatistics = ${requestScope.get('womenAgeStatistics')};
    </script>
    <title>Статистика</title>
    <link rel="icon" href="<c:url value="/sysImages/logo.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
    <link rel="stylesheet" href="<c:url value="/css/datagrid.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/actionTooltip.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/popup.css"/>">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.bundle.min.js"></script>
    <script src="<c:url value="/js/main.js"/>"></script>
    <script src="<c:url value="/js/statistics.js"/>"></script>
</head>
<body onload="statisticsView.drawCharts()">
<c:remove var="tooltip-text" scope="session"/>
<c:remove var="tooltip-type" scope="session"/>
<jsp:include page="header.jsp"/>
<span id="tooltip"></span>
<div class="container">
    <div class="jlab-row">
        <div class="jlab-cell-12">
            <section>
                <canvas id="age"></canvas>
            </section>
        </div>
    </div>
    <div class="jlab-row">
        <div class="jlab-cell-6">
            <section>
                <canvas id="country"></canvas>
            </section>
        </div>
        <div class="jlab-cell-6">
            <section>
                <canvas id="contactGroups"></canvas>
            </section>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>