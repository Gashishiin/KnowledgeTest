<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Test assignment and results</title>
    <script src="/resources/jquery/jquery-3.2.1.js"></script>
    <script src="/resources/jsTree/jstree.js"></script>
    <link rel="stylesheet" href="/resources/jsTree/themes/default/style.css"/>
    <script>

        var DTree = {};
        DTree.treename = "discipline_tree";
        $(document).ready(function () {
            $("#"+DTree.treename).jstree({
                'core': {
                    'data': {
                        type: "POST",
                        url: "/disciplines",
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
            var data = $('#testassignment').serialize();
            if (data !="") data+="&disciplineid="+DTree.id;
            $.ajax({
                type: "POST",
                url: "/assigntest",
                data: data,
                success: function (data) {
                }
            })
        }
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            var token = $('meta[name="csrf-token"]').attr('content');
            return jqXHR.setRequestHeader('X-CSRF-Token', token);
        });
    </script>

    <meta name="csrf-token" content="${_csrf.token}"/>
</head>
<body>
<jsp:include page="/include"/>
<div id="header">
    <div id="discipline_tree" style="float: left; width: 200px"></div>
    <form id="testassignment" method="post" action="#">
    <div style="margin-left: 220px">
        <button type="button" onclick="assigntest()">Assign test</button><br/>
        <c:forEach items="${userlist}" var="users">
            <input type="checkbox" name="login" value=${users.login}>
            <c:out value="${users}"/><br/>
        </c:forEach>
    </div>
    </form>

</div>

</body>
</html>
