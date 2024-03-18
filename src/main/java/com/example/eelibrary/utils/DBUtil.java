package com.example.eelibrary.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    public static String JDBC_URL = "jdbc:postgresql://localhost:5432/eeLib";
    public static String JDBC_USER = "postgres";
    public static String JDBC_PASSWORD = "Kamikadze11";
    public static String JDBC_DRIVER = "org.postgresql.Driver";

    public static Connection connectDB() {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER).getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(JDBC_URL,JDBC_USER, JDBC_PASSWORD);
        }

        catch (Exception message) {
            System.out.println(message);
        }
        return connection;
    }

}
