package controllers;

import DAO.QuestionDAO;
import DAO.TestManagementDAO;
import DAO.UsersDAO;
import base.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class TestManagementController {
    private static final Logger LOG = LoggerFactory.getLogger(TestManagementController.class);

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
        String threshold = request.getParameter("threshold");
        Map<String, String> properties = new HashMap<String, String>();
        if (questionAmount != null)
            properties.put("questionAmount", questionAmount);
        if (threshold != null)
            properties.put("threshold", threshold);
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
                    .retrieveAssignmentsByLogin(principal.getName(), TestIsNotDone);
            assignedTestList.addAll(new TestManagementDAO()
                    .retrieveAssignmentsByLogin(principal.getName(), null));
            model.addAttribute("assignedtestlist", assignedTestList);
            System.out.println("assigned list" + assignedTestList);
            List<TestManagement> doneTestList = new TestManagementDAO()
                    .retrieveAssignmentsByLogin(principal.getName(), TestIsDone);
            model.addAttribute("donetestlist", doneTestList);
        } else {
            long testID = Long.parseLong(test);
            TestManagement assignment = new TestManagementDAO().retrieveAssignmentByID(testID);
            assignment.setTestDone(null);
            List<Question> questionList = new QuestionDAO().retrieveQuestionByDiscipline(assignment.getDiscipline().getDisciplineID());
            Collections.shuffle(questionList);
            String questionAmountString = assignment.getProperties().get("questionAmount");
            if (questionAmountString != null) {
                int questionAmount = Integer.parseInt(questionAmountString);
                if (questionList.size() > questionAmount) ;
                questionList = questionList.subList(0, questionAmount);
            }
            List<String> questionsHTML = new ArrayList<String>();
            for (Question q :
                    questionList) {
                String questionBlockHTML = "";
                switch (q.getQuestionType()) {
                    case SINGLE_CHOICE:
                        questionBlockHTML += "<div name='questions'>" + q.getQuestionText() + "<br/>\n";
                        for (Answer a :
                                q.getAnswerSet()) {
                            questionBlockHTML += "<input type='radio' name='" + q.getQuestionID()
                                    + "' value='" + a.getAnswerID() + "'>"
                                    + a.getAnswerText() + "<br/>\n";
                        }
                        break;
                    case MULTI_CHOICE:
                        questionBlockHTML += "<div name='questionm'>" + q.getQuestionText() + "<br/>\n";
                        for (Answer a :
                                q.getAnswerSet()) {
                            questionBlockHTML += "<input type='checkbox' name='" + q.getQuestionID()
                                    + "' value='" + a.getAnswerID() + "'>"
                                    + a.getAnswerText() + "<br/>\n";
                        }
                        break;
                    case FREE:
                        questionBlockHTML += "<div name='questionf'>" + q.getQuestionText() + "<br/>\n";
                        for (Answer a :
                                q.getAnswerSet()) {
                            questionBlockHTML += "<input type='text' name='" + q.getQuestionID() + "'><br/>\n";
                        }
                        break;
                }
                questionBlockHTML += "<br/></div>\n";
                questionsHTML.add(questionBlockHTML);
            }
            model.addAttribute("assignmentid", test);
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
        int questionAmount = results.size() - 1;
        for (Map.Entry<String, String[]> entry : results.entrySet()) {
            if (entry.getKey().equals(assignmentParameter)) continue;
            Question question = questionDAO.retrieveQuestion(Long.parseLong(entry.getKey()));
            List<Answer> answers = questionDAO.retrieveAnswers(Long.parseLong(entry.getKey()));
            List<String> resultAnswerIDs = Arrays.asList(entry.getValue());
            switch (question.getQuestionType()) {
                case SINGLE_CHOICE:
                    if (questionDAO.retrieveAnswer(Long.parseLong(resultAnswerIDs.get(0))).isCorrect())
                        resultScore += maxScore / questionAmount;
                    break;
                case MULTI_CHOICE:
                    for (Answer a :
                            answers) {
                        if (a.isCorrect() == resultAnswerIDs.contains(Long.toString(a.getAnswerID()))) {
                            resultScore += maxScore / questionAmount / answers.size();
                        }
                    }
                    break;
                case FREE:
                    if (answers.get(0).getAnswerText().equalsIgnoreCase(entry.getValue()[0]))
                        resultScore += maxScore / questionAmount;
            }


        }
        resultScore = Math.rint(100.0 * resultScore) / 100.0;
        new TestManagementDAO().updateResults(assignmentID, resultScore, true);
        return "test";
    }

    @RequestMapping("getassignments")
    @ResponseBody
    public String getAssignments(WebRequest request, Model model) {
        String htmlBody = "<table style='padding-left: 10px'>";
        long userID = Long.parseLong(request.getParameter("userid"));
        List<TestManagement> assignments = new TestManagementDAO().retrieveAssignmentsByUserID(userID);
        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        Date date;
        String dateFromDB;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Boolean isTestDone = null;
        String repeatOrCancelButton = "";
        String status = "";
        String success;
        double threshold;
        String thresholdString;
        for (TestManagement assignment :
                assignments) {
            success = "";
            dateFromDB = assignment.getProperties().get("Date");
            if (dateFromDB != null)
                try {
                    date = parser.parse(dateFromDB);
                    dateFromDB = dateFormat.format(date);
                } catch (ParseException e) {
                    LOG.error("Can not parse date from DB");
                }
            isTestDone = assignment.isTestDone();
            if (isTestDone == null) {
                status = "В процессе";
                repeatOrCancelButton = "";
            } else if (isTestDone) {
                status = "Закрыт";
                repeatOrCancelButton = "<button type='button' onclick='repeattest("
                        + assignment.getAssignmentID() + "," +
                        + assignment.getUser().getUserID() + ")'>Повторить</button>";
            } else {
                status = "Открыт";
                repeatOrCancelButton = "<button type='button' onclick='canceltest("
                        + assignment.getAssignmentID() + "," +
                        + assignment.getUser().getUserID() + ")'>Отменить</button>";
            }

            thresholdString = assignment.getProperties().get("threshold");
            if (assignment.isTestDone() && thresholdString != null) {
                threshold = Double.parseDouble(thresholdString);
                if (assignment.getResultScore() >= threshold) success = "Успешно";
                else success = "Неуспешно";
            }

            htmlBody += "<tr><td>" + assignment.getDiscipline().getDisciplineName() + "</td>\n<td>"
                    + assignment.getResultScore() + "</td>\n<td>"
                    + status + "</td>\n<td>"
                    + dateFromDB + "</td>\n<td>"
                    + success + "</td>\n<td>"
                    + repeatOrCancelButton + "</td>"
            ;
        }
        htmlBody += "</tr>";
        return htmlBody;
    }

    @RequestMapping("deleteassignment")
    @ResponseBody
    public String deleteAssignment(WebRequest request, Model model) {
        String assignmentIDString = request.getParameter("assignmentid");
        long assignmentID;
        if (assignmentIDString != null) {
            assignmentID = Long.parseLong(assignmentIDString);
            if (new TestManagementDAO().retrieveAssignmentByID(assignmentID).isTestDone() != null)
                new TestManagementDAO().deleteAssignment(assignmentID);
        }
        return "";
    }

    @RequestMapping("repeatassignment")
    @ResponseBody
    public String repeatAssignment(WebRequest request) {
        long assignmentID = Long.parseLong(request.getParameter("assignmentid"));
        TestManagement assignment = new TestManagementDAO().retrieveAssignmentByID(assignmentID);
        long disciplineID = assignment.getDiscipline().getDisciplineID();
        String[] logins = {assignment.getUser().getLogin()};
        Map<String,String> properties = new HashMap<String, String>(assignment.getProperties());
        List<TestManagement> assignments = new TestManagementDAO().createTestAssignments(disciplineID,logins,properties);
        return assignments.get(0).getDiscipline().getDisciplineName() + " " + assignments.get(0).getAssignmentID();
    }
}
