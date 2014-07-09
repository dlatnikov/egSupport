package com.egrocery;

import java.sql.*;

public class ConnectionManager {

    private static Connection connection = null;
    private static String dbUrl = "jdbc:mysql://127.0.0.1:3306/factual_price_fix";
    private static String dbUserName = "root";
    private static String dbPassword = "";

    private static String whDbdbUrl = "jdbc:mysql://127.0.0.1:3306/factual_wh";
    private static String whDbdbUserName = "root";
    private static String whDbdbPassword = "";

    public static enum ConnectionType {
        factual,
        factual_warehouse
    }

    static {
        try {
            try {
                DriverManager.getDriver(dbUrl);
            } catch (SQLException e) {
                if (e.getSQLState().equals("08001")) {
                    DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                } else {
                    throw e;
                }
            }
            connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static Connection getConnection(ConnectionType connectionType) {
        switch (connectionType) {
            case factual_warehouse:
                return WhDbConnectionSingleton.connection;

            default:
                return connection;
        }
    }

    private static class WhDbConnectionSingleton {
        public static Connection connection;

        static {
            try {
                connection = DriverManager.getConnection(whDbdbUrl, whDbdbUserName, whDbdbPassword);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
