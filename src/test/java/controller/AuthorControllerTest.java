package controller;

import com.example.eelibrary.controller.AuthorController;
import com.example.eelibrary.dto.AuthorRequest;
import com.example.eelibrary.dto.AuthorResponse;
import com.example.eelibrary.entity.Author;
import com.example.eelibrary.service.AuthorService;
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
class AuthorControllerTest {

    @InjectMocks
    private AuthorController controller;

    @Mock
    private AuthorService authorService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    Author author;
    AuthorRequest authorRequest;
    AuthorResponse authorResponse;
    @Mock
    PrintWriter writer;
    StringWriter stringWriter;

    @BeforeEach
    public void init() {
        author = new Author(1L, "John", "Doe", null);
        authorRequest = new AuthorRequest(1L, "John", "Doe");
        authorResponse = new AuthorResponse(1L, "John", "Doe", null);
        stringWriter = new StringWriter();
    }

    @Test
    public void testDoGetWithId() throws Exception {
        when(request.getParameter("id")).thenReturn("1");

        when(authorService.getById(1L)).thenReturn(authorResponse);

        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoGetWithNullId() throws Exception {
        when(request.getParameter("id")).thenReturn(null);

        List<AuthorResponse> authorResponses = Arrays.asList(authorResponse, authorResponse);
        when(authorService.getAll()).thenReturn(authorResponses);

        List<String> expectedList = Stream.of(authorResponse, authorResponse)
                .map(authorResponse -> new Gson().toJson(authorResponse))
                .toList();


        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(authorService, times(1)).getAll();
        assertEquals(expectedList.size(), authorResponses.size());
    }

    @Test
    public void testDoPost() throws IOException, SQLException {
        when(request.getParameter("name")).thenReturn("John");
        when(request.getParameter("second_name")).thenReturn("Doe");
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(authorService, times(1)).save(AuthorRequest.builder()
                .name("John")
                .secondName("Doe").build());
        verify(writer).print("Успешно добавлено");
        verify(writer).flush();

    }

    @Test
    public void testDoPost_InvalidData() throws IOException {
        when(request.getParameter("name")).thenReturn("John");
        when(request.getParameter("second_name")).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).print("Невалидные данные");
        verify(writer).flush();
    }

    @Test
    public void testDoPut() throws IOException {
        when(request.getParameter("id")).thenReturn("123");
        when(request.getParameter("name")).thenReturn("John");
        when(request.getParameter("second_name")).thenReturn("Doe");
        when(response.getWriter()).thenReturn(writer);

        controller.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).print("Успешно обновлено");
        verify(writer).flush();
    }

    @Test
    public void testDoPut_InvalidIdFormat() throws IOException {
        when(request.getParameter("id")).thenReturn("invalid_id");
        when(response.getWriter()).thenReturn(writer);

        controller.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).println("Невалидный формат id");
    }
    @Test
    public void testDoDeleteWithValidId() throws SQLException, IOException {
        String id = "123";
        when(request.getParameter("id")).thenReturn(id);
        when(response.getWriter()).thenReturn(writer);

        controller.doDelete(request, response);

        verify(response.getWriter()).println("Успешно удалено");
        verify(authorService).delete(Long.valueOf(id));
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(authorService).delete(Long.valueOf(id));
    }

    @Test
    public void testDoDeleteWithEmptyId() throws IOException, SQLException {
        when(request.getParameter("id")).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);

        controller.doDelete(request, response);

        verify(response.getWriter()).println("Пустой параметр id");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(authorService, never()).delete(anyLong());
    }

    @Test
    public void testDoDeleteWithInvalidIdFormat() throws IOException, SQLException {
        String invalidId = "abc";
        when(request.getParameter("id")).thenReturn(invalidId);
        when(response.getWriter()).thenReturn(writer);

        controller.doDelete(request, response);

        verify(response.getWriter()).println("Невалидный формат id");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(authorService, never()).delete(anyLong());
    }
    
}
