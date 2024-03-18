package Impl;

import com.example.eelibrary.service.Impl.AuthorServiceImpl;
import com.example.eelibrary.dto.AuthorRequest;
import com.example.eelibrary.dto.AuthorResponse;
import com.example.eelibrary.entity.Author;
import com.example.eelibrary.mapper.AuthorMapper;
import com.example.eelibrary.repo.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    AuthorRepository authorRepository;
    @Mock
    AuthorMapper mapper;
    @InjectMocks
    AuthorServiceImpl authorService;

    Author author;
    AuthorRequest authorRequest;
    AuthorResponse authorResponse;

    @BeforeEach
    public void init() {
        author = new Author(1L, "John", "Doe", null);
        authorRequest = new AuthorRequest(1L, "John", "Doe");
        authorResponse = new AuthorResponse(1L, "John", "Doe", null);
    }

    @Test
    public void testSaveAuthor() throws SQLException {
        doNothing().when(authorRepository).save(author);
        when(mapper.toEntity(authorRequest)).thenReturn(author);

        authorService.save(authorRequest);

        verify(authorRepository).save(author);
    }

    @Test
    public void save_shouldThrowException() throws SQLException {
        assertThrows(Exception.class, () -> doThrow().when(authorRepository).save(author));
        authorService.save(authorRequest);
    }

    @Test
    public void testGetById() throws SQLException {
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(author);
        when(mapper.toDtoResponse(author)).thenReturn(authorResponse);

        AuthorResponse response = authorService.getById(id);

        verify(authorRepository, times(1)).findById(id);
        verify(mapper, times(1)).toDtoResponse(author);
        assertNotNull(response);
    }

    @Test
    void getAll() throws SQLException {
        List<Author> authors = new ArrayList<>();
        authors.add(author);
        authors.add(author);

        List<AuthorResponse> expectedResponses = new ArrayList<>();
        expectedResponses.add(authorResponse);
        expectedResponses.add(authorResponse);

        when(authorRepository.findAll()).thenReturn(authors);
        when(mapper.toDtoResponse(author)).thenReturn(authorResponse);

        List<AuthorResponse> actualResponses = authorService.getAll();

        assertEquals(expectedResponses.size(), actualResponses.size());
        for (int i = 0; i < expectedResponses.size(); i++) {
            assertEquals(expectedResponses.get(i), actualResponses.get(i));
        }

        verify(mapper, times(authors.size())).toDtoResponse(any(Author.class));
    }

    @Test
    public void update() throws SQLException {
        doNothing().when(authorRepository).update(author);

        when(mapper.toEntity(authorRequest)).thenReturn(author);

        authorService.update(authorRequest);

        verify(authorRepository, times(1)).update(author);
    }

    @Test
    void delete() throws SQLException {
        Long id = 1L;
        doNothing().when(authorRepository).delete(id);

        authorService.delete(id);

        verify(authorRepository, times(1)).delete(id);
    }
}