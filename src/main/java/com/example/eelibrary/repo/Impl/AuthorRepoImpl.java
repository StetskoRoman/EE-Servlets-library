package com.example.eelibrary.repo.Impl;

import com.example.eelibrary.entity.Author;
import com.example.eelibrary.entity.Book;
import com.example.eelibrary.repo.AuthorRepository;
import com.example.eelibrary.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eelibrary.utils.DBUtil.*;

public class AuthorRepoImpl implements AuthorRepository {

    @Override
    public void save(Author author) throws SQLException {
        String sql = "INSERT INTO author (name, second_name) VALUES (?, ?)";

//        Connection connection = DBUtil.connectDB();

//        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        try (Connection connection = DBUtil.connectDB();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, author.getName());
            statement.setString(2, author.getSecondName());


            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Не удалось создать автора");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public Author findById(Long id) throws SQLException {
        Author author = null;
        String sql = "SELECT a.*, b.id AS book_id, b.title, b.description " +
                "FROM author a " +
                "JOIN book_author ba ON a.id = ba.author_id " +
                "JOIN book b ON ba.book_id = b.id " +
                "WHERE a.id = ?";
        try (Connection connection = DBUtil.connectDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (author == null) {
                    author = mapAuthor(resultSet);
                }
                if (resultSet.getLong("book_id") != 0) {
                    author.getBooks().add(mapBook(resultSet));
                }
            }
            if (author == null) {
                throw new SQLException("Не удалось найти автора с id " + id);
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        return author;
    }

    @Override
    public List<Author> findAll() throws SQLException {
        List<Author> authors = new ArrayList<>();

        String sql = "SELECT a.*, b.id AS book_id, b.title, b.description " +
                "FROM author a " +
                "LEFT JOIN book_author ba ON a.id = ba.author_id " +
                "LEFT JOIN book b ON ba.book_id = b.id " +
                "ORDER BY a.id, b.id";
        try (Connection connection = DBUtil.connectDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            Map<Long, Author> authorMap = new HashMap<>();
            while (resultSet.next()) {
                Long authorId = resultSet.getLong("id");
                Author author = authorMap.get(authorId);
                if (author == null) {
                    author = mapAuthor(resultSet);
                    authors.add(author);
                    authorMap.put(authorId, author);
                }
                if (resultSet.getLong("book_id") != 0) {
                    author.getBooks().add(mapBook(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());

        }
        return authors;
    }

    @Override
    public void update(Author author) throws SQLException {
        String sql = "UPDATE author SET name = ?, second_name = ? WHERE id = ?";
        try (Connection connection = DBUtil.connectDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, author.getName());
            statement.setString(2, author.getSecondName());
            statement.setLong(3, author.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Автора с id " + author.getId() + " не существует");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }


    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM author WHERE id = ?";
        try (Connection connection = DBUtil.connectDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Автора с id " + id + " не существует");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    private Author mapAuthor(ResultSet resultSet) throws SQLException {
        return Author.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .secondName(resultSet.getString("second_name"))
                .books(new ArrayList<>())
                .build();
    }

    private Book mapBook(ResultSet resultSet) throws SQLException {
        return Book.builder()
                .id(resultSet.getLong("book_id"))
                .title(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .build();
    }

}
