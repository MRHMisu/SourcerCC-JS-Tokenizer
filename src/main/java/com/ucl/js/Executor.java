package com.ucl.js;

import com.ucl.js.document.CodeBlock;
import com.ucl.js.document.Configuration;
import com.ucl.js.tokenizer.FileProcessor;
import com.ucl.js.tokenizer.PropertyFileLoader;
import com.ucl.js.tokenizer.SourcerCCJSTokenizer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Executor {

    public static void main(String[] args) {
        File configFile = new File(args[1]);
        if (configFile.isFile()) {
            Map<String, String> properties = PropertyFileLoader.getProperties(configFile);
            Configuration configuration = getConfiguration(properties);
            if (checkConfiguration(configuration)) {
                executeTokenizer(configuration);
            }
        }
    }

    public static Configuration getConfiguration(Map<String, String> properties) {
        String sourceDirectoryPath = properties.get("sourceDirectoryPath");
        String headerFilePath = properties.get("headerFilePath");
        String tokenFilePath = properties.get("tokenFilePath");
        String granularity = properties.get("granularity");
        String language = properties.get("language");
        int maximumLine = Integer.parseInt(properties.get("maximumLine"));
        int minimumLine = Integer.parseInt(properties.get("minimumLine"));
        int maximumToken = Integer.parseInt(properties.get("maximumToken"));
        int minimumToken = Integer.parseInt(properties.get("minimumToken"));
        int numberOfThread = Integer.parseInt(properties.get("numberOfThread"));
        Configuration configuration = new Configuration(sourceDirectoryPath,
                headerFilePath, tokenFilePath, granularity, language, maximumLine, minimumLine, maximumToken, minimumToken, numberOfThread);
        return configuration;
    }


    public static boolean checkConfiguration(Configuration configuration) {
        File sourceDir = new File(configuration.getSourceDirectoryPath());
        if (!sourceDir.isDirectory())
            return false;
        if (!(configuration.getGranularity().equals("function") || configuration.getGranularity().equals("file")))
            return false;
        if (!configuration.getLanguage().equals("javascript"))
            return false;
        return true;
    }

    public static void executeTokenizer(Configuration configuration) {
        SourcerCCJSTokenizer sc = new SourcerCCJSTokenizer(configuration);
        List<CodeBlock> codeBlocks = sc.tokenizeSourceFilesConcurrently();
        try {
            FileProcessor.writeHeaderFile(codeBlocks, configuration.getHeaderFilePath());
            FileProcessor.writeTokenFile(codeBlocks, configuration.getTokenFilePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
