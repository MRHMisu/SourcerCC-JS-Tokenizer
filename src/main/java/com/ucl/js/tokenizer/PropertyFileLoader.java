package com.ucl.js.tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileLoader {

    public static String getPropertyByName(String propertyName) {
        Properties prop = new Properties();
        InputStream input = null;
        String requiredPorprerty = "";
        try {

            String filename = "config.properties";
            input = PropertyFileLoader.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return requiredPorprerty;
            }

            // load a properties file from class path, inside static method
            prop.load(input);

            // get the property value and print it out
            requiredPorprerty = prop.getProperty(propertyName);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return requiredPorprerty;

    }

}
