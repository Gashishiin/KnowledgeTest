package controllers;

import DAO.DisciplineDAO;
import base.Discipline;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
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
        return "Created discipline " + disciplineName;
    }


    @RequestMapping(value = "/disciplines")
    public String getDisciplines(WebRequest request,Model model){
        List<Discipline> disciplineList = new ArrayList<Discipline>();
        try{
            disciplineList = new DisciplineDAO().retrieveDisciplines();
        }catch (HibernateException e){

        }
        model.addAttribute("disciplinelist", disciplineList);
        return ("/disciplines");
    }

    @RequestMapping(value = "/deletediscipline")
    @ResponseBody
    public String deleteDiscipline(@RequestParam(value = "disciplineID") long disciplineID){
        String disciplineName = new DisciplineDAO().retrieveDiscipline(disciplineID).getDisciplineName();
        System.out.println("Deleting discipline " + disciplineName);
        new DisciplineDAO().deleteDiscipline(disciplineID);
        return disciplineName;
    }

}
