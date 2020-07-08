package com.ucl.js;

import com.ucl.js.document.CodeBlock;
import com.ucl.js.document.Configuration;
import com.ucl.js.tokenizer.FileProcessor;
import com.ucl.js.tokenizer.PropertyFileLoader;
import com.ucl.js.tokenizer.SourcerCCJSTokenizer;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Executor {

    public static void main(String[] args) {
        String sourceDirectoryPath = PropertyFileLoader.getPropertyByName("sourceDirectoryPath");
        String headerFilePath = PropertyFileLoader.getPropertyByName("headerFilePath");
        String tokenFilePath = PropertyFileLoader.getPropertyByName("tokenFilePath");
        String granularity = PropertyFileLoader.getPropertyByName("granularity");
        String language = PropertyFileLoader.getPropertyByName("language");
        int maximumLine = Integer.parseInt(PropertyFileLoader.getPropertyByName("maximumLine"));
        int minimumLine = Integer.parseInt(PropertyFileLoader.getPropertyByName("minimumLine"));
        int maximumToken = Integer.parseInt(PropertyFileLoader.getPropertyByName("maximumToken"));
        int minimumToken = Integer.parseInt(PropertyFileLoader.getPropertyByName("minimumToken"));
        int numberOfThread = Integer.parseInt(PropertyFileLoader.getPropertyByName("numberOfThread"));

        Configuration configuration = new Configuration(sourceDirectoryPath,
                headerFilePath, tokenFilePath, granularity, language, maximumLine, minimumLine, maximumToken, minimumToken, numberOfThread);

        if (checkConfiguration(configuration)) {
            //executeTokenizer(configuration);
        }
    }

    public static boolean checkConfiguration(Configuration configuration) {
        File sourceDir = new File(configuration.getSourceDirectoryPath());
        if (!sourceDir.isDirectory())
            return false;
        if (!configuration.getGranularity().equals("function") || !configuration.getGranularity().equals("file"))
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
