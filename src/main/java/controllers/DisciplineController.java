package controllers;

import DAO.DisciplineDAO;
import base.Discipline;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Controller
public class DisciplineController {

    @RequestMapping(value = "/creatediscipline", method = RequestMethod.POST)
    @ResponseBody
    public String createDiscipline(WebRequest request, Model model) {
        String disciplineName = request.getParameter("disciplineName");
        String parentDiscipline = request.getParameter("parentDisciplineID");
        long parentDisciplineID;
        if (parentDiscipline == null || parentDiscipline.equals("")) {
            parentDisciplineID = 0L;
        } else{
            parentDisciplineID = Long.parseLong(parentDiscipline);
        }

            new DisciplineDAO().createDiscipline(disciplineName,parentDisciplineID);
        List<Discipline> disciplines =  new DisciplineDAO().retrieveDisciplines();
        model.addAttribute("disciplines",disciplines);
        return "Created discipline " + disciplineName;
    }


    @RequestMapping(value = "/disciplines")
    @ResponseBody
    public String getDisciplines(){
        List<Discipline> disciplineList = new DisciplineDAO().retrieveDisciplines();
        StringBuilder disciplineTreeBody = new StringBuilder();
        disciplineTreeBody.append("[{\"id\": \"0\", \"parent\": \"#\", \"text\": \"Top\", \"state\": {\"selected\": \"true\"}},");
        for (Discipline d :
                disciplineList) {
            disciplineTreeBody.append("{\"id\":\"").append(d.getDisciplineID()).append("\",\"parent\":\"").append(d.getParentDisciplineID()).append("\",\"text\":\"").append(d.getDisciplineName()).append("\"},");
        }
        disciplineTreeBody = new StringBuilder(disciplineTreeBody.substring(0, disciplineTreeBody.length() - 1) + "]");
        return disciplineTreeBody.toString();
    }

    @RequestMapping(value = "/deletediscipline")
    @ResponseBody
    public String deleteDiscipline(@RequestParam(value = "disciplineID") long disciplineID){
        String disciplineName = new DisciplineDAO().retrieveDiscipline(disciplineID).getDisciplineName();
        new DisciplineDAO().deleteDiscipline(disciplineID);
        return disciplineName;
    }

}
