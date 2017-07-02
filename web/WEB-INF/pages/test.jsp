<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Тест</title>
    <meta name="csrf-token" content="${_csrf.token}"/>
    <script src="${pageContext.request.contextPath}/resources/jquery/jquery-3.2.1.js"></script>
    <script src="${pageContext.request.contextPath}/resources/jquery/jquery.validate.min.js"></script>
    <script>
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            var token = $('meta[name="csrf-token"]').attr('content');
            return jqXHR.setRequestHeader('X-CSRF-Token', token);
        });

        function submitresults() {
            if (!checkform()) {
                alert("Бланк ответов заполнен не полностью");
                return;
            }
            var data = $("#questionform").serialize();
            $.ajax({
                type: "POST",
                url: "submitresults",
                data: data,
                success: function () {
                    window.location.href = "${pageContext.request.contextPath}/test";
                }
            })
        }

        function checkform() {
            var flags = document.querySelectorAll('[name="questionm"],[name="questions"]');
            var arr = [];
            var stopexec = false;

            $.each(flags, function () {
                if (stopexec) return;
                arr = [];
                $.each(this.getElementsByTagName("input"), function () {
                    if (this.checked) arr.push(this.checked);
                });
                if (arr.length == 0) {
                    stopexec = true;
                    return;
                }
            });
            if (stopexec) return false;
            $("#questionform input[type=text]").each(function () {
                var fieldvalue = $.trim(this.value);
                if (fieldvalue.length == 0) {
                    stopexec = true;
                    return;
                }
            });
            return !stopexec;
        }

        function issuccess(thresholdstring, score, isDone) {
            var threshold = parseFloat(thresholdstring);
            console.log("Threshold " + threshold + " " + score);
            if (isDone) {
                if (score >= threshold) return "Успешно";
                else return "Неуспешно";
            } else
                return "";
        }


    </script>
</head>
<body>
<jsp:include page="/include"/>
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
        </c:otherwise>
    </c:choose>
</div>
<div id="passedtest">
    <c:choose>
        <c:when test="${donetestlist != null && !empty donetestlist}">
            Пройденные тесты
            <table>
                <tr>
                    <td>Раздел</td>
                    <td>Балл</td>
                    <td>Итог</td>
                    <td>Дата завершения</td>
                </tr>
                <c:forEach items="${donetestlist}" var="donetest">
                    <tr>
                        <td>${donetest.discipline.disciplineName}</td>
                        <td>${donetest.resultScore}</td>
                        <td>
                            <script>
                                document.write(
                                    issuccess(
                                        ${donetest.properties.get("threshold")},
                                        ${donetest.resultScore},
                                    ${donetest.isTestDone()}))
                            </script>
                        </td>
                        <td>${donetest.properties.get("Date")}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            Назначенных тестов нет
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
