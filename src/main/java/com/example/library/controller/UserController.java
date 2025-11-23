package com.example.library.controller;

import java.io.IOException;

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
            return "redirect:/user/login";  // Not logged in
        }
        model.addAttribute("student", loggedInUser);
        return "studprofile";
    }

    // -------------------- Update Profile --------------------
    @PostMapping("/update")
public String updateProfile(@ModelAttribute("user") User user,
                            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
                            HttpSession session,
                            Model model) {
    // Get logged-in user from session
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        return "redirect:/user/login";
    }

    // Update fields
    loggedInUser.setFullName(user.getFullName());
    loggedInUser.setEmail(user.getEmail());
    loggedInUser.setPassword(user.getPassword()); // or encrypt if needed

    // Handle profile image if uploaded
    if (profileImageFile != null && !profileImageFile.isEmpty()) {
        try {
            // Save file to /images/ folder (or store path in DB)
            String filename = profileImageFile.getOriginalFilename();
            String filepath = "src/main/resources/static/images/" + filename;
            profileImageFile.transferTo(new java.io.File(filepath));
            loggedInUser.setProfileImage("/images/" + filename);
        } catch (IOException | IllegalStateException e) {
            // Log the error (optional: use a logger in production)
            System.err.println("Profile image upload failed: " + e.getMessage());
            // Show user-friendly error in the UI
            model.addAttribute("error", "Profile image upload failed. Please try again.");
        }
    }

    // Save updates via service
    userService.updateUser(loggedInUser);

    // Update session
    session.setAttribute("loggedInUser", loggedInUser);

    // Redirect to dashboard instead of profile page
    return "redirect:/studdash";
}


    // -------------------- Logout --------------------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}
