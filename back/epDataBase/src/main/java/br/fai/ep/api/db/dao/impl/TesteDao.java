package br.fai.ep.api.db.dao.impl;

import br.fai.ep.api.db.connection.ConnectionFactory;
import br.fai.ep.api.db.dao.BaseDao;
import br.fai.ep.api.db.helper.DataBaseHelper;
import br.fai.ep.api.db.helper.DataBaseHelper.Sql;
import br.fai.ep.api.db.helper.DataBaseHelper.TestTable;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.entities.Teste;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TesteDao implements BaseDao {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public List<? extends BasePojo> readAll() {
        List<Teste> testList = null;
        resetValuesForNewQuery();

        try {
            final String sql = Sql.SELECT_FULL.getName() + TestTable.TABLE_NAME.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            testList = new ArrayList<>();
            while (resultSet.next()) {
                final Teste test = new Teste();
                test.setId(resultSet.getLong(TestTable.ID_COLUMN.getName()));
                test.setDataHora(resultSet.getTimestamp(TestTable.DATE_TIME_COLUMN.getName()));
                test.setAcertos(resultSet.getInt(TestTable.HIT_COLUMN.getName()));
                test.setIdUsuario(resultSet.getLong(TestTable.ID_USER_COLUMN.getName()));

                testList.add(test);
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

        return testList;
    }

    @Override
    public Object readById(final long id) {
        Teste test = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + TestTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += TestTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                test = new Teste();
                test.setId(resultSet.getLong(TestTable.ID_COLUMN.getName()));
                test.setDataHora(resultSet.getTimestamp(TestTable.DATE_TIME_COLUMN.getName()));
                test.setAcertos(resultSet.getInt(TestTable.HIT_COLUMN.getName()));
                test.setIdUsuario(resultSet.getLong(TestTable.ID_USER_COLUMN.getName()));
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

        return test;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = Sql.INSERT.getName();
            sql += TestTable.TABLE_NAME.getName() + Sql.OPEN_PARENTHESIS.getName();
            sql += TestTable.DATE_TIME_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += TestTable.HIT_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += TestTable.ID_USER_COLUMN.getName() + Sql.CLOSE_PARENTHESIS.getName();
            sql += Sql.VALUES.getName();
            sql += Sql.DEFAULT_VALUE_DECLARTION.getName() + Sql.SEPARATOR.getName(); // data_hora
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // qtd_acerto
            sql += Sql.LAST_PARAM_INSERT_TO_COMPLETE.getName(); // id_usuario

            preparForReadingOrCreating(sql, true, true);

            final Teste test = (Teste) entity;
            int i = 1;
            preparedStatement.setTimestamp(i++, test.getDataHora());
            preparedStatement.setInt(i++, test.getAcertos());
            preparedStatement.setLong(i++, test.getIdUsuario());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(TestTable.ID_COLUMN.getName());
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
            String sql = Sql.UPDATE.getName() + DataBaseHelper.TestTable.TABLE_NAME.getName() + Sql.SET_UPDATE.getName();
            sql += TestTable.DATE_TIME_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += TestTable.HIT_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName();
            sql += Sql.WHERE.getName();
            sql += TestTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";
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
            String sql = Sql.DELETE.getName() + TestTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += TestTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

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
        List<Teste> testList = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + TestTable.TABLE_NAME.getName();
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            testList = new ArrayList<>();
            while (resultSet.next()) {
                final Teste test = new Teste();
                test.setId(resultSet.getLong(TestTable.ID_COLUMN.getName()));
                test.setDataHora(resultSet.getTimestamp(TestTable.DATE_TIME_COLUMN.getName()));
                test.setAcertos(resultSet.getInt(TestTable.HIT_COLUMN.getName()));
                test.setIdUsuario(resultSet.getLong(TestTable.ID_USER_COLUMN.getName()));

                testList.add(test);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDao.class.getName());
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return testList;
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
