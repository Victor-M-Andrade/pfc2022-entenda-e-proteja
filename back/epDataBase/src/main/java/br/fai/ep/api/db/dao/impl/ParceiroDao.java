package br.fai.ep.api.db.dao.impl;

import br.fai.ep.api.db.connection.ConnectionFactory;
import br.fai.ep.api.db.dao.BaseDao;
import br.fai.ep.api.db.helper.DataBaseHelper.PartinerTable;
import br.fai.ep.api.db.helper.DataBaseHelper.Sql;
import br.fai.ep.api.db.helper.DataBaseHelper.UserTable;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.entities.Parceiro;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ParceiroDao implements BaseDao {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public List<? extends BasePojo> readAll() {
        List<Parceiro> partnerList = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + PartinerTable.TABLE_NAME.getName();
            sql += Sql.INNER_JOIN.getName() + UserTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += PartinerTable.SHORT_TABLE_NAME.getName() + PartinerTable.ID_USER_COLUMN.getName();
            sql += Sql.EQUAL_COMPATION.getName();
            sql += UserTable.SHORT_TABLE_NAME.getName() + UserTable.ID_COLUMN.getName();

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            partnerList = new ArrayList<>();
            while (resultSet.next()) {
                final Parceiro partner = new Parceiro();
                // on the user
                partner.setId(resultSet.getLong(UserTable.ID_COLUMN.getName()));
                partner.setNome(resultSet.getString(UserTable.NAME_COLUMN.getName()));
                partner.setTipo(resultSet.getString(UserTable.TYPE_COLUMN.getName()));
                partner.setEmail(resultSet.getString(UserTable.EMAIL_COLUMN.getName()));
                partner.setSenha(resultSet.getString(UserTable.PASSWORD_COLUMN.getName()));
                partner.setAceite(resultSet.getBoolean(UserTable.ACCEPT_COLUMN.getName()));
                partner.setDataHora(resultSet.getTimestamp(UserTable.DATE_TIME_COLUMN.getName()));

                // on the partner
                partner.setCnpj(resultSet.getString(PartinerTable.CNPJ_COLUMN.getName()));
                partner.setWebsite(resultSet.getString(PartinerTable.WEBSITE_COLUMN.getName()));
                partner.setSituacao(resultSet.getString(PartinerTable.SITUATION_COLUMN.getName()));
                partner.setDescricao(resultSet.getString(PartinerTable.DESCRIPTION_COLUMN.getName()));
                partner.setNomeEmpresa(resultSet.getString(PartinerTable.COMPANY_NAME_COLUMN.getName()));
                partner.setIdUsuario(resultSet.getLong(PartinerTable.SERVICE_TYPE_COLUMN.getName()));

                partnerList.add(partner);
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

        return partnerList;
    }

    @Override
    public Object readById(final long id) {
        Parceiro partner = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + PartinerTable.TABLE_NAME.getName();
            sql += Sql.INNER_JOIN.getName() + UserTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += PartinerTable.SHORT_TABLE_NAME.getName() + PartinerTable.ID_USER_COLUMN.getName();
            sql += Sql.EQUAL_COMPATION.getName();
            sql += UserTable.SHORT_TABLE_NAME.getName() + UserTable.ID_COLUMN.getName();
            sql += Sql.AND.getName();
            sql += PartinerTable.SHORT_TABLE_NAME.getName() + PartinerTable.ID_COLUMN.getName();
            sql += Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName();

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                partner = new Parceiro();
                // on the user
                partner.setId(resultSet.getLong(UserTable.ID_COLUMN.getName()));
                partner.setNome(resultSet.getString(UserTable.NAME_COLUMN.getName()));
                partner.setTipo(resultSet.getString(UserTable.TYPE_COLUMN.getName()));
                partner.setEmail(resultSet.getString(UserTable.EMAIL_COLUMN.getName()));
                partner.setSenha(resultSet.getString(UserTable.PASSWORD_COLUMN.getName()));
                partner.setAceite(resultSet.getBoolean(UserTable.ACCEPT_COLUMN.getName()));
                partner.setDataHora(resultSet.getTimestamp(UserTable.DATE_TIME_COLUMN.getName()));

                // on the partner
                partner.setCnpj(resultSet.getString(PartinerTable.CNPJ_COLUMN.getName()));
                partner.setWebsite(resultSet.getString(PartinerTable.WEBSITE_COLUMN.getName()));
                partner.setSituacao(resultSet.getString(PartinerTable.SITUATION_COLUMN.getName()));
                partner.setDescricao(resultSet.getString(PartinerTable.DESCRIPTION_COLUMN.getName()));
                partner.setNomeEmpresa(resultSet.getString(PartinerTable.COMPANY_NAME_COLUMN.getName()));
                partner.setIdUsuario(resultSet.getLong(PartinerTable.SERVICE_TYPE_COLUMN.getName()));
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

        return partner;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = Sql.INSERT.getName();
            sql += PartinerTable.TABLE_NAME.getName() + Sql.OPEN_PARENTHESIS.getName();
            sql += PartinerTable.CNPJ_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += PartinerTable.WEBSITE_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += PartinerTable.SITUATION_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += PartinerTable.DESCRIPTION_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += PartinerTable.SERVICE_TYPE_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += PartinerTable.COMPANY_NAME_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += PartinerTable.ID_USER_COLUMN.getName() + Sql.CLOSE_PARENTHESIS.getName();
            sql += Sql.VALUES.getName();
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // cnpj
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // website
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // situacao
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // descricao
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // tipo_servico
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // nome_empresa
            sql += Sql.LAST_PARAM_INSERT_TO_COMPLETE.getName(); //id_usuario

            preparForReadingOrCreating(sql, true, true);

            final Parceiro partner = (Parceiro) entity;
            int i = 1;
            preparedStatement.setString(i++, partner.getCnpj());
            preparedStatement.setString(i++, partner.getWebsite());
            preparedStatement.setString(i++, Parceiro.PartinerSituation.REQUESTED.getName());
            preparedStatement.setString(i++, partner.getDescricao());
            preparedStatement.setString(i++, partner.getTipoServico());
            preparedStatement.setString(i++, partner.getNomeEmpresa());
            preparedStatement.setLong(i++, partner.getIdUsuario());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(PartinerTable.ID_COLUMN.getName());
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
            String sql = Sql.UPDATE.getName() + PartinerTable.TABLE_NAME.getName() + Sql.SET_UPDATE.getName();
            sql += PartinerTable.CNPJ_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += PartinerTable.WEBSITE_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += PartinerTable.SITUATION_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += PartinerTable.DESCRIPTION_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += PartinerTable.COMPANY_NAME_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += PartinerTable.SERVICE_TYPE_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName();
            sql += Sql.WHERE.getName();
            sql += PartinerTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";
            preparForUpdateOrDelete(sql);

            final Parceiro client = (Parceiro) entity;
            int i = 1;
            preparedStatement.setString(i++, client.getCnpj());
            preparedStatement.setString(i++, client.getWebsite());
            preparedStatement.setString(i++, client.getSituacao());
            preparedStatement.setString(i++, client.getDescricao());
            preparedStatement.setString(i++, client.getNomeEmpresa());
            preparedStatement.setString(i++, client.getTipoServico());
            preparedStatement.setLong(i++, client.getId());

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
            String sql = Sql.DELETE.getName() + PartinerTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += PartinerTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

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
        List<Parceiro> partnerList = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + PartinerTable.TABLE_NAME.getName();
            sql += Sql.INNER_JOIN.getName() + UserTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += PartinerTable.SHORT_TABLE_NAME.getName() + PartinerTable.ID_USER_COLUMN.getName();
            sql += Sql.EQUAL_COMPATION.getName();
            sql += UserTable.SHORT_TABLE_NAME.getName() + UserTable.ID_COLUMN.getName();
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            partnerList = new ArrayList<>();
            while (resultSet.next()) {
                final Parceiro partner = new Parceiro();
                // on the user
                partner.setId(resultSet.getLong(UserTable.ID_COLUMN.getName()));
                partner.setNome(resultSet.getString(UserTable.NAME_COLUMN.getName()));
                partner.setTipo(resultSet.getString(UserTable.TYPE_COLUMN.getName()));
                partner.setEmail(resultSet.getString(UserTable.EMAIL_COLUMN.getName()));
                partner.setSenha(resultSet.getString(UserTable.PASSWORD_COLUMN.getName()));
                partner.setAceite(resultSet.getBoolean(UserTable.ACCEPT_COLUMN.getName()));
                partner.setDataHora(resultSet.getTimestamp(UserTable.DATE_TIME_COLUMN.getName()));

                // on the partner
                partner.setCnpj(resultSet.getString(PartinerTable.CNPJ_COLUMN.getName()));
                partner.setWebsite(resultSet.getString(PartinerTable.WEBSITE_COLUMN.getName()));
                partner.setSituacao(resultSet.getString(PartinerTable.SITUATION_COLUMN.getName()));
                partner.setDescricao(resultSet.getString(PartinerTable.DESCRIPTION_COLUMN.getName()));
                partner.setNomeEmpresa(resultSet.getString(PartinerTable.COMPANY_NAME_COLUMN.getName()));
                partner.setIdUsuario(resultSet.getLong(PartinerTable.SERVICE_TYPE_COLUMN.getName()));

                partnerList.add(partner);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDao.class.getName());
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return partnerList;
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
