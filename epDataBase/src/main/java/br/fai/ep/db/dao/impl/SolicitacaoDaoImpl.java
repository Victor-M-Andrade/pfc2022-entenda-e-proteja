package br.fai.ep.db.dao.impl;

import br.fai.ep.db.connection.ConnectionFactory;
import br.fai.ep.db.dao.BaseDao;
import br.fai.ep.db.dao.BaseDaoInterface;
import br.fai.ep.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.Solicitacao;
import br.fai.ep.epEntities.Solicitacao.RESQUEST_TABLE;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SolicitacaoDaoImpl extends BaseDao implements BaseDaoInterface {

    @Override
    public List<? extends BasePojo> readAll() {
        List<Solicitacao> requestList;
        resetValuesForNewQuery();

        try {
            final String sql = SQL_COMMAND.SELECT_FULL + RESQUEST_TABLE.TABLE_NAME + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            requestList = new ArrayList<>();
            while (resultSet.next()) {
                final Solicitacao request = new Solicitacao();
                request.setId(resultSet.getLong(RESQUEST_TABLE.ID_COLUMN));
                request.setDemanda(resultSet.getString(RESQUEST_TABLE.DEMAND_COLUMN));
                request.setTipoServico(resultSet.getString(RESQUEST_TABLE.SERVICE_TYPE_COLUMN));
                request.setIdCliente(resultSet.getLong(RESQUEST_TABLE.ID_CLIENT_COLUMN));

                requestList.add(request);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAll | classe: " + SolicitacaoDaoImpl.class);
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return requestList;
    }

    @Override
    public Object readById(final long id) {
        Solicitacao request = null;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + RESQUEST_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += RESQUEST_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                request = new Solicitacao();
                request.setId(resultSet.getLong(RESQUEST_TABLE.ID_COLUMN));
                request.setDemanda(resultSet.getString(RESQUEST_TABLE.DEMAND_COLUMN));
                request.setTipoServico(resultSet.getString(RESQUEST_TABLE.SERVICE_TYPE_COLUMN));
                request.setIdCliente(resultSet.getLong(RESQUEST_TABLE.ID_CLIENT_COLUMN));
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readById | classe: " + SolicitacaoDaoImpl.class);
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return request;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = SQL_COMMAND.INSERT;
            sql += RESQUEST_TABLE.TABLE_NAME + SQL_COMMAND.OPEN_PARENTHESIS;
            sql += RESQUEST_TABLE.DEMAND_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += RESQUEST_TABLE.SERVICE_TYPE_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += RESQUEST_TABLE.ID_CLIENT_COLUMN + SQL_COMMAND.CLOSE_PARENTHESIS;
            sql += SQL_COMMAND.VALUES;
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // demanda
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; //tipo_servico
            sql += SQL_COMMAND.LAST_PARAM_INSERT_TO_COMPLETE; // id_cliente
            preparForReadingOrCreating(sql, true, false);

            final Solicitacao request = (Solicitacao) entity;
            int i = 1;
            preparedStatement.setString(i++, request.getDemanda());
            preparedStatement.setString(i++, request.getTipoServico());
            preparedStatement.setLong(i++, request.getIdCliente());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(RESQUEST_TABLE.ID_COLUMN);
            }

            if (newId == -1) {
                connection.rollback();
                return -1;
            }
            connection.commit();

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:create | classe: " + SolicitacaoDaoImpl.class);
            System.out.println(e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return newId;
    }

    @Override
    public boolean update(final Object entity) {
        resetValuesForNewQuery();
        boolean isUpdateCompleted;

        try {
            String sql = SQL_COMMAND.UPDATE + RESQUEST_TABLE.TABLE_NAME + SQL_COMMAND.SET_UPDATE;
            sql += RESQUEST_TABLE.DEMAND_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += RESQUEST_TABLE.SERVICE_TYPE_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += RESQUEST_TABLE.ID_CLIENT_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;
            sql += SQL_COMMAND.WHERE;
            sql += RESQUEST_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";
            preparForUpdateOrDelete(sql);

            final Solicitacao request = (Solicitacao) entity;
            int i = 1;
            preparedStatement.setString(i++, request.getDemanda());
            preparedStatement.setString(i++, request.getTipoServico());
            preparedStatement.setLong(i++, request.getIdCliente());
            preparedStatement.setLong(i++, request.getId());

            preparedStatement.execute();
            if (preparedStatement.getUpdateCount() == -1) {
                connection.rollback();
                return false;
            }

            connection.commit();
            isUpdateCompleted = true;

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:update | classe: " + SolicitacaoDaoImpl.class);
            System.out.println(e.getMessage());
            isUpdateCompleted = false;
        } finally {
            ConnectionFactory.close(preparedStatement);
        }

        return isUpdateCompleted;
    }

    @Override
    public boolean delete(final long id) {
        resetValuesForNewQuery();
        boolean isDeleteCompleted;

        try {
            String sql = SQL_COMMAND.DELETE + RESQUEST_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += RESQUEST_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForUpdateOrDelete(sql);
            preparedStatement.setLong(1, id);

            preparedStatement.execute();
            connection.commit();
            isDeleteCompleted = true;
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:delete | classe: " + SolicitacaoDaoImpl.class);

            try {
                connection.rollback();
            } catch (final SQLException e2) {
                System.out.println(e2.getMessage());
            }
            isDeleteCompleted = false;
        } finally {
            ConnectionFactory.close(preparedStatement);
        }

        return isDeleteCompleted;
    }

    @Override
    public List<? extends BasePojo> readByCriteria(final String criteria) {
        List<Solicitacao> requestList;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + RESQUEST_TABLE.TABLE_NAME;
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            requestList = new ArrayList<>();
            while (resultSet.next()) {
                final Solicitacao request = new Solicitacao();
                request.setId(resultSet.getLong(RESQUEST_TABLE.ID_COLUMN));
                request.setDemanda(resultSet.getString(RESQUEST_TABLE.DEMAND_COLUMN));
                request.setTipoServico(resultSet.getString(RESQUEST_TABLE.SERVICE_TYPE_COLUMN));
                request.setIdCliente(resultSet.getLong(RESQUEST_TABLE.ID_CLIENT_COLUMN));

                requestList.add(request);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + SolicitacaoDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return requestList;
    }
}