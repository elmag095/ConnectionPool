package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private final static String DB_NAME = "jdbc:postgresql://localhost:5432/postgres";
    private final static String USER = "postgres";
    private final static String PASS = "postgres";

    public static void main(String[] args) throws SQLException {
        PooledDataSource dataSource = initializeDataSource();
        long start = System.nanoTime();
        double total = 0.0;
        for (int i = 1; i < 20; i++) {
            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                try (Statement statement = connection.createStatement()) {
                    ResultSet rs = statement.executeQuery("select random() from products");
                    rs.next();
                    total += rs.getDouble(1);
                }
                connection.rollback();
            }
        }
        System.out.println((System.nanoTime() - start) / 1000000 + " ms");
    }


    private static PooledDataSource initializeDataSource() {
        return new PooledDataSource(DB_NAME, USER, PASS);
    }
}