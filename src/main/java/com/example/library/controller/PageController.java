package com.example.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.service.BookService;

@Controller
public class PageController {

    @Autowired
    private BookService bookService;

    @GetMapping({"/","/index"})
    public String index() { return "index"; }

    @GetMapping("/info")
    public String info() { return "info"; }

    @GetMapping("/help")
    public String help() { return "help"; }

    @GetMapping("/librarian")
    public String librarian() { return "librarian"; }

    @GetMapping("/login")
    public String login() { return "login"; }

    // --- Student & Admin handled by their own controllers ---
    @GetMapping("/studdash")
    public String studentDashboard() { return "studdash"; }

    @GetMapping("/studsearch")
    public String studentSearch(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "studsearch";
    }

    @GetMapping("/studreserve")
    public String studentReserve(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("student", new User());
        return "studreserve";
    }

    @GetMapping("/studissue-return")
    public String studentIssueReturn() { return "studissue-return"; }

    @GetMapping("/studprofile")
    public String studentProfile(Model model) {
        model.addAttribute("student", new User());
        return "studprofile";
    }

    @GetMapping("/studnotif")
    public String studentNotif() { return "studnotif"; }

    @GetMapping("/logout")
    public String logout() { return "redirect:/"; }
}
