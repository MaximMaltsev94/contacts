<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Контакты</title>
</head>
<body>
<c:forEach var="i" items="${requestScope.get('contactList')}" >
    <c:out value="${i.first_name}"/> <br>
</c:forEach>
</body>
</html>
