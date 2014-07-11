package com.egrocery;

/**
 * Created by akolesnik on 7/9/14.
 */

import com.mysql.jdbc.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final Properties properties;

    private static class ConfigSingleton {
        private static final Config config;

        static {
            try {
                config = new Config();
            } catch (IOException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    public static Config getConfig() {
        return ConfigSingleton.config;
    }

    private Config() throws IOException {
        properties = new Properties();
        InputStream input = new FileInputStream("./src/resources/config.properties");
        properties.load(input);

        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Object key : properties.keySet()) {
            String systemPropertyValue = System.getProperty((String) key);
            if (!StringUtils.isNullOrEmpty(systemPropertyValue)) {
                properties.put(key, systemPropertyValue);
            }
        }
    }

    private String getStringProperty(String name) {
        String res = properties.getProperty(name);
        if (res == null) {
            throw new RuntimeException("Property not found: " + name);
        }
        return res;
    }

    public String getFactualDbConnectionString() {
        return getStringProperty("factual_db_connection_string");
    }

    public String getDbUserName() {
        return getStringProperty("db_username");
    }

    public String getDbPassword() {
        return getStringProperty("db_password");
    }

    public String getFactualWhConnectionString() {
        return getStringProperty("factual_warehouse_connection_string");
    }

    public String getImpexExportFolderName() {
        return getStringProperty("impex_export_folder_name");
    }


}
