<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Вопросы и разделы</title>
    <script src="${pageContext.request.contextPath}/resources/jquery/jquery-3.2.1.js"></script>
    <script src="${pageContext.request.contextPath}/resources/jsTree/jstree.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/jsTree/themes/default/style.css"/>
    <script src="${pageContext.request.contextPath}/resources/questions.js"></script>
    <meta name="csrf-token" content="${_csrf.token}"/>

</head>
<body>
<div id="header">
    <jsp:include page="/include"/>
</div>
<div id="buttons">
    <button onclick="createDiscipline()">Новый раздел</button>
    <button onclick="deleteDiscipline()">Удалить раздел</button>


</div>

<div id="wrap">
    <div id="discipline_tree" style="float: left; width: 200px"></div>
    <div id="right-panel" style="margin-left: 220px;">
        <form id="answerbox" method="post" action="#">
            <div id="input_fields_wrap"></div>
        </form>
        <p><button onclick="deleteQuestions()">Удалить вопросы</button></p>
        <form id="questionlistform">

        <div id="question_list"></div>
        </form>
    </div>

</div>
</body>
</html>
