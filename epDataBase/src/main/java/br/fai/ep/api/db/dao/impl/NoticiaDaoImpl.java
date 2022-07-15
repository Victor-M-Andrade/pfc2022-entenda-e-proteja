package br.fai.ep.api.db.dao.impl;

import br.fai.ep.api.db.connection.ConnectionFactory;
import br.fai.ep.api.db.dao.BaseDao;
import br.fai.ep.api.db.dao.BaseDaoInterface;
import br.fai.ep.api.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.entities.Noticia;
import br.fai.ep.api.entities.Noticia.NEWS_TABLE;
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
            final String sql = SQL_COMMAND.SELECT_FULL + NEWS_TABLE.TABLE_NAME + ";";

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
                news.setPalabraChave(resultSet.getString(NEWS_TABLE.KEYWORD_COLUMN));
                news.setDataCriacao(resultSet.getTimestamp(NEWS_TABLE.CREATION_DATE_COLUMN));
                news.setDataPublicao(resultSet.getTimestamp(NEWS_TABLE.PUBLICATION_DATE_COLUMN));
                news.setIdAutor(resultSet.getLong(NEWS_TABLE.ID_AUTHOR_COLUMN));
                news.setIdPublicador(resultSet.getLong(NEWS_TABLE.ID_PUBLISHER_COLUMN));

                newsList.add(news);
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

        return newsList;
    }

    @Override
    public Object readById(final long id) {
        Noticia news = null;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + NEWS_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += NEWS_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

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
                news.setPalabraChave(resultSet.getString(NEWS_TABLE.KEYWORD_COLUMN));
                news.setDataCriacao(resultSet.getTimestamp(NEWS_TABLE.CREATION_DATE_COLUMN));
                news.setDataPublicao(resultSet.getTimestamp(NEWS_TABLE.PUBLICATION_DATE_COLUMN));
                news.setIdAutor(resultSet.getLong(NEWS_TABLE.ID_AUTHOR_COLUMN));
                news.setIdPublicador(resultSet.getLong(NEWS_TABLE.ID_PUBLISHER_COLUMN));
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

        return news;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = SQL_COMMAND.INSERT;
            sql += NEWS_TABLE.TABLE_NAME + SQL_COMMAND.OPEN_PARENTHESIS;
            sql += NEWS_TABLE.TITLE_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.ARTICLE_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.CONTEXT_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.SITUATION_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.KEYWORD_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.CREATION_DATE_COLUMN + SQL_COMMAND.SEPARATOR;
            sql += NEWS_TABLE.ID_AUTHOR_COLUMN + SQL_COMMAND.CLOSE_PARENTHESIS;
            sql += SQL_COMMAND.VALUES;
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // titulo
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // artigo
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // contexto
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // situacao
            sql += SQL_COMMAND.PARAM_INSERT_TO_COMPLETE; // palavra-chave
            sql += SQL_COMMAND.DEFAULT_VALUE_DECLARTION + SQL_COMMAND.SEPARATOR; // cricao
            sql += SQL_COMMAND.LAST_PARAM_INSERT_TO_COMPLETE; // autor

            preparForReadingOrCreating(sql, true, true);

            final Noticia news = (Noticia) entity;
            int i = 1;
            preparedStatement.setString(i++, news.getTitulo());
            preparedStatement.setInt(i++, news.getArtigo());
            preparedStatement.setString(i++, news.getContexto());
            preparedStatement.setString(i++, Noticia.NewsSituation.CREATED.getName());
            preparedStatement.setString(i++, news.getPalabraChave());
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
            String sql = SQL_COMMAND.UPDATE + NEWS_TABLE.TABLE_NAME + SQL_COMMAND.SET_UPDATE;
            sql += NEWS_TABLE.TITLE_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.ARTICLE_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.CONTEXT_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.SITUATION_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.KEYWORD_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.CREATION_DATE_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.PUBLICATION_DATE_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.ID_AUTHOR_COLUMN + SQL_COMMAND.PARAM_UPDATE_TO_COMPLETE;
            sql += NEWS_TABLE.ID_PUBLISHER_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE;
            sql += SQL_COMMAND.WHERE;
            sql += NEWS_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";
            preparForUpdateOrDelete(sql);

            final Noticia news = (Noticia) entity;
            int i = 1;
            preparedStatement.setString(i++, NEWS_TABLE.TABLE_NAME);
            preparedStatement.setString(i++, news.getTitulo());
            preparedStatement.setInt(i++, news.getArtigo());
            preparedStatement.setString(i++, news.getContexto());
            preparedStatement.setString(i++, news.getSituacao());
            preparedStatement.setString(i++, news.getPalabraChave());
            preparedStatement.setTimestamp(i++, news.getDataCriacao());
            preparedStatement.setTimestamp(i++, news.getDataPublicao());
            preparedStatement.setLong(i++, news.getIdAutor());
            preparedStatement.setLong(i++, news.getIdPublicador());
            preparedStatement.setLong(i++, news.getId());

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
            String sql = SQL_COMMAND.DELETE + NEWS_TABLE.TABLE_NAME;
            sql += SQL_COMMAND.WHERE;
            sql += NEWS_TABLE.ID_COLUMN + SQL_COMMAND.lAST_PARAM_UPDATE_TO_COMPLETE + ";";

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
        List<Noticia> newsList = null;
        resetValuesForNewQuery();

        try {
            String sql = SQL_COMMAND.SELECT_FULL + NEWS_TABLE.TABLE_NAME;
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
                news.setPalabraChave(resultSet.getString(NEWS_TABLE.KEYWORD_COLUMN));
                news.setDataCriacao(resultSet.getTimestamp(NEWS_TABLE.CREATION_DATE_COLUMN));
                news.setDataPublicao(resultSet.getTimestamp(NEWS_TABLE.PUBLICATION_DATE_COLUMN));
                news.setIdAutor(resultSet.getLong(NEWS_TABLE.ID_AUTHOR_COLUMN));
                news.setIdPublicador(resultSet.getLong(NEWS_TABLE.ID_PUBLISHER_COLUMN));

                newsList.add(news);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDaoImpl.class);
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return newsList;
    }
}