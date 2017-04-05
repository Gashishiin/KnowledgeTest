<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Disciplines</title>
</head>
<body>
<form action="creatediscipline" method="POST">
    Discipline: <input type="text" name="disciplineName"><br/>
    <input type="submit" value="Create discipline">
</form>

<div>
    <c:forEach items="${disciplinelist}" var="disciplines">
        <input type="checkbox" name="disciplineName" value=${disciplines.disciplineName}>
        <c:out value="${disciplines.disciplineName}"/><br/>
    </c:forEach>
    <input type="submit" value="Delete disciplines">
</div>

</body>
</html>
