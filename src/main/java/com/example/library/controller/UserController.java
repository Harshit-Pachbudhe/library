package com.example.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.library.model.User;
import com.example.library.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // -------------------- Registration --------------------
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "studreg";  // Loads templates/studreg.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user);
            model.addAttribute("success", "Registration successful! You can now log in.");
            return "studlogin"; // Redirect to login page after registration
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "studreg"; // Return to form with error
        }
    }

    // -------------------- Login --------------------
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "studlogin"; // Loads templates/studlogin.html
    }

   @PostMapping("/login")
public String loginUser(@ModelAttribute("user") User user, Model model) {
    String email = user.getEmail().trim();
    String password = user.getPassword().trim();

    User loggedInUser = userService.authenticate(email, password);

    if (loggedInUser != null) {
        model.addAttribute("user", loggedInUser);
        return "studdash";
    } else {
        model.addAttribute("error", "Invalid email or password!");
        model.addAttribute("user", new User()); // Reset the form
        return "studlogin";
    }
}

}
