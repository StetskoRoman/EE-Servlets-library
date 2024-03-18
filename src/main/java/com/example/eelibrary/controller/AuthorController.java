package com.example.eelibrary.controller;

import com.example.eelibrary.dto.AuthorRequest;
import com.example.eelibrary.service.AuthorService;
import com.example.eelibrary.service.Impl.AuthorServiceImpl;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/author")
public class AuthorController extends HttpServlet {
    private AuthorService authorService = new AuthorServiceImpl();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = initResp(resp, HttpServletResponse.SC_CREATED).getWriter();
        String name = req.getParameter("name");
        String secondName = req.getParameter("second_name");

        if (isValid(name) && isValid(secondName)) {
            AuthorRequest authorRequest = AuthorRequest.builder().name(name).secondName(secondName).build();
            try {
                authorService.save(authorRequest);
                out.print("Успешно добавлено");
            } catch (SQLException e) {
                out = initResp(resp, HttpServletResponse.SC_BAD_REQUEST).getWriter();
                out.print(e.getMessage());
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("Невалидные данные");
        }
        out.flush();
    }
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = initResp(resp, HttpServletResponse.SC_OK).getWriter();
        String id = req.getParameter("id");

        Long idNew = null;
        if (id != null) {
            if (!isValid(id)) {
                initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Пустой параметр");
                return;
            }
            try {
                idNew = Long.parseLong(id);
            } catch (NumberFormatException e) {
                initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Невалидный формат id");
                return;
            }
            try {
                out.print(new Gson().toJson(authorService.getById(idNew)));
            } catch (SQLException e) {
                initResp(resp, HttpServletResponse.SC_BAD_REQUEST).getWriter();
                out.print(e.getMessage());
            }
        } else {
            try {
                out.print(authorService.getAll().stream().map(authorResponse -> new Gson().toJson(authorResponse)).toList());
            } catch (SQLException e) {
                initResp(resp, HttpServletResponse.SC_NO_CONTENT).getWriter();
                out.print(e.getMessage());
            }
        }
        out.flush();
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = initResp(resp, HttpServletResponse.SC_OK).getWriter();

        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String secondName = req.getParameter("second_name");
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Невалидный формат id");
            return;
        }
        if (isValid(id) && isValid(name) && isValid(secondName)) {
            AuthorRequest authorRequest = AuthorRequest.builder().id(Long.valueOf(id)).name(name).secondName(secondName).build();
            try {
                authorService.update(authorRequest);
                out.print("Успешно обновлено");
            } catch (SQLException e) {
                out = initResp(resp, HttpServletResponse.SC_BAD_REQUEST).getWriter();
                out.print(e.getMessage());
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Невалидные данные");
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
            authorService.delete(Long.valueOf(id));
            out.println("Успешно удалено");
        } catch (SQLException e) {
            out = initResp(resp, HttpServletResponse.SC_BAD_REQUEST).getWriter();
            out.print(e.getMessage());

        }
        out.flush();
    }

    private static HttpServletResponse initResp(HttpServletResponse resp, int status) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);
        return resp;
    }

    private static boolean isValid(String parameter) {
        return parameter != null && !parameter.isEmpty();
    }
}

