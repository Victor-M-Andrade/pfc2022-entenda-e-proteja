package br.fai.ep.api.service.impl;

import br.fai.ep.api.service.BaseService;
import br.fai.ep.db.dao.impl.NoticiaDaoImpl;
import br.fai.ep.db.dao.impl.UsuarioDaoImpl;
import br.fai.ep.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.NewsDto;
import br.fai.ep.epEntities.Noticia;
import br.fai.ep.epEntities.Noticia.NEWS_TABLE;
import br.fai.ep.epEntities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NoticiaServiceImpl implements BaseService {
    @Autowired
    private NoticiaDaoImpl dao;

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
        final long newId = dao.create(entity);
        if (newId == -1) {
            return -1;
        }
        final Noticia myNew = (Noticia) entity;
        final UsuarioDaoImpl userDao = new UsuarioDaoImpl();
        final Usuario user = (Usuario) userDao.readById(myNew.getIdAutor());
        user.setAutor(true);
        if (!userDao.update(user)) {
            dao.delete(newId);
            return -1;
        }
        return newId;
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
            if (NEWS_TABLE.ARTICLE_COLUMN.equalsIgnoreCase(column) ||
                    NEWS_TABLE.ID_AUTHOR_COLUMN.equalsIgnoreCase(column) ||
                    NEWS_TABLE.ID_PUBLISHER_COLUMN.equalsIgnoreCase(column)) {
                param += SQL_COMMAND.AND + column + SQL_COMMAND.EQUAL_COMPATION + criteria.get(key);
                continue;
            } else if (NEWS_TABLE.CREATION_DATE_COLUMN.equalsIgnoreCase(column) ||
                    NEWS_TABLE.PUBLICATION_DATE_COLUMN.equalsIgnoreCase(column)) {
                param += SQL_COMMAND.AND + column + "::text" + SQL_COMMAND.ILIKE + "\'%" + criteria.get(key) + "%\'";
                continue;
            }
            param += SQL_COMMAND.AND + column + SQL_COMMAND.ILIKE + "\'%" + criteria.get(key) + "%\'";
        }

        return param;
    }

    public List<NewsDto> readAllNewsDto() {
        return dao.readAllNewsDto();
    }

    public NewsDto readByNewsDtoId(final long id) {
        return dao.readByNewsDtoId(id);
    }

    public List<NewsDto> readByDtoCriteria(final Map criteria) {
        String queryCriteria = SQL_COMMAND.WHERE + " 1=1 ";
        queryCriteria += buildCriteriaParameters(criteria) + ";";
        return dao.readByDtoCriteria(queryCriteria);
    }

    public List<? extends BasePojo> readLastNewsWithLimit(final int limit) {
        return dao.readLastNewsWithLimit(limit);
    }

    public List<NewsDto> readLastNewsDtoWithLimit(final int limit) {
        return dao.readLastNewsDtoWithLimit(limit);
    }

    public List<NewsDto> readLastNewsByDtoCriteriaWithLimit(final Map criteria, final int limit) {
        String queryCriteria = SQL_COMMAND.WHERE + " 1=1 ";
        queryCriteria += buildCriteriaParameters(criteria);
        return dao.readLastNewsByDtoCriteriaWithLimit(queryCriteria, limit);
    }
}