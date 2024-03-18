package com.example.eelibrary.repo.Impl;

import com.example.eelibrary.entity.BookAuthor;
import com.example.eelibrary.repo.BookAuthorRepository;
import com.example.eelibrary.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookAuthorRepoImpl implements BookAuthorRepository {
    @Override
    public void save(BookAuthor bookAuthor) throws SQLException {
        String sql = "INSERT INTO book_author (book_id, author_id) " +
                "VALUES (?, ?)";
        try (Connection connection = DBUtil.connectDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bookAuthor.getBookId());
            statement.setLong(2, bookAuthor.getAuthorId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Автор с id " + bookAuthor.getAuthorId() + " не существует");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

    }

}
