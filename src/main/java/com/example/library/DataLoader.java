package com.example.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.library.model.Admin;
import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.repository.AdminRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() == 0) {
            Book b1 = new Book();
            b1.setTitle("Introduction to Algorithms");
            b1.setAuthor("Cormen et al.");
            b1.setIsbn("9780262033848");
            bookRepository.save(b1);

            Book b2 = new Book();
            b2.setTitle("Clean Code");
            b2.setAuthor("Robert C. Martin");
            b2.setIsbn("9780132350884");
            bookRepository.save(b2);
        }

        if (adminRepository.count() == 0) {
            Admin a = new Admin();
            a.setFullName("Default Admin");
            a.setEmail("admin@example.com");
            a.setAdminId("admin001");
            a.setPassword("adminpass");
            adminRepository.save(a);
        }

        if (userRepository.count() == 0) {
            User u = new User();
            u.setFullName("Student One");
            u.setEmail("student@example.com");
            u.setStudentId("stu001");
            u.setPassword("password");
            userRepository.save(u);
        }
    }

}
