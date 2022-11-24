package br.fai.ep.db.connection;

import java.sql.*;

public abstract class ConnectionFactory {
    private static final String DB_ENDPOINT = "jdbc:postgresql://localhost:5433/Entenda_E_Proteja";
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(DB_ENDPOINT, USER_NAME, PASSWORD);

        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    private static void closeConnection() {
        try {
            connection.close();
        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void closeResultSet(final ResultSet resultSet) {
        try {
            resultSet.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static void closePreparedStatement(final PreparedStatement preparedStatement) {
        try {
            preparedStatement.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }


    public static void close(final ResultSet resultSet, final PreparedStatement preparedStatement) {
        closeConnection();
        closePreparedStatement(preparedStatement);
        closeResultSet(resultSet);
    }

    public static void close(final PreparedStatement preparedStatement) {
        closeConnection();
        closePreparedStatement(preparedStatement);
    }
}