package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QuestionsController {

    @RequestMapping("/questions")
    public String getQuestions(){
        return "questions";
    }


}