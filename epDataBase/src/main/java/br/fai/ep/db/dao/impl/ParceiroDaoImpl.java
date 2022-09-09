package br.fai.ep.db.dao.impl;

import br.fai.ep.db.connection.ConnectionFactory;
import br.fai.ep.db.dao.BaseDao;
import br.fai.ep.db.dao.BaseDaoInterface;
import br.fai.ep.db.helper.DataBaseHelper;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.Parceiro;
import br.fai.ep.epEntities.Parceiro.PARTNER_TABLE;
import br.fai.ep.epEntities.Usuario;
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
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + PARTNER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.INNER_JOIN + Usuario.USER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.ON;
            sql += PARTNER_TABLE.SHORT_TABLE_NAME + PARTNER_TABLE.ID_USER_COLUMN;
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
                partner.setPathImageProfile(resultSet.getString(Usuario.USER_TABLE.PATH_IMG_PROFILE));
                partner.setAceite(resultSet.getBoolean(Usuario.USER_TABLE.ACCEPT_COLUMN));
                partner.setAutor(resultSet.getBoolean(Usuario.USER_TABLE.IS_AUTHOR_COLUMN));
                partner.setParceiro(resultSet.getBoolean(Usuario.USER_TABLE.IS_PARTNER_COLUMN));
                partner.setAnonimo(resultSet.getBoolean(Usuario.USER_TABLE.IS_ANONYMOUS_COLUMN));
                partner.setAdministrador(resultSet.getBoolean(Usuario.USER_TABLE.IS_ADMINISTRATOR_COLUMN));
                partner.setDataHora(resultSet.getTimestamp(Usuario.USER_TABLE.DATE_TIME_COLUMN));

                // on the partner
                partner.setCnpj(resultSet.getString(PARTNER_TABLE.CNPJ_COLUMN));
                partner.setWebsite(resultSet.getString(PARTNER_TABLE.WEBSITE_COLUMN));
                partner.setSituacao(resultSet.getString(PARTNER_TABLE.SITUATION_COLUMN));
                partner.setDescricao(resultSet.getString(PARTNER_TABLE.DESCRIPTION_COLUMN));
                partner.setLegislativo(resultSet.getBoolean(PARTNER_TABLE.IS_LEGISLATE_SERVICE));
                partner.setTecnico(resultSet.getBoolean(PARTNER_TABLE.IS_TECHNICAL_SERVICE));
                partner.setNomeEmpresa(resultSet.getString(PARTNER_TABLE.COMPANY_NAME_COLUMN));
                partner.setIdUsuario(resultSet.getLong(PARTNER_TABLE.ID_USER_COLUMN));

                partnerList.add(partner);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAll | classe: " + ParceiroDaoImpl.class);
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
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + PARTNER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.INNER_JOIN + Usuario.USER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.ON;
            sql += PARTNER_TABLE.SHORT_TABLE_NAME + PARTNER_TABLE.ID_USER_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.EQUAL_COMPATION;
            sql += Usuario.USER_TABLE.SHORT_TABLE_NAME + Usuario.USER_TABLE.ID_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.AND;
            sql += PARTNER_TABLE.SHORT_TABLE_NAME + PARTNER_TABLE.ID_COLUMN;
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
                partner.setPathImageProfile(resultSet.getString(Usuario.USER_TABLE.PATH_IMG_PROFILE));
                partner.setAceite(resultSet.getBoolean(Usuario.USER_TABLE.ACCEPT_COLUMN));
                partner.setAutor(resultSet.getBoolean(Usuario.USER_TABLE.IS_AUTHOR_COLUMN));
                partner.setParceiro(resultSet.getBoolean(Usuario.USER_TABLE.IS_PARTNER_COLUMN));
                partner.setAnonimo(resultSet.getBoolean(Usuario.USER_TABLE.IS_ANONYMOUS_COLUMN));
                partner.setAdministrador(resultSet.getBoolean(Usuario.USER_TABLE.IS_ADMINISTRATOR_COLUMN));
                partner.setDataHora(resultSet.getTimestamp(Usuario.USER_TABLE.DATE_TIME_COLUMN));

                // on the partner
                partner.setCnpj(resultSet.getString(PARTNER_TABLE.CNPJ_COLUMN));
                partner.setWebsite(resultSet.getString(PARTNER_TABLE.WEBSITE_COLUMN));
                partner.setSituacao(resultSet.getString(PARTNER_TABLE.SITUATION_COLUMN));
                partner.setDescricao(resultSet.getString(PARTNER_TABLE.DESCRIPTION_COLUMN));
                partner.setLegislativo(resultSet.getBoolean(PARTNER_TABLE.IS_LEGISLATE_SERVICE));
                partner.setTecnico(resultSet.getBoolean(PARTNER_TABLE.IS_TECHNICAL_SERVICE));
                partner.setNomeEmpresa(resultSet.getString(PARTNER_TABLE.COMPANY_NAME_COLUMN));
                partner.setIdUsuario(resultSet.getLong(PARTNER_TABLE.ID_USER_COLUMN));
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readById | classe: " + ParceiroDaoImpl.class);
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
            sql += PARTNER_TABLE.TABLE_NAME + DataBaseHelper.SQL_COMMAND.OPEN_PARENTHESIS;
            sql += PARTNER_TABLE.CNPJ_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTNER_TABLE.WEBSITE_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTNER_TABLE.SITUATION_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTNER_TABLE.DESCRIPTION_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTNER_TABLE.IS_LEGISLATE_SERVICE + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTNER_TABLE.IS_TECHNICAL_SERVICE + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTNER_TABLE.COMPANY_NAME_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += PARTNER_TABLE.ID_USER_COLUMN + DataBaseHelper.SQL_COMMAND.CLOSE_PARENTHESIS;
            sql += DataBaseHelper.SQL_COMMAND.VALUES;
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // cnpj
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // website
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // situacao
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // descricao
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // isLegislativo
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // isTecnico
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // nome_empresa
            sql += DataBaseHelper.SQL_COMMAND.LAST_PARAM_INSERT_TO_COMPLETE; //id_usuario

            preparForReadingOrCreating(sql, true, false);

            final Parceiro partner = (Parceiro) entity;
            int i = 1;
            preparedStatement.setString(i++, partner.getCnpj());
            preparedStatement.setString(i++, partner.getWebsite());
            preparedStatement.setString(i++, Parceiro.SITUATIONS.REQUESTED);
            preparedStatement.setString(i++, partner.getDescricao());
            preparedStatement.setBoolean(i++, partner.isLegislativo());
            preparedStatement.setBoolean(i++, partner.isTecnico());
            preparedStatement.setString(i++, partner.getNomeEmpresa());
            preparedStatement.setLong(i++, partner.getIdUsuario());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(PARTNER_TABLE.ID_COLUMN);
            }

            if (newId == -1) {
                connection.rollback();
                return -1;
            }
            connection.commit();
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:create | classe: " + ParceiroDaoImpl.class);
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
            String sql = DataBaseHelper.SQL_COMMAND.UPDATE + PARTNER_TABLE.TABLE_NAME + DataBaseHelper.SQL_COMMAND.SET_UPDATE;
            sql += PARTNER_TABLE.CNPJ_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += PARTNER_TABLE.WEBSITE_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += PARTNER_TABLE.SITUATION_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += PARTNER_TABLE.DESCRIPTION_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += PARTNER_TABLE.IS_LEGISLATE_SERVICE + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += PARTNER_TABLE.IS_TECHNICAL_SERVICE + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += PARTNER_TABLE.COMPANY_NAME_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;
            sql += DataBaseHelper.SQL_COMMAND.WHERE;
            sql += PARTNER_TABLE.ID_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";
            preparForUpdateOrDelete(sql);

            final Parceiro partner = (Parceiro) entity;
            int i = 1;
            preparedStatement.setString(i++, partner.getCnpj());
            preparedStatement.setString(i++, partner.getWebsite());
            preparedStatement.setString(i++, partner.getSituacao());
            preparedStatement.setString(i++, partner.getDescricao());
            preparedStatement.setBoolean(i++, partner.isLegislativo());
            preparedStatement.setBoolean(i++, partner.isTecnico());
            preparedStatement.setString(i++, partner.getNomeEmpresa());
            preparedStatement.setLong(i++, partner.getId());

            preparedStatement.execute();
            if (preparedStatement.getUpdateCount() == -1) {
                connection.rollback();
                return false;
            }

            connection.commit();
            isUpdateCompleted = true;

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:update | classe: " + ParceiroDaoImpl.class);
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
            String sql = DataBaseHelper.SQL_COMMAND.DELETE + PARTNER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.WHERE;
            sql += PARTNER_TABLE.ID_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForUpdateOrDelete(sql);
            preparedStatement.setLong(1, id);

            preparedStatement.execute();
            connection.commit();
            isDeleteCompleted = true;
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:delete | classe: " + ParceiroDaoImpl.class);

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
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + PARTNER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.INNER_JOIN + Usuario.USER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.ON;
            sql += PARTNER_TABLE.SHORT_TABLE_NAME + PARTNER_TABLE.ID_USER_COLUMN;
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
                partner.setPathImageProfile(resultSet.getString(Usuario.USER_TABLE.PATH_IMG_PROFILE));
                partner.setAceite(resultSet.getBoolean(Usuario.USER_TABLE.ACCEPT_COLUMN));
                partner.setAutor(resultSet.getBoolean(Usuario.USER_TABLE.IS_AUTHOR_COLUMN));
                partner.setParceiro(resultSet.getBoolean(Usuario.USER_TABLE.IS_PARTNER_COLUMN));
                partner.setAnonimo(resultSet.getBoolean(Usuario.USER_TABLE.IS_ANONYMOUS_COLUMN));
                partner.setAdministrador(resultSet.getBoolean(Usuario.USER_TABLE.IS_ADMINISTRATOR_COLUMN));
                partner.setDataHora(resultSet.getTimestamp(Usuario.USER_TABLE.DATE_TIME_COLUMN));

                // on the partner
                partner.setCnpj(resultSet.getString(PARTNER_TABLE.CNPJ_COLUMN));
                partner.setWebsite(resultSet.getString(PARTNER_TABLE.WEBSITE_COLUMN));
                partner.setSituacao(resultSet.getString(PARTNER_TABLE.SITUATION_COLUMN));
                partner.setDescricao(resultSet.getString(PARTNER_TABLE.DESCRIPTION_COLUMN));
                partner.setLegislativo(resultSet.getBoolean(PARTNER_TABLE.IS_LEGISLATE_SERVICE));
                partner.setTecnico(resultSet.getBoolean(PARTNER_TABLE.IS_TECHNICAL_SERVICE));
                partner.setNomeEmpresa(resultSet.getString(PARTNER_TABLE.COMPANY_NAME_COLUMN));
                partner.setIdUsuario(resultSet.getLong(PARTNER_TABLE.ID_USER_COLUMN));

                partnerList.add(partner);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ParceiroDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return partnerList;
    }
}