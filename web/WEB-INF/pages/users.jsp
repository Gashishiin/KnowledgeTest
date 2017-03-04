<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>All users</title>
</head>
<body>
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
