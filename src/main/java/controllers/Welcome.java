package controllers;

import com.sun.deploy.net.HttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

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
