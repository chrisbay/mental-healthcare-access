package org.launchcode.mentalhealthcareaccess.controllers;

import org.launchcode.mentalhealthcareaccess.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserLandingController {

    @GetMapping("/user")
    public String displayUserPage(Model model, HttpSession session) {
        model.addAttribute("welcome", "Welcome ");
        return "user/user";
    }

}
