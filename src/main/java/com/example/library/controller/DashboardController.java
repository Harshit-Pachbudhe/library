package com.example.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.library.repository.AdminRepository;

@Controller
public class DashboardController {

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("admins", adminRepository.findAll());
        return "admindash"; // Thymeleaf template
    }
}
