package com.example.eelibrary.mapper;

import com.example.eelibrary.dto.AuthorRequest;
import com.example.eelibrary.dto.AuthorResponse;
import com.example.eelibrary.entity.Author;

public interface AuthorMapper {
    Author toEntity(AuthorRequest authorRequest);

    AuthorResponse toDtoResponse(Author author);
}
