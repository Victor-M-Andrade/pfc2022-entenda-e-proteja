package br.fai.ep.api.service.impl;

import br.fai.ep.db.dao.impl.UsuarioDaoImpl;
import br.fai.ep.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.api.email.EmailService;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epEntities.Usuario.USER_TABLE;
import br.fai.ep.api.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UsuarioServiceImpl implements BaseService {
    @Autowired
    private UsuarioDaoImpl dao;

    @Override
    public List<? extends BasePojo> readAll() {
        return dao.readAll();
    }

    @Override
    public Object readById(final long id) {
        return dao.readById(id);
    }

    @Override
    public long create(final Object entity) {
        return dao.create(entity);
    }

    @Override
    public boolean update(final Object entity) {
        return dao.update(entity);
    }

    @Override
    public boolean delete(final long id) {
        return dao.delete(id);
    }

    @Override
    public List<? extends BasePojo> readByCriteria(final Map criteria) {
        String queryCriteria = SQL_COMMAND.WHERE + " 1=1 ";
        queryCriteria += buildCriteriaParameters(criteria) + ";";
        return dao.readByCriteria(queryCriteria);
    }

    @Override
    public String buildCriteriaParameters(final Map criteria) {
        String param = "";
        for (final Object key : criteria.keySet()) {
            final String column = (String) key;
            if (USER_TABLE.ACCEPT_COLUMN.equalsIgnoreCase(column) ||
                    USER_TABLE.ID_COLUMN.equalsIgnoreCase(column)) {
                param += SQL_COMMAND.OR + column + SQL_COMMAND.EQUAL_COMPATION + criteria.get(key);
                continue;
            } else if (USER_TABLE.DATE_TIME_COLUMN.equalsIgnoreCase(column)) {
                param += SQL_COMMAND.OR + column + "::text" + SQL_COMMAND.ILIKE + "\'%" + criteria.get(key) + "%\'";
                continue;
            }
            param += SQL_COMMAND.OR + column + SQL_COMMAND.ILIKE + "\'%" + criteria.get(key) + "%\'";
        }

        return param;
    }

    public boolean forgotPassword(final Map criteria) {
        final String userEmail = (String) criteria.get(USER_TABLE.EMAIL_COLUMN);

        final String queryCriteria = SQL_COMMAND.WHERE + USER_TABLE.EMAIL_COLUMN + SQL_COMMAND.EQUAL_COMPATION + "\'" + userEmail + "\';";
        final List<Usuario> userList = (List<Usuario>) dao.readByCriteria(queryCriteria);
        if (userList == null || userList.isEmpty()) {
            return false;
        }

        final EmailService emailService = new EmailService();
        final String subject = "Recuperação de senha - Projeto Entenda e Proteja";
        final String message = "Clique neste link para recupar a senha";
        return emailService.send(userEmail, subject, message);
    }
}