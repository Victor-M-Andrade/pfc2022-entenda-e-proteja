package br.fai.ep.db.dao.impl;

import br.fai.ep.db.connection.ConnectionFactory;
import br.fai.ep.db.dao.BaseDao;
import br.fai.ep.db.dao.BaseDaoInterface;
import br.fai.ep.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.QuestionDto;
import br.fai.ep.epEntities.Questao;
import br.fai.ep.epEntities.Questao.QUESTION_TABLE;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QuestaoDaoImpl extends BaseDao implements BaseDaoInterface {

    @Override
    public List<? extends BasePojo> readAll() {
        List<br.fai.ep.epEntities.Questao> questionList = null;
        resetValuesForNewQuery();

        try {
            final String sql = SQL_COMMAND.SELECT_FULL + QUESTION_TABLE.TABLE_NAME + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            questionList = new ArrayList<>();
            while (resultSet.next()) {
                final Questao question = new Questao();
                question.setId(resultSet.getLong(QUESTION_TABLE.ID_COLUMN));
                question.setPergunta(resultSet.getString(QUESTION_TABLE.QUESTION_COLUMN));
                question.setAlternativaA(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_A_COLUMN));
                question.setAlternativaB(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_B_COLUMN));
                question.setAlternativaC(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_C_COLUMN));
                question.setAlternativaD(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_D_COLUMN));
                question.setResposta(resultSet.getString(QUESTION_TABLE.ANSWER_COLUMN));
                question.setNivel(resultSet.getInt(QUESTION_TABLE.LEVEL_COLUMN));

                questionList.add(question);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAll | classe: " + QuestaoDaoImpl.class);
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
            String sql = SQL_COMMAND.SELECT_FULL + QUESTION_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += QUESTION_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                question = new Questao();
                question.setId(resultSet.getLong(QUESTION_TABLE.ID_COLUMN));
                question.setPergunta(resultSet.getString(QUESTION_TABLE.QUESTION_COLUMN));
                question.setAlternativaA(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_A_COLUMN));
                question.setAlternativaB(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_B_COLUMN));
                question.setAlternativaC(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_C_COLUMN));
                question.setAlternativaD(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_D_COLUMN));
                question.setResposta(resultSet.getString(QUESTION_TABLE.ANSWER_COLUMN));
                question.setNivel(resultSet.getInt(QUESTION_TABLE.LEVEL_COLUMN));
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readById | classe: " + QuestaoDaoImpl.class);
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
            String sql = SQL_COMMAND.INSERT;
            sql += QUESTION_TABLE.TABLE_NAME + SQL_COMMAND.OPEN_PARENTHESIS;
            sql += QUESTION_TABLE.QUESTION_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += QUESTION_TABLE.ALTERNATIVE_A_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += QUESTION_TABLE.ALTERNATIVE_B_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += QUESTION_TABLE.ALTERNATIVE_C_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += QUESTION_TABLE.ALTERNATIVE_D_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += QUESTION_TABLE.ANSWER_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += QUESTION_TABLE.LEVEL_COLUMN + SQL_COMMAND.CLOSE_PARENTHESIS;
            sql += SQL_COMMAND.VALUES;
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // pergunta
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // alternativa A
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // alternativa B
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // alternativa C
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // alternativa D
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // resposta
            sql += SQL_COMMAND.LAST_PARAM_INSERT_TO_COMPLETE; // nivel

            preparForReadingOrCreating(sql, true, false);

            final Questao question = (br.fai.ep.epEntities.Questao) entity;
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
                newId = resultSet.getLong(QUESTION_TABLE.ID_COLUMN);
            }

            if (newId == -1) {
                connection.rollback();
                return -1;
            }
            connection.commit();

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:create | classe: " + QuestaoDaoImpl.class);
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
            String sql = SQL_COMMAND.UPDATE + QUESTION_TABLE.TABLE_NAME + SQL_COMMAND.SET_UPDATE;
            sql += QUESTION_TABLE.QUESTION_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += QUESTION_TABLE.ALTERNATIVE_A_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += QUESTION_TABLE.ALTERNATIVE_B_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += QUESTION_TABLE.ALTERNATIVE_C_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += QUESTION_TABLE.ALTERNATIVE_D_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += QUESTION_TABLE.ANSWER_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += QUESTION_TABLE.LEVEL_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;
            sql += SQL_COMMAND.WHERE;
            sql += QUESTION_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";
            preparForUpdateOrDelete(sql);

            final Questao question = (br.fai.ep.epEntities.Questao) entity;
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
            System.out.println("Excecao -> metodo:update | classe: " + QuestaoDaoImpl.class);
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
            String sql = SQL_COMMAND.DELETE + QUESTION_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += QUESTION_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForUpdateOrDelete(sql);
            preparedStatement.setLong(1, id);

            preparedStatement.execute();
            connection.commit();
            isDeleteCompleted = true;
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:delete | classe: " + QuestaoDaoImpl.class);

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
        List<br.fai.ep.epEntities.Questao> questionList = null;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + QUESTION_TABLE.TABLE_NAME;
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            questionList = new ArrayList<>();
            while (resultSet.next()) {
                final Questao question = new Questao();
                question.setId(resultSet.getLong(QUESTION_TABLE.ID_COLUMN));
                question.setId(resultSet.getLong(QUESTION_TABLE.ID_COLUMN));
                question.setPergunta(resultSet.getString(QUESTION_TABLE.QUESTION_COLUMN));
                question.setAlternativaA(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_A_COLUMN));
                question.setAlternativaB(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_B_COLUMN));
                question.setAlternativaC(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_C_COLUMN));
                question.setAlternativaD(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_D_COLUMN));
                question.setResposta(resultSet.getString(QUESTION_TABLE.ANSWER_COLUMN));
                question.setNivel(resultSet.getInt(QUESTION_TABLE.LEVEL_COLUMN));
                questionList.add(question);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + QuestaoDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return questionList;
    }

    public List<QuestionDto> readByDtoCritia(final String criteria) {
        List<QuestionDto> questionList = null;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + QUESTION_TABLE.TABLE_NAME;
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            questionList = new ArrayList<>();
            while (resultSet.next()) {
                final QuestionDto question = new QuestionDto();
                question.setId(resultSet.getLong(QUESTION_TABLE.ID_COLUMN));
                question.setPergunta(resultSet.getString(QUESTION_TABLE.QUESTION_COLUMN));
                question.setAlternativaA(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_A_COLUMN));
                question.setAlternativaB(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_B_COLUMN));
                question.setAlternativaC(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_C_COLUMN));
                question.setAlternativaD(resultSet.getString(QUESTION_TABLE.ALTERNATIVE_D_COLUMN));
                question.setResposta(resultSet.getString(QUESTION_TABLE.ANSWER_COLUMN));
                question.setNivel(resultSet.getInt(QUESTION_TABLE.LEVEL_COLUMN));
                questionList.add(question);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + QuestaoDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return questionList;
    }
}