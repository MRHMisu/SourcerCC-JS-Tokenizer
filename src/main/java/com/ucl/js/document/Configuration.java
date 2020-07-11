package com.ucl.js.document;


public class Configuration {

    private String sourceDirectoryPath;
    private String headerFilePath;
    private String tokenFilePath;
    private String granularity;
    private String language;
    private int maximumLines;
    private int minimumLines;
    private int maximumTokens;
    private int minimumTokens;
    private int numberOfThreads;

    public Configuration(String sourceDirectoryPath, String headerFilePath, String tokenFilePath,
                         String granularity, String language, int maximumLines, int minimumLines, int maximumTokens,
                         int minimumTokens, int numberOfThreads) {
        this.sourceDirectoryPath = sourceDirectoryPath;
        this.headerFilePath = headerFilePath;
        this.tokenFilePath = tokenFilePath;
        this.granularity = granularity;
        this.language = language;
        this.maximumLines = maximumLines;
        this.minimumLines = minimumLines;
        this.maximumTokens = maximumTokens;
        this.minimumTokens = minimumTokens;
        this.numberOfThreads = numberOfThreads;
    }

    public String getSourceDirectoryPath() {
        return sourceDirectoryPath;
    }

    public String getHeaderFilePath() {
        return headerFilePath;
    }

    public String getTokenFilePath() {
        return tokenFilePath;
    }

    public String getGranularity() {
        return granularity;
    }

    public String getLanguage() {
        return language;
    }

    public int getMaximumLines() {
        return maximumLines;
    }

    public int getMinimumLines() {
        return minimumLines;
    }

    public int getMaximumTokens() {
        return maximumTokens;
    }

    public int getMinimumTokens() {
        return minimumTokens;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }
}
