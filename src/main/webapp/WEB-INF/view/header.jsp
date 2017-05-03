<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script>var contextPath = "${pageContext.request.contextPath}"</script>
<div class="nav">
    <div class="container1">
        <div class="jlab-row">
            <img src="<c:url value="/sysImages/logo.png"/>" width="25px" height="25px">
            <div class="element">
                <a class="text-large" href="<c:url value="/contact/show?page=1" />">
                    Контакты
                </a>
            </div>
            <c:if test="${not (pageContext.request.userPrincipal.name eq null)}">
                <div class="element text-medium">|</div>

                <div class="element">
                    <a class="text-small-bold" href="<c:url value="/contact/email" />">Написать письмо</a>
                </div>
                <div class="element">
                    <a class="text-small-bold" href="<c:url value="/contact/search" />">Поиск</a>
                </div>
                <div class="element">
                    <a class="text-small-bold" href="<c:url value="/contact/createList" />">Создать список</a>
                </div>
                <div class="element">
                    <a class="text-small-bold" href="<c:url value="/contact/importVK"/>">Импорт контактов</a>
                </div>

                <div class="element right">
                    <img src="<c:url value="${requestScope.get('user').profilePicture}" /> " width="25px" height="25px">
                </div>
                <div class="element">
                    <a class="text-small-bold" href="<c:url value="/contact/user"/>">
                        ${pageContext.request.userPrincipal.name}
                    </a>
                </div>
                <div class="element text-medium">|</div>

                <div class="element">
                    <a class="text-small-bold" href="<c:url value="/contact/logout"/>">Выйти</a>
                </div>
            </c:if>
        </div>
    </div>
</div>