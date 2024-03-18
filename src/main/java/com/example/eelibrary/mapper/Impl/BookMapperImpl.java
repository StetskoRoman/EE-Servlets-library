package com.example.eelibrary.mapper.Impl;

import com.example.eelibrary.dto.BookRequest;
import com.example.eelibrary.dto.BookResponse;
import com.example.eelibrary.entity.Book;
import com.example.eelibrary.mapper.BookMapper;

public class BookMapperImpl implements BookMapper {
    @Override
    public Book toEntity(BookRequest bookRequest) {
        return Book.builder()
                .id(bookRequest.getId())
                .title(bookRequest.getTitle())
                .description(bookRequest.getDescription())
                .build();
    }


    @Override
    public BookResponse toDtoResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .authors(book.getAuthors())
                .build();
    }
}
