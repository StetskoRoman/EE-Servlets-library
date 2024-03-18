package com.example.eelibrary.service.Impl;

import com.example.eelibrary.dto.AuthorRequest;
import com.example.eelibrary.dto.AuthorResponse;
import com.example.eelibrary.entity.Author;
import com.example.eelibrary.mapper.AuthorMapper;
import com.example.eelibrary.mapper.Impl.AuthorMapperImpl;
import com.example.eelibrary.repo.AuthorRepository;
import com.example.eelibrary.repo.Impl.AuthorRepoImpl;
import com.example.eelibrary.service.AuthorService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorServiceImpl implements AuthorService {
    private AuthorRepository authorRepo = new AuthorRepoImpl();
    private AuthorMapper mapper = new AuthorMapperImpl();

    @Override
    public void save(AuthorRequest authorRequest) throws SQLException {
        authorRepo.save(mapper.toEntity(authorRequest));
    }

    @Override
    public AuthorResponse getById(Long id) throws SQLException {
        Author author = authorRepo.findById(id);
        return (author != null) ? mapper.toDtoResponse(author) : null;
    }

    @Override
    public List<AuthorResponse> getAll() throws SQLException {
        List<Author> authors = authorRepo.findAll();
        List<AuthorResponse> authorResponses = new ArrayList<>();
        for (Author author : authors) {
            authorResponses.add(mapper.toDtoResponse(author));
        }
        return authorResponses;
    }

    @Override
    public void update(AuthorRequest authorRequest) throws SQLException {
        authorRepo.update(mapper.toEntity(authorRequest));
    }

    @Override
    public void delete(Long id) throws SQLException {
        authorRepo.delete(id);
    }

}
