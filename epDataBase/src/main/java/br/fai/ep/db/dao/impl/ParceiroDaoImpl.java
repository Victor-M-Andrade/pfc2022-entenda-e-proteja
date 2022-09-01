package br.fai.ep.db.dao.impl;

import br.fai.ep.db.connection.ConnectionFactory;
import br.fai.ep.db.dao.BaseDao;
import br.fai.ep.db.dao.BaseDaoInterface;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.Parceiro;
import br.fai.ep.epEntities.Parceiro.PARTINER_TABLE;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.db.helper.DataBaseHelper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ParceiroDaoImpl extends BaseDao implements BaseDaoInterface {

    @Override
    public List<? extends BasePojo> readAll() {
        List<Parceiro> partnerList = null;
        resetValuesForNewQuery();

        try {
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + PARTINER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.INNER_JOIN + Usuario.USER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.ON;
            sql += PARTINER_TABLE.SHORT_TABLE_NAME + PARTINER_TABLE.ID_USER_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.EQUAL_COMPATION;
            sql += Usuario.USER_TABLE.SHORT_TABLE_NAME + Usuario.USER_TABLE.ID_COLUMN;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            partnerList = new ArrayList<>();
            while (resultSet.next()) {
                final Parceiro partner = new Parceiro();
                // on the user
                partner.setId(resultSet.getLong(Usuario.USER_TABLE.ID_COLUMN));
                partner.setNome(resultSet.getString(Usuario.USER_TABLE.NAME_COLUMN));
                partner.setEmail(resultSet.getString(Usuario.USER_TABLE.EMAIL_COLUMN));
                partner.setSenha(resultSet.getString(Usuario.USER_TABLE.PASSWORD_COLUMN));
                partner.setAceite(resultSet.getBoolean(Usuario.USER_TABLE.ACCEPT_COLUMN));
                partner.setAutor(resultSet.getBoolean(Usuario.USER_TABLE.IS_AUTHOR_COLUMN));
                partner.setParceiro(resultSet.getBoolean(Usuario.USER_TABLE.IS_PARTNER_COLUMN));
                partner.setDataHora(resultSet.getTimestamp(Usuario.USER_TABLE.DATE_TIME_COLUMN));

                // on the partner
                partner.setCnpj(resultSet.getString(PARTINER_TABLE.CNPJ_COLUMN));
                partner.setWebsite(resultSet.getString(PARTINER_TABLE.WEBSITE_COLUMN));
                partner.setSituacao(resultSet.getString(PARTINER_TABLE.SITUATION_COLUMN));
                partner.setDescricao(resultSet.getString(PARTINER_TABLE.DESCRIPTION_COLUMN));
                partner.setNomeEmpresa(resultSet.getString(PARTINER_TABLE.COMPANY_NAME_COLUMN));
                partner.setTipoServico(resultSet.getString(PARTINER_TABLE.SERVICE_TYPE_COLUMN));
                partner.setIdUsuario(resultSet.getLong(PARTINER_TABLE.ID_USER_COLUMN));

                partnerList.add(partner);
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

        return partnerList;
    }

    @Override
    public Object readById(final long id) {
        Parceiro partner = null;
        resetValuesForNewQuery();

        try {
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + PARTINER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.INNER_JOIN + Usuario.USER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.ON;
            sql += PARTINER_TABLE.SHORT_TABLE_NAME + PARTINER_TABLE.ID_USER_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.EQUAL_COMPATION;
            sql += Usuario.USER_TABLE.SHORT_TABLE_NAME + Usuario.USER_TABLE.ID_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.AND;
            sql += PARTINER_TABLE.SHORT_TABLE_NAME + PARTINER_TABLE.ID_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                partner = new Parceiro();
                // on the user
                partner.setId(resultSet.getLong(Usuario.USER_TABLE.ID_COLUMN));
                partner.setNome(resultSet.getString(Usuario.USER_TABLE.NAME_COLUMN));
                partner.setEmail(resultSet.getString(Usuario.USER_TABLE.EMAIL_COLUMN));
                partner.setSenha(resultSet.getString(Usuario.USER_TABLE.PASSWORD_COLUMN));
                partner.setAceite(resultSet.getBoolean(Usuario.USER_TABLE.ACCEPT_COLUMN));
                partner.setAutor(resultSet.getBoolean(Usuario.USER_TABLE.IS_AUTHOR_COLUMN));
                partner.setParceiro(resultSet.getBoolean(Usuario.USER_TABLE.IS_PARTNER_COLUMN));
                partner.setDataHora(resultSet.getTimestamp(Usuario.USER_TABLE.DATE_TIME_COLUMN));

                // on the partner
                partner.setCnpj(resultSet.getString(PARTINER_TABLE.CNPJ_COLUMN));
                partner.setWebsite(resultSet.getString(PARTINER_TABLE.WEBSITE_COLUMN));
                partner.setSituacao(resultSet.getString(PARTINER_TABLE.SITUATION_COLUMN));
                partner.setDescricao(resultSet.getString(PARTINER_TABLE.DESCRIPTION_COLUMN));
                partner.setNomeEmpresa(resultSet.getString(PARTINER_TABLE.COMPANY_NAME_COLUMN));
                partner.setTipoServico(resultSet.getString(PARTINER_TABLE.SERVICE_TYPE_COLUMN));
                partner.setIdUsuario(resultSet.getLong(PARTINER_TABLE.ID_USER_COLUMN));
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

        return partner;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = DataBaseHelper.SQL_COMMAND.INSERT;
            sql += PARTINER_TABLE.TABLE_NAME + DataBaseHelper.SQL_COMMAND.OPEN_PARENTHESIS;
            sql += PARTINER_TABLE.CNPJ_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTINER_TABLE.WEBSITE_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTINER_TABLE.SITUATION_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTINER_TABLE.DESCRIPTION_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTINER_TABLE.SERVICE_TYPE_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTINER_TABLE.COMPANY_NAME_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTINER_TABLE.ID_USER_COLUMN + DataBaseHelper.SQL_COMMAND.CLOSE_PARENTHESIS;
            sql += DataBaseHelper.SQL_COMMAND.VALUES;
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // cnpj
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // website
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // situacao
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // descricao
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // tipo_servico
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // nome_empresa
            sql += DataBaseHelper.SQL_COMMAND.LAST_PARAM_INSERT_TO_COMPLETE; //id_usuario

            preparForReadingOrCreating(sql, true, false);

            final Parceiro partner = (Parceiro) entity;
            int i = 1;
            preparedStatement.setString(i++, partner.getCnpj());
            preparedStatement.setString(i++, partner.getWebsite());
            preparedStatement.setString(i++, Parceiro.SITUATIONS.REQUESTED);
            preparedStatement.setString(i++, partner.getDescricao());
            preparedStatement.setString(i++, partner.getTipoServico());
            preparedStatement.setString(i++, partner.getNomeEmpresa());
            preparedStatement.setLong(i++, partner.getIdUsuario());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(PARTINER_TABLE.ID_COLUMN);
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
            String sql = DataBaseHelper.SQL_COMMAND.UPDATE + PARTINER_TABLE.TABLE_NAME + DataBaseHelper.SQL_COMMAND.SET_UPDATE;
            sql += PARTINER_TABLE.CNPJ_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += PARTINER_TABLE.WEBSITE_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += PARTINER_TABLE.SITUATION_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += PARTINER_TABLE.DESCRIPTION_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += PARTINER_TABLE.COMPANY_NAME_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += PARTINER_TABLE.SERVICE_TYPE_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;
            sql += DataBaseHelper.SQL_COMMAND.WHERE;
            sql += PARTINER_TABLE.ID_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";
            preparForUpdateOrDelete(sql);

            final Parceiro partner = (Parceiro) entity;
            int i = 1;
            preparedStatement.setString(i++, partner.getCnpj());
            preparedStatement.setString(i++, partner.getWebsite());
            preparedStatement.setString(i++, partner.getSituacao());
            preparedStatement.setString(i++, partner.getDescricao());
            preparedStatement.setString(i++, partner.getNomeEmpresa());
            preparedStatement.setString(i++, partner.getTipoServico());
            preparedStatement.setLong(i++, partner.getId());

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
            String sql = DataBaseHelper.SQL_COMMAND.DELETE + PARTINER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.WHERE;
            sql += PARTINER_TABLE.ID_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

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
        List<Parceiro> partnerList = null;
        resetValuesForNewQuery();

        try {
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + PARTINER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.INNER_JOIN + Usuario.USER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.ON;
            sql += PARTINER_TABLE.SHORT_TABLE_NAME + PARTINER_TABLE.ID_USER_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.EQUAL_COMPATION;
            sql += Usuario.USER_TABLE.SHORT_TABLE_NAME + Usuario.USER_TABLE.ID_COLUMN;
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            partnerList = new ArrayList<>();
            while (resultSet.next()) {
                final Parceiro partner = new Parceiro();
                // on the user
                partner.setId(resultSet.getLong(Usuario.USER_TABLE.ID_COLUMN));
                partner.setNome(resultSet.getString(Usuario.USER_TABLE.NAME_COLUMN));
                partner.setEmail(resultSet.getString(Usuario.USER_TABLE.EMAIL_COLUMN));
                partner.setSenha(resultSet.getString(Usuario.USER_TABLE.PASSWORD_COLUMN));
                partner.setAceite(resultSet.getBoolean(Usuario.USER_TABLE.ACCEPT_COLUMN));
                partner.setAutor(resultSet.getBoolean(Usuario.USER_TABLE.IS_AUTHOR_COLUMN));
                partner.setParceiro(resultSet.getBoolean(Usuario.USER_TABLE.IS_PARTNER_COLUMN));
                partner.setDataHora(resultSet.getTimestamp(Usuario.USER_TABLE.DATE_TIME_COLUMN));

                // on the partner
                partner.setCnpj(resultSet.getString(PARTINER_TABLE.CNPJ_COLUMN));
                partner.setWebsite(resultSet.getString(PARTINER_TABLE.WEBSITE_COLUMN));
                partner.setSituacao(resultSet.getString(PARTINER_TABLE.SITUATION_COLUMN));
                partner.setDescricao(resultSet.getString(PARTINER_TABLE.DESCRIPTION_COLUMN));
                partner.setNomeEmpresa(resultSet.getString(PARTINER_TABLE.COMPANY_NAME_COLUMN));
                partner.setTipoServico(resultSet.getString(PARTINER_TABLE.SERVICE_TYPE_COLUMN));
                partner.setIdUsuario(resultSet.getLong(PARTINER_TABLE.ID_USER_COLUMN));

                partnerList.add(partner);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return partnerList;
    }
}