package com.example.eelibrary.repo;

import com.example.eelibrary.entity.BookAuthor;

import java.sql.SQLException;

public interface BookAuthorRepository {
    void save(BookAuthor bookAuthor) throws SQLException;

}

