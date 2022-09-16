package br.fai.ep.db.dao.impl;

import br.fai.ep.db.connection.ConnectionFactory;
import br.fai.ep.db.dao.BaseDao;
import br.fai.ep.db.dao.BaseDaoInterface;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.Cliente;
import br.fai.ep.epEntities.Cliente.CLIENT_TABLE;
import br.fai.ep.db.helper.DataBaseHelper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClienteDaoImpl extends BaseDao implements BaseDaoInterface {
    @Override
    public List<? extends BasePojo> readAll() {
        List<Cliente> clientList = null;
        resetValuesForNewQuery();
        try {
            final String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + CLIENT_TABLE.TABLE_NAME + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            clientList = new ArrayList<>();
            while (resultSet.next()) {
                final Cliente client = new Cliente();
                client.setId(resultSet.getLong(CLIENT_TABLE.ID_COLUMN));
                client.setNome(resultSet.getString(CLIENT_TABLE.NAME_COLUMN));
                client.setCnpj(resultSet.getString(CLIENT_TABLE.CNPJ_COLUMN));
                client.setAceite(resultSet.getBoolean(CLIENT_TABLE.ACCEPT_COLUMN));
                client.setDataHora(resultSet.getTimestamp(CLIENT_TABLE.DATE_TIME_COLUMN));

                clientList.add(client);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAll | classe: " + ClienteDaoImpl.class);
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
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + CLIENT_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.WHERE;
            sql += CLIENT_TABLE.ID_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                client = new Cliente();
                client.setId(resultSet.getLong(CLIENT_TABLE.ID_COLUMN));
                client.setNome(resultSet.getString(CLIENT_TABLE.NAME_COLUMN));
                client.setCnpj(resultSet.getString(CLIENT_TABLE.CNPJ_COLUMN));
                client.setAceite(resultSet.getBoolean(CLIENT_TABLE.ACCEPT_COLUMN));
                client.setDataHora(resultSet.getTimestamp(CLIENT_TABLE.DATE_TIME_COLUMN));
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readById | classe: " + ClienteDaoImpl.class);
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
            String sql = DataBaseHelper.SQL_COMMAND.INSERT;
            sql += CLIENT_TABLE.TABLE_NAME + DataBaseHelper.SQL_COMMAND.OPEN_PARENTHESIS;
            sql += CLIENT_TABLE.NAME_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += CLIENT_TABLE.CNPJ_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += CLIENT_TABLE.ACCEPT_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += CLIENT_TABLE.DATE_TIME_COLUMN + DataBaseHelper.SQL_COMMAND.CLOSE_PARENTHESIS;
            sql += DataBaseHelper.SQL_COMMAND.VALUES;
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // nome
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; //cnpj
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // aceite
            sql += DataBaseHelper.SQL_COMMAND.DEFAULT_VALUE_DECLARTION + DataBaseHelper.SQL_COMMAND.FINAL_CLOSE_PARENTHESIS + ";";

            preparForReadingOrCreating(sql, true, false);

            final Cliente client = (Cliente) entity;
            int i = 1;
            preparedStatement.setString(i++, client.getNome());
            preparedStatement.setString(i++, client.getCnpj());
            preparedStatement.setBoolean(i++, client.isAceite());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(CLIENT_TABLE.ID_COLUMN);
            }

            if (newId == -1) {
                connection.rollback();
                return -1;
            }
            connection.commit();

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:create | classe: " + ClienteDaoImpl.class);
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
            String sql = DataBaseHelper.SQL_COMMAND.UPDATE + CLIENT_TABLE.TABLE_NAME + DataBaseHelper.SQL_COMMAND.SET_UPDATE;
            sql += CLIENT_TABLE.NAME_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += CLIENT_TABLE.CNPJ_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;
            sql += DataBaseHelper.SQL_COMMAND.WHERE;
            sql += CLIENT_TABLE.ID_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";
            preparForUpdateOrDelete(sql);

            final Cliente client = (Cliente) entity;
            int i = 1;
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
            System.out.println("Excecao -> metodo:update | classe: " + ClienteDaoImpl.class);
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
            String sql = DataBaseHelper.SQL_COMMAND.DELETE + CLIENT_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.WHERE;
            sql += CLIENT_TABLE.ID_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForUpdateOrDelete(sql);
            preparedStatement.setLong(1, id);

            preparedStatement.execute();
            connection.commit();
            isDeleteCompleted = true;
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:delete | classe: " + ClienteDaoImpl.class);

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
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + CLIENT_TABLE.TABLE_NAME;
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            clientList = new ArrayList<>();
            while (resultSet.next()) {
                final Cliente client = new Cliente();
                client.setId(resultSet.getLong(CLIENT_TABLE.ID_COLUMN));
                client.setNome(resultSet.getString(CLIENT_TABLE.NAME_COLUMN));
                client.setCnpj(resultSet.getString(CLIENT_TABLE.CNPJ_COLUMN));
                client.setAceite(resultSet.getBoolean(CLIENT_TABLE.ACCEPT_COLUMN));
                client.setDataHora(resultSet.getTimestamp(CLIENT_TABLE.DATE_TIME_COLUMN));
                clientList.add(client);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return clientList;
    }
}