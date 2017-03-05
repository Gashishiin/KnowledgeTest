<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>All users</title>
</head>
<body>
<form action="createuser" method="POST">
    Login: <input type="text" name="login"><br/>
    Password: <input type="password" name="password"><br/>
    Full name: <input type="text" name="fullname"><br/>
    <input type="submit" value="Submit">
</form>
<table>
    <c:forEach items="${userlist}" var="list">
        <tr>
            <td>
                <c:out value="${list}"/>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="/">Go home</a>
</body>
</html>
