package com.example.eelibrary.service.Impl;

import com.example.eelibrary.dto.BookRequest;
import com.example.eelibrary.dto.BookResponse;
import com.example.eelibrary.entity.Book;
import com.example.eelibrary.mapper.BookMapper;
import com.example.eelibrary.mapper.Impl.BookMapperImpl;
import com.example.eelibrary.repo.BookRepository;
import com.example.eelibrary.repo.Impl.BookRepoImpl;
import com.example.eelibrary.service.BookService;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {
    private BookRepository bookRepo = new BookRepoImpl();
    private BookMapper mapper = new BookMapperImpl();

    @Override
    public void save(BookRequest bookRequest, List<Long> authorIds) throws SQLException {
        bookRepo.save(mapper.toEntity(bookRequest), authorIds);
    }

    @Override
    public BookResponse getById(Long id) throws SQLException {
        Book book = bookRepo.findById(id);
        return mapper.toDtoResponse(book);
    }

    @Override
    public List<BookResponse> getAll() throws SQLException {
        List<Book> books = bookRepo.findAll();
        return books.stream()
                .map(mapper::toDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void update(BookRequest bookRequest) throws SQLException {
        bookRepo.update(mapper.toEntity(bookRequest));
    }

    @Override
    public void delete(Long id) throws SQLException {
        bookRepo.delete(id);

    }
}
