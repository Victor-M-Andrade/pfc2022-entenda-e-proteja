package br.fai.ep.db.dao.impl;

import br.fai.ep.db.connection.ConnectionFactory;
import br.fai.ep.db.dao.BaseDao;
import br.fai.ep.db.dao.BaseDaoInterface;
import br.fai.ep.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epEntities.Usuario.USER_TABLE;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioDaoImpl extends BaseDao implements BaseDaoInterface {

    @Override
    public List<? extends BasePojo> readAll() {
        List<Usuario> userList = null;
        resetValuesForNewQuery();

        try {
            final String sql = SQL_COMMAND.SELECT_FULL + USER_TABLE.TABLE_NAME + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            userList = new ArrayList<>();
            while (resultSet.next()) {
                final Usuario user = new Usuario();
                user.setId(resultSet.getLong(USER_TABLE.ID_COLUMN));
                user.setNome(resultSet.getString(USER_TABLE.NAME_COLUMN));
                user.setEmail(resultSet.getString(USER_TABLE.EMAIL_COLUMN));
                user.setSenha(resultSet.getString(USER_TABLE.PASSWORD_COLUMN));
                user.setPathImageProfile(resultSet.getString(USER_TABLE.PATH_IMG_PROFILE));
                user.setAceite(resultSet.getBoolean(USER_TABLE.ACCEPT_COLUMN));
                user.setAutor(resultSet.getBoolean(USER_TABLE.IS_AUTHOR_COLUMN));
                user.setParceiro(resultSet.getBoolean(USER_TABLE.IS_PARTNER_COLUMN));
                user.setAnonimo(resultSet.getBoolean(USER_TABLE.IS_ANONYMOUS_COLUMN));
                user.setDataHora(resultSet.getTimestamp(USER_TABLE.DATE_TIME_COLUMN));

                userList.add(user);
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

        return userList;
    }

    @Override
    public Object readById(final long id) {
        Usuario user = null;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + USER_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += USER_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new Usuario();
                user.setId(resultSet.getLong(USER_TABLE.ID_COLUMN));
                user.setNome(resultSet.getString(USER_TABLE.NAME_COLUMN));
                user.setEmail(resultSet.getString(USER_TABLE.EMAIL_COLUMN));
                user.setSenha(resultSet.getString(USER_TABLE.PASSWORD_COLUMN));
                user.setPathImageProfile(resultSet.getString(USER_TABLE.PATH_IMG_PROFILE));
                user.setAceite(resultSet.getBoolean(USER_TABLE.ACCEPT_COLUMN));
                user.setAutor(resultSet.getBoolean(USER_TABLE.IS_AUTHOR_COLUMN));
                user.setParceiro(resultSet.getBoolean(USER_TABLE.IS_PARTNER_COLUMN));
                user.setAnonimo(resultSet.getBoolean(USER_TABLE.IS_ANONYMOUS_COLUMN));
                user.setDataHora(resultSet.getTimestamp(USER_TABLE.DATE_TIME_COLUMN));
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

        return user;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = SQL_COMMAND.INSERT;
            sql += USER_TABLE.TABLE_NAME + SQL_COMMAND.OPEN_PARENTHESIS;
            sql += USER_TABLE.NAME_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += USER_TABLE.EMAIL_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += USER_TABLE.PASSWORD_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += USER_TABLE.PATH_IMG_PROFILE + SQL_COMMAND.SEPARATOR;
            sql += USER_TABLE.IS_AUTHOR_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += USER_TABLE.IS_PARTNER_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += USER_TABLE.IS_ANONYMOUS_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += USER_TABLE.ACCEPT_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += USER_TABLE.DATE_TIME_COLUMN + SQL_COMMAND.CLOSE_PARENTHESIS;
            sql += SQL_COMMAND.VALUES;
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // nome
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // email
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // senha
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // pathImgProfile
            sql += SQL_COMMAND.DEFAULT_VALUE_DECLARTION + SQL_COMMAND.SEPARATOR; // isAutor
            sql += SQL_COMMAND.DEFAULT_VALUE_DECLARTION + SQL_COMMAND.SEPARATOR; // isParceiro
            sql += SQL_COMMAND.DEFAULT_VALUE_DECLARTION + SQL_COMMAND.SEPARATOR; // isAnonimo
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // aceite
            sql += SQL_COMMAND.DEFAULT_VALUE_DECLARTION + SQL_COMMAND.FINAL_CLOSE_PARENTHESIS; //data_hora

            preparForReadingOrCreating(sql, true, false);

            final Usuario user = (Usuario) entity;
            int i = 1;
            preparedStatement.setString(i++, user.getNome());
            preparedStatement.setString(i++, user.getEmail());
            preparedStatement.setString(i++, user.getSenha());
            preparedStatement.setString(i++, user.getPathImageProfile());
            preparedStatement.setBoolean(i++, user.isAceite());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(USER_TABLE.ID_COLUMN);
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
        boolean isUpdateCompleted = false;

        try {
            String sql = SQL_COMMAND.UPDATE + USER_TABLE.TABLE_NAME + SQL_COMMAND.SET_UPDATE;
            sql += USER_TABLE.NAME_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += USER_TABLE.EMAIL_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += USER_TABLE.PASSWORD_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += USER_TABLE.PATH_IMG_PROFILE + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += USER_TABLE.IS_AUTHOR_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += USER_TABLE.IS_PARTNER_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += USER_TABLE.IS_ANONYMOUS_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;
            sql += SQL_COMMAND.WHERE;
            sql += USER_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";
            preparForUpdateOrDelete(sql);

            final Usuario user = (Usuario) entity;
            int i = 1;
            preparedStatement.setString(i++, user.getNome());
            preparedStatement.setString(i++, user.getEmail());
            preparedStatement.setString(i++, user.getSenha());
            preparedStatement.setString(i++, user.getPathImageProfile());
            preparedStatement.setBoolean(i++, user.isAutor());
            preparedStatement.setBoolean(i++, user.isParceiro());
            preparedStatement.setBoolean(i++, user.isAnonimo());
            preparedStatement.setLong(i++, user.getId());

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
        boolean isDeleteCompleted = false;

        try {
            String sql = SQL_COMMAND.DELETE + USER_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += USER_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

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
        List<Usuario> userList = null;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + USER_TABLE.TABLE_NAME;
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            userList = new ArrayList<>();
            while (resultSet.next()) {
                final Usuario user = new Usuario();
                user.setId(resultSet.getLong(USER_TABLE.ID_COLUMN));
                user.setNome(resultSet.getString(USER_TABLE.NAME_COLUMN));
                user.setEmail(resultSet.getString(USER_TABLE.EMAIL_COLUMN));
                user.setSenha(resultSet.getString(USER_TABLE.PASSWORD_COLUMN));
                user.setPathImageProfile(resultSet.getString(USER_TABLE.PATH_IMG_PROFILE));
                user.setAceite(resultSet.getBoolean(USER_TABLE.ACCEPT_COLUMN));
                user.setAutor(resultSet.getBoolean(USER_TABLE.IS_AUTHOR_COLUMN));
                user.setParceiro(resultSet.getBoolean(USER_TABLE.IS_PARTNER_COLUMN));
                user.setAnonimo(resultSet.getBoolean(USER_TABLE.IS_ANONYMOUS_COLUMN));
                user.setDataHora(resultSet.getTimestamp(USER_TABLE.DATE_TIME_COLUMN));

                userList.add(user);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return userList;
    }
}