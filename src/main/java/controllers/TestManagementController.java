package controllers;

import DAO.QuestionDAO;
import DAO.TestManagementDAO;
import DAO.UsersDAO;
import base.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;
import java.util.*;

@Controller
public class TestManagementController {

    private QuestionDAO questionDAO = new QuestionDAO();

    @RequestMapping("testmanagement")
    public String testManagement(WebRequest request, Model model) {
        List<Users> usersList = new UsersDAO().retrieveAllUsers();
        model.addAttribute("userlist", usersList);
        return "testmanagement";
    }

    @RequestMapping("assigntest")
    @ResponseBody
    public String assignTest(WebRequest request, Model model) {
        String[] logins = request.getParameterValues("login");
        String discipline = request.getParameter("disciplineid");
        long disciplineID = Long.parseLong(discipline);
        System.out.println("Login " + Arrays.toString(logins));
        System.out.println("discipline " + disciplineID);
        Map<String, String> properties = new HashMap<String, String>();
        new TestManagementDAO().createTestAssignments(disciplineID, logins, properties);
        return "Assignment success";
    }

    @RequestMapping("test")
    public String test(WebRequest request, Model model, Principal principal) {
        String test = request.getParameter("id");
        if (test == null) {
            List<TestManagement> testassignmentList = new TestManagementDAO().retrieveAssignmentsByLogin(principal.getName());
            model.addAttribute("assignmentlist", testassignmentList);
        } else {
            long testID = Long.parseLong(test);
            TestManagement assignment = new TestManagementDAO().retrieveAssignmentByID(testID);
            List<Question> questionList = new QuestionDAO().retrieveQuestionByDiscipline(assignment.getDiscipline().getDisciplineID());
            List<String> questionsHTML = new ArrayList<String>();
            String questionBlockHTML;
            for (Question q :
                    questionList) {
                questionBlockHTML = q.getQuestionText() + "<br/>\n";
                for (Answer a :
                        q.getAnswerSet()) {
                    if (q.getQuestionType() == QuestionType.SINGLE_CHOICE) {
                        questionBlockHTML += "<input type='radio' name='" + q.getQuestionID()
                                + "' value='" + a.getAnswerID() + "'>"
                                + a.getAnswerText() + "<br/>\n";
                    } else if (q.getQuestionType() == QuestionType.MULTI_CHOICE) {
                        questionBlockHTML += "<input type='checkbox' name='" + q.getQuestionID()
                                + "' value='" + a.getAnswerID() + "'>"
                                + a.getAnswerText() + "<br/>\n";
                    }
                }
                questionBlockHTML += "<br/>\n";
                questionsHTML.add(questionBlockHTML);
            }
            model.addAttribute("questionlist", questionsHTML);
            model.addAttribute("questiontype", QuestionType.values());
        }
        return "test";
    }

    @RequestMapping("submitresults")
    @ResponseBody
    public String submitresults(WebRequest request, Model model) {
        double maxScore = 100.0;
        double resultScore = 0.0;
        Map<String, String[]> results = request.getParameterMap();
        boolean rightAnswer = false;
        int questionAmount = results.size();
        for (Map.Entry<String, String[]> entry : results.entrySet()) {
            Question question = questionDAO.retrieveQuestion(Long.parseLong(entry.getKey()));
            System.out.println("Q: " + question.getQuestionText());
            List<Answer> answers = questionDAO.retrieveAnswers(Long.parseLong(entry.getKey()));
            List<String> resultAnswerIDs = Arrays.asList(entry.getValue());
            if (question.getQuestionType() == QuestionType.SINGLE_CHOICE
                    && questionDAO.retrieveAnswer(Long.parseLong(resultAnswerIDs.get(0))).isCorrect()) {
                resultScore += maxScore / questionAmount;
            } else if (question.getQuestionType() == QuestionType.MULTI_CHOICE) {
                for (Answer a :
                        answers) {
                    if (a.isCorrect() == resultAnswerIDs.contains(Long.toString(a.getAnswerID()))) {
                        resultScore+= maxScore / questionAmount / answers.size();
                    }

                }
            }
        }
        System.out.println("Result score " + resultScore);
        return "test";
    }
}
