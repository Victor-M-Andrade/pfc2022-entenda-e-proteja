package br.fai.ep.api.db.dao.impl;

import br.fai.ep.api.db.connection.ConnectionFactory;
import br.fai.ep.api.db.dao.BaseDao;
import br.fai.ep.api.db.dao.BaseDaoInterface;
import br.fai.ep.api.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.entities.ParceiroSolicitacao;
import br.fai.ep.api.entities.ParceiroSolicitacao.PARTINER_REQUEST_TABLE;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParceiroSolicitacaoDaoImpl extends BaseDao implements BaseDaoInterface {
    
    @Override
    public List<? extends BasePojo> readAll() {
        List<ParceiroSolicitacao> partinerRequestList;
        resetValuesForNewQuery();

        try {
            final String sql = SQL_COMMAND.SELECT_FULL + PARTINER_REQUEST_TABLE.TABLE_NAME + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            partinerRequestList = new ArrayList<>();
            while (resultSet.next()) {
                final ParceiroSolicitacao partinerRequest = new ParceiroSolicitacao();
                partinerRequest.setId(resultSet.getLong(PARTINER_REQUEST_TABLE.ID_COLUMN));
                partinerRequest.setIdParceiro(resultSet.getLong(PARTINER_REQUEST_TABLE.ID_PARTINER_COLUMN));
                partinerRequest.setIdSolicitacao(resultSet.getLong(PARTINER_REQUEST_TABLE.ID_SOLICITATION_COLUMN));

                partinerRequestList.add(partinerRequest);
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

        return partinerRequestList;
    }

    @Override
    public Object readById(final long id) {
        ParceiroSolicitacao partinerRequest = null;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + PARTINER_REQUEST_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += PARTINER_REQUEST_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                partinerRequest = new ParceiroSolicitacao();
                partinerRequest.setId(resultSet.getLong(PARTINER_REQUEST_TABLE.ID_COLUMN));
                partinerRequest.setIdParceiro(resultSet.getLong(PARTINER_REQUEST_TABLE.ID_PARTINER_COLUMN));
                partinerRequest.setIdSolicitacao(resultSet.getLong(PARTINER_REQUEST_TABLE.ID_SOLICITATION_COLUMN));

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

        return partinerRequest;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = SQL_COMMAND.INSERT;
            sql += PARTINER_REQUEST_TABLE.TABLE_NAME + SQL_COMMAND.OPEN_PARENTHESIS;
            sql += PARTINER_REQUEST_TABLE.ID_PARTINER_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += PARTINER_REQUEST_TABLE.ID_SOLICITATION_COLUMN + SQL_COMMAND.CLOSE_PARENTHESIS;
            sql += SQL_COMMAND.VALUES;
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // id_parceiro
            sql += SQL_COMMAND.LAST_PARAM_INSERT_TO_COMPLETE; // id_solicitacao
            preparForReadingOrCreating(sql, true, true);

            final ParceiroSolicitacao partinerRequest = (ParceiroSolicitacao) entity;
            int i = 1;
            preparedStatement.setLong(i++, partinerRequest.getIdParceiro());
            preparedStatement.setLong(i++, partinerRequest.getIdSolicitacao());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(PARTINER_REQUEST_TABLE.ID_COLUMN);
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
        boolean isUpdateCompleted;

        try {
            String sql = SQL_COMMAND.UPDATE + PARTINER_REQUEST_TABLE.TABLE_NAME + SQL_COMMAND.SET_UPDATE;
            sql += PARTINER_REQUEST_TABLE.ID_PARTINER_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += PARTINER_REQUEST_TABLE.ID_PARTINER_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;
            sql += SQL_COMMAND.WHERE;
            sql += PARTINER_REQUEST_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";
            preparForUpdateOrDelete(sql);

            final ParceiroSolicitacao partinerRequest = (ParceiroSolicitacao) entity;
            int i = 1;
            preparedStatement.setLong(i++, partinerRequest.getIdParceiro());
            preparedStatement.setLong(i++, partinerRequest.getIdSolicitacao());
            preparedStatement.setLong(i++, partinerRequest.getId());

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
        boolean isDeleteCompleted;

        try {
            String sql = SQL_COMMAND.DELETE + PARTINER_REQUEST_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += PARTINER_REQUEST_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

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
        List<ParceiroSolicitacao> partinerRequestList;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + PARTINER_REQUEST_TABLE.TABLE_NAME;
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            partinerRequestList = new ArrayList<>();
            while (resultSet.next()) {
                final ParceiroSolicitacao partinerRequest = new ParceiroSolicitacao();
                partinerRequest.setId(resultSet.getLong(PARTINER_REQUEST_TABLE.ID_COLUMN));
                partinerRequest.setIdParceiro(resultSet.getLong(PARTINER_REQUEST_TABLE.ID_PARTINER_COLUMN));
                partinerRequest.setIdSolicitacao(resultSet.getLong(PARTINER_REQUEST_TABLE.ID_SOLICITATION_COLUMN));

                partinerRequestList.add(partinerRequest);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return partinerRequestList;
    }
}