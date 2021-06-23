package org.launchcode.mentalhealthcareaccess.controllers;
import org.launchcode.mentalhealthcareaccess.models.Languages;
import org.launchcode.mentalhealthcareaccess.models.data.ProviderRepository;
import org.launchcode.mentalhealthcareaccess.models.Provider;

import org.launchcode.mentalhealthcareaccess.models.data.dto.LoginFormDTO;
import org.launchcode.mentalhealthcareaccess.models.data.dto.ProviderSettingsDTO;
import org.launchcode.mentalhealthcareaccess.models.data.dto.RegisterFormDTO;
import org.launchcode.mentalhealthcareaccess.models.dto.UserLoginFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Controller
public class ProviderController {

    @Autowired
    ProviderRepository providerRepository;

    private static final String providerSessionKey = "provider";

    //Get the provider info
    public Provider getProviderFromSession(HttpSession session) {
        Integer providerId = (Integer) session.getAttribute(providerSessionKey);
        if (providerId == null) {
            return null;
        }

        Optional<Provider> provider = providerRepository.findById(providerId);

        if (provider.isEmpty()) {
            return null;
        }

        return provider.get();
    }
    //Set the provider in  session
    private static void setProviderInSession(HttpSession session, Provider provider) {
        session.setAttribute(providerSessionKey, provider.getId());
    }


     @GetMapping("provider/signup")
     public String displaySignUpForm(Model model) {
         model.addAttribute(new RegisterFormDTO());
         model.addAttribute("title", "Sign-Up");
         return "provider/signup";
    }

    @PostMapping("provider/signup")
    public String processSignUpForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, HttpServletRequest request,
                                          Model model, HttpSession session) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Sign-up");
            return "provider/signup";
        }

        if (registerFormDTO.getEmail() == "") {
            //This is a custom error
            errors.rejectValue("email", "email.isBlank", "An email is required.");
            model.addAttribute("title", "Sign-Up");
            return "provider/signup";
        }

        Provider existingProvider = providerRepository.findByEmail(registerFormDTO.getEmail());

        if (existingProvider != null) {
            //This is a custom error
            errors.rejectValue("email", "email.alreadyExists", "This email is already registered, please choose a new one");
            model.addAttribute("title", "Sign-Up");
            return "provider/signup";
        }

        if (registerFormDTO.getCompanyName() == "" && (registerFormDTO.getFirstName() == "" || registerFormDTO.getLastName() == "") ){
            //This is a custom error
            errors.rejectValue("companyName", "companyName.cannotBeBlank", "You must enter a Company Name or a First and Last Name for the provider");
            model.addAttribute("title", "Sign-Up");
            return "provider/signup";
        }
        if (registerFormDTO.getCompanyName() != "" && (registerFormDTO.getFirstName() != "" || registerFormDTO.getLastName() != "") ){
            //This is a custom error
            errors.rejectValue("companyName", "companyName.onlyOneType", "You must choose to enter either a Company Name or a First and Last Name");
            model.addAttribute("title", "Sign-Up");
            return "provider/signup";
        }

        //This password was set in LoginFormDTO
        String password = registerFormDTO.getPassword();
        //This password was set in RegisterFormDTO that EXTENDS LoginFormDTO
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Sign-up");
            return "provider/signup";
        }

        Provider newProvider = new Provider(registerFormDTO.getDisplayName(),
                registerFormDTO.getCompanyName(),
                registerFormDTO.getFirstName(),
                registerFormDTO.getLastName(),
                registerFormDTO.getEmail(),
                registerFormDTO.getPhoneNumber(),
                registerFormDTO.getPassword(),
                registerFormDTO.getLang());

        providerRepository.save(newProvider);

        setProviderInSession(request.getSession(), newProvider);

        session.setAttribute("name", newProvider.getDisplayName());
        session.setAttribute("email", newProvider.getEmail());
        session.setAttribute("phone", newProvider.getPhoneNumber());

        return "provider/dashboard";
    }

    // login mapping
    @GetMapping("/provider/login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return "/provider/login";
    }

    @PostMapping("/provider/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model, HttpSession session) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            return "/provider/login";
        }
        //Find Provider in DB
        Provider theProvider = providerRepository.findByEmail(loginFormDTO.getEmail());

        //Custom error if this email is not registered
        if (theProvider == null) {
            errors.rejectValue("email", "provider.invalid", "This e-mail is not registered");
            model.addAttribute("title", "Log In");
            return "/provider/login";
        }

        //Compare password input to DB
        String password = loginFormDTO.getPassword();
        if (!theProvider.isMatchingPassword(password)) {
            //Error if wrong pass
            errors.rejectValue("password", "password.invalid", "Incorrect password, please verify");
            model.addAttribute("title", "Log In");
            return "/provider/login";
        }
        //Create a new session for the provider
        setProviderInSession(request.getSession(), theProvider);

        // Set attributes for provider session
        session.setAttribute("name", theProvider.getDisplayName());
        session.setAttribute("email", theProvider.getEmail());
        session.setAttribute("phone", theProvider.getPhoneNumber());
        session.setAttribute("providerId", theProvider.getId());
        session.setAttribute("languages", theProvider.getLanguages());

        //Login to dashboard
        return "/provider/dashboard";
    }

    //displays provider info in their dashboard
    @GetMapping("/provider/dashboard")
    public String displayDashboard(Model model, Provider theProvider, HttpSession session) {
        Integer providerId = (Integer) session.getAttribute("providerId");
        Optional<Provider> result = providerRepository.findById(providerId);
        Provider dashProvider = result.get();
        model.addAttribute("title", "Dashboard");
        model.addAttribute("dashboardLanguages", dashProvider.getLanguages());
        return "/provider/dashboard";
    }
        //Change languages for services provided
        private static final String INSURANCE_TYPES = "insuranceTypes";



    @GetMapping("/provider/settings")
    public String displaySettingsForm(Model model,
                                      HttpSession session){
        ProviderSettingsDTO providerSettings = new ProviderSettingsDTO();
        Integer providerId = (Integer) session.getAttribute("providerId");
       Optional<Provider> result = providerRepository.findById(providerId);
       Provider modProvider = result.get();
        model.addAttribute("title", "Settings");
        model.addAttribute("languages", Languages.values());
        model.addAttribute("modProvider", modProvider);
        providerSettings.setProvider(modProvider);
        model.addAttribute("providerSettings", providerSettings);
        model.addAttribute("currentLanguages", modProvider.getLanguages());
        model.addAttribute("insuranceTypes", Arrays.asList(" MedicAid", " MediCare", " Private", " Self-Pay Discount", " Veteran's Affairs"));


        return "/provider/settings";
    }


    @PostMapping ("/provider/settings")
    public String processSettingsForm(@ModelAttribute @Valid ProviderSettingsDTO providerSettings,
                                      Errors errors,
                                      Model model,
                                      HttpSession session,
                                      Provider modProvider,  @RequestParam List<String> insuranceValues){
        if (!modProvider.getInsurance().contains(insuranceValues)) {
            modProvider.setInsurance(insuranceValues);
        }
        providerRepository.save(modProvider);
        if (!errors.hasErrors()){
            Provider provider = providerSettings.getProvider();
            Languages languages = providerSettings.getLanguages();
                if (!provider.getLanguages().contains(languages)) {
                    provider.addLanguages(languages);
                    providerRepository.save(provider);
                }
            return "/provider/dashboard";

        }
        return "/provider/dashboard";
    }

}
