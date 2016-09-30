<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="nav">
    <div class="container">
        <div class="jlab-row">
            <div class="element">
                <a class="text-large" href="<c:url value="/contact/?action=show&page=1" />">
                    Contacts
                </a>
            </div>
            <div class="element right">
                <div class="element">
                    <a href="#">
                        <div class="imageButton email">
                        </div>
                    </a>
                </div>
                <div class="element">
                    <a href="<c:url value="/contact/?action=search" />">
                        <div class="imageButton search">
                        </div>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>