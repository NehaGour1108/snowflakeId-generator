package org.example;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2DatabaseExample {

    public static void main(String[] args) {
        // Set up H2 in-memory database
        String jdbcUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        JdbcConnectionPool connectionPool = JdbcConnectionPool.create(jdbcUrl, "sa", "");

        try (Connection conn = connectionPool.getConnection()) {
            // Create the Snowflake ID generator procedure in H2
            conn.createStatement().execute("CREATE ALIAS GENERATE_SNOWFLAKE_ID FOR \"org.example.SnowflakeIdGenerator.generateId\"");

            // Call the Snowflake ID generator stored procedure
            PreparedStatement stmt = conn.prepareStatement("SELECT GENERATE_SNOWFLAKE_ID()");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long generatedId = rs.getLong(1);
                System.out.println("Generated Snowflake ID: " + generatedId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
