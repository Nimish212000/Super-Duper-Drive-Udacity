package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    @Autowired
    private final UserService userService;
    private final CredentialService credentialService;
    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService= userService;
        this.credentialService=credentialService;
    }
    @PostMapping("/save-credential")
    public String saveCredential(@ModelAttribute Credentials credentials, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.getUser(username);

            if (credentials.getCredentialid() == null) {
                credentialService.addCredential(credentials, user);
            }
            else {

                credentialService.editCredential(credentials);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Credentials saved successfully.");
            return "redirect:/result";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred during credential saving.");
            return "redirect:/result";
        }
    }

    @GetMapping("/delete-credential/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, Model model, RedirectAttributes redirectAttributes) {
        credentialService.deleteCredential(credentialId);
        redirectAttributes.addFlashAttribute("successMessage", "Credentials deleted successfully.");
        return "redirect:/result";
    }
}
