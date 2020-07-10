package com.ucl.js.tokenizer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyFileLoader {

    public static Map<String, String> getProperties(File configFile) {
        Properties prop = new Properties();
        Map<String, String> properties = new HashMap<>();
        InputStream input = null;
        try {
            input = new FileInputStream(configFile);
            // load a properties file
            prop.load(input);
            properties.put("sourceDirectoryPath", prop.getProperty("sourceDirectoryPath"));
            properties.put("headerFilePath", prop.getProperty("headerFilePath"));
            properties.put("tokenFilePath", prop.getProperty("tokenFilePath"));
            properties.put("granularity", prop.getProperty("granularity"));
            properties.put("language", prop.getProperty("language"));
            properties.put("maximumLine", prop.getProperty("maximumLine"));
            properties.put("minimumLine", prop.getProperty("minimumLine"));
            properties.put("maximumToken", prop.getProperty("maximumToken"));
            properties.put("minimumToken", prop.getProperty("minimumToken"));
            properties.put("numberOfThread", prop.getProperty("numberOfThread"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }
}
