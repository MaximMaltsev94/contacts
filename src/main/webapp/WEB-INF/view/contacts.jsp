<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Контакты</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css" />">
</head>
<body>
<nav>
    <span class="text-large">
        Contacts
    </span>
</nav>
<div class="container">
    <c:forEach var="i" items="${requestScope.get('contactList')}">
        <section>
            <form action="<c:url value="/contact/"/>" method="get">
                <input type="text" name="action" value="edit" hidden/>
                <input type="text" name="id" value="${i.id}" hidden/>
                <div class="jlab-row">

                    <div class="jlab-cell-1">
                        <input type="checkbox"/>
                    </div>

                    <div class="jlab-cell-1">
                        <img src="<c:url value="${i.profilePicture}" /> " width="50px" height="50px">
                    </div>

                    <div class="jlab-cell-10">

                        <div class="jlab-row">
                            <div class="jlab-cell-12">
                                <input type="submit" class="btn-link" value="<c:out value="${i.firstName} ${i.lastName}"/>">
                            </div>
                        </div>

                        <div class="jlab-row">
                            <div class="jlab-cell-12">
                                <span class="text-small">
                                    <c:out value="${i.birthDate}, ${i.companyName}, ${i.street}"/>
                                </span>
                            </div>
                        </div>

                    </div>
                </div>
            </form>
        </section>
    </c:forEach>
</div>
</body>
</html>
