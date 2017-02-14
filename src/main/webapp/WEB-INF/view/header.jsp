<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="nav">
    <div class="container1">
        <div class="jlab-row">
            <div class="element">
                <a class="text-large" href="<c:url value="?action=show&page=1" />">
                    Контакты
                </a>
            </div>
            <c:if test="${not (pageContext.request.userPrincipal.name eq null)}">
                <div class="element text-medium">|</div>

                <div class="element">
                    <a class="text-small-bold" href="<c:url value="?action=email" />">Написать письмо</a>
                </div>
                <div class="element">
                    <a class="text-small-bold" href="<c:url value="?action=search" />">Поиск</a>
                </div>
                <div class="element">
                    <a class="text-small-bold" href="#">Импорт контактов</a>
                </div>

                <div class="element right">
                <span class="text-small-bold">
                        ${pageContext.request.userPrincipal.name}
                </span>
                </div>
                <div class="element text-medium">|</div>

                <div class="element">
                    <a class="text-small-bold" href="<c:url value="/logout/"/>">Выйти</a>
                </div>
            </c:if>
        </div>
    </div>
</div>