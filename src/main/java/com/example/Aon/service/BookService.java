package com.example.Aon.service;

import com.example.Aon.entity.Book;
import com.example.Aon.entity.Borrower;
import com.example.Aon.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book registerBook(Book book){
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book borrowBook(Long id, Borrower borrower) throws Exception{
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new Exception("Book not found"));
        if (book.isBorrowed()){
            throw new Exception("Book is already borrowed");
        }
        book.setBorrowed(true);
        book.setBorrower(borrower);
        return bookRepository.save(book);
    }
}
