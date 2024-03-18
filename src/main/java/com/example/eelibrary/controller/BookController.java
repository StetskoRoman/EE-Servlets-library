package com.example.eelibrary.controller;

import com.example.eelibrary.dto.BookRequest;
import com.example.eelibrary.service.BookService;
import com.example.eelibrary.service.Impl.BookServiceImpl;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/book")
public class BookController extends HttpServlet {
    private BookService bookService = new BookServiceImpl();



    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = initResp(resp, HttpServletResponse.SC_OK).getWriter();
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String[] authorIds = req.getParameterValues("author_id");

        List<Long> ids = new ArrayList<>();
        if (authorIds != null) {
            for (String authorId : authorIds) {
                try {
                    Long id = Long.parseLong(authorId);
                    ids.add(id);
                } catch (NumberFormatException e) {
                    initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println("Невалидный параметр id");
                    return;
                }
            }
        }

        if (isValid(title) && isValid(description)) {
            BookRequest bookRequest = BookRequest.builder().title(title).description(description).build();

            out = initResp(resp, HttpServletResponse.SC_CREATED).getWriter();
            try {
                bookService.save(bookRequest, ids);
                out.print("Успешно добавлено");
            } catch (SQLException e) {
                initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
                out.print(e.getMessage());
            }
        } else {
            initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
            out.print("Невалидные данные");
        }
        out.flush();

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = initResp(resp, HttpServletResponse.SC_OK).getWriter();
        String id = req.getParameter("id");
        if (id != null) {
            if (!isValid(id)) {
                initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Пустой параметр");
                return;
            }
            try {
                Long.parseLong(id);
            } catch (NumberFormatException e) {
                initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Невалидный формат id");
                return;
            }
            try {
                out.print(new Gson().toJson(bookService.getById(Long.valueOf((req.getParameter("id"))))));
            } catch (SQLException e) {
                initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
                out.print(e.getMessage());
            }
        } else {
            try {
                out.print(bookService.getAll().stream().map(bookResponse -> new Gson().toJson(bookResponse)).toList());
            } catch (SQLException e) {
                initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
                out.print(e.getMessage());
            }
        }
        out.flush();
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = initResp(resp, HttpServletResponse.SC_OK).getWriter();

        String id = req.getParameter("id");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Невалидный формат id");
            return;
        }
        if (isValid(id) && isValid(title) && isValid(description)) {
            BookRequest bookRequest = BookRequest.builder().id(Long.valueOf(id)).title(title).description(description).build();
            try {
                bookService.update(bookRequest);
                out.println("Успешно обновлено");
            } catch (SQLException e) {
                initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
                out.print(e.getMessage());
            }

        } else {
            initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
            out.print("Невалидные данные");
        }
        out.flush();

    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = initResp(resp, HttpServletResponse.SC_OK).getWriter();
        String id = req.getParameter("id");
        if (!isValid(id)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Пустой параметр id");
            return;
        }
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Невалидный формат id");
            return;
        }
        try {
            bookService.delete(Long.valueOf(id));
            out.println("Успешно удалено");
        } catch (SQLException e) {
            initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
            out.print(e.getMessage());
        }
        out.flush();


    }

    private HttpServletResponse initResp(HttpServletResponse resp, int status) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);
        return resp;
    }

    private boolean isValid(String parameter) {
        return parameter != null && !parameter.isEmpty();
    }
}

