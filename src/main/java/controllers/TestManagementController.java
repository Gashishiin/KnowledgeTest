package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestManagementController {

    @RequestMapping("testmanagement")
    public String testManagement(){
        return "testmanagement";
    }
}
