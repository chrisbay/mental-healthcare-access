package org.launchcode.mentalhealthcareaccess;

import org.launchcode.mentalhealthcareaccess.controllers.ProviderController;
import org.launchcode.mentalhealthcareaccess.controllers.UserSignupController;
import org.launchcode.mentalhealthcareaccess.models.Provider;
import org.launchcode.mentalhealthcareaccess.models.User;
import org.launchcode.mentalhealthcareaccess.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter extends HandlerInterceptorAdapter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserSignupController userSignupController;

    @Autowired
    ProviderController providerController;

    private static final List<String> whitelist = Arrays.asList("/user/login", "/user/signup", "/provider/login", "/provider/signup", "/logout", "/css", "/webjar");

    private static boolean isWhitelisted(String path) {
        for (String pathRoot : whitelist) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        if(isWhitelisted(request.getRequestURI())) {
            return true;
        }

        HttpSession session = request.getSession();
        User user = userSignupController.getUserFromSession(session);
        Provider provider = providerController.getProviderFromSession(session);

        if (user != null || provider != null) {
            return true;
        }

        response.sendRedirect("/user/login");
        return false;
    }
}
