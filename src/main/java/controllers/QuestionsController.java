package controllers;

import DAO.DisciplineDAO;
import DAO.QuestionDAO;
import base.Answer;
import base.Discipline;
import base.Question;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class QuestionsController {

    public static List<Discipline> disciplines = new ArrayList<Discipline>();
    @RequestMapping("/questions")
    public String getQuestions(WebRequest request, Model model){
        disciplines = new DisciplineDAO().retrieveDisciplines();
        model.addAttribute("disciplines",disciplines);
        return "questions";
    }

    @RequestMapping("/question_list")
    @ResponseBody
    public String getQuestionList(WebRequest request, Model model){
        String questionsWithAnswers="";
        String discipline = request.getParameter("disciplineID");
        long disciplineID = Long.parseLong(discipline);
        List<Question> questionList = new ArrayList<Question>();
        questionList = new QuestionDAO().retrieveQuestionByDiscipline(disciplineID);
        List<Answer> answerList;
        for (Question q :
                questionList) {
            questionsWithAnswers+=q.getQuestionText()+"\n";
            answerList = new QuestionDAO().retrieveAnswers(q.getQuestionID());
            for (Answer a :
                    answerList) {
                questionsWithAnswers+=(a.isCorrect()
                        ? "<li style=\"list-style: disc\">"
                        : "<li style=\"list-style: circle\">")
                        + a.getAnswerText()+"</li>\n";
            }
        }
        return questionsWithAnswers;
    }



}
