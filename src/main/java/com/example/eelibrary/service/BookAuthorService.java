package com.example.eelibrary.service;

import com.example.eelibrary.entity.BookAuthor;

import java.sql.SQLException;

public interface BookAuthorService {
    void save(BookAuthor bookAuthor) throws SQLException;

}

