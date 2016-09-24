<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="nav">
    <div class="container">
        <div class="jlab-row">
            <div class="jlab-cell-2 center">
                <a class="text-large" href="<c:url value="/contact/?action=show&page=1" />">
                    Contacts
                </a>
                <%--<span class="text-large">--%>
                    <%--Contacts--%>
                <%--</span>--%>
            </div>
            <div class="jlab-cell-2 center">

                <a class="text-medium" href="#">
                    Отправить письмо
                </a>
                <%--<span class="text-medium">--%>
                    <%--Отправить письмо--%>
                <%--</span>--%>
            </div>
            <div class="jlab-cell-8 center align-right">
                <input type="text" placeholder="Поиск">
                <button>Поиск</button>
            </div>
        </div>
    </div>
</div>