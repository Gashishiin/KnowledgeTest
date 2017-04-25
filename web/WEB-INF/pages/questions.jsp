<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Questions</title>
</head>
<body>
<form action="choose_discipline" method="POST">
    <c:forEach items="${disciplines}" var="disciplines">
        <input type="radio" name="discipline_name" value="${disciplines.disciplineID}">
        <c:out value="${disciplines.disciplineName}"/><br/>
    </c:forEach>
</form>

<a href="${pageContext.request.contextPath}/">Go home</a>
</body>
</html>
