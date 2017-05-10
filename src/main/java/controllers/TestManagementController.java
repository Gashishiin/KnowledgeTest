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
        String questionAmount = request.getParameter("questionamount");
        Map<String, String> properties = new HashMap<String, String>();
        if (questionAmount!= null)
            properties.put("questionAmount",questionAmount);
        long disciplineID = Long.parseLong(discipline);
        new TestManagementDAO().createTestAssignments(disciplineID, logins, properties);
        return "Assignment success";
    }

    @RequestMapping("test")
    public String test(WebRequest request, Model model, Principal principal) {
        String test = request.getParameter("id");
        if (test == null) {
            boolean TestIsDone = true;
            boolean TestIsNotDone = false;
            List<TestManagement> assignedTestList = new TestManagementDAO()
                    .retrieveAssignmentsByLogin(principal.getName(),TestIsNotDone);
            model.addAttribute("assignedtestlist", assignedTestList);
            List<TestManagement> doneTestList = new TestManagementDAO()
                    .retrieveAssignmentsByLogin(principal.getName(),TestIsDone);
            model.addAttribute("donetestlist",doneTestList);
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
            model.addAttribute("assignmentid",test);
            model.addAttribute("questionlist", questionsHTML);
        }
        return "test";
    }

    @RequestMapping("submitresults")
    public String submitresults(WebRequest request, Model model) {
        QuestionDAO questionDAO = new QuestionDAO();
        double maxScore = 100.0;
        double resultScore = 0.0;
        Map<String, String[]> results = request.getParameterMap();
        String assignmentParameter = "assignmentid";
        long assignmentID = Long.parseLong(request.getParameter(assignmentParameter));
        int questionAmount = results.size()-1;
        System.out.println("Map results " + results );
        for (Map.Entry<String, String[]> entry : results.entrySet()) {
            if (entry.getKey().equals(assignmentParameter)) continue;
            Question question = questionDAO.retrieveQuestion(Long.parseLong(entry.getKey()));
            List<Answer> answers = questionDAO.retrieveAnswers(Long.parseLong(entry.getKey()));
            System.out.println("Q: " + question.getQuestionText());
            List<String> resultAnswerIDs = Arrays.asList(entry.getValue());
            if (question.getQuestionType() == QuestionType.SINGLE_CHOICE
                    && questionDAO.retrieveAnswer(Long.parseLong(resultAnswerIDs.get(0))).isCorrect()) {
                resultScore += maxScore / questionAmount;
                System.out.println(questionDAO.retrieveAnswer(Long.parseLong(resultAnswerIDs.get(0))).getAnswerText());
            } else if (question.getQuestionType() == QuestionType.MULTI_CHOICE) {
                for (Answer a :
                        answers) {
                    if (a.isCorrect() == resultAnswerIDs.contains(Long.toString(a.getAnswerID()))) {
                        resultScore+= maxScore / questionAmount / answers.size();
                    }

                }
            }
        }
        resultScore=Math.rint(100.0*resultScore)/100.0;
        new TestManagementDAO().updateResults(assignmentID,resultScore);
        return "test";
    }

    @RequestMapping("getassignments")
    @ResponseBody
    public String getAssignments(WebRequest request, Model model){
        String htmlBody="";
        long userID = Long.parseLong(request.getParameter("userid"));
        List<TestManagement> assignments = new TestManagementDAO().retrieveAssignmentsByUserID(userID);
        for (TestManagement assignment :
                assignments) {
            htmlBody+=assignment.getDiscipline().getDisciplineName()+" "
                    + assignment.getResultScore() + " "
                    + (assignment.isTestDone() ? "Закрыт" : "Открыт") + "<br/>";
        }
        return htmlBody;
    }
}
