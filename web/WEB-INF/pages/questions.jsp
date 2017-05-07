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
                    renderQuestions();
                }
            })
                .jstree({
                    'core': {
                        'data': [
                            {"id": "0", "parent": "#", "text": "Top", "state": {"selected": "true"}},
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
            initQuestionFormCreation();
        });

        function renderQuestions() {
            $.ajax({
                    type: "POST",
                    url: "/question_list",
                    data: "disciplineID=" + DTree.id,
                    success: function (data) {
                        $("#question_list").html(data);
                        console.log("Questions html " + data);
                    }
                }
            );
        }

        function createDiscipline() {
            var disciplineName = prompt("Enter discipline name");
            if (disciplineName != null && disciplineName != "") {
                $.post("/creatediscipline",
                    {
                        disciplineName: disciplineName,
                        parentDisciplineID: DTree.id
                    },
                    function () {
                        $.get('/questions');
                        $('#discipline_tree').jstree("refresh");
                    }
                )
            }
        }
        function deleteDiscipline() {
            if (window.confirm("Are sure to delete discipline " + DTree.name + "?")) {
                $.post("/deletediscipline",
                    {
                        disciplineID: DTree.id
                    }
                )
            }
        }

        function createQuestion() {
            var str = $('#answerbox input:not([type="checkbox"])').serialize();
            var textarea = $('#answerbox textarea').serialize();
            if (str!="" && textarea!="") str +=  "&" + textarea;
            else str += textarea;
            var str1 = $("#answerbox input[type='checkbox']").map(function () {
                return this.name + "=" + this.checked;
            }).get().join("&");
            if (str1 != "" && str != "") str += "&" + str1;
            else str += str1;
            str += "&disciplineid=" + DTree.id;
            $.ajax({
                type: "POST",
                url: "/createquestion",
                data: str,
                success: function () {
                    renderQuestions();
                    initQuestionFormCreation();
                }
            })

        }

        function deleteQuestions() {
            var qids = $('#questionbox').serialize();
            $.ajax({
                type: "POST",
                url: "deletequestions",
                data: qids,
                success: function () {
                    renderQuestions();
                }
            })
        }

        var answercount = 1;
        var answerid;

        function addAnswer() {
            answercount++;
            answerid="answerid"+answercount;
            var newAnswer = document.createElement('div');
            newAnswer.innerHTML = '<div id="' +  answerid +'">' +
                '<input type="text" name="answertext[]">' +
                '<input type="checkbox" name="checkanswer[]">' +
                '<a href="#" onclick="deleteAnswer()">Remove</a></div>';

            document.getElementById("input_fields_wrap").appendChild(newAnswer);
            console.log("Added Answerid " + answerid);
        }

        function deleteAnswer() {
            answerid="answerid"+answercount;
            var element = document.getElementById(answerid);
            element.outerHTML="";
            delete  element;
            console.log("Deleted answerid" + answerid);
            answercount--;
        }
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            var token = $('meta[name="csrf-token"]').attr('content');
            return jqXHR.setRequestHeader('X-CSRF-Token', token);
        });

        function initQuestionFormCreation() {
            answercount = 1;
            $('#input_fields_wrap').html('Input Question:<br/>'+
                '<textarea name="questiontext" rows="3" cols="40"></textarea><br/>'+
                '<button type="button" onclick="addAnswer()">Add Answer</button>'+
                '<div id="answerfield1">' +
                '<input type="text" name="answertext[]"><input type="checkbox" name="checkanswer[]">'+
                '</div>');
        }
    </script>
    <meta name="csrf-token" content="${_csrf.token}"/>

</head>
<body>
<div id="header">
    <jsp:include page="include.jsp"/>
</div>
<div id="buttons">
    <button onclick="createDiscipline()">New Discipline</button>
    <button onclick="deleteDiscipline()">Delete Discipline</button>
    <button onclick="deleteQuestions()">Delete Question</button>

</div>

<div id="wrap">
    <div id="discipline_tree" style="float: left; width: 200px"></div>
    <div id="right-panel" style="margin-left: 220px;">
        <form id="answerbox" method="post" action="#">
            <div id="input_fields_wrap"></div>
        </form>
        <button onclick="createQuestion()">Create question</button>
        <div id="question_list"></div>
    </div>

</div>
</body>
</html>
