package com.example.eelibrary.dto;

import com.example.eelibrary.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AuthorResponse {
    private Long id;
    private String name;
    private String secondName;
    private List<Book> books;
}
