package controllers;

import DAO.DisciplineDAO;
import base.Discipline;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DisciplineController {

    @RequestMapping(value = "/creatediscipline", method = RequestMethod.POST)
    public String createDiscipline(WebRequest request, Model model) {
        String disciplineName = request.getParameter("disciplineName");
        String parentDiscipline = request.getParameter("parentDisciplineID");
        long parentDisciplineID;
        if (parentDiscipline == null || parentDiscipline.equals("")) {
            parentDisciplineID = 0L;
        } else{
            parentDisciplineID = Long.parseLong(parentDiscipline);
        }
        try{
            new DisciplineDAO().createDiscipline(disciplineName,parentDisciplineID);
        }catch(HibernateException e){

        }

        return "redirect:/disciplines";
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
}
