package controller;

import com.example.eelibrary.controller.BookController;
import com.example.eelibrary.dto.BookRequest;
import com.example.eelibrary.dto.BookResponse;
import com.example.eelibrary.entity.Book;
import com.example.eelibrary.service.BookService;
import com.google.gson.Gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    @InjectMocks
    private BookController controller;

    @Mock
    private BookService bookService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    Book book;
    BookRequest bookRequest;
    BookResponse bookResponse;
    @Mock
    PrintWriter writer;
    StringWriter stringWriter;

    @BeforeEach
    public void init() {
        book = new Book(1L, "title", "description", null);
        bookRequest = new BookRequest(1L, "title", "description");
        bookResponse = new BookResponse(1L, "title", "description", null);
        stringWriter = new StringWriter();
    }

    @Test
    public void testDoGetWithValidId() throws IOException, SQLException {
        String id = "123";
        when(request.getParameter("id")).thenReturn(id);
        when(response.getWriter()).thenReturn(writer);
        when(bookService.getById(Long.valueOf(id))).thenReturn(bookResponse);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).print(new Gson().toJson(bookResponse));
        verify(response.getWriter()).flush();
    }

    @Test
    public void testDoGetWithNullId() throws Exception {
        when(request.getParameter("id")).thenReturn(null);

        List<BookResponse> bookResponses = Arrays.asList(bookResponse, bookResponse);
        when(bookService.getAll()).thenReturn(bookResponses);

        List<String> expectedList = Stream.of(bookResponse, bookResponse).map(bookResponse -> new Gson().toJson(bookResponse))
                .map(authorResponse -> new Gson().toJson(authorResponse))
                .toList();


        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(bookService, times(1)).getAll();
        assertEquals(expectedList.size(), bookResponses.size());
    }

    @Test
    public void testDoPostWithValidData() throws IOException, SQLException {
        String title = "Test Book";
        String description = "Test Description";
        String[] authorIds = {"1", "2", "3"};

        when(request.getParameter("title")).thenReturn(title);
        when(request.getParameter("description")).thenReturn(description);
        when(request.getParameterValues("author_id")).thenReturn(authorIds);

        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(bookService).save(any(BookRequest.class), anyList());
        verify(response.getWriter()).print("Успешно добавлено");
        verify(response.getWriter()).flush();
    }

    @Test
    public void testDoPostWithInvalidData() throws IOException {
        String title = null;
        String description = "Test Description";
        String[] authorIds = {"1", "2", "3"};

        when(request.getParameter("title")).thenReturn(title);
        when(request.getParameter("description")).thenReturn(description);
        when(request.getParameterValues("author_id")).thenReturn(authorIds);

        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response.getWriter()).print("Невалидные данные");
        verify(response.getWriter()).flush();
    }

    @Test
    public void testDoPutWithValidData() throws IOException, SQLException {
        String id = "1";
        String title = "Test Title";
        String description = "Test Description";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("id")).thenReturn(id);
        when(request.getParameter("title")).thenReturn(title);
        when(request.getParameter("description")).thenReturn(description);

        controller.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(bookService).update(any(BookRequest.class));
        verify(response.getWriter()).println("Успешно обновлено");
        verify(response.getWriter()).flush();
    }

    @Test
    public void testDoPutWithInvalidId() throws IOException {
        String id = "invalidId";
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("id")).thenReturn(id);

        controller.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response.getWriter()).println("Невалидный формат id");
    }

    @Test
    public void testDoDeleteWithValidId() throws IOException, SQLException {
        String id = "1";
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("id")).thenReturn(id);

        controller.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(bookService).delete(Long.valueOf(id));
        verify(response.getWriter()).println("Успешно удалено");
        verify(response.getWriter()).flush();
    }

    @Test
    public void testDoDeleteWithEmptyId() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("id")).thenReturn(null);

        controller.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response.getWriter()).println("Пустой параметр id");
    }

    @Test
    public void testDoDeleteWithInvalidIdFormat() throws IOException {
        String id = "invalidId";
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("id")).thenReturn(id);

        controller.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response.getWriter()).println("Невалидный формат id");
    }

}