package org.launchcode.mentalhealthcareaccess.controllers;

import org.launchcode.mentalhealthcareaccess.models.User;
import org.launchcode.mentalhealthcareaccess.models.data.UserRepository;
import org.launchcode.mentalhealthcareaccess.models.dto.UserLoginFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserLoginController {

    @Autowired
    UserRepository userRepository;

    private static final String userSessionKey = "user";

    @GetMapping("user/login")
    public String displayLogInForm(Model model) {
        model.addAttribute(new UserLoginFormDTO());
        return "user/login";
    }

    @PostMapping("/user/login")
    public String processLoginForm(@ModelAttribute @Valid UserLoginFormDTO userLoginFormDTO, Errors errors, HttpServletRequest request, Model model) {
        if (errors.hasErrors()) {
            return "user/login";
        }

        User theUser = userRepository.findByEmail(userLoginFormDTO.getEmail());

        if (theUser == null) {
            errors.rejectValue("email", "email.invalid", "The given email address does not exist");
            return "user/login";
        }

        String password = userLoginFormDTO.getPassword();

        if (!theUser.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid", "Invalid password");
            return "user/login";
        }

        setUserInSession(request.getSession(), theUser);

        return "redirect:/user";
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/login";
    }
}