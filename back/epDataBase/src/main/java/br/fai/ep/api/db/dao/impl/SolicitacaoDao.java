package br.fai.ep.api.db.dao.impl;

import br.fai.ep.api.db.connection.ConnectionFactory;
import br.fai.ep.api.db.dao.BaseDao;
import br.fai.ep.api.db.helper.DataBaseHelper.RequestTable;
import br.fai.ep.api.db.helper.DataBaseHelper.Sql;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.entities.Solicitacao;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SolicitacaoDao implements BaseDao {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public List<? extends BasePojo> readAll() {
        List<Solicitacao> requestList;
        resetValuesForNewQuery();

        try {
            final String sql = Sql.SELECT_FULL.getName() + RequestTable.TABLE_NAME.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            requestList = new ArrayList<>();
            while (resultSet.next()) {
                final Solicitacao request = new Solicitacao();
                request.setId(resultSet.getLong(RequestTable.ID_COLUMN.getName()));
                request.setDemanda(resultSet.getString(RequestTable.DEMAND_COLUMN.getName()));
                request.setTipoServico(resultSet.getString(RequestTable.SERVICE_TYPE_COLUMN.getName()));
                request.setIdCliente(resultSet.getString(RequestTable.ID_CLIENT_COLUMN.getName()));

                requestList.add(request);
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

        return requestList;
    }

    @Override
    public Object readById(final long id) {
        Solicitacao request = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + RequestTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += RequestTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                request = new Solicitacao();
                request.setId(resultSet.getLong(RequestTable.ID_COLUMN.getName()));
                request.setDemanda(resultSet.getString(RequestTable.DEMAND_COLUMN.getName()));
                request.setTipoServico(resultSet.getString(RequestTable.SERVICE_TYPE_COLUMN.getName()));
                request.setIdCliente(resultSet.getString(RequestTable.ID_CLIENT_COLUMN.getName()));
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

        return request;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = Sql.INSERT.getName();
            sql += RequestTable.TABLE_NAME.getName() + Sql.OPEN_PARENTHESIS.getName();
            sql += RequestTable.DEMAND_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += RequestTable.SERVICE_TYPE_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += RequestTable.ID_CLIENT_COLUMN.getName() + Sql.CLOSE_PARENTHESIS.getName();
            sql += Sql.VALUES.getName();
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // demanda
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); //tipo_servico
            sql += Sql.LAST_PARAM_INSERT_TO_COMPLETE.getName(); // id_requeste
            preparForReadingOrCreating(sql, true, true);

            final Solicitacao request = (Solicitacao) entity;
            int i = 1;
            preparedStatement.setString(i++, request.getDemanda());
            preparedStatement.setString(i++, request.getTipoServico());
            preparedStatement.setString(i++, request.getIdCliente());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(RequestTable.ID_COLUMN.getName());
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
        boolean isUpdateCompleted;

        try {
            String sql = Sql.UPDATE.getName();
            sql += RequestTable.DEMAND_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += RequestTable.SERVICE_TYPE_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += RequestTable.ID_CLIENT_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName();
            sql += Sql.WHERE.getName();
            sql += RequestTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";
            preparForUpdateOrDelete(sql);

            final Solicitacao request = (Solicitacao) entity;
            int i = 1;
            preparedStatement.setString(i++, RequestTable.TABLE_NAME.getName());
            preparedStatement.setString(i++, request.getDemanda());
            preparedStatement.setString(i++, request.getTipoServico());
            preparedStatement.setString(i++, request.getIdCliente());
            preparedStatement.setLong(i++, request.getId());

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
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return isUpdateCompleted;
    }

    @Override
    public boolean delete(final long id) {
        resetValuesForNewQuery();
        boolean isDeleteCompleted;

        try {
            String sql = Sql.DELETE.getName() + RequestTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += RequestTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

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
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return isDeleteCompleted;
    }

    @Override
    public List<? extends BasePojo> readByCriteria(final String criteria) {
        List<Solicitacao> requestList;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + RequestTable.TABLE_NAME.getName();
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            requestList = new ArrayList<>();
            while (resultSet.next()) {
                final Solicitacao request = new Solicitacao();
                request.setId(resultSet.getLong(RequestTable.ID_COLUMN.getName()));
                request.setDemanda(resultSet.getString(RequestTable.DEMAND_COLUMN.getName()));
                request.setTipoServico(resultSet.getString(RequestTable.SERVICE_TYPE_COLUMN.getName()));
                request.setIdCliente(resultSet.getString(RequestTable.ID_CLIENT_COLUMN.getName()));

                requestList.add(request);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDao.class.getName());
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return requestList;
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
