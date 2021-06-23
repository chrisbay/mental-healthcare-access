package org.launchcode.mentalhealthcareaccess.controllers;

import org.launchcode.mentalhealthcareaccess.models.User;
import org.launchcode.mentalhealthcareaccess.models.data.UserRepository;
import org.launchcode.mentalhealthcareaccess.models.dto.UserSignupFormDTO;
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
public class UserSignupController {

    @Autowired
    UserRepository userRepository;

    private static final String userSessionKey = "user";

    @GetMapping("/user/signup")
    public String displaySignUpForm(Model model) {
        model.addAttribute(new UserSignupFormDTO());
        return "user/signup";
    }

    @PostMapping("/user/signup")
    public String processSignUpForm(@ModelAttribute @Valid UserSignupFormDTO userSignupFormDTO, Errors errors, HttpServletRequest request, Model model) {
        if (errors.hasErrors()) {
            return "user/signup";
        }

        User existingUser = userRepository.findByEmail(userSignupFormDTO.getEmail());

        if (existingUser != null) {
            errors.rejectValue("email", "email.alreadyexists", "A user with that email address already exists");
            return "user/signup";
        }

        String password = userSignupFormDTO.getPassword();
        String verifyPassword = userSignupFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("verifyPassword", "passwords.mismatch", "Passwords do not match");
            return "user/signup";
        }

        User newUser = new User(userSignupFormDTO.getFirstName(), userSignupFormDTO.getLastName(), userSignupFormDTO.getEmail(), userSignupFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);

        return "redirect:";
    }

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    protected static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }
}

