package br.fai.ep.db.dao.impl;

import br.fai.ep.db.connection.ConnectionFactory;
import br.fai.ep.db.dao.BaseDao;
import br.fai.ep.db.dao.QuestTestDaoInterface;
import br.fai.ep.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.epEntities.DTO.QuestionDto;
import br.fai.ep.epEntities.Questao;
import br.fai.ep.epEntities.QuestaoTeste;
import br.fai.ep.epEntities.QuestaoTeste.QUESTION_TEST_TABLE;
import br.fai.ep.epEntities.Teste;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestaoTesteDaoImpl extends BaseDao implements QuestTestDaoInterface {

    @Override
    public List<QuestaoTeste> readAll() {
        List<QuestaoTeste> questionTestList;
        resetValuesForNewQuery();

        try {
            final String sql = SQL_COMMAND.SELECT_FULL + QUESTION_TEST_TABLE.TABLE_NAME + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            questionTestList = new ArrayList<>();
            while (resultSet.next()) {
                final QuestaoTeste questionTest = new QuestaoTeste();
                questionTest.setId(resultSet.getLong(QUESTION_TEST_TABLE.ID_COLUMN));
                questionTest.setEscolha(resultSet.getString(QUESTION_TEST_TABLE.CHOICE_COLUMN));
                questionTest.setIdQuestao(resultSet.getLong(QUESTION_TEST_TABLE.ID_QUEST_COLUMN));
                questionTest.setIdTeste(resultSet.getLong(QUESTION_TEST_TABLE.ID_TEST_COLUMN));

                questionTestList.add(questionTest);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAll | classe: " + QuestaoTesteDaoImpl.class);
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return questionTestList;
    }

    @Override
    public QuestaoTeste readById(final long id) {
        QuestaoTeste questionTest = null;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + QUESTION_TEST_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += QUESTION_TEST_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                questionTest = new QuestaoTeste();
                questionTest.setId(resultSet.getLong(QUESTION_TEST_TABLE.ID_COLUMN));
                questionTest.setEscolha(resultSet.getString(QUESTION_TEST_TABLE.CHOICE_COLUMN));
                questionTest.setIdQuestao(resultSet.getLong(QUESTION_TEST_TABLE.ID_QUEST_COLUMN));
                questionTest.setIdTeste(resultSet.getLong(QUESTION_TEST_TABLE.ID_TEST_COLUMN));

            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readById | classe: " + QuestaoTesteDaoImpl.class);
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return questionTest;
    }

    @Override
    public long create(final List<QuestionDto> questTestList) {
        if (questTestList == null || questTestList.isEmpty()) {
            return -1;
        }

        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);
        final String valuesFormat = "('%s', %d, %d),";
        try {
            String sql = SQL_COMMAND.INSERT;
            sql += QUESTION_TEST_TABLE.TABLE_NAME + SQL_COMMAND.OPEN_PARENTHESIS;
            sql += QUESTION_TEST_TABLE.CHOICE_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += QUESTION_TEST_TABLE.ID_QUEST_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += QUESTION_TEST_TABLE.ID_TEST_COLUMN + SQL_COMMAND.CLOSE_PARENTHESIS;
            sql += "VALUES ";
            for (final QuestionDto question : questTestList) {
                sql += String.format(valuesFormat, question.getUserAswer(), question.getId(), question.getTestId());
            }
            sql = sql.substring(0, sql.length() - 1) + ";";

            preparForReadingOrCreating(sql, true, false);
            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(QUESTION_TEST_TABLE.ID_COLUMN);
            }

            if (newId == -1) {
                connection.rollback();
                return -1;
            }
            connection.commit();

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:create | classe: " + QuestaoTesteDaoImpl.class);
            System.out.println(e.getMessage());
            newId = -1;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return newId;
    }

    @Override
    public boolean update(final QuestaoTeste entity) {
        resetValuesForNewQuery();
        boolean isUpdateCompleted;

        try {
            String sql = SQL_COMMAND.UPDATE + QUESTION_TEST_TABLE.TABLE_NAME + SQL_COMMAND.SET_UPDATE;
            sql += QUESTION_TEST_TABLE.CHOICE_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += QUESTION_TEST_TABLE.ID_QUEST_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += QUESTION_TEST_TABLE.ID_TEST_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;
            sql += SQL_COMMAND.WHERE;
            sql += QUESTION_TEST_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";
            preparForUpdateOrDelete(sql);

            final QuestaoTeste questionTest = entity;
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
            System.out.println("Excecao -> metodo:update | classe: " + QuestaoTesteDaoImpl.class);
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
            String sql = SQL_COMMAND.DELETE + QUESTION_TEST_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += QUESTION_TEST_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForUpdateOrDelete(sql);
            preparedStatement.setLong(1, id);

            preparedStatement.execute();
            connection.commit();
            isDeleteCompleted = true;
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:delete | classe: " + QuestaoTesteDaoImpl.class);

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
    public List<QuestaoTeste> readByCriteria(final String criteria) {
        List<QuestaoTeste> questionTestList;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + QUESTION_TEST_TABLE.TABLE_NAME;
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            questionTestList = new ArrayList<>();
            while (resultSet.next()) {
                final QuestaoTeste questionTest = new QuestaoTeste();
                questionTest.setId(resultSet.getLong(QUESTION_TEST_TABLE.ID_COLUMN));
                questionTest.setEscolha(resultSet.getString(QUESTION_TEST_TABLE.CHOICE_COLUMN));
                questionTest.setIdQuestao(resultSet.getLong(QUESTION_TEST_TABLE.ID_QUEST_COLUMN));
                questionTest.setIdTeste(resultSet.getLong(QUESTION_TEST_TABLE.ID_TEST_COLUMN));

                questionTestList.add(questionTest);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + QuestaoTesteDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return questionTestList;
    }

    @Override
    public List<Teste> readAllTestsByQuestion(final long questionId) {
        List<Teste> testList = null;
        resetValuesForNewQuery();

        try {
            final String sql = "select T.* from teste as T inner join teste_questao as TQ ON TQ.id_teste = T.id where TQ.id_questao = ?;";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, questionId);
            resultSet = preparedStatement.executeQuery();

            testList = new ArrayList<>();
            while (resultSet.next()) {
                final Teste test = new Teste();
                test.setId(resultSet.getLong(Teste.TEST_TABLE.ID_COLUMN));
                test.setDataHora(resultSet.getTimestamp(Teste.TEST_TABLE.DATE_TIME_COLUMN));
                test.setAcertos(resultSet.getInt(Teste.TEST_TABLE.HIT_COLUMN));
                test.setIdUsuario(resultSet.getLong(Teste.TEST_TABLE.ID_USER_COLUMN));

                testList.add(test);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAll | readAllTestsByQuestion: " + QuestaoTesteDaoImpl.class);
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
    public List<QuestionDto> readAllQuestionsByTest(final long testId) {
        List<QuestionDto> testList = null;
        resetValuesForNewQuery();

        try {
            final String sql = "select Q.*, TQ.id_teste, TQ.escolha from questao as Q inner join teste_questao TQ ON TQ.id_questao = Q.id where TQ.id_teste = ?;";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, testId);
            resultSet = preparedStatement.executeQuery();

            testList = new ArrayList<>();
            while (resultSet.next()) {
                final QuestionDto question = new QuestionDto();
                question.setId(resultSet.getLong(Questao.QUESTION_TABLE.ID_COLUMN));
                question.setPergunta(resultSet.getString(Questao.QUESTION_TABLE.QUESTION_COLUMN));
                question.setAlternativaA(resultSet.getString(Questao.QUESTION_TABLE.ALTERNATIVE_A_COLUMN));
                question.setAlternativaB(resultSet.getString(Questao.QUESTION_TABLE.ALTERNATIVE_B_COLUMN));
                question.setAlternativaC(resultSet.getString(Questao.QUESTION_TABLE.ALTERNATIVE_C_COLUMN));
                question.setAlternativaD(resultSet.getString(Questao.QUESTION_TABLE.ALTERNATIVE_D_COLUMN));
                question.setResposta(resultSet.getString(Questao.QUESTION_TABLE.ANSWER_COLUMN));
                question.setNivel(resultSet.getInt(Questao.QUESTION_TABLE.LEVEL_COLUMN));
                question.setUserAswer(resultSet.getString(QUESTION_TEST_TABLE.CHOICE_COLUMN));
                question.setTestId(resultSet.getLong(QUESTION_TEST_TABLE.ID_TEST_COLUMN));

                testList.add(question);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAllQuestionsByTest | classe: " + QuestaoTesteDaoImpl.class);
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
}