package com.example.Aon.controller;

import com.example.Aon.entity.Book;
import com.example.Aon.entity.Borrower;
import com.example.Aon.repository.BookRepository;
import com.example.Aon.repository.BorrowerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/library")
public class LibraryController {

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/borrowers")
    public ResponseEntity<Borrower> registerBorrower(@RequestBody @Valid Borrower borrower) {
        Borrower savedBorrower = borrowerRepository.save(borrower);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBorrower);
    }

    @PostMapping("/books")
    public ResponseEntity<Book> registerBook(@RequestBody @Valid Book book) {
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(books);
    }

    @PostMapping("/borrowers/{borrower_id}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long borrower_id, @PathVariable Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        Optional<Borrower> borrower = borrowerRepository.findById(borrower_id);

        if(book.isPresent() && borrower.isPresent()) {
            Book selectedBook = book.get();
            if(selectedBook.isBorrowed()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book is already borrowed");
            }

            selectedBook.setBorrower(borrower.get());
            selectedBook.setBorrowed(true);
            bookRepository.save(selectedBook);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully borrowed book");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
    }


}
