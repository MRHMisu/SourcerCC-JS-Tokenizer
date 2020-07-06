package com.ucl.js;

import com.ucl.js.tokenizer.PropertyFileLoader;

public class Executor {

    public static void main(String[] args) {
        String inputSourceDirectoryPath = PropertyFileLoader.getPropertyByName("inputSourceDirectoryPath");
        String headerFilePath = PropertyFileLoader.getPropertyByName("headerFilePath");
        String tokenFilePath = PropertyFileLoader.getPropertyByName("tokenFilePath");

        int maximumLine = Integer.parseInt(PropertyFileLoader.getPropertyByName("maximumLine"));
        int minimumLine = Integer.parseInt(PropertyFileLoader.getPropertyByName("minimumLine"));
        int maximumToken = Integer.parseInt(PropertyFileLoader.getPropertyByName("maximumToken"));
        int minimumToken = Integer.parseInt(PropertyFileLoader.getPropertyByName("minimumToken"));
    }
}
