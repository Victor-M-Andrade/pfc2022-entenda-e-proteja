package br.fai.ep.db.dao.impl;

import br.fai.ep.db.connection.ConnectionFactory;
import br.fai.ep.db.dao.BaseDao;
import br.fai.ep.db.dao.BaseDaoInterface;
import br.fai.ep.db.helper.DataBaseHelper;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.NewsDto;
import br.fai.ep.epEntities.Noticia;
import br.fai.ep.epEntities.Noticia.NEWS_TABLE;
import br.fai.ep.epEntities.Usuario;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NoticiaDaoImpl extends BaseDao implements BaseDaoInterface {

    @Override
    public List<? extends BasePojo> readAll() {
        List<Noticia> newsList = null;
        resetValuesForNewQuery();

        try {
            final String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + NEWS_TABLE.TABLE_NAME + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            newsList = new ArrayList<>();
            while (resultSet.next()) {
                final Noticia news = new Noticia();
                news.setId(resultSet.getLong(NEWS_TABLE.ID_COLUMN));
                news.setTitulo(resultSet.getString(NEWS_TABLE.TITLE_COLUMN));
                news.setArtigo(resultSet.getInt(NEWS_TABLE.ARTICLE_COLUMN));
                news.setContexto(resultSet.getString(NEWS_TABLE.CONTEXT_COLUMN));
                news.setSituacao(resultSet.getString(NEWS_TABLE.SITUATION_COLUMN));
                news.setCategoria(resultSet.getString(NEWS_TABLE.CATEGORY_COLUMN));
                news.setPalavraChave(resultSet.getString(NEWS_TABLE.KEYWORD_COLUMN));
                news.setPathImageNews(resultSet.getString(NEWS_TABLE.PATH_IMG_NEWS));
                news.setDataCriacao(resultSet.getTimestamp(NEWS_TABLE.CREATION_DATE_COLUMN));
                news.setDataPublicacao(resultSet.getTimestamp(NEWS_TABLE.PUBLICATION_DATE_COLUMN));
                news.setIdAutor(resultSet.getLong(NEWS_TABLE.ID_AUTHOR_COLUMN));
                news.setIdPublicador(resultSet.getLong(NEWS_TABLE.ID_PUBLISHER_COLUMN));

                newsList.add(news);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAll | classe: " + NoticiaDaoImpl.class);
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return newsList;
    }

    @Override
    public Object readById(final long id) {
        Noticia news = null;
        resetValuesForNewQuery();

        try {
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + NEWS_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.WHERE;
            sql += NEWS_TABLE.ID_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                news = new Noticia();
                news.setId(resultSet.getLong(NEWS_TABLE.ID_COLUMN));
                news.setTitulo(resultSet.getString(NEWS_TABLE.TITLE_COLUMN));
                news.setArtigo(resultSet.getInt(NEWS_TABLE.ARTICLE_COLUMN));
                news.setContexto(resultSet.getString(NEWS_TABLE.CONTEXT_COLUMN));
                news.setSituacao(resultSet.getString(NEWS_TABLE.SITUATION_COLUMN));
                news.setCategoria(resultSet.getString(NEWS_TABLE.CATEGORY_COLUMN));
                news.setPalavraChave(resultSet.getString(NEWS_TABLE.KEYWORD_COLUMN));
                news.setPathImageNews(resultSet.getString(NEWS_TABLE.PATH_IMG_NEWS));
                news.setDataCriacao(resultSet.getTimestamp(NEWS_TABLE.CREATION_DATE_COLUMN));
                news.setDataPublicacao(resultSet.getTimestamp(NEWS_TABLE.PUBLICATION_DATE_COLUMN));
                news.setIdAutor(resultSet.getLong(NEWS_TABLE.ID_AUTHOR_COLUMN));
                news.setIdPublicador(resultSet.getLong(NEWS_TABLE.ID_PUBLISHER_COLUMN));
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readById | classe: " + NoticiaDaoImpl.class);
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return news;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = DataBaseHelper.SQL_COMMAND.INSERT;
            sql += NEWS_TABLE.TABLE_NAME + DataBaseHelper.SQL_COMMAND.OPEN_PARENTHESIS;
            sql += NEWS_TABLE.TITLE_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.ARTICLE_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.CONTEXT_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.SITUATION_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.CATEGORY_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.KEYWORD_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.PATH_IMG_NEWS + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.CREATION_DATE_COLUMN + DataBaseHelper.SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.ID_AUTHOR_COLUMN + DataBaseHelper.SQL_COMMAND.CLOSE_PARENTHESIS;
            sql += DataBaseHelper.SQL_COMMAND.VALUES;
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // titulo
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // artigo
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // contexto
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // situacao
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // categoria
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // palavra-chave
            sql += DataBaseHelper.SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // path_img_news
            sql += DataBaseHelper.SQL_COMMAND.DEFAULT_VALUE_DECLARTION + DataBaseHelper.SQL_COMMAND.SEPARATOR; // cricao
            sql += DataBaseHelper.SQL_COMMAND.LAST_PARAM_INSERT_TO_COMPLETE; // autor

            preparForReadingOrCreating(sql, true, false);

            final Noticia news = (Noticia) entity;
            int i = 1;
            preparedStatement.setString(i++, news.getTitulo());
            preparedStatement.setInt(i++, news.getArtigo());
            preparedStatement.setString(i++, news.getContexto());
            preparedStatement.setString(i++, Noticia.SITUATIONS.CREATED);
            preparedStatement.setString(i++, news.getCategoria());
            preparedStatement.setString(i++, news.getPalavraChave());
            preparedStatement.setString(i++, news.getPathImageNews());
            preparedStatement.setLong(i++, news.getIdAutor());

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                newId = resultSet.getLong(NEWS_TABLE.ID_COLUMN);
            }

            if (newId == -1) {
                connection.rollback();
                return -1;
            }
            connection.commit();
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:create | classe: " + NoticiaDaoImpl.class);
            System.out.println(e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return newId;
    }

    @Override
    public boolean update(final Object entity) {
        resetValuesForNewQuery();
        boolean isUpdateCompleted = false;

        try {
            String sql = DataBaseHelper.SQL_COMMAND.UPDATE + NEWS_TABLE.TABLE_NAME + DataBaseHelper.SQL_COMMAND.SET_UPDATE;
            sql += NEWS_TABLE.TITLE_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.ARTICLE_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.CONTEXT_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.SITUATION_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.CATEGORY_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.KEYWORD_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.PATH_IMG_NEWS + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.CREATION_DATE_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.PUBLICATION_DATE_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.ID_AUTHOR_COLUMN + DataBaseHelper.SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.ID_PUBLISHER_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;
            sql += DataBaseHelper.SQL_COMMAND.WHERE;
            sql += NEWS_TABLE.ID_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";
            preparForUpdateOrDelete(sql);

            final Noticia news = (Noticia) entity;
            int i = 1;
            preparedStatement.setString(i++, news.getTitulo());
            preparedStatement.setInt(i++, news.getArtigo());
            preparedStatement.setString(i++, news.getContexto());
            preparedStatement.setString(i++, news.getSituacao());
            preparedStatement.setString(i++, news.getCategoria());
            preparedStatement.setString(i++, news.getPalavraChave());
            preparedStatement.setString(i++, news.getPathImageNews());
            preparedStatement.setTimestamp(i++, news.getDataCriacao());
            preparedStatement.setTimestamp(i++, news.getDataPublicacao());
            preparedStatement.setLong(i++, news.getIdAutor());
            if (news.getIdPublicador() <= 0) {
                preparedStatement.setNull(i++, 0);
            } else {
                preparedStatement.setLong(i++, news.getIdPublicador());
            }
            preparedStatement.setLong(i++, news.getId());

            preparedStatement.execute();
            if (preparedStatement.getUpdateCount() == -1) {
                connection.rollback();
                return false;
            }

            connection.commit();
            isUpdateCompleted = true;

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:update | classe: " + NoticiaDaoImpl.class);
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
        boolean isDeleteCompleted = false;

        try {
            String sql = DataBaseHelper.SQL_COMMAND.DELETE + NEWS_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.WHERE;
            sql += NEWS_TABLE.ID_COLUMN + DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

            preparForUpdateOrDelete(sql);
            preparedStatement.setLong(1, id);

            preparedStatement.execute();
            connection.commit();
            isDeleteCompleted = true;
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:delete | classe: " + NoticiaDaoImpl.class);

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
    public List<? extends BasePojo> readByCriteria(final String criteria) {
        List<Noticia> newsList = null;
        resetValuesForNewQuery();

        try {
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + NEWS_TABLE.TABLE_NAME;
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            newsList = new ArrayList<>();
            while (resultSet.next()) {
                final Noticia news = new Noticia();
                news.setId(resultSet.getLong(NEWS_TABLE.ID_COLUMN));
                news.setTitulo(resultSet.getString(NEWS_TABLE.TITLE_COLUMN));
                news.setArtigo(resultSet.getInt(NEWS_TABLE.ARTICLE_COLUMN));
                news.setContexto(resultSet.getString(NEWS_TABLE.CONTEXT_COLUMN));
                news.setSituacao(resultSet.getString(NEWS_TABLE.SITUATION_COLUMN));
                news.setCategoria(resultSet.getString(NEWS_TABLE.CATEGORY_COLUMN));
                news.setPalavraChave(resultSet.getString(NEWS_TABLE.KEYWORD_COLUMN));
                news.setPathImageNews(resultSet.getString(NEWS_TABLE.PATH_IMG_NEWS));
                news.setDataCriacao(resultSet.getTimestamp(NEWS_TABLE.CREATION_DATE_COLUMN));
                news.setDataPublicacao(resultSet.getTimestamp(NEWS_TABLE.PUBLICATION_DATE_COLUMN));
                news.setIdAutor(resultSet.getLong(NEWS_TABLE.ID_AUTHOR_COLUMN));
                news.setIdPublicador(resultSet.getLong(NEWS_TABLE.ID_PUBLISHER_COLUMN));

                newsList.add(news);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + NoticiaDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return newsList;
    }

    public List<NewsDto> readAllNewsDto() {
        List<NewsDto> newsDtoList = null;
        resetValuesForNewQuery();

        try {
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + Noticia.NEWS_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.INNER_JOIN + Usuario.USER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.ON;
            sql += Noticia.NEWS_TABLE.SHORT_TABLE_NAME + NEWS_TABLE.ID_AUTHOR_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.EQUAL_COMPATION;
            sql += Usuario.USER_TABLE.SHORT_TABLE_NAME + Usuario.USER_TABLE.ID_COLUMN;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            newsDtoList = new ArrayList<>();
            while (resultSet.next()) {
                final NewsDto newsDto = new NewsDto();
                newsDto.setId(resultSet.getLong(NEWS_TABLE.ID_COLUMN));
                newsDto.setTitulo(resultSet.getString(NEWS_TABLE.TITLE_COLUMN));
                newsDto.setArtigo(resultSet.getInt(NEWS_TABLE.ARTICLE_COLUMN));
                newsDto.setContexto(resultSet.getString(NEWS_TABLE.CONTEXT_COLUMN));
                newsDto.setSituacao(resultSet.getString(NEWS_TABLE.SITUATION_COLUMN));
                newsDto.setCategoria(resultSet.getString(NEWS_TABLE.CATEGORY_COLUMN));
                newsDto.setPalavraChave(resultSet.getString(NEWS_TABLE.KEYWORD_COLUMN));
                newsDto.setPathImageNews(resultSet.getString(NEWS_TABLE.PATH_IMG_NEWS));
                newsDto.setDataCriacao(resultSet.getTimestamp(NEWS_TABLE.CREATION_DATE_COLUMN));
                newsDto.setDataPublicacao(resultSet.getTimestamp(NEWS_TABLE.PUBLICATION_DATE_COLUMN));
                newsDto.setIdAutor(resultSet.getLong(NEWS_TABLE.ID_AUTHOR_COLUMN));
                newsDto.setIdPublicador(resultSet.getLong(NEWS_TABLE.ID_PUBLISHER_COLUMN));

                newsDto.setAuthorName(resultSet.getString(Usuario.USER_TABLE.NAME_COLUMN));
                newsDto.setAuthorEmail(resultSet.getString(Usuario.USER_TABLE.EMAIL_COLUMN));
                newsDto.setAnonimo(resultSet.getBoolean(Usuario.USER_TABLE.IS_ANONYMOUS_COLUMN));

                newsDtoList.add(newsDto);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAllNewsDto | classe: " + NoticiaDaoImpl.class);
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return newsDtoList;
    }

    public NewsDto readByNewsDtoId(final long id) {
        NewsDto newsDto = null;
        resetValuesForNewQuery();

        try {
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + Noticia.NEWS_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.INNER_JOIN + Usuario.USER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.ON;
            sql += Noticia.NEWS_TABLE.SHORT_TABLE_NAME + NEWS_TABLE.ID_AUTHOR_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.EQUAL_COMPATION;
            sql += Usuario.USER_TABLE.SHORT_TABLE_NAME + Usuario.USER_TABLE.ID_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.AND;
            sql += Noticia.NEWS_TABLE.SHORT_TABLE_NAME + Noticia.NEWS_TABLE.ID_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                newsDto = new NewsDto();
                newsDto.setId(resultSet.getLong(NEWS_TABLE.ID_COLUMN));
                newsDto.setTitulo(resultSet.getString(NEWS_TABLE.TITLE_COLUMN));
                newsDto.setArtigo(resultSet.getInt(NEWS_TABLE.ARTICLE_COLUMN));
                newsDto.setContexto(resultSet.getString(NEWS_TABLE.CONTEXT_COLUMN));
                newsDto.setSituacao(resultSet.getString(NEWS_TABLE.SITUATION_COLUMN));
                newsDto.setCategoria(resultSet.getString(NEWS_TABLE.CATEGORY_COLUMN));
                newsDto.setPalavraChave(resultSet.getString(NEWS_TABLE.KEYWORD_COLUMN));
                newsDto.setPathImageNews(resultSet.getString(NEWS_TABLE.PATH_IMG_NEWS));
                newsDto.setDataCriacao(resultSet.getTimestamp(NEWS_TABLE.CREATION_DATE_COLUMN));
                newsDto.setDataPublicacao(resultSet.getTimestamp(NEWS_TABLE.PUBLICATION_DATE_COLUMN));
                newsDto.setIdAutor(resultSet.getLong(NEWS_TABLE.ID_AUTHOR_COLUMN));
                newsDto.setIdPublicador(resultSet.getLong(NEWS_TABLE.ID_PUBLISHER_COLUMN));

                newsDto.setAuthorName(resultSet.getString(Usuario.USER_TABLE.NAME_COLUMN));
                newsDto.setAuthorEmail(resultSet.getString(Usuario.USER_TABLE.EMAIL_COLUMN));
                newsDto.setAnonimo(resultSet.getBoolean(Usuario.USER_TABLE.IS_ANONYMOUS_COLUMN));
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByNewsDtoId | classe: " + NoticiaDaoImpl.class);
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return newsDto;
    }

    public List<NewsDto> readByDtoCriteria(final String criteria) {
        List<NewsDto> newsDtoList = null;
        resetValuesForNewQuery();

        try {
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + Noticia.NEWS_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.INNER_JOIN + Usuario.USER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.ON;
            sql += Noticia.NEWS_TABLE.SHORT_TABLE_NAME + NEWS_TABLE.ID_AUTHOR_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.EQUAL_COMPATION;
            sql += Usuario.USER_TABLE.SHORT_TABLE_NAME + Usuario.USER_TABLE.ID_COLUMN;
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            newsDtoList = new ArrayList<>();
            while (resultSet.next()) {
                final NewsDto newsDto = new NewsDto();
                newsDto.setId(resultSet.getLong(NEWS_TABLE.ID_COLUMN));
                newsDto.setTitulo(resultSet.getString(NEWS_TABLE.TITLE_COLUMN));
                newsDto.setArtigo(resultSet.getInt(NEWS_TABLE.ARTICLE_COLUMN));
                newsDto.setContexto(resultSet.getString(NEWS_TABLE.CONTEXT_COLUMN));
                newsDto.setSituacao(resultSet.getString(NEWS_TABLE.SITUATION_COLUMN));
                newsDto.setCategoria(resultSet.getString(NEWS_TABLE.CATEGORY_COLUMN));
                newsDto.setPalavraChave(resultSet.getString(NEWS_TABLE.KEYWORD_COLUMN));
                newsDto.setPathImageNews(resultSet.getString(NEWS_TABLE.PATH_IMG_NEWS));
                newsDto.setDataCriacao(resultSet.getTimestamp(NEWS_TABLE.CREATION_DATE_COLUMN));
                newsDto.setDataPublicacao(resultSet.getTimestamp(NEWS_TABLE.PUBLICATION_DATE_COLUMN));
                newsDto.setIdAutor(resultSet.getLong(NEWS_TABLE.ID_AUTHOR_COLUMN));
                newsDto.setIdPublicador(resultSet.getLong(NEWS_TABLE.ID_PUBLISHER_COLUMN));

                newsDto.setAuthorName(resultSet.getString(Usuario.USER_TABLE.NAME_COLUMN));
                newsDto.setAuthorEmail(resultSet.getString(Usuario.USER_TABLE.EMAIL_COLUMN));
                newsDto.setAnonimo(resultSet.getBoolean(Usuario.USER_TABLE.IS_ANONYMOUS_COLUMN));

                newsDtoList.add(newsDto);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByDtoCriteria | classe: " + NoticiaDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return newsDtoList;
    }

    public List<? extends BasePojo> readLastNewsWithLimit(final int limit) {
        List<Noticia> newsList = null;
        resetValuesForNewQuery();

        try {
            final String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + NEWS_TABLE.TABLE_NAME + " ORDER BY N.data_publicacao DESC LIMIT ?;";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setInt(1, limit);
            resultSet = preparedStatement.executeQuery();

            newsList = new ArrayList<>();
            while (resultSet.next()) {
                final Noticia news = new Noticia();
                news.setId(resultSet.getLong(NEWS_TABLE.ID_COLUMN));
                news.setTitulo(resultSet.getString(NEWS_TABLE.TITLE_COLUMN));
                news.setArtigo(resultSet.getInt(NEWS_TABLE.ARTICLE_COLUMN));
                news.setContexto(resultSet.getString(NEWS_TABLE.CONTEXT_COLUMN));
                news.setSituacao(resultSet.getString(NEWS_TABLE.SITUATION_COLUMN));
                news.setCategoria(resultSet.getString(NEWS_TABLE.CATEGORY_COLUMN));
                news.setPalavraChave(resultSet.getString(NEWS_TABLE.KEYWORD_COLUMN));
                news.setPathImageNews(resultSet.getString(NEWS_TABLE.PATH_IMG_NEWS));
                news.setDataCriacao(resultSet.getTimestamp(NEWS_TABLE.CREATION_DATE_COLUMN));
                news.setDataPublicacao(resultSet.getTimestamp(NEWS_TABLE.PUBLICATION_DATE_COLUMN));
                news.setIdAutor(resultSet.getLong(NEWS_TABLE.ID_AUTHOR_COLUMN));
                news.setIdPublicador(resultSet.getLong(NEWS_TABLE.ID_PUBLISHER_COLUMN));

                newsList.add(news);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAll | classe: " + NoticiaDaoImpl.class);
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return newsList;
    }

    public List<NewsDto> readLastNewsDtoWithLimit(final int limit) {
        List<NewsDto> newsDtoList = null;
        resetValuesForNewQuery();

        try {
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + Noticia.NEWS_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.INNER_JOIN + Usuario.USER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.ON;
            sql += Noticia.NEWS_TABLE.SHORT_TABLE_NAME + NEWS_TABLE.ID_AUTHOR_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.EQUAL_COMPATION;
            sql += Usuario.USER_TABLE.SHORT_TABLE_NAME + Usuario.USER_TABLE.ID_COLUMN;
            sql += " ORDER BY N.data_publicacao DESC LIMIT ?;";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setInt(1, limit);
            resultSet = preparedStatement.executeQuery();

            newsDtoList = new ArrayList<>();
            while (resultSet.next()) {
                final NewsDto newsDto = new NewsDto();
                newsDto.setId(resultSet.getLong(NEWS_TABLE.ID_COLUMN));
                newsDto.setTitulo(resultSet.getString(NEWS_TABLE.TITLE_COLUMN));
                newsDto.setArtigo(resultSet.getInt(NEWS_TABLE.ARTICLE_COLUMN));
                newsDto.setContexto(resultSet.getString(NEWS_TABLE.CONTEXT_COLUMN));
                newsDto.setSituacao(resultSet.getString(NEWS_TABLE.SITUATION_COLUMN));
                newsDto.setCategoria(resultSet.getString(NEWS_TABLE.CATEGORY_COLUMN));
                newsDto.setPalavraChave(resultSet.getString(NEWS_TABLE.KEYWORD_COLUMN));
                newsDto.setPathImageNews(resultSet.getString(NEWS_TABLE.PATH_IMG_NEWS));
                newsDto.setDataCriacao(resultSet.getTimestamp(NEWS_TABLE.CREATION_DATE_COLUMN));
                newsDto.setDataPublicacao(resultSet.getTimestamp(NEWS_TABLE.PUBLICATION_DATE_COLUMN));
                newsDto.setIdAutor(resultSet.getLong(NEWS_TABLE.ID_AUTHOR_COLUMN));
                newsDto.setIdPublicador(resultSet.getLong(NEWS_TABLE.ID_PUBLISHER_COLUMN));

                newsDto.setAuthorName(resultSet.getString(Usuario.USER_TABLE.NAME_COLUMN));
                newsDto.setAuthorEmail(resultSet.getString(Usuario.USER_TABLE.EMAIL_COLUMN));
                newsDto.setAnonimo(resultSet.getBoolean(Usuario.USER_TABLE.IS_ANONYMOUS_COLUMN));

                newsDtoList.add(newsDto);
            }
        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readAllNewsDto | classe: " + NoticiaDaoImpl.class);
            if (e instanceof SQLException) {
                System.out.println("SQLException: olhar metodo newReadOrCreateInstances");
            }
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return newsDtoList;
    }

    public List<NewsDto> readLastNewsByDtoCriteriaWithLimit(final String criteria, final int limit) {
        List<NewsDto> newsDtoList = null;
        resetValuesForNewQuery();

        try {
            String sql = DataBaseHelper.SQL_COMMAND.SELECT_FULL + Noticia.NEWS_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.INNER_JOIN + Usuario.USER_TABLE.TABLE_NAME;
            sql += DataBaseHelper.SQL_COMMAND.ON;
            sql += Noticia.NEWS_TABLE.SHORT_TABLE_NAME + NEWS_TABLE.ID_AUTHOR_COLUMN;
            sql += DataBaseHelper.SQL_COMMAND.EQUAL_COMPATION;
            sql += Usuario.USER_TABLE.SHORT_TABLE_NAME + Usuario.USER_TABLE.ID_COLUMN;
            sql += " " + criteria;
            sql += " ORDER BY N.data_publicacao DESC LIMIT ?;";
            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setInt(1, limit);

            resultSet = preparedStatement.executeQuery();

            newsDtoList = new ArrayList<>();
            while (resultSet.next()) {
                final NewsDto newsDto = new NewsDto();
                newsDto.setId(resultSet.getLong(NEWS_TABLE.ID_COLUMN));
                newsDto.setTitulo(resultSet.getString(NEWS_TABLE.TITLE_COLUMN));
                newsDto.setArtigo(resultSet.getInt(NEWS_TABLE.ARTICLE_COLUMN));
                newsDto.setContexto(resultSet.getString(NEWS_TABLE.CONTEXT_COLUMN));
                newsDto.setSituacao(resultSet.getString(NEWS_TABLE.SITUATION_COLUMN));
                newsDto.setCategoria(resultSet.getString(NEWS_TABLE.CATEGORY_COLUMN));
                newsDto.setPalavraChave(resultSet.getString(NEWS_TABLE.KEYWORD_COLUMN));
                newsDto.setPathImageNews(resultSet.getString(NEWS_TABLE.PATH_IMG_NEWS));
                newsDto.setDataCriacao(resultSet.getTimestamp(NEWS_TABLE.CREATION_DATE_COLUMN));
                newsDto.setDataPublicacao(resultSet.getTimestamp(NEWS_TABLE.PUBLICATION_DATE_COLUMN));
                newsDto.setIdAutor(resultSet.getLong(NEWS_TABLE.ID_AUTHOR_COLUMN));
                newsDto.setIdPublicador(resultSet.getLong(NEWS_TABLE.ID_PUBLISHER_COLUMN));

                newsDto.setAuthorName(resultSet.getString(Usuario.USER_TABLE.NAME_COLUMN));
                newsDto.setAuthorEmail(resultSet.getString(Usuario.USER_TABLE.EMAIL_COLUMN));
                newsDto.setAnonimo(resultSet.getBoolean(Usuario.USER_TABLE.IS_ANONYMOUS_COLUMN));

                newsDtoList.add(newsDto);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByDtoCriteria | classe: " + NoticiaDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement);
        }

        return newsDtoList;
    }
}