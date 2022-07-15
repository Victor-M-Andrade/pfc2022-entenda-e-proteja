package br.fai.ep.api.db.dao.impl;

import br.fai.ep.api.db.connection.ConnectionFactory;
import br.fai.ep.api.db.dao.BaseDao;
import br.fai.ep.api.db.helper.DataBaseHelper;
import br.fai.ep.api.db.helper.DataBaseHelper.QuestionTable;
import br.fai.ep.api.db.helper.DataBaseHelper.Sql;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.entities.Questao;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QuestaoDao implements BaseDao {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public List<? extends BasePojo> readAll() {
        List<Questao> questionList = null;
        resetValuesForNewQuery();

        try {
            final String sql = Sql.SELECT_FULL.getName() + QuestionTable.TABLE_NAME.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            questionList = new ArrayList<>();
            while (resultSet.next()) {
                final Questao question = new Questao();
                question.setId(resultSet.getLong(QuestionTable.ID_COLUMN.getName()));
                question.setPergunta(resultSet.getString(QuestionTable.QUESTION_COLUMN.getName()));
                question.setAlternativaA(resultSet.getString(QuestionTable.ALTERNATIVE_A_COLUMN.getName()));
                question.setAlternativaB(resultSet.getString(QuestionTable.ALTERNATIVE_B_COLUMN.getName()));
                question.setAlternativaC(resultSet.getString(QuestionTable.ALTERNATIVE_C_COLUMN.getName()));
                question.setAlternativaD(resultSet.getString(QuestionTable.ALTERNATIVE_D_COLUMN.getName()));
                question.setResposta(resultSet.getString(QuestionTable.ANSWER_COLUMN.getName()));
                question.setNivel(resultSet.getInt(QuestionTable.LEVEL_COLUMN.getName()));

                questionList.add(question);
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

        return questionList;
    }

    @Override
    public Object readById(final long id) {
        Questao question = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + QuestionTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += QuestionTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                question = new Questao();
                question.setId(resultSet.getLong(QuestionTable.ID_COLUMN.getName()));
                question.setPergunta(resultSet.getString(QuestionTable.QUESTION_COLUMN.getName()));
                question.setAlternativaA(resultSet.getString(QuestionTable.ALTERNATIVE_A_COLUMN.getName()));
                question.setAlternativaB(resultSet.getString(QuestionTable.ALTERNATIVE_B_COLUMN.getName()));
                question.setAlternativaC(resultSet.getString(QuestionTable.ALTERNATIVE_C_COLUMN.getName()));
                question.setAlternativaD(resultSet.getString(QuestionTable.ALTERNATIVE_D_COLUMN.getName()));
                question.setResposta(resultSet.getString(QuestionTable.ANSWER_COLUMN.getName()));
                question.setNivel(resultSet.getInt(QuestionTable.LEVEL_COLUMN.getName()));
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

        return question;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = Sql.INSERT.getName();
            sql += QuestionTable.TABLE_NAME.getName() + Sql.OPEN_PARENTHESIS.getName();
            sql += QuestionTable.QUESTION_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += QuestionTable.ALTERNATIVE_A_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += QuestionTable.ALTERNATIVE_B_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += QuestionTable.ALTERNATIVE_C_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += QuestionTable.ALTERNATIVE_D_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += QuestionTable.ANSWER_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += QuestionTable.LEVEL_COLUMN.getName() + Sql.CLOSE_PARENTHESIS.getName();
            sql += Sql.VALUES.getName();
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // pergunta
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // alternativa A
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // alternativa B
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // alternativa C
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // alternativa D
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // resposta
            sql += Sql.LAST_PARAM_INSERT_TO_COMPLETE.getName(); // nivel

            preparForReadingOrCreating(sql, true, true);

            final Questao question = (Questao) entity;
            int i = 1;
            preparedStatement.setString(i++, question.getPergunta());
            preparedStatement.setString(i++, question.getAlternativaA());
            preparedStatement.setString(i++, question.getAlternativaB());
            preparedStatement.setString(i++, question.getAlternativaC());
            preparedStatement.setString(i++, question.getAlternativaD());
            preparedStatement.setString(i++, question.getResposta());
            preparedStatement.setInt(i++, question.getNivel());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(QuestionTable.ID_COLUMN.getName());
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
            String sql = Sql.UPDATE.getName() + DataBaseHelper.QuestionTable.TABLE_NAME.getName() + Sql.SET_UPDATE.getName();
            sql += QuestionTable.QUESTION_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += QuestionTable.ALTERNATIVE_A_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += QuestionTable.ALTERNATIVE_B_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += QuestionTable.ALTERNATIVE_C_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += QuestionTable.ALTERNATIVE_D_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += QuestionTable.ANSWER_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += QuestionTable.LEVEL_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName();
            sql += Sql.WHERE.getName();
            sql += QuestionTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";
            preparForUpdateOrDelete(sql);

            final Questao question = (Questao) entity;
            int i = 1;
            preparedStatement.setString(i++, question.getPergunta());
            preparedStatement.setString(i++, question.getAlternativaA());
            preparedStatement.setString(i++, question.getAlternativaB());
            preparedStatement.setString(i++, question.getAlternativaC());
            preparedStatement.setString(i++, question.getAlternativaD());
            preparedStatement.setString(i++, question.getResposta());
            preparedStatement.setInt(i++, question.getNivel());
            preparedStatement.setLong(i++, question.getId());

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
            String sql = Sql.DELETE.getName() + QuestionTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += QuestionTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

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
        List<Questao> questionList = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + QuestionTable.TABLE_NAME.getName();
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            questionList = new ArrayList<>();
            while (resultSet.next()) {
                final Questao question = new Questao();
                question.setId(resultSet.getLong(QuestionTable.ID_COLUMN.getName()));
                question.setId(resultSet.getLong(QuestionTable.ID_COLUMN.getName()));
                question.setPergunta(resultSet.getString(QuestionTable.QUESTION_COLUMN.getName()));
                question.setAlternativaA(resultSet.getString(QuestionTable.ALTERNATIVE_A_COLUMN.getName()));
                question.setAlternativaB(resultSet.getString(QuestionTable.ALTERNATIVE_B_COLUMN.getName()));
                question.setAlternativaC(resultSet.getString(QuestionTable.ALTERNATIVE_C_COLUMN.getName()));
                question.setAlternativaD(resultSet.getString(QuestionTable.ALTERNATIVE_D_COLUMN.getName()));
                question.setResposta(resultSet.getString(QuestionTable.ANSWER_COLUMN.getName()));
                question.setNivel(resultSet.getInt(QuestionTable.LEVEL_COLUMN.getName()));
                questionList.add(question);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDao.class.getName());
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return questionList;
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
            throw new SQLException("Verificar metodo newReadOrCreateInstances");
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

    @Override
    public void resetValuesForNewQuery() {
        connection = null;
        preparedStatement = null;
        resultSet = null;
    }
}
