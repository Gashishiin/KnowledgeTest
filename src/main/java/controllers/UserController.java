package controllers;

import DAO.UsersDAO;
import base.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
