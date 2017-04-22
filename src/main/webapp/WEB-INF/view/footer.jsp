<%@ page import="java.time.LocalDate" %>
<% pageContext.setAttribute("year", LocalDate.now().getYear());%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="footer text-small"> Полоцкий Государственный Университет &copy; 2012-${year} Дёмина Татьяна 12-ИТ-2 </div>

