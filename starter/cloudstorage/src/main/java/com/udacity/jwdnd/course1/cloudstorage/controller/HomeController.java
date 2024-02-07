package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping()
public class HomeController {

        @Autowired
        private final UserService userService;
        private final FileService fileService;
        private final NoteService notesService;
        private final CredentialService credentialService;

        public HomeController(UserService userService, FileService fileService,NoteService notesService, CredentialService credentialService) {
            this.userService= userService;
            this.fileService= fileService;
            this.notesService = notesService;
            this.credentialService=credentialService;
        }


        @GetMapping("/home")
        public String getHomePage(Authentication authentication, Model model) {
            String username = authentication.getName();
            User user = userService.getUser(username);
            Credentials credentials=credentialService.getCredentialByUserId(user.getUserId());


            List<Credentials> credentialsList = credentialService.getAllUserCredentials(user.getUserId());
            for (Credentials credential : credentialsList) {
                credential.setDecryptedPassword(credentialService.decryptPassword(credential));
            }


            model.addAttribute("uploadedFiles", fileService.getAllFiles(user.getUserId()));
            model.addAttribute("userNotes", notesService.getNotesByUserId(user.getUserId()));
            model.addAttribute("credentialsList", credentialService.getAllUserCredentials(user.getUserId()));

            return "home";
        }



    @GetMapping("/result")
    public String showResultPage(Model model) {
        String successMessage = (String) model.getAttribute("successMessage");
        String errorMessage = (String) model.getAttribute("errorMessage");

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        return "result";
    }


}


