<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Назначение тестов и результаты</title>
    <script src="${pageContext.request.contextPath}/resources/jquery/jquery-3.2.1.js"></script>
    <script src="${pageContext.request.contextPath}/resources/jsTree/jstree.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/jsTree/themes/default/style.css"/>
    <script>
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            var token = $('meta[name="csrf-token"]').attr('content');
            return jqXHR.setRequestHeader('X-CSRF-Token', token);
        });
        var DTree = {};
        DTree.treename = "discipline_tree";
        $(document).ready(function () {
            $("#"+DTree.treename).jstree({
                'core': {
                    'data': {
                        type: "POST",
                        url: "disciplines",
                        dataType: "json"
                    }
                }
            }).on("ready.jstree", function () {
                $("#"+DTree.treename).jstree("open_all");
            }).on('changed.jstree', function (e, data) {
                var i, j, r = [], n = [];
                for (i = 0, j = data.selected.length; i < j; i++) {
                    r.push(data.instance.get_node(data.selected[i]).id);
                    n.push(data.instance.get_node(data.selected[i]).text);
                    DTree.id = r[0];
                    DTree.name = n[0];
                }
            });
        });
        function assigntest() {
            if (DTree.id == 0){
                alert("Не выбран раздел для назначения теста");
                return;
            }
            var data = $('#testassignment').serialize();
            if (data !="") data+="&disciplineid="+DTree.id;
            $.ajax({
                type: "POST",
                url: "assigntest",
                data: data,
                success: function (data) {
                    window.location.href="testmanagement";
                }
            })
        }

        function showassignments(id) {
            $.ajax({
                type: "POST",
                url: "getassignments",
                data: "userid="+ id,
                success: function (data) {
                    $("#assignedtests").html(data);

                }
            })
        }

        function canceltest(testid,userid) {
            $.ajax({
                type: "POST",
                url: "deleteassignment",
                data: "assignmentid="+testid,
                success: function () {
                    showassignments(userid);
                }
            })
        }

        function repeattest(testid,userid) {
            $.ajax({
                type: "POST",
                url: "repeatassignment",
                data: "assignmentid="+testid,
                success: function () {
                    showassignments(userid)
                }
            })
        }

    </script>

    <meta name="csrf-token" content="${_csrf.token}"/>
</head>
<body>
<jsp:include page="/include"/>
<div id="header">
    <div id="discipline_tree" style="float: left; width: 200px"></div>
    <div style="margin-left: 20px; float: left;" >
    <form id="testassignment" method="post" action="#">

        <p><button type="button" onclick="assigntest()">Назначить тест</button>
            Количество вопросов <input type="number" min="1" max="100" value="20" size="2" name="questionamount">
            Порог <input type="number" min="0.01" max="100" value="80" step="0.01" size="2" name="threshold"></p>
        <table border="1" style="border-collapse: collapse;">
            <tr>
                <td></td>
                <td>Полное имя пользователя</td>
                <td>Логин</td>
                <td>Роль</td>
            </tr>
        <c:forEach items="${userlist}" var="users">
            <tr>
                <td><button type="button" onclick="showassignments(${users.userID})">Назначенные тесты</button>
                    <input type="checkbox" name="login" value=${users.login}></td>
                <td>${users.fullname}</td>
                <td> ${users.login}</td>
                <td>
                    <c:if test="${users.userRole eq 'ROLE_ADMIN'}">Администратор</c:if>
                    <c:if test="${users.userRole eq 'ROLE_METHODIST'}">Методист</c:if>
                    <c:if test="${users.userRole eq 'ROLE_STUDENT'}">Студент</c:if>
                </td>
            </tr>
        </c:forEach>
        </table>
    </form>
    </div>

    <div id="assignedtests" style="padding-left: 50px">

    </div>

</div>

</body>
</html>
