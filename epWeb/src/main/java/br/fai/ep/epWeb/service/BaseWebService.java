package br.fai.ep.epWeb.service;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public abstract class BaseWebService {
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String DATE_FORMAT_COMPLETE = "dd/MM/yyyy HH:mm:ss";
    private static final String DATE_FORMAT_SAVE_FILES = "dd-MM-yyyy_HH-mm-ss";

    protected final String API_HOST = "http://api-entenda-e-proteja.us-east-1.elasticbeanstalk.com/api";
//    protected final String API_HOST = "http://localhost:5001/api";

    public static final String PATH_IMAGENS_USERS = "images/users/";
    public static final String PATH_IMAGENS_NEWS = "images/news/";
    public static final String PATH_IMAGENS_PARTNER = "images/partners/";

    public static final String dateFormate(final Timestamp time) {
        return new SimpleDateFormat(DATE_FORMAT).format(time);
    }

    public static final String dateFormateComplete(final Timestamp time) {
        return new SimpleDateFormat(DATE_FORMAT_COMPLETE).format(time);
    }

    public static final String dateFormateForSaveFiles(final Timestamp time) {
        return new SimpleDateFormat(DATE_FORMAT_SAVE_FILES).format(time);
    }

}