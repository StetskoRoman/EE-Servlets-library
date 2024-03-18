package com.example.eelibrary.mapper;

import com.example.eelibrary.dto.BookRequest;
import com.example.eelibrary.dto.BookResponse;
import com.example.eelibrary.entity.Book;

public interface BookMapper {
    Book toEntity(BookRequest bookDto);

    BookResponse toDtoResponse(Book book);

}
