package com.example.eelibrary.mapper.Impl;

import com.example.eelibrary.dto.AuthorRequest;
import com.example.eelibrary.dto.AuthorResponse;
import com.example.eelibrary.entity.Author;
import com.example.eelibrary.mapper.AuthorMapper;

public class AuthorMapperImpl implements AuthorMapper {
    @Override
    public Author toEntity(AuthorRequest authorRequest) {
        return Author.builder()
                .id(authorRequest.getId())
                .name(authorRequest.getName())
                .secondName(authorRequest.getSecondName())
                .build();
    }


    @Override
    public AuthorResponse toDtoResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .secondName(author.getSecondName())
                .books(author.getBooks())
                .build();
    }
}
