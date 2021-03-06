<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Пользователи</title>
    <script src="${pageContext.request.contextPath}/resources/jquery/jquery-3.2.1.js"></script>
    <script>
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            var token = $('meta[name="csrf-token"]').attr('content');
            return jqXHR.setRequestHeader('X-CSRF-Token', token);
        });
        function edituser(id) {
            $.ajax({
                type: "POST",
                url: "getuser",
                data: "userid=" + id,
                success: function (data) {
                    $("#userform").html(data);
                }

            })
        }

    </script>
    <meta name="csrf-token" content="${_csrf.token}"/>
</head>
<body>


<jsp:include page="/include"/>
<div id="errormessages" style="padding-left: 200px">
    <c:forEach items="${errormessagelist}" var="error_message">
        <p style="color: red; margin: 0 "><c:out value="${error_message}"/></p>
    </c:forEach>
</div>
<div id="userform" style="padding-left: 200px">
    <form action="createuser" method="POST">
        Логин<br/>
        <input type="text" name="login"><br/>
        Полное имя<br/>
        <input type="text" name="fullname"><br/>
        Пароль <br/>
        <input type="password" name="password"><br/>
        Подтверждение пароля<br/>
        <input type="password" name="password2"><br/>
        Роль<br/>
        <select name="role">
            <option value="role_admin">Администратор</option>
            <option value="role_methodist">Методист</option>
            <option value="role_student" selected>Студент</option>
        </select><br/>
        <p style="align-content: center"><input type="submit" value="Создать пользователя"></p>
        <input type="hidden" name="<c:out value="${_csrf.parameterName}"/>"
               value="<c:out value="${_csrf.token}"/>"/>
    </form>
</div>
<div id="existingusers" style="margin-left: 200px">
    <form action="deleteusers" method="POST">
        <table border="1" style="border-collapse: collapse;">
            <tr>
                <td></td>
                <td>Полное имя</td>
                <td>Логин</td>
                <td>Роль</td>
            </tr>
            <c:forEach items="${userlist}" var="users">
                <tr>
                    <td><button type="button" onclick="edituser(${users.userID})">Редактировать</button>
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
            <p><input type="submit" value="Удалить пользователей"></p>
        </table>
        <input type="hidden" name="<c:out value="${_csrf.parameterName}"/>"
               value="<c:out value="${_csrf.token}"/>"/>
    </form>
</div>


</body>
</html>
