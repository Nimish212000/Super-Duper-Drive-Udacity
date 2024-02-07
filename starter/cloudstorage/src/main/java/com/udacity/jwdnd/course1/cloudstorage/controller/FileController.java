package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    private final UserService userService;
    private final FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService= userService;
        this.fileService= fileService;
    }

    @PostMapping("/upload")
    public String uploadFile(@ModelAttribute("file") @RequestParam("fileUpload") MultipartFile file, Model model, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        String username = authentication.getName();
        User user = userService.getUser(username);

        if (fileService.doesFileExist(user.getUserId(), file.getOriginalFilename())) {
            redirectAttributes.addFlashAttribute("errorMessage", "File already exists.");
            return "redirect:/result";
        }
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot upload an empty file.");
            return "redirect:/result";
        }


        try {


            fileService.uploadFile(file, user);
                model.addAttribute("uploadSuccess", true);
                redirectAttributes.addFlashAttribute("successMessage", "Your file was successfully saved.");


        }
        catch (IOException e) {
            model.addAttribute("uploadError", true);
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred during file save.");
        }
        return "redirect:/result";

    }



    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId) {

        File file = fileService.getFileById(fileId);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(file.getFiledata());

        return ResponseEntity.ok()
                .contentLength(file.getFiledata().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Model model, RedirectAttributes redirectAttributes) {

        File file = fileService.getFileById(fileId);

        if (file != null) {
            fileService.deleteFileById(fileId);
            redirectAttributes.addFlashAttribute("successMessage", "File deleted successfully");
        }

        return "redirect:/result";
    }

}
