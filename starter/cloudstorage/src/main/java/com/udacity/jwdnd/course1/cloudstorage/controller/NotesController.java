package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NotesController {
    @Autowired
    private final UserService userService;
    private final NoteService notesService;

    public NotesController(UserService userService, NoteService notesService) {
        this.userService= userService;
        this.notesService = notesService;
    }


    @PostMapping("/save-note")
    public String saveOrUpdateNote(@ModelAttribute Notes note, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {

        try {
            String username = authentication.getName();
            User user = userService.getUser(username);
            note.setUserId(user.getUserId());

            if (note.getNotesId() != null) {
                notesService.editNote(note);
            } else {
                notesService.addOrUpdateNote(note);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Note saved successfully");
            return "redirect:/result";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred during note save.");
            return "redirect:/result";
        }
    }

    @GetMapping("/delete-note/{noteId}")
    public String deleteNoteById(Model model, Authentication authentication, @PathVariable Integer noteId, RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            Notes existingNote = notesService.getNoteById(noteId);

            if (existingNote != null) {
                notesService.deleteNote(noteId);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Note deleted successfully.");
            return "redirect:/result";
        }
        catch (Exception e) {

            model.addAttribute("errorMessage", "An error occurred during note deletion.");
            return "redirect:/result";
        }
    }



}
