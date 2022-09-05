package br.fai.ep.epWeb.service;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public abstract class BaseServiceWeb {
    protected final String API_HOST = "http://localhost:8080/api";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String DATE_FORMAT_COMPLETE = "dd/MM/yyyy HH:mm:ss";
    
    public static final String dateFormate(final Timestamp time) {
        return new SimpleDateFormat(DATE_FORMAT).format(time);
    }

    public static final String dateFormateComplete(final Timestamp time) {
        return new SimpleDateFormat(DATE_FORMAT_COMPLETE).format(time);
    }
}