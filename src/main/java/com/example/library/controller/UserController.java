package com.example.library.controller;


import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.library.model.User;
import com.example.library.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // -------------------- Registration --------------------
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "studreg";  // Loads studreg.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user);
            model.addAttribute("success", "Registration successful! You can now log in.");
            return "studlogin";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "studreg";
        }
    }

    // -------------------- Login --------------------
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "studlogin";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user, HttpSession session, Model model) {
        String email = user.getEmail().trim();
        String password = user.getPassword().trim();

        User loggedInUser = userService.authenticate(email, password);
        if (loggedInUser != null) {
            session.setAttribute("loggedInUser", loggedInUser);  // Store user in session
            return "redirect:/user/profile";  // Redirect to profile page
        } else {
            model.addAttribute("error", "Invalid email or password!");
            model.addAttribute("user", new User());
            return "studlogin";
        }
    }

    // -------------------- Profile --------------------
   @GetMapping("/profile")
public String viewProfile(HttpSession session, Model model) {
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        return "redirect:/user/login";
    }
    model.addAttribute("user", loggedInUser); // 🔥 FIX
    return "studprofile";
}

    // -------------------- Update Profile --------------------
  @PostMapping("/update")
public String updateProfile(@ModelAttribute("user") User user,
                            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
                            HttpSession session,
                            Model model) {

    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        return "redirect:/user/login";
    }

    // 1️⃣ Update text fields
    loggedInUser.setFullName(user.getFullName());
    loggedInUser.setEmail(user.getEmail());
    loggedInUser.setPassword(user.getPassword());

    // 2️⃣ PROFILE IMAGE UPDATE
    if (profileImageFile != null && !profileImageFile.isEmpty()) {
        try {
            String projectRoot = System.getProperty("user.dir");
            String uploadDir = projectRoot + "/uploads/profile/";

            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();

            // 🔥 Delete old image
            String oldImage = loggedInUser.getProfileImage();
            if (oldImage != null && oldImage.startsWith("/uploads/")) {
                File oldFile = new File(projectRoot + oldImage.replace("/", File.separator));
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            // 🔥 Save new image with unique name
            String originalName = profileImageFile.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));
            String filename = "profile_" + loggedInUser.getId() + "_" + System.currentTimeMillis() + extension;

            File newFile = new File(uploadDir + filename);
            profileImageFile.transferTo(newFile);

            loggedInUser.setProfileImage("/uploads/profile/" + filename);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Image upload failed");
        }
    }

    // 3️⃣ Save user
    userService.updateUser(loggedInUser);

    // 4️⃣ Update session
    session.setAttribute("loggedInUser", loggedInUser);

    // 5️⃣ Redirect
    return "redirect:/studdash";
}


    // -------------------- Logout --------------------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}
