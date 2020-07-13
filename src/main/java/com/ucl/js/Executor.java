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
        File configFile = new File(args[0]);
        if (configFile.isFile()) {
            Map<String, String> properties = PropertyFileLoader.getProperties(configFile);
            Configuration configuration = getConfiguration(properties);
            System.out.println(configuration.toString());
            System.out.println("Checking Configuration...");
            if (validateConfiguration(configuration)) {
                fixedConfiguration(configuration);
                System.out.println("Execution Started...");
                executeTokenizer(configuration);
            } else {
                System.out.println("Wrong Configuration Parameters");
            }
        }
    }

    public static void executeTokenizer(Configuration configuration) {
        SourcerCCJSTokenizer sc = new SourcerCCJSTokenizer(configuration);
        List<CodeBlock> codeBlocks = sc.tokenizeSourceFiles();
        try {
            FileProcessor.writeHeaderFile(codeBlocks, configuration.getHeaderFilePath());
            FileProcessor.writeTokenFile(codeBlocks, configuration.getTokenFilePath());
            System.out.println("Task Completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configuration getConfiguration(Map<String, String> properties) {
        String sourceDirectoryPath = properties.get("sourceDirectoryPath");
        String headerFilePath = properties.get("headerFilePath");
        String tokenFilePath = properties.get("tokenFilePath");
        String granularity = properties.get("granularity");
        String language = properties.get("language");
        int maximumLines = Integer.parseInt(properties.get("maximumLines"));
        int minimumLines = Integer.parseInt(properties.get("minimumLines"));
        int maximumTokens = Integer.parseInt(properties.get("maximumTokens"));
        int minimumTokens = Integer.parseInt(properties.get("minimumTokens"));
        int numberOfThreads = Integer.parseInt(properties.get("numberOfThreads"));
        return new Configuration(sourceDirectoryPath,
                headerFilePath, tokenFilePath, granularity, language, maximumLines, minimumLines, maximumTokens, minimumTokens, numberOfThreads);
    }

    public static boolean validateConfiguration(Configuration configuration) {
        File sourceDir = new File(configuration.getSourceDirectoryPath());
        if (!sourceDir.isDirectory())
            return false;
        if (!(configuration.getGranularity().equals("function") || configuration.getGranularity().equals("file")))
            return false;
        if (!configuration.getLanguage().equals("javascript"))
            return false;
        if (configuration.getMinimumLines() < 0 || configuration.getMaximumLines() < 0)
            return false;
        if (configuration.getMinimumTokens() < 0 || configuration.getMaximumTokens() < 0)
            return false;
        if (configuration.getMaximumTokens() != 0 && (configuration.getMinimumTokens() >= configuration.getMaximumTokens()))
            return false;
        if (configuration.getMaximumLines() != 0 && (configuration.getMinimumLines() >= configuration.getMaximumLines()))
            return false;
        return configuration.getNumberOfThreads() >= 0;
    }

    public static void fixedConfiguration(Configuration configuration) {
        int numberOfCore = Runtime.getRuntime().availableProcessors();
        if (configuration.getNumberOfThreads() > numberOfCore) {
            configuration.setNumberOfThreads(numberOfCore);
        }
        if (configuration.getMinimumLines() == 0 && configuration.getMaximumLines() == 0) {
            configuration.setMaximumLines(Integer.MAX_VALUE);
        } else if (configuration.getMinimumLines() != 0 && configuration.getMaximumLines() == 0) {
            configuration.setMaximumLines(Integer.MAX_VALUE);
        }
        if (configuration.getMinimumTokens() == 0 && configuration.getMaximumTokens() == 0) {
            configuration.setMaximumTokens(Integer.MAX_VALUE);
        } else if (configuration.getMinimumTokens() != 0 && configuration.getMaximumTokens() == 0) {
            configuration.setMaximumTokens(Integer.MAX_VALUE);
        }
    }
}
