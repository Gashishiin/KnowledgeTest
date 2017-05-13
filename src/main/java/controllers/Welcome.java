package controllers;

import DAO.UsersDAO;
import base.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;

@Controller
public class Welcome {
    @RequestMapping(value = "/")
    public String hello() {
        return "welcome";
    }

    @RequestMapping(value = "login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "include")
    public String include(Principal principal, Model model) {
        Users user = new UsersDAO().retrieveUser(principal.getName());
        model.addAttribute("fullname", user.getFullname());
        Map<String, String> allowedLinks = new LinkedHashMap<String, String>();
        switch (user.getUserRole()) {
            case ROLE_ADMIN:
                allowedLinks.put("users", "Пользователи");
                allowedLinks.put("questions", "Вопросы и разделы");
                allowedLinks.put("testmanagement","Назначение тестов и результаты");
                allowedLinks.put("test","Тест");
                break;
            case ROLE_METHODIST:
                allowedLinks.put("questions", "Вопросы и разделы");
                allowedLinks.put("testmanagement","Назначение тестов и результаты");
                allowedLinks.put("test","Тест");
                break;
        }
        model.addAttribute("allowedlinks", allowedLinks);
        return "include";
    }

    @RequestMapping(value = "logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login";
    }

}
