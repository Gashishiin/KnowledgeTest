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
        },
        "plugins" : [ "contextmenu" ]
    }).on("ready.jstree", function () {
        $("#"+DTree.treename).jstree("open_all");
    }).on('changed.jstree', function (e, data) {
        var i, j, r = [], n = [];
        for (i = 0, j = data.selected.length; i < j; i++) {
            r.push(data.instance.get_node(data.selected[i]).id);
            n.push(data.instance.get_node(data.selected[i]).text);
            DTree.id = r[0];
            DTree.name = n[0];
            renderQuestions();
        }
    });
    initQuestionFormCreation();
});

function renderQuestions() {
    $.ajax({
            type: "POST",
            url: "question_list",
            data: "disciplineID=" + DTree.id,
            success: function (data) {
                $("#question_list").html(data);
            }
        }
    );
}

function createDiscipline() {
    var disciplineName = prompt("Enter discipline name");
    if (disciplineName != null && disciplineName != "") {
        $.post("creatediscipline",
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
        $.ajax({
            type: "POST",
            url: "deletediscipline",
            data: "disciplineID="+DTree.id,
            success: function () {
                $("#discipline_tree").jstree("refresh");
            }
        })
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
        url: "createquestion",
        data: str,
        success: function () {
            renderQuestions();
            initQuestionFormCreation();
        }
    })

}

function deleteQuestions() {
    var qids = $("#questionlistform").serialize();
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
    newAnswer.innerHTML = '<div id=answerid[]">' +
        '<input type="text" name="answertext[]">' +
        '<input type="checkbox" name="checkanswer[]">' +
        '<a href="#" onclick="deleteAnswer(this)">Удалить поле</a></div>';

    document.getElementById("input_fields_wrap").appendChild(newAnswer);
}

function deleteAnswer(e) {
    e.closest("div").remove();
}


function initQuestionFormCreation() {
    answercount = 1;
    $('#input_fields_wrap').html('Введите вопрос:<br/>'+
        '<textarea name="questiontext" rows="3" cols="60"></textarea><br/>'+
        '<button type="button" onclick="addAnswer()">Добавить поле для ответа</button>'+
        '<button style="margin-left: 10px" type="button" onclick="createQuestion()">Создать вопрос</button>' +
        '<div id="answerfield1">' +
        '<input type="text" name="answertext[]"><input type="checkbox" name="checkanswer[]">'+
        '</div>');
}

function editquestion(id) {
    $.ajax({
        type: "POST",
        url: "getquestion",
        data: "questionid="+id,
        success: function (data) {
            $('#input_fields_wrap').html(data);
        }
    })
}

function updateQuestion() {
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
        url: "updatequestion",
        data: str,
        success: function () {
            renderQuestions();
            initQuestionFormCreation();
        }
    })
}