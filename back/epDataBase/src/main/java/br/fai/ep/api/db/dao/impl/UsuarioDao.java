package br.fai.ep.api.db.dao.impl;

import br.fai.ep.api.db.connection.ConnectionFactory;
import br.fai.ep.api.db.dao.BaseDao;
import br.fai.ep.api.db.helper.DataBaseHelper.Sql;
import br.fai.ep.api.db.helper.DataBaseHelper.UserTable;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.entities.Usuario;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioDao implements BaseDao {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public List<? extends BasePojo> readAll() {
        List<Usuario> userList = null;
        resetValuesForNewQuery();

        try {
            final String sql = Sql.SELECT_FULL.getName() + UserTable.TABLE_NAME.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            userList = new ArrayList<>();
            while (resultSet.next()) {
                final Usuario user = new Usuario();
                user.setId(resultSet.getLong(UserTable.ID_COLUMN.getName()));
                user.setNome(resultSet.getString(UserTable.NAME_COLUMN.getName()));
                user.setTipo(resultSet.getString(UserTable.TYPE_COLUMN.getName()));
                user.setEmail(resultSet.getString(UserTable.EMAIL_COLUMN.getName()));
                user.setSenha(resultSet.getString(UserTable.PASSWORD_COLUMN.getName()));
                user.setAceite(resultSet.getBoolean(UserTable.ACCEPT_COLUMN.getName()));
                user.setDataHora(resultSet.getTimestamp(UserTable.DATE_TIME_COLUMN.getName()));

                userList.add(user);
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

        return userList;
    }

    @Override
    public Object readById(final long id) {
        Usuario user = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + UserTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += UserTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new Usuario();
                user.setId(resultSet.getLong(UserTable.ID_COLUMN.getName()));
                user.setNome(resultSet.getString(UserTable.NAME_COLUMN.getName()));
                user.setTipo(resultSet.getString(UserTable.TYPE_COLUMN.getName()));
                user.setEmail(resultSet.getString(UserTable.EMAIL_COLUMN.getName()));
                user.setSenha(resultSet.getString(UserTable.PASSWORD_COLUMN.getName()));
                user.setAceite(resultSet.getBoolean(UserTable.ACCEPT_COLUMN.getName()));
                user.setDataHora(resultSet.getTimestamp(UserTable.DATE_TIME_COLUMN.getName()));
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

        return user;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = Sql.INSERT.getName();
            sql += UserTable.TABLE_NAME.getName() + Sql.OPEN_PARENTHESIS.getName();
            sql += UserTable.NAME_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += UserTable.TYPE_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += UserTable.EMAIL_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += UserTable.PASSWORD_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += UserTable.ACCEPT_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += UserTable.DATE_TIME_COLUMN.getName() + Sql.CLOSE_PARENTHESIS.getName();
            sql += Sql.VALUES.getName();
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // nome
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); //tipo
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // email
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // senha
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // aceite
            sql += Sql.DEFAULT_VALUE_DECLARTION.getName() + Sql.FINAL_CLOSE_PARENTHESIS.getName();

            preparForReadingOrCreating(sql, true, true);

            final Usuario user = (Usuario) entity;
            int i = 1;
            preparedStatement.setString(i++, user.getNome());
            preparedStatement.setString(i++, user.getTipo());
            preparedStatement.setString(i++, user.getEmail());
            preparedStatement.setString(i++, user.getSenha());
            preparedStatement.setBoolean(i++, user.isAceite());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(UserTable.ID_COLUMN.getName());
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
            String sql = Sql.UPDATE.getName() + UserTable.TABLE_NAME.getName() + Sql.SET_UPDATE.getName();
            sql += UserTable.NAME_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += UserTable.TYPE_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += UserTable.EMAIL_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += UserTable.PASSWORD_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName();
            sql += Sql.WHERE.getName();
            sql += UserTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";
            preparForUpdateOrDelete(sql);

            final Usuario user = (Usuario) entity;
            int i = 1;
            preparedStatement.setString(i++, user.getNome());
            preparedStatement.setString(i++, user.getTipo());
            preparedStatement.setString(i++, user.getEmail());
            preparedStatement.setString(i++, user.getSenha());
            preparedStatement.setLong(i++, user.getId());

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
            String sql = Sql.DELETE.getName() + UserTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += UserTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

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
        List<Usuario> userList = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + UserTable.TABLE_NAME.getName();
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            userList = new ArrayList<>();
            while (resultSet.next()) {
                final Usuario user = new Usuario();
                user.setId(resultSet.getLong(UserTable.ID_COLUMN.getName()));
                user.setNome(resultSet.getString(UserTable.NAME_COLUMN.getName()));
                user.setTipo(resultSet.getString(UserTable.TYPE_COLUMN.getName()));
                user.setEmail(resultSet.getString(UserTable.EMAIL_COLUMN.getName()));
                user.setSenha(resultSet.getString(UserTable.PASSWORD_COLUMN.getName()));
                user.setAceite(resultSet.getBoolean(UserTable.ACCEPT_COLUMN.getName()));
                user.setDataHora(resultSet.getTimestamp(UserTable.DATE_TIME_COLUMN.getName()));

                userList.add(user);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDao.class.getName());
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return userList;
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
