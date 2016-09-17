<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Контакты</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
</head>
<body>
<nav>
    <h1>Contacts</h1>
</nav>
<div class="container">
    <c:forEach var="i" items="${requestScope.get('contactList')}">
        <section>
            <input type="checkbox" />
            <h4>
                <c:out value="${i.firstName} ${i.lastName}"/>
            </h4>
            <p>
                <c:out value="${i.birthDate} ${i.companyName}"/>
            </p>
        </section>
    </c:forEach>
</div>
</body>
</html>
