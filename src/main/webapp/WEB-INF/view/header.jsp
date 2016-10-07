<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="nav">
    <div class="container1">
        <div class="jlab-row">
            <div class="element">
                <a class="text-large" href="<c:url value="?action=show&page=1" />">
                    Contacts
                </a>
            </div>
            <div class="element right">
                <div class="element">
                    <a href="<c:url value="?action=email" />">
                        <div class="imageButton email">
                        </div>
                    </a>
                </div>
                <div class="element">
                    <a href="<c:url value="?action=search" />">
                        <div class="imageButton search">
                        </div>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>