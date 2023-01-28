package com.burakkolay.credit.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("")
public class UserController {


    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }

}
