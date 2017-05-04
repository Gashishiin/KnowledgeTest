<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Questions</title>
    <script src="/resources/jquery/jquery-3.2.1.js"></script>
    <script src="/resources/jsTree/jstree.js"></script>
    <link rel="stylesheet" href="/resources/jsTree/themes/default/style.css"/>

    <script>
        var DTree = {};
        $(document).ready(function () {
            $("#discipline_tree").on('changed.jstree', function (e, data) {
                var i, j, r = [], n = [];
                for (i = 0, j = data.selected.length; i < j; i++) {
                    r.push(data.instance.get_node(data.selected[i]).id);
                    n.push(data.instance.get_node(data.selected[i]).text);
                    DTree.id = r[0];
                    DTree.name = n[0];
                    $.ajax({
                            type: "POST",
                            url: "/question_list",
                            data: "disciplineID=" + DTree.id,
                            success: function (data) {
                                $("#question_list").html(data);
                            }
                        }
                    );
                }
            })
                .jstree({
                    'core': {
                        'data': [
                            {"id": "0", "parent": "#", "text": "Top","state":{"selected":"true"}},
                            <c:forEach items="${disciplines}" var="disciplines">
                            {
                                "id": "${disciplines.disciplineID}",
                                "parent": "${disciplines.parentDisciplineID}",
                                "text": "${disciplines.disciplineName}"
                            },
                            </c:forEach>
                        ]
                    }
                });
            $("#discipline_tree").on("ready.jstree", function () {
                $("#discipline_tree").jstree("open_all");
            });
        });

    </script>
</head>
<body>
<div id="header">
    <a href="${pageContext.request.contextPath}/">Go home</a>
</div>
<div id="wrap">
    <div id="buttons">
        <button onclick="createDiscipline()">New Discipline</button>
        <button onclick="deleteDiscipline()">Delete Discipline</button>
        <button onclick="createQuestion()">Create Question</button>
        <button onclick="deleteQuestion()">Delete Question</button>
        <script>
            function createDiscipline() {
                var disciplineName = prompt("Enter discipline name");
                if (disciplineName != null && disciplineName != ""){
                    $.post("/creatediscipline",
                        {
                            disciplineName:disciplineName,
                            parentDisciplineID:DTree.id,
                        },
                        function () {
                        DTree.tree.jstree("refresh");
                        }
                    )
                }
            }
            function deleteDiscipline() {
                if (window.confirm("Are sure to delete discipline " + DTree.name + "?")) {
                    $.post("/deletedisciplinearray",
                        {
                            disciplineArray: DTree.array,
                        },function () {}
                    )
                }
            }

            function createQuestion() {

            }
            
            function deleteQuestion() {

            }
            function addAnswer() {
                var line='<div id="answer"><input type="checkbox" name="answer">' +
                        '<input type="text" name="answertext" id="">' +
                    '<a href="#" onclick="deleteAnswer()">Remove</a></div>';
                document.getElementById("input_fields_wrap").innerHTML+=line;
            }
            function deleteAnswer() {
                    console.log("Remove div " + this.text);
                    $(this).parent('div').remove();
            }
        </script>
    </div>
    <div id="discipline_tree" style="float: left; width: 200px"></div>
    <div id="question_list" style="float: left"></div>
    <div id="input_fields_wrap">
        <div><input type="text" name="questionText"></div>
        <button onclick="addAnswer()">Add Answer</button>
        <div id="answer">
            <input type="checkbox" name="answer">
            <input type="text" name="answertext">
        </div>
    </div>
</div>
</body>
</html>
