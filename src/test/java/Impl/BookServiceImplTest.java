package Impl;

import com.example.eelibrary.service.Impl.BookServiceImpl;
import com.example.eelibrary.dto.BookRequest;
import com.example.eelibrary.dto.BookResponse;
import com.example.eelibrary.entity.Book;
import com.example.eelibrary.mapper.BookMapper;
import com.example.eelibrary.repo.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    BookRepository bookRepository;
    @Mock
    BookMapper mapper;

    @InjectMocks
    BookServiceImpl bookService;

    Book book;
    BookRequest bookRequest;
    BookResponse bookResponse;

    @BeforeEach
    void setUp() {
        book = new Book(1L, "Book Title", "Book Author", null);
        bookRequest = new BookRequest();
        bookResponse = new BookResponse();
    }

    @Test
    void save() throws SQLException {
        doNothing().when(bookRepository).save(book, new ArrayList<>());
        when(mapper.toEntity(bookRequest)).thenReturn(book);

        bookService.save(bookRequest, new ArrayList<>());

        verify(bookRepository).save(book, new ArrayList<>());
    }

    @Test
    void getById() throws SQLException {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(book);
        when(mapper.toDtoResponse(book)).thenReturn(bookResponse);

        BookResponse response = bookService.getById(id);

        verify(bookRepository, times(1)).findById(id);
        verify(mapper, times(1)).toDtoResponse(book);
        assertNotNull(response);
    }

    @Test
    void getAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        books.add(book);
        books.add(book);

        List<BookResponse> expectedResponses = new ArrayList<>();
        expectedResponses.add(bookResponse);
        expectedResponses.add(bookResponse);

        when(bookRepository.findAll()).thenReturn(books);
        when(mapper.toDtoResponse(book)).thenReturn(bookResponse);

        List<BookResponse> actualResponses = bookService.getAll();

        assertEquals(expectedResponses.size(), actualResponses.size());
        for (int i = 0; i < expectedResponses.size(); i++) {
            assertEquals(expectedResponses.get(i), actualResponses.get(i));
        }

        verify(mapper, times(books.size())).toDtoResponse(any(Book.class));
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void update() throws SQLException {
        doNothing().when(bookRepository).update(book);

        when(mapper.toEntity(bookRequest)).thenReturn(book);

        bookService.update(bookRequest);

        verify(bookRepository, times(1)).update(book);
    }

    @Test
    void delete() throws SQLException {
        Long id = 1L;
        doNothing().when(bookRepository).delete(id);

        bookService.delete(id);

        verify(bookRepository, times(1)).delete(id);
    }
}