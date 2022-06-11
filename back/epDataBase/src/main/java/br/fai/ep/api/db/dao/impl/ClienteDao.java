package br.fai.ep.api.db.dao.impl;

import br.fai.ep.api.db.connection.ConnectionFactory;
import br.fai.ep.api.db.dao.BaseDao;
import br.fai.ep.api.db.helper.DataBaseHelper;
import br.fai.ep.api.db.helper.DataBaseHelper.ClientTable;
import br.fai.ep.api.db.helper.DataBaseHelper.Sql;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.entities.Cliente;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClienteDao implements BaseDao {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public List<? extends BasePojo> readAll() {
        List<Cliente> clientList = null;
        resetValuesForNewQuery();
        try {
            final String sql = Sql.SELECT_FULL.getName() + ClientTable.TABLE_NAME.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            clientList = new ArrayList<>();
            while (resultSet.next()) {
                final Cliente client = new Cliente();
                client.setId(resultSet.getLong(ClientTable.ID_COLUMN.getName()));
                client.setNome(resultSet.getString(ClientTable.NAME_COLUMN.getName()));
                client.setCnpj(resultSet.getString(ClientTable.CNPJ_COLUMN.getName()));
                client.setAceite(resultSet.getBoolean(ClientTable.ACCEPT_COLUMN.getName()));
                client.setDataHora(resultSet.getTimestamp(ClientTable.DATE_TIME_COLUMN.getName()));

                clientList.add(client);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAll | classe: " + ClienteDao.class.getName());
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return clientList;
    }

    @Override
    public Object readById(final long id) {
        Cliente client = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + ClientTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += ClientTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                client = new Cliente();
                client.setId(resultSet.getLong(ClientTable.ID_COLUMN.getName()));
                client.setNome(resultSet.getString(ClientTable.NAME_COLUMN.getName()));
                client.setCnpj(resultSet.getString(ClientTable.CNPJ_COLUMN.getName()));
                client.setAceite(resultSet.getBoolean(ClientTable.ACCEPT_COLUMN.getName()));
                client.setDataHora(resultSet.getTimestamp(ClientTable.DATE_TIME_COLUMN.getName()));
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readById | classe: " + ClienteDao.class.getName());
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return client;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = Sql.INSERT.getName();
            sql += ClientTable.TABLE_NAME.getName() + Sql.OPEN_PARENTHESIS.getName();
            sql += ClientTable.NAME_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += ClientTable.CNPJ_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += ClientTable.ACCEPT_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += ClientTable.DATE_TIME_COLUMN.getName() + Sql.CLOSE_PARENTHESIS.getName();
            sql += Sql.VALUES.getName();
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // nome
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName();
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName();
            sql += Sql.DEFAULT_VALUE_DECLARTION.getName() + Sql.FINAL_CLOSE_PARENTHESIS.getName();

            preparForReadingOrCreating(sql, true, true);

            final Cliente client = (Cliente) entity;
            int i = 1;
            preparedStatement.setString(i++, client.getNome());
            preparedStatement.setString(i++, client.getCnpj());
            preparedStatement.setBoolean(i++, client.isAceite());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(ClientTable.ID_COLUMN.getName());
            }

            if (newId == -1) {
                connection.rollback();
                return -1;
            }
            connection.commit();

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:create | classe: " + ClienteDao.class.getName());
            System.out.println(e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return newId;
    }

    @Override
    public boolean update(final Object entity) {
        resetValuesForNewQuery();
        boolean isUpdateCompleted = false;

        try {
            String sql = Sql.UPDATE.getName() + DataBaseHelper.ClientTable.TABLE_NAME.getName() + Sql.SET_UPDATE.getName();
            sql += ClientTable.NAME_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += ClientTable.CNPJ_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName();
            sql += Sql.WHERE.getName();
            sql += ClientTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";
            preparForUpdateOrDelete(sql);

            final Cliente client = (Cliente) entity;
            int i = 1;
            preparedStatement.setString(i++, ClientTable.TABLE_NAME.getName());
            preparedStatement.setString(i++, client.getNome());
            preparedStatement.setString(i++, client.getCnpj());
            preparedStatement.setLong(i++, client.getId());

            preparedStatement.execute();
            if (preparedStatement.getUpdateCount() == -1) {
                connection.rollback();
                return false;
            }

            connection.commit();
            isUpdateCompleted = true;

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:update | classe: " + ClienteDao.class.getName());
            System.out.println(e.getMessage());
            isUpdateCompleted = false;
        } finally {
            ConnectionFactory.close(preparedStatement, connection);
        }

        return isUpdateCompleted;
    }

    @Override
    public boolean delete(final long id) {
        resetValuesForNewQuery();
        boolean isDeleteCompleted = false;

        try {
            String sql = Sql.DELETE.getName() + ClientTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += ClientTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

            preparForUpdateOrDelete(sql);
            preparedStatement.setLong(1, id);

            preparedStatement.execute();
            connection.commit();
            isDeleteCompleted = true;
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:delete | classe: " + ClienteDao.class.getName());

            try {
                connection.rollback();
            } catch (final SQLException e2) {
                System.out.println(e2.getMessage());
            }
            isDeleteCompleted = false;
        } finally {
            ConnectionFactory.close(preparedStatement, connection);
        }

        return isDeleteCompleted;
    }

    @Override
    public List<? extends BasePojo> readByCriteria(final String criteria) {
        List<Cliente> clientList = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + ClientTable.TABLE_NAME.getName();
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            clientList = new ArrayList<>();
            while (resultSet.next()) {
                final Cliente client = new Cliente();
                client.setId(resultSet.getLong(ClientTable.ID_COLUMN.getName()));
                client.setNome(resultSet.getString(ClientTable.NAME_COLUMN.getName()));
                client.setCnpj(resultSet.getString(ClientTable.CNPJ_COLUMN.getName()));
                client.setAceite(resultSet.getBoolean(ClientTable.ACCEPT_COLUMN.getName()));
                client.setDataHora(resultSet.getTimestamp(ClientTable.DATE_TIME_COLUMN.getName()));
                clientList.add(client);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDao.class.getName());
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return clientList;
    }

    @Override
    public void preparForReadingOrCreating(final String sql, final boolean isGenerateKeys, final boolean isAutoCommit) throws SQLException {
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

    @Override
    public void preparForUpdateOrDelete(final String sql) throws SQLException {
        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
        } catch (final Exception e) {
            throw new SQLException("Verificar metodo newUpdateOrDeleteInstances");
        }
    }

    public void resetValuesForNewQuery() {
        connection = null;
        preparedStatement = null;
        resultSet = null;
    }
}
