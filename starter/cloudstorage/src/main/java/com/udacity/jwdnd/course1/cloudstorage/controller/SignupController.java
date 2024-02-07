package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    public SignupController(UserService userService,AuthenticationService authenticationService)
    {
        this.userService=userService;
        this.authenticationService = authenticationService;
    }


    @GetMapping()
    public String signupView(){return "signup";}

    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model, HttpSession session){
        String signupError=null;

        if (!userService.isUsernameAvailable(user.getUsername())){
            signupError="The username already exists.";
        }

        if(signupError==null){
            int rowsAdded=userService.createUser(user);
            if(rowsAdded<0){
                signupError="There was an error signing you up. Please try again.";
            }
        }

        if(signupError==null){
            session.setAttribute("signupSuccess",true);
            return "redirect:/login";
        }
        else {
            model.addAttribute("signupError", signupError);

        }
        return "signup";
    }
}

