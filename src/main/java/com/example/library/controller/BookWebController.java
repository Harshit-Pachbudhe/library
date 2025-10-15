package com.example.library.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.library.model.Book;
import com.example.library.service.BookService;

@Controller
@RequestMapping("/web/books")
public class BookWebController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public String listBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "managebooks"; // student-facing list
    }

    @GetMapping("/admin")
    public String adminList(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "adminmngbook"; // admin management page
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        return "adminmngbook";
    }

    @PostMapping
    public String saveBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/web/books/admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Book> opt = bookService.getBookById(id);
        model.addAttribute("book", opt.orElseGet(Book::new));
        return "adminmngbook";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/web/books/admin";
    }

}
