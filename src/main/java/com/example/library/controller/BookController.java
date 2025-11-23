package com.example.library.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.library.model.Book;
import com.example.library.service.BookService;




@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;


    // If you still want REST endpoints, move them to a separate RestController.

    @GetMapping("/someMapping")
    public String getBookPage(@RequestParam Long id, Model model) {
        Optional<Book> bookOpt = bookService.getBookById(id);
        if (bookOpt.isPresent()) {
            model.addAttribute("book", bookOpt.get());
            return "someView";
        } else {
            model.addAttribute("error", "Book not found");
            return "errorView"; // Or use a generic error page
        }
    }

}


   