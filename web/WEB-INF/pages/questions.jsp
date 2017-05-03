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
        var Nodes = {};
        $(document).ready(function () {
            $("#discipline_tree").on('changed.jstree', function (e, data) {
                var i, j, r = [];
                for (i = 0, j = data.selected.length; i < j; i++) {
                    r.push(data.instance.get_node(data.selected[i]).id);
                    Nodes.id = r[0];
                }
                $.ajax({
                        type: "POST",
                        url: "/question_list",
                        data: "disciplineID=" + Nodes.id,
                        success: function (data) {

                        }
                    }
                );

            })
                .jstree({
                    'core': {
                        'data': [
                            {"id": "0", "parent": "#", "text": "Top"},
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
<div id="discipline_tree">
</div>
<div id="event_result">
    Result
</div>


<a href="${pageContext.request.contextPath}/">Go home</a>
</body>
</html>
