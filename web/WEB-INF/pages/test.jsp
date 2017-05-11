<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <meta name="csrf-token" content="${_csrf.token}"/>
    <script src="${pageContext.request.contextPath}/resources/jquery/jquery-3.2.1.js"></script>
    <script src="${pageContext.request.contextPath}/resources/jquery/jquery.validate.min.js"></script>
    <script>
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            var token = $('meta[name="csrf-token"]').attr('content');
            return jqXHR.setRequestHeader('X-CSRF-Token', token);
        });

        function submitresults() {
            checkform();
            var data = $("#questionform").serialize();
            $.ajax({
                type: "POST",
                url: "submitresults",
                data: data,
                success: function () {
                    window.location.href = "/test";
                }
            })
        }

        function checkform() {

        }

    </script>
</head>
<body>
<jsp:include page="include"/>
<div id="testassignment">
    <c:choose>
        <c:when test="${assignedtestlist != null && !empty assignedtestlist}">
            Назначенные тесты<br/>
            <form id="assignmentform" method="post" action="test">
                <c:forEach items="${assignedtestlist}" var="assignment" varStatus="loop">
                    <input type="radio" name="id"
                           value="${assignment.assignmentID}" ${loop.index == 0 ? 'checked' : ''}>
                    ${assignment.discipline.disciplineName}<br/>
                </c:forEach>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <br/><input type="submit" value="Начать тест"/>
            </form>
        </c:when>
        <c:otherwise>
            Назначенных тестов нет
        </c:otherwise>
    </c:choose>
</div>
<div id="passedtest">
    <c:choose>
        <c:when test="${donetestlist != null && !empty donetestlist}">
            Пройденные тесты<br/>
            <c:forEach items="${donetestlist}" var="donetest">
                ${donetest.discipline.disciplineName}: ${donetest.resultScore}<br/>
            </c:forEach>
        </c:when>
        <c:otherwise>
            Пройденных тестов нет
        </c:otherwise>
    </c:choose>
</div>

<c:if test="${questionlist!=null && !empty questionlist}">
    <div id="testlist" style="margin-left: 400px">
        <form id="questionform" method="post" action="#">
            <c:forEach items="${questionlist}" var="questions">
                ${questions}
            </c:forEach>
            <input type="hidden" name="assignmentid" value="${assignmentid}">
            <button type="button" onclick="submitresults()">Отправить результаты</button>
        </form>
    </div>
</c:if>

</body>
</html>
