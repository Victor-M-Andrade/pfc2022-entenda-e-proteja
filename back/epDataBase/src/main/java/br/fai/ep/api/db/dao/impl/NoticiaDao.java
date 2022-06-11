package br.fai.ep.api.db.dao.impl;

import br.fai.ep.api.db.connection.ConnectionFactory;
import br.fai.ep.api.db.dao.BaseDao;
import br.fai.ep.api.db.helper.DataBaseHelper;
import br.fai.ep.api.db.helper.DataBaseHelper.NewsTable;
import br.fai.ep.api.db.helper.DataBaseHelper.Sql;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.entities.Noticia;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NoticiaDao implements BaseDao {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public List<? extends BasePojo> readAll() {
        List<Noticia> newsList = null;
        resetValuesForNewQuery();

        try {
            final String sql = Sql.SELECT_FULL.getName() + NewsTable.TABLE_NAME.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            newsList = new ArrayList<>();
            while (resultSet.next()) {
                final Noticia news = new Noticia();
                news.setId(resultSet.getLong(NewsTable.ID_COLUMN.getName()));
                news.setTitulo(resultSet.getString(NewsTable.TITLE_COLUMN.getName()));
                news.setArtigo(resultSet.getInt(NewsTable.ARTICLE_COLUMN.getName()));
                news.setContexto(resultSet.getString(NewsTable.CONTEXT_COLUMN.getName()));
                news.setSituacao(resultSet.getString(NewsTable.SITUATION_COLUMN.getName()));
                news.setPalabraChave(resultSet.getString(NewsTable.KEYWORD_COLUMN.getName()));
                news.setDataCriacao(resultSet.getTimestamp(NewsTable.CREATION_DATE_COLUMN.getName()));
                news.setDataPublicao(resultSet.getTimestamp(NewsTable.PUBLICATION_DATE_COLUMN.getName()));
                news.setIdAutor(resultSet.getLong(NewsTable.ID_AUTHOR_COLUMN.getName()));
                news.setIdPublicador(resultSet.getLong(NewsTable.ID_PUBLISHER_COLUMN.getName()));

                newsList.add(news);
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

        return newsList;
    }

    @Override
    public Object readById(final long id) {
        Noticia news = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + NewsTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += NewsTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

            preparForReadingOrCreating(sql, false, true);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                news = new Noticia();
                news.setId(resultSet.getLong(NewsTable.ID_COLUMN.getName()));
                news.setTitulo(resultSet.getString(NewsTable.TITLE_COLUMN.getName()));
                news.setArtigo(resultSet.getInt(NewsTable.ARTICLE_COLUMN.getName()));
                news.setContexto(resultSet.getString(NewsTable.CONTEXT_COLUMN.getName()));
                news.setSituacao(resultSet.getString(NewsTable.SITUATION_COLUMN.getName()));
                news.setPalabraChave(resultSet.getString(NewsTable.KEYWORD_COLUMN.getName()));
                news.setDataCriacao(resultSet.getTimestamp(NewsTable.CREATION_DATE_COLUMN.getName()));
                news.setDataPublicao(resultSet.getTimestamp(NewsTable.PUBLICATION_DATE_COLUMN.getName()));
                news.setIdAutor(resultSet.getLong(NewsTable.ID_AUTHOR_COLUMN.getName()));
                news.setIdPublicador(resultSet.getLong(NewsTable.ID_PUBLISHER_COLUMN.getName()));
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

        return news;
    }

    @Override
    public long create(final Object entity) {
        resetValuesForNewQuery();
        long newId = Long.valueOf(-1);

        try {
            String sql = Sql.INSERT.getName();
            sql += NewsTable.TABLE_NAME.getName() + Sql.OPEN_PARENTHESIS.getName();
            sql += NewsTable.TITLE_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += NewsTable.ARTICLE_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += NewsTable.CONTEXT_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += NewsTable.SITUATION_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += NewsTable.KEYWORD_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += NewsTable.CREATION_DATE_COLUMN.getName() + Sql.SEPARATOR.getName();
            sql += NewsTable.ID_AUTHOR_COLUMN.getName() + Sql.CLOSE_PARENTHESIS.getName();
            sql += Sql.VALUES.getName();
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // titulo
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // artigo
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // contexto
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // situacao
            sql += Sql.PARAM_INSERT_TO_COMPLETE.getName(); // palavra-chave
            sql += Sql.DEFAULT_VALUE_DECLARTION.getName() + Sql.SEPARATOR.getName(); // cricao
            sql += Sql.LAST_PARAM_INSERT_TO_COMPLETE.getName(); // autor

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
                newId = resultSet.getLong(NewsTable.ID_COLUMN.getName());
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
            String sql = Sql.UPDATE.getName() + DataBaseHelper.NewsTable.TABLE_NAME.getName() + Sql.SET_UPDATE.getName();
            sql += NewsTable.TITLE_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += NewsTable.ARTICLE_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += NewsTable.CONTEXT_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += NewsTable.SITUATION_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += NewsTable.KEYWORD_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += NewsTable.CREATION_DATE_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += NewsTable.PUBLICATION_DATE_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += NewsTable.ID_AUTHOR_COLUMN.getName() + Sql.PARAM_UPDATE_TO_COMPLETE.getName();
            sql += NewsTable.ID_PUBLISHER_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName();
            sql += Sql.WHERE.getName();
            sql += NewsTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";
            preparForUpdateOrDelete(sql);

            final Noticia news = (Noticia) entity;
            int i = 1;
            preparedStatement.setString(i++, NewsTable.TABLE_NAME.getName());
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
            String sql = Sql.DELETE.getName() + NewsTable.TABLE_NAME.getName();
            sql += Sql.WHERE.getName();
            sql += NewsTable.ID_COLUMN.getName() + Sql.lAST_PARAM_UPDATE_TO_COMPLETE.getName() + ";";

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
        List<Noticia> newsList = null;
        resetValuesForNewQuery();

        try {
            String sql = Sql.SELECT_FULL.getName() + NewsTable.TABLE_NAME.getName();
            sql += " " + criteria;

            preparForReadingOrCreating(sql, false, true);
            resultSet = preparedStatement.executeQuery();

            newsList = new ArrayList<>();
            while (resultSet.next()) {
                final Noticia news = new Noticia();
                news.setId(resultSet.getLong(NewsTable.ID_COLUMN.getName()));
                news.setTitulo(resultSet.getString(NewsTable.TITLE_COLUMN.getName()));
                news.setArtigo(resultSet.getInt(NewsTable.ARTICLE_COLUMN.getName()));
                news.setContexto(resultSet.getString(NewsTable.CONTEXT_COLUMN.getName()));
                news.setSituacao(resultSet.getString(NewsTable.SITUATION_COLUMN.getName()));
                news.setPalabraChave(resultSet.getString(NewsTable.KEYWORD_COLUMN.getName()));
                news.setDataCriacao(resultSet.getTimestamp(NewsTable.CREATION_DATE_COLUMN.getName()));
                news.setDataPublicao(resultSet.getTimestamp(NewsTable.PUBLICATION_DATE_COLUMN.getName()));
                news.setIdAutor(resultSet.getLong(NewsTable.ID_AUTHOR_COLUMN.getName()));
                news.setIdPublicador(resultSet.getLong(NewsTable.ID_PUBLISHER_COLUMN.getName()));

                newsList.add(news);
            }

        } catch (final Exception e) {
            System.out.println("Excecao -> metodo:readByCriteria | classe: " + ClienteDao.class.getName());
            System.out.println(e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet, preparedStatement, connection);
        }

        return newsList;
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
