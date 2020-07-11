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
            properties.put("maximumLines", prop.getProperty("maximumLines"));
            properties.put("minimumLines", prop.getProperty("minimumLines"));
            properties.put("maximumTokens", prop.getProperty("maximumTokens"));
            properties.put("minimumTokens", prop.getProperty("minimumTokens"));
            properties.put("numberOfThreads", prop.getProperty("numberOfThreads"));
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
