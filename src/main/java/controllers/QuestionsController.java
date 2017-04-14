package controllers;

import DAO.DisciplineDAO;
import base.Discipline;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class QuestionsController {

    public static List<Discipline> disciplines = new ArrayList<>();
    @RequestMapping("/questions")
    public String getQuestions(){
        disciplines = new DisciplineDAO().retrieveDisciplines();
        return "questions";
    }


}
