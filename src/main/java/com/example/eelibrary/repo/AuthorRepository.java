package com.example.eelibrary.repo;

import com.example.eelibrary.entity.Author;

import java.sql.SQLException;
import java.util.List;

public interface AuthorRepository {
    void save(Author author) throws SQLException;
    Author findById(Long id) throws SQLException;
    List<Author> findAll() throws SQLException;
    void update(Author author) throws SQLException;
    void delete(Long id) throws SQLException;
}
