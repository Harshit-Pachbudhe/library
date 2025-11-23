package com.example.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.service.BookService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @Autowired
    private BookService bookService;

    // --- Existing mappings ---
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

    // --------------------------
    // Student Dashboard Mapping
    // --------------------------
   @GetMapping("/studdash")
    public String studentDashboard(HttpSession session, Model model) {
        // 1. Get logged-in user from session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }

        // 2. Load all books
        List<Book> books = bookService.getAllBooks();

        // 3. Pass to model
        model.addAttribute("user", loggedInUser);
        model.addAttribute("books", books);

        // 4. Dynamically compute department slug for each book (for CSS)
        books.forEach(book -> {
            if (book.getDepartment() != null) {
                String deptSlug = book.getDepartment().replaceAll("&", "_").replaceAll("\\s+", "_");
                book.setDepartment(deptSlug); // overwrite for easier use in Thymeleaf class
            } else {
                book.setDepartment("DEFAULT");
            }
        });

        return "studdash";
    }
    // --------------------------
    // Other student pages
    // --------------------------
    @GetMapping("/studsearch")
    public String studentSearch(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/user/login";

        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("session", session);
        return "studsearch";
    }

    @GetMapping("/studreserve")
    public String studentReserve(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/user/login";

        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("student", loggedInUser);
        model.addAttribute("session", session);
        return "studreserve";
    }

    @GetMapping("/studissue-return")
    public String studentIssueReturn(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/user/login";

        return "studissue-return";
    }

   @GetMapping("/studprofile")
    public String studentProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", loggedInUser);
        return "studprofile";
    }

    @GetMapping("/studnotif")
    public String studentNotif(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/user/login";

        return "studnotif";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
