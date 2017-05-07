package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Welcome {
    @RequestMapping(value = "/")
    public String hello(){
        return "welcome";
    }

    @RequestMapping(value = "login")
    public String login(){return "login";}

    @RequestMapping(value = "include")
    public String include(){return "include";}
}
