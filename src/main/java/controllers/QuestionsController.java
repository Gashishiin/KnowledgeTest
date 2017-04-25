package controllers;

import DAO.DisciplineDAO;
import base.Discipline;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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



}
