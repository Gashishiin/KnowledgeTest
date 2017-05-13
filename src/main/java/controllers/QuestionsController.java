package controllers;

import DAO.QuestionDAO;
import base.Answer;
import base.Question;
import org.apache.taglibs.standard.lang.jstl.test.PageContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Set;

@Controller
public class QuestionsController {

    @RequestMapping("questions")
    public String getQuestions(){
        return "questions";
    }

    @RequestMapping("question_list")
    @ResponseBody
    public String getQuestionList(WebRequest request, Model model){
        StringBuilder questionsWithAnswers= new StringBuilder();
        String discipline = request.getParameter("disciplineID");
        long disciplineID = Long.parseLong(discipline);
        List<Question> questionList;
        questionList = new QuestionDAO().retrieveQuestionByDiscipline(disciplineID);
        List<Answer> answerList;
        Question q;
        String contextPath = request.getContextPath();
        for (int i = 0; i < questionList.size();i++) {
            q = questionList.get(i);
            questionsWithAnswers
                    .append("<div>")
                    .append("<button type='button' onclick='editquestion(")
                    .append(q.getQuestionID()).append(")'>Редактировать</button><br/>")
                    .append("<input type=\"checkbox\" name=\"checkboxquestion\" value=\"")
                    .append(q.getQuestionID())
                    .append("\">")
                    .append(i + 1)
                    .append(". ")
                    .append(q.getQuestionText())
                    .append("\n");
            answerList = new QuestionDAO().retrieveAnswers(q.getQuestionID());
            for (Answer a :
                    answerList) {
                questionsWithAnswers.append(a.isCorrect()
                        ? "<li style=\"list-style-image: url(" + contextPath + "/resources/img/right.png)\">"
                        : "<li style=\"list-style-image: url(" + contextPath + "/resources/img/wrong.png)\">")
                        .append(a.getAnswerText()).append("</li>\n");
            }
            questionsWithAnswers.append("</div><br/>");
        }
        return questionsWithAnswers.toString();
    }

    @RequestMapping("createquestion")
    @ResponseBody
    public String createQuestion(WebRequest request, Model model){
        String questionText = request.getParameter("questiontext");
        String[] answerText = request.getParameterValues("answertext[]");
        String[] checks = request.getParameterValues("checkanswer[]");
        String disciplineidtext = request.getParameter("disciplineid");

        long disciplineID = Long.parseLong(disciplineidtext);
        new QuestionDAO().createQuestion(disciplineID,questionText,answerText,checks);
        return questionText;
    }

    @RequestMapping("deletequestions")
    @ResponseBody
    public String deleteQuestion(WebRequest request, Model model){
        String[] textIDs = request.getParameterValues("checkboxquestion");
        long[] ids = new long[textIDs.length];
        for (int i = 0; i < textIDs.length; i++) {
            ids[i] = Long.parseLong(textIDs[i]);
        }
        new QuestionDAO().deleteQuestions(ids);
        return "";
    }

    @RequestMapping("getquestion")
    @ResponseBody
    public String getQuestion(WebRequest request, Model model){
        long questionID = Long.parseLong(request.getParameter("questionid"));
        Question question = new QuestionDAO().retrieveQuestion(questionID);
        String htmlBody="Введите вопрос:<br/>"+
                "<textarea name='questiontext' rows='3' cols='60'>"+ question.getQuestionText() + "</textarea><br/>"+
                "<button type='button' onclick='addAnswer()'>Добавить поле для ответа</button>"+
                "<button style='margin-left: 10px' type='button' onclick='updateQuestion()'>Обновить вопрос</button>"+
                "<button style='margin-left: 10px' type='button' onclick='initQuestionFormCreation()'>Отмена</button>";
        Set<Answer> answerList = question.getAnswerSet();
        int answerFieldNum = 1;
        for (Answer a :
                answerList) {
            htmlBody+= "<div id='answerfield'" + answerFieldNum + "'>" +
                    "Вариант ответа <input type='text' name='answertext[]' value='" + a.getAnswerText()+ "'>" +
                    "<input type='checkbox' name='checkanswer[]' " + (a.isCorrect() ? "checked" : "") + ">" +

                    (answerFieldNum > 1 ? "<a href='#' onclick='deleteAnswer(this)'>Удалить поле</a>": "") +
                    "</div>";
            answerFieldNum++;
        }
        htmlBody+="<input type='hidden' name='questionid' value='" + questionID + "'>";

        return htmlBody;
    }

    @RequestMapping("updatequestion")
    @ResponseBody
    public String updateQuestion(WebRequest request, Model model){
        System.out.println("updateq " + request.getParameterMap());
        String questionText = request.getParameter("questiontext");
        long questionID = Long.parseLong(request.getParameter("questionid"));
        String[] answerText = request.getParameterValues("answertext[]");
        String[] checks = request.getParameterValues("checkanswer[]");
        new QuestionDAO().updateQuestion(questionID,questionText,answerText,checks);
        return "";
    }

}
