package com.example.eelibrary.repo;

import com.example.eelibrary.entity.Book;

import java.sql.SQLException;
import java.util.List;

public interface BookRepository {
    void save(Book book, List<Long> authorIds) throws SQLException;

    Book findById(Long id) throws SQLException;

    List<Book> findAll() throws SQLException;

    void update(Book book) throws SQLException;

    void delete(Long id) throws SQLException;

}
