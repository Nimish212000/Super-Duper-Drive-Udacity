package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;
    private final UserService userService;

    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating FileService bean");
    }
    public List<File> getAllFiles(int userId) {
        return fileMapper.getAllFiles(userId);
    }

    public File getFileById(int fileId) {
        return fileMapper.getFileById(fileId);
    }

    public File getFileByNameAndUserId(String filename, int userId) {
        return fileMapper.getFileByNameAndUser(filename, userId);
    }

    public boolean doesFileExist(int userId, String filename) {
        File existingFile = fileMapper.getFileByNameAndUser(filename, userId);
        return existingFile != null;
    }

    public int uploadFile(MultipartFile file, User user) throws IOException {

        String fileName = file.getOriginalFilename();

        File newFile = new File();
        newFile.setFilename(fileName);
        newFile.setContenttype(file.getContentType());
        newFile.setFilesize(String.valueOf(file.getSize()));
        newFile.setFiledata(file.getBytes());

        newFile.setUserid(user.getUserId());

        return fileMapper.insertFile(newFile);
    }

    public int deleteFileById(int fileId) {
        return fileMapper.deleteFile(fileId);
    }

}
