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
<script src="/webjars/jquery/3.1.1/jquery.js"/>
<script src="/webjars/jstree/3.3.3/jstree.js"/>
<script>
    $(function () {
        $('#using_json_2').jstree({
            'core': {
                'data': [
                    {"id": "ajson1", "parent": "#", "text": "Simple root node"},
                    {"id": "ajson2", "parent": "#", "text": "Root node 2"},
                    {"id": "ajson3", "parent": "ajson2", "text": "Child 1"},
                    {"id": "ajson4", "parent": "ajson2", "text": "Child 2"},
                ]
            }
        });
    });
</script>

<a href="${pageContext.request.contextPath}/">Go home</a>
</body>
</html>
