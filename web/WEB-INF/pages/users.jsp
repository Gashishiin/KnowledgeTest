<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Users</title>
</head>
<body>


<form action="createuser" method="POST">
    Login: <input type="text" name="login"><br/>
    Password: <input type="password" name="password"><br/>
    Full name: <input type="text" name="fullname"><br/>
    <input type="submit" value="Create user">
</form>
<c:if test="${not empty nothingDelete}">
    <c:out value="${nothingDelete}"/>
</c:if>
<c:if test="${not empty userAlreadyExists}">
    <c:out value="${userAlreadyExists}"/>
</c:if>
<form action="deleteusers" method="POST">
    <div>
        <c:forEach items="${userlist}" var="users">
            <input type="checkbox" name="login" value=${users.login}>
            <c:out value="${users}"/><br/>
        </c:forEach>
        <input type="submit" value="Delete users">
    </div>
</form>
<a href="${pageContext.request.contextPath}">Go home</a>
</body>
</html>
