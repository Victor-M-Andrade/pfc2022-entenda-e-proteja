package br.fai.ep.api.db.helper;

public abstract class DataBaseHelper {

    public static class SQL_COMMAND {
        public static String SELECT_FULL = "SELECT * FROM ";
        public static String INSERT = "INSERT INTO ";
        public static String VALUES = " VALUES(";
        public static String UPDATE = "UPDATE ";
        public static String SET_UPDATE = " SET ";
        public static String DELETE = "DELETE FROM ";
        public static String WHERE = " WHERE ";
        public static String SEPARATOR = ", ";
        public static String OPEN_PARENTHESIS = "(";
        public static String CLOSE_PARENTHESIS = ") ";
        public static String FINAL_CLOSE_PARENTHESIS = ");";
        public static String DEFAULT_VALUE_DECLARTION = "default";
        public static String PARAM_INSERT_TO_COMPLETE = "?, ";
        public static String LAST_PARAM_INSERT_TO_COMPLETE = "?); ";
        public static String PARAM_UPDATE_TO_COMPLETE = "= ?, ";
        public static String lAST_PARAM_UPDATE_TO_COMPLETE = " = ?";
        public static String INNER_JOIN = " INNER JOIN ";
        public static String EQUAL_COMPATION = " = ";
        public static String AND = " AND ";
        public static String OR = " OR ";
        public static String ILIKE = " ILIKE ";
    }
}