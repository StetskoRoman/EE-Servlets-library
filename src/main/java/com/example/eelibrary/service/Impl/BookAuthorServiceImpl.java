package com.example.eelibrary.service.Impl;

import com.example.eelibrary.entity.BookAuthor;
import com.example.eelibrary.repo.BookAuthorRepository;
import com.example.eelibrary.repo.Impl.BookAuthorRepoImpl;
import com.example.eelibrary.service.BookAuthorService;

import java.sql.SQLException;

public class BookAuthorServiceImpl implements BookAuthorService {
    BookAuthorRepository bookAuthorRepository = new BookAuthorRepoImpl();

    @Override
    public void save(BookAuthor bookAuthor) throws SQLException {
        bookAuthorRepository.save(bookAuthor);
    }

}
