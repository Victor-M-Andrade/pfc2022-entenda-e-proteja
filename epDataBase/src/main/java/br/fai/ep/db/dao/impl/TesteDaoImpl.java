package br.fai.ep.db.dao.impl;

import br.fai.ep.db.connection.ConnectionFactory;
import br.fai.ep.db.dao.BaseDao;
import br.fai.ep.db.dao.BaseDaoInterface;
import br.fai.ep.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.TestDto;
import br.fai.ep.epEntities.Questao;
import br.fai.ep.epEntities.Teste;
import br.fai.ep.epEntities.Teste.TEST_TABLE;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TesteDaoImpl extends BaseDao implements BaseDaoInterface {

    @Override
    public List<? extends BasePojo> readAll() {
        List<Teste> testList = null;
        resetValuesForNewQuery();

        try {
            final String sql = SQL_COMMAND.SELECT_FULL + TEST_TABLE.TABLE_NAME + ";";

            preparForReadingOrCreating(sql, false, false);
            resultSet = preparedStatement.executeQuery();

            testList = new ArrayList<>();
            while (resultSet.next()) {
                final Teste test = new Teste();
                test.setId(resultSet.getLong(TEST_TABLE.ID_COLUMN));
                test.setDataHora(resultSet.getTimestamp(TEST_TABLE.DATE_TIME_COLUMN));
                test.setAcertos(resultSet.getInt(TEST_TABLE.HIT_COLUMN));
                test.setIdUsuario(resultSet.getLong(TEST_TABLE.ID_USER_COLUMN));

                testList.add(test);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAll | classe: " + TesteDaoImpl.class);
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return testList;
    }

    @Override
    public Object readById(final long id) {
        Teste test = null;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + TEST_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += TEST_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                test = new Teste();
                test.setId(resultSet.getLong(TEST_TABLE.ID_COLUMN));
                test.setDataHora(resultSet.getTimestamp(TEST_TABLE.DATE_TIME_COLUMN));
                test.setAcertos(resultSet.getInt(TEST_TABLE.HIT_COLUMN));
                test.setIdUsuario(resultSet.getLong(TEST_TABLE.ID_USER_COLUMN));
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readById | classe: " + TesteDaoImpl.class);
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return test;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = SQL_COMMAND.INSERT;
            sql += TEST_TABLE.TABLE_NAME + SQL_COMMAND.OPEN_PARENTHESIS;
            sql += TEST_TABLE.DATE_TIME_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += TEST_TABLE.HIT_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += TEST_TABLE.ID_USER_COLUMN + SQL_COMMAND.CLOSE_PARENTHESIS;
            sql += SQL_COMMAND.VALUES;
            sql += SQL_COMMAND.DEFAULT_VALUE_DECLARTION + SQL_COMMAND.SEPARATOR; // data_hora
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // qtd_acerto
            sql += SQL_COMMAND.LAST_PARAM_INSERT_TO_COMPLETE; // id_usuario

            preparForReadingOrCreating(sql, true, false);

            final Teste test = (Teste) entity;
            int i = 1;
            preparedStatement.setInt(i++, test.getAcertos());
            preparedStatement.setLong(i++, test.getIdUsuario());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(TEST_TABLE.ID_COLUMN);
            }

            if (newId == -1) {
                connection.rollback();
                return -1;
            }
            connection.commit();

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:create | classe: " + TesteDaoImpl.class);
            System.out.println(e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return newId;
    }

    @Override
    public boolean update(final Object entity) {
        resetValuesForNewQuery();
        boolean isUpdateCompleted = false;

        try {
            String sql = SQL_COMMAND.UPDATE + TEST_TABLE.TABLE_NAME + SQL_COMMAND.SET_UPDATE;
            sql += TEST_TABLE.DATE_TIME_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += TEST_TABLE.HIT_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;
            sql += SQL_COMMAND.WHERE;
            sql += TEST_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";
            preparForUpdateOrDelete(sql);

            final Teste test = (Teste) entity;
            int i = 1;
            preparedStatement.setTimestamp(i++, test.getDataHora());
            preparedStatement.setInt(i++, test.getAcertos());
            preparedStatement.setLong(i++, test.getId());

            preparedStatement.execute();
            if (preparedStatement.getUpdateCount() == -1) {
                connection.rollback();
                return false;
            }

            connection.commit();
            isUpdateCompleted = true;

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:update | classe: " + TesteDaoImpl.class);
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
        boolean isDeleteCompleted = false;

        try {
            String sql = SQL_COMMAND.DELETE + TEST_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += TEST_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForUpdateOrDelete(sql);
            preparedStatement.setLong(1, id);

            preparedStatement.execute();
            connection.commit();
            isDeleteCompleted = true;
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:delete | classe: " + TesteDaoImpl.class);

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
        List<Teste> testList = null;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + TEST_TABLE.TABLE_NAME;
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            testList = new ArrayList<>();
            while (resultSet.next()) {
                final Teste test = new Teste();
                test.setId(resultSet.getLong(TEST_TABLE.ID_COLUMN));
                test.setDataHora(resultSet.getTimestamp(TEST_TABLE.DATE_TIME_COLUMN));
                test.setAcertos(resultSet.getInt(TEST_TABLE.HIT_COLUMN));
                test.setIdUsuario(resultSet.getLong(TEST_TABLE.ID_USER_COLUMN));

                testList.add(test);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + TesteDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return testList;
    }

    public List<TestDto> readAllUserTest(final long userId) {
        List<TestDto> testList = null;
        resetValuesForNewQuery();

        try {
            final String questionIdColumnName = "question_id";
            final String sql = "select userTest.*, Q.nivel, Q.id as " + questionIdColumnName + " from questao as Q " +
                    "inner join teste_questao TQ on Q.id = TQ.id_questao " +
                    "inner join (select * from teste as T where T.id_usuario = ?) as userTest " +
                    "on TQ.id_teste = userTest.id;";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, userId);
            resultSet = preparedStatement.executeQuery();

            testList = new ArrayList<>();
            while (resultSet.next()) {
                final TestDto test = new TestDto();
                test.setId(resultSet.getLong(TEST_TABLE.ID_COLUMN));
                test.setDataHora(resultSet.getTimestamp(TEST_TABLE.DATE_TIME_COLUMN));
                test.setAcertos(resultSet.getInt(TEST_TABLE.HIT_COLUMN));
                test.setIdUsuario(resultSet.getLong(TEST_TABLE.ID_USER_COLUMN));
                test.setLevelTest(resultSet.getInt(Questao.QUESTION_TABLE.LEVEL_COLUMN));
                test.setQuestionId(resultSet.getInt(questionIdColumnName));

                testList.add(test);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByDtoCriteria | classe: " + TesteDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return testList;
    }
}