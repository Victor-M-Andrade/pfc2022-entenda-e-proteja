package br.fai.ep.api.db.dao.impl;

import br.fai.ep.api.db.connection.ConnectionFactory;
import br.fai.ep.api.db.dao.BaseDao;
import br.fai.ep.api.db.helper.DataBaseHelper;
import br.fai.ep.api.db.helper.DataBaseHelper.QuestionTestTable;
import br.fai.ep.api.db.helper.DataBaseHelper.Sql;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.entities.QuestaoTeste;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestaoTesteDao implements BaseDao {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public List<? extends BasePojo> readAll() {
        List<QuestaoTeste> questionTestList;
        resetValuesForNewQuery();

        try {
            final String sql = Sql.SELECT_FULL.getName() + QuestionTestTable.TABLE_NAME.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            questionTestList = new ArrayList<>();
            while (resultSet.next()) {
                final QuestaoTeste questionTest = new QuestaoTeste();
                questionTest.setId(resultSet.getLong(QuestionTestTable.ID_COLUMN.getName()));
                questionTest.setEscolha(resultSet.getString(QuestionTestTable.CHOICE_COLUMN.getName()));
                questionTest.setIdQuestao(resultSet.getLong(QuestionTestTable.ID_QUEST_COLUMN.getName()));
                questionTest.setIdTeste(resultSet.getLong(QuestionTestTable.ID_TEST_COLUMN.getName()));

                questionTestList.add(questionTest);
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

        return questionTestList;
    }

    @Override
    public Object readById(final long id) {
        QuestaoTeste questionTest = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + QuestionTestTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += QuestionTestTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                questionTest = new QuestaoTeste();
                questionTest.setId(resultSet.getLong(QuestionTestTable.ID_COLUMN.getName()));
                questionTest.setEscolha(resultSet.getString(QuestionTestTable.CHOICE_COLUMN.getName()));
                questionTest.setIdQuestao(resultSet.getLong(QuestionTestTable.ID_QUEST_COLUMN.getName()));
                questionTest.setIdTeste(resultSet.getLong(QuestionTestTable.ID_TEST_COLUMN.getName()));

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

        return questionTest;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = Sql.INSERT.getName();
            sql += QuestionTestTable.TABLE_NAME.getName() + Sql.OPEN_PARENTHESIS.getName();
            sql += QuestionTestTable.CHOICE_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += QuestionTestTable.ID_QUEST_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += QuestionTestTable.ID_TEST_COLUMN.getName() + Sql.CLOSE_PARENTHESIS.getName();
            sql += Sql.VALUES.getName();
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // demanda
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); //tipo_servico
            sql += Sql.LAST_PARAM_INSERT_TO_COMPLETE.getName(); // id_questionTeste
            preparForReadingOrCreating(sql, true, true);

            final QuestaoTeste questionTest = (QuestaoTeste) entity;
            int i = 1;
            preparedStatement.setString(i++, questionTest.getEscolha());
            preparedStatement.setLong(i++, questionTest.getIdQuestao());
            preparedStatement.setLong(i++, questionTest.getIdTeste());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(QuestionTestTable.ID_COLUMN.getName());
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
            String sql = Sql.UPDATE.getName() + DataBaseHelper.QuestionTestTable.TABLE_NAME.getName() + Sql.SET_UPDATE.getName();
            sql += QuestionTestTable.CHOICE_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += QuestionTestTable.ID_QUEST_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += QuestionTestTable.ID_TEST_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName();
            sql += Sql.WHERE.getName();
            sql += QuestionTestTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";
            preparForUpdateOrDelete(sql);

            final QuestaoTeste questionTest = (QuestaoTeste) entity;
            int i = 1;
            preparedStatement.setString(i++, questionTest.getEscolha());
            preparedStatement.setLong(i++, questionTest.getIdQuestao());
            preparedStatement.setLong(i++, questionTest.getIdTeste());
            preparedStatement.setLong(i++, questionTest.getId());

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
        boolean isDeleteCompleted;

        try {
            String sql = Sql.DELETE.getName() + QuestionTestTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += QuestionTestTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

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
        List<QuestaoTeste> questionTestList;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + QuestionTestTable.TABLE_NAME.getName();
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            questionTestList = new ArrayList<>();
            while (resultSet.next()) {
                final QuestaoTeste questionTest = new QuestaoTeste();
                questionTest.setId(resultSet.getLong(QuestionTestTable.ID_COLUMN.getName()));
                questionTest.setEscolha(resultSet.getString(QuestionTestTable.CHOICE_COLUMN.getName()));
                questionTest.setIdQuestao(resultSet.getLong(QuestionTestTable.ID_QUEST_COLUMN.getName()));
                questionTest.setIdTeste(resultSet.getLong(QuestionTestTable.ID_TEST_COLUMN.getName()));

                questionTestList.add(questionTest);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDao.class.getName());
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return questionTestList;
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
