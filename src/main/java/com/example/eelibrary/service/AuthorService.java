package com.example.eelibrary.service;

import com.example.eelibrary.dto.AuthorRequest;
import com.example.eelibrary.dto.AuthorResponse;

import java.sql.SQLException;
import java.util.List;

public interface AuthorService {
    AuthorResponse getById(Long id) throws SQLException;
    List<AuthorResponse> getAll() throws SQLException;
    void save(AuthorRequest authorRequest) throws SQLException;

    void update(AuthorRequest authorRequest) throws SQLException;

    void delete(Long id) throws SQLException;
}
