package com.example.library.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final String UPLOAD_DIR = "src/main/resources/static/images/profiles/";

    // -------------------- Register --------------------
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }
        if (userRepository.existsByStudentId(user.getStudentId())) {
            throw new RuntimeException("Student ID already registered!");
        }
        return userRepository.save(user);
    }

    // -------------------- Authenticate --------------------
    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) return null;
        return user.getPassword().equals(password) ? user : null;
    }

    // -------------------- Update Profile --------------------
    public User updateUserProfile(Long userId, User updatedUser, MultipartFile profileImageFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(updatedUser.getFullName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setDepartment(updatedUser.getDepartment());
        user.setYear(updatedUser.getYear());
        user.setPassword(updatedUser.getPassword());

		

        // Handle profile image
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            try {
                Files.createDirectories(Paths.get(UPLOAD_DIR));
                String filename = user.getId() + "_" + profileImageFile.getOriginalFilename();
                String filepath = UPLOAD_DIR + filename;
                profileImageFile.transferTo(new File(filepath));
                user.setProfileImage("/images/profiles/" + filename); // Web-accessible path
            } catch (IOException e) {
                throw new RuntimeException("Failed to save profile image: " + e.getMessage());
            }
        }
        return userRepository.save(user); // Optionally, return the updated user here or just 'user'
    }

    // -------------------- Update User --------------------
    public User updateUser(User user) {
        return userRepository.save(user);
    }

}
