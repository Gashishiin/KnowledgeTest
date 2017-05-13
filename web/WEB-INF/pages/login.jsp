<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Страница входа</title>
</head>
<body>
<div style="padding-left: 30%;">

    <form name="frm" action="<c:url value='login'/>" method="post">
        <table>
            <tr>
                <h1>Страница входа</h1>
                <c:if test="${param.login_error ne null}">
                <p style="color: red;">Неправильный логин или пароль</p>
                </c:if>
                <td>Логин:</td>
                <td><input type="text" name="username"></td>
            </tr>

            <tr>
                <td>Пароль:</td>
                <td><input type="password" name="password"></td>
            </tr>

            <tr>
                <td colspan = "2" align="center"><input name="submit" type="submit" value="Войти">
                    <input name="reset" type="reset" value="Отмена"></td>
            </tr>
        </table>

        <input type="hidden" name="<c:out value="${_csrf.parameterName}"/>"
               value="<c:out value="${_csrf.token}"/>"/>
    </form>
</div>

</body>

</html>