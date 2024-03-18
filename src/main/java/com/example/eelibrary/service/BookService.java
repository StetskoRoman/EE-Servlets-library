package com.example.eelibrary.service;

import com.example.eelibrary.dto.BookRequest;
import com.example.eelibrary.dto.BookResponse;

import java.sql.SQLException;
import java.util.List;

public interface BookService {

    BookResponse getById(Long id) throws SQLException;
    void save(BookRequest bookRequest, List<Long> authorIds) throws SQLException;

    List<BookResponse> getAll() throws SQLException;

    void update(BookRequest bookRequest) throws SQLException;

    void delete(Long id) throws SQLException;
}
