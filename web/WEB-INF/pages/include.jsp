<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div style="float: right">
    <a href="${pageContext.request.contextPath}/logout">Выйти</a>
    <br/>Вы вошли как ${fullname}<br/>
    <c:forEach items="${allowedlinks}" var="link">
        <a href="${pageContext.request.contextPath}/${link.key}">${link.value}</a><br/>
    </c:forEach>
</div>
<br/>
