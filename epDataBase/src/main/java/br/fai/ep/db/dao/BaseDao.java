package br.fai.ep.db.dao;

import br.fai.ep.db.connection.ConnectionFactory;

import java.sql.*;

public abstract class BaseDao {
    protected Connection connection;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;

    protected void preparForReadingOrCreating(final String sql, final boolean isGenerateKeys, final boolean isAutoCommit) throws SQLException {
        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(isAutoCommit);

            if (isGenerateKeys) {
                preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                return;
            }
            preparedStatement = connection.prepareStatement(sql);
        } catch (final Exception e) {
            throw new SQLException("SQLException: olhar metodo newReadOrCreateInstances");
        }
    }

    protected void preparForUpdateOrDelete(final String sql) throws SQLException {
        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
        } catch (final Exception e) {
            throw new SQLException("Verificar metodo newUpdateOrDeleteInstances");
        }
    }

    protected void resetValuesForNewQuery() {
        connection = null;
        preparedStatement = null;
        resultSet = null;
    }

}