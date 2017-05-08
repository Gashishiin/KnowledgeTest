<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <meta name="csrf-token" content="${_csrf.token}"/>
    <script src="/resources/jquery/jquery-3.2.1.js"></script>
    <script src="/resources/jquery/jquery.validate.min.js"></script>
    <script>
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            var token = $('meta[name="csrf-token"]').attr('content');
            return jqXHR.setRequestHeader('X-CSRF-Token', token);
        });

        function submitresults() {
            checkform();
            var data = $("#questionform").serialize();
            console.log("Results " + data);
            $.ajax({
                type: "POST",
                url: "/submitresults",
                data: data
            })
        }

        function checkform() {

        }
    </script>
</head>
<body>
<jsp:include page="/include"/>
<div id="testassignment">
    <c:forEach items="${assignmentlist}" var="assignment">
        <a href=<c:out value="/test?id=${assignment.assignmentID}"/>>
            <c:out value="${assignment.discipline.disciplineName}"/>
        </a>
    </c:forEach>
</div>
<c:if test="${questionlist!=null && !empty questionlist}">
    <div id="testlist" style="margin-left: 400px">
        <form id="questionform" method="post" action="#">
        <c:forEach items="${questionlist}" var="questions">
            ${questions}
        </c:forEach>
            <button type="button" onclick="submitresults()">Отправить результаты</button>
        </form>
    </div>
</c:if>

</body>
</html>