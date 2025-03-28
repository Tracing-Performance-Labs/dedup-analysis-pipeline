package io.github.tracingperformancelabs.dupe.services;

import io.github.tracingperformancelabs.dupe.model.DataPoint;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteDataStorage implements DataStorageService<String> {
    private final static String CONN_URL = "jdbc:sqlite:db.sqlite";

    // TODO: Replace with proper logging.

    @Override
    public void store(DataPoint<String> data) {
        final var query = "INSERT INTO raw_span_data (id, content, source)" +
                "values (?, ?, ?)";
        System.out.println("Inserting data point into database");

        try (final var conn = DriverManager.getConnection(CONN_URL)) {
            final var stmt = conn.prepareStatement(query);

            stmt.setString(1, data.getId());
            stmt.setString(2, data.getContent());
            stmt.setString(3, data.getSource());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Executed insertion, rows affected: " + rowsAffected);
        } catch (SQLException ex) {
            System.err.println("There was an error while storing the data point: " + ex);
        }
    }
}
