package controllers;

import DAO.DisciplineDAO;
import DAO.QuestionDAO;
import base.Answer;
import base.Discipline;
import base.Question;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Controller
public class QuestionsController {

    @RequestMapping("/questions")
    public String getQuestions(){
        return "questions";
    }

    @RequestMapping("/question_list")
    @ResponseBody
    public String getQuestionList(WebRequest request, Model model){
        StringBuilder questionsWithAnswers= new StringBuilder();
        String discipline = request.getParameter("disciplineID");
        long disciplineID = Long.parseLong(discipline);
        List<Question> questionList;
        questionList = new QuestionDAO().retrieveQuestionByDiscipline(disciplineID);
        List<Answer> answerList;
        Question q;
        for (int i = 0; i < questionList.size();i++) {
            q = questionList.get(i);
            questionsWithAnswers
                    .append("<div>")
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
                        ? "<li style=\"list-style-image: url(/resources/img/right.png)\">"
                        : "<li style=\"list-style-image: url(/resources/img/wrong.png)\">")
                        .append(a.getAnswerText()).append("</li>\n");
            }
            questionsWithAnswers.append("</div><br/>");
        }
        return questionsWithAnswers.toString();
    }

    @RequestMapping("/createquestion")
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


}
