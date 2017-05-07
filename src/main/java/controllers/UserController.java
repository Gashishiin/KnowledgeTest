package controllers;

import DAO.UsersDAO;
import base.UserRole;
import base.Users;
import exceptions.UserDuplicatesException;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private final String NOTHING_DELETE = "nothingDelete";
    private final String ALREADY_EXISTS = "userAlreadyExists";
    private final String EMPTY_FIELDS="emptyFields";

    @RequestMapping("/users")
    public String getUsers(WebRequest request, Model model) {
        List<Users> usersList = new ArrayList();
        try {
            usersList = new UsersDAO().retrieveAllUsers();

        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("userlist", usersList);
        String nothingDelete = request.getParameter(NOTHING_DELETE);
        if ( nothingDelete!= null) model.addAttribute(NOTHING_DELETE,"No users to delete");
        String userExists = request.getParameter(ALREADY_EXISTS);
        if ( userExists!= null) model.addAttribute(ALREADY_EXISTS,"User " + userExists + " already exists");
        for (Users u :
                usersList) {
        }
        return "users";
    }

    @RequestMapping(value = "/createuser", method = RequestMethod.POST)
    public String createUser(WebRequest request, Model model) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String fullname = request.getParameter("fullname");
        String userrole = request.getParameter("role").toUpperCase();
        try {
            new UsersDAO().createUser(login, password, fullname, UserRole.valueOf(userrole));
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (UserDuplicatesException e) {
            model.addAttribute(ALREADY_EXISTS,login);
        }
        return "redirect:/users";
    }

    @RequestMapping(value = "/deleteusers", method = RequestMethod.POST)
    public String deleteUsers(WebRequest request, Model model) {
        String userLogins[] = request.getParameterValues("login");
        if (userLogins == null) {
            model.addAttribute(NOTHING_DELETE,"true");
        } else {
            new UsersDAO().deleteUsers(userLogins);
        }
        return "redirect:/users";
    }
}
