package com.example.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.library.model.Admin;
import com.example.library.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ---------------- Registration ----------------
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "adminreg"; // templates/adminreg.html
    }

    @PostMapping("/register")
    public String registerAdmin(@ModelAttribute("admin") Admin admin,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {

        if (!admin.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "adminreg";
        }

        try {
            adminService.registerAdmin(admin);
            model.addAttribute("success", "Registration successful!");
            return "adminlogin"; // redirect to login page after registration
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "adminreg";
        }
    }

    // ---------------- Login ----------------
    @GetMapping("/login")
    public String showLoginForm() {
        return "adminlogin"; // templates/adminlogin.html
    }

    @PostMapping("/login")
    public String loginAdmin(@RequestParam("adminId") String adminId,
                             @RequestParam("password") String password,
                             Model model) {

        boolean isValid = adminService.validateAdmin(adminId, password);

        if (isValid) {
            return "admindash"; // templates/admindash.html
        } else {
            model.addAttribute("error", "Invalid Admin ID or Password!");
            return "adminlogin";
        }
    }
}
