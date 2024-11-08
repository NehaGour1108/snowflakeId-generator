package org.example;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class PaginationBenchmark {

    private static final String JDBC_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";

    public static void main(String[] args) throws SQLException {
        JdbcConnectionPool pool = JdbcConnectionPool.create(JDBC_URL, "sa", "");

        try (Connection conn = pool.getConnection()) {
            // Create table and insert data
            conn.createStatement().execute("CREATE TABLE users (id INT PRIMARY KEY, age INT)");
            conn.createStatement().execute("INSERT INTO users SELECT x, x % 100 FROM SYSTEM_RANGE(1, 1000000)");

            // Benchmark Limit-Offset Pagination
            benchmarkLimitOffset(conn);

            // Benchmark ID-Limit Pagination
            benchmarkIdLimit(conn);
        }
    }

    private static void benchmarkLimitOffset(Connection conn) throws SQLException {
        long startTime = System.nanoTime();
        for (int offset = 0; offset <= 1000000; offset += 1000) {
            String query = "SELECT * FROM users ORDER BY id LIMIT 1000 OFFSET " + offset;
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Process data
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("Limit-Offset Total Time: " + duration + " ms");
    }

    private static void benchmarkIdLimit(Connection conn) throws SQLException {
        long startTime = System.nanoTime();
        int lastId = 0;
        for (int page = 0; page < 1000; page++) {
            String query = "SELECT * FROM users WHERE id > " + lastId + " ORDER BY id LIMIT 1000";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lastId = rs.getInt("id");
                    // Process data
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("ID-Limit Total Time: " + duration + " ms");
    }
}
