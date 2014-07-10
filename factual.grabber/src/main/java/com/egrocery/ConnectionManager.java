package com.egrocery;

import java.sql.*;

public class ConnectionManager {

    private static Connection connection = null;
    private static String dbUrl = Config.getConfig().getFactualDbConnectionString();
    private static String dbUserName = Config.getConfig().getDbUserName();
    private static String dbPassword = Config.getConfig().getDbPassword();

    private static String whDbdbUrl = Config.getConfig().getFactualWhConnectionString();

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
                connection = DriverManager.getConnection(whDbdbUrl, dbUserName, dbPassword);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
