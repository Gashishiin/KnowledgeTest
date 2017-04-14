<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Questions</title>
</head>
<body>
<form:select path="disciplines" items="${disciplines}"/>

<a href="${pageContext.request.contextPath}/">Go home</a>
</body>
</html>
