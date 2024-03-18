package com.example.eelibrary.repo.Impl;

import com.example.eelibrary.entity.Author;
import com.example.eelibrary.entity.Book;
import com.example.eelibrary.entity.BookAuthor;
import com.example.eelibrary.repo.BookRepository;
import com.example.eelibrary.service.BookAuthorService;
import com.example.eelibrary.service.Impl.BookAuthorServiceImpl;
import com.example.eelibrary.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookRepoImpl implements BookRepository {
    BookAuthorService bookAuthorService = new BookAuthorServiceImpl();

    @Override
    public void save(Book book, List<Long> authorIds) throws SQLException {
        if (authorIds.isEmpty()) {
            throw new SQLException("Добавьте хотя бы одного автора");
        }
        String sql = "INSERT INTO book (title, description) VALUES (?,?)";
        try (Connection connection = DBUtil.connectDB();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (Long authorId : authorIds) {
                if (!authorExists(connection, authorId)) {
                    throw new SQLException("Автора с id " + authorId + " не существует");
                }
            }

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getDescription());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Не удалось создать книгу");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getLong(1));
                for (Long authorId : authorIds) {
                    bookAuthorService.save(BookAuthor.builder().bookId(book.getId()).authorId(authorId).build());
                }
            } else {
                throw new SQLException("Не удалось создать книгу, идентификатор не получен");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }


    }

    @Override
    public Book findById(Long id) throws SQLException {
        Book book = null;
        String sql = "SELECT b.*, a.id AS author_id, a.name, a.second_name " +
                "FROM book b " +
                "JOIN book_author ba ON b.id = ba.book_id " +
                "JOIN author a ON ba.author_id = a.id " +
                "WHERE b.id = ?";
        try (Connection connection = DBUtil.connectDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (book == null) {
                    book = mapBook(resultSet);
                }
                if (resultSet.getLong("author_id") != 0) {
                    book.getAuthors().add(mapAuthor(resultSet));
                }
            }
            if (book == null) {
                throw new SQLException("Книга с id " + id + " не найдена");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return book;
    }

    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();

        String sql = "SELECT b.*, a.id AS author_id, a.name, a.second_name " +
                "FROM book b " +
                "JOIN book_author ba ON b.id = ba.book_id " +
                "JOIN author a ON ba.author_id = a.id " +
                "ORDER BY b.id";
        try (Connection connection = DBUtil.connectDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            Map<Long, Book> bookMap = new HashMap<>();
            while (resultSet.next()) {
                Long bookId = resultSet.getLong("id");
                Book book = bookMap.get(bookId);
                if (book == null) {
                    book = mapBook(resultSet);
                    books.add(book);
                    bookMap.put(bookId, book);
                }
                if (resultSet.getLong("author_id") != 0) {
                    book.getAuthors().add(mapAuthor(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        return books;
    }

    @Override
    public void update(Book book) throws SQLException {
        String sql = "UPDATE book SET title = ?, description = ? WHERE id = ?";
        try (Connection connection = DBUtil.connectDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getDescription());
            statement.setLong(3, book.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Книги с id " + book.getId() + " не существует");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM book WHERE id = ?";
        try (Connection connection = DBUtil.connectDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Книги с id " + id + " не существует");
            }


        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }


    }

    private Book mapBook(ResultSet resultSet) throws SQLException {
        return Book.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .authors(new ArrayList<>())
                .build();
    }

    private Author mapAuthor(ResultSet resultSet) throws SQLException {
        return Author.builder()
                .id(resultSet.getLong("author_id"))
                .name(resultSet.getString("name"))
                .secondName(resultSet.getString("second_name"))
                .build();
    }

    private boolean authorExists(Connection connection, Long authorId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM author WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, authorId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return false;
    }
}
