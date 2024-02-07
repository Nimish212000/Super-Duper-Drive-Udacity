package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;


    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating CredentialService bean");
    }

    public List<Credentials> getAllUserCredentials(Integer userid) {
        List<Credentials> credentialsList = credentialMapper.getCredentialsByCredentialid(userid);
        for (Credentials credential : credentialsList) {
            String decryptedPassword = decryptPassword(credential);
            credential.setDecryptedPassword(decryptedPassword);
        }
        return credentialsList;
    }

    public Credentials getCredentialByUserId(Integer userid) {
        return credentialMapper.getCredentialById(userid);
    }


    public int addCredential(Credentials credentials, User user) {
        String key = generateRandomKey();
        credentials.setKey(key);

        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), key);
        credentials.setPassword(encryptedPassword);
        credentials.setUserId(user.getUserId());

        int newCredentialId = credentialMapper.addCredentials(credentials);
        credentials.setCredentialid(newCredentialId);

        return newCredentialId;
    }


    public int editCredential(Credentials credentials) {
        Credentials existingCredential = credentialMapper.getCredentialById(credentials.getCredentialid());
        credentials.setKey(existingCredential.getKey());

        if (!credentials.getPassword().equals(existingCredential.getPassword())) {
            String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), existingCredential.getKey());
            credentials.setPassword(encryptedPassword);
        } else {
            credentials.setPassword(existingCredential.getPassword());
        }

        return credentialMapper.editCredentials(credentials);
    }


    public void deleteCredential(Integer credentialid) {
        credentialMapper.deleteCredentials(credentialid);
    }

    public String decryptPassword(Credentials credentials) {
        String key = credentials.getKey();
        String encryptedPassword = credentials.getPassword();
        return encryptionService.decryptValue(encryptedPassword, key);
    }


    private String generateRandomKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[16];
        random.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }


}
