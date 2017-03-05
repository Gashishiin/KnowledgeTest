package controllers;

import DAO.UsersDAO;
import base.Users;
import basics.UserApp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @RequestMapping("/users")
    public String getUsers(Model model){
        List<String> users = new ArrayList();
        try {
            List<Users> usersList = new UsersDAO().retrieveAllUsers();
            for (Users u :
                    usersList) {
                users.add(String.format("%d\t%s\t%s",u.getId(),u.getLogin(),u.getFullname()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("userlist",users);
        return "users";
    }
    @RequestMapping(value = "/createuser", method = RequestMethod.POST)
    public String createUser(WebRequest request){
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String fullname = request.getParameter("fullname");
        try {
            new UsersDAO().createUser(login,password,fullname);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "users";
    }
}
