package com.ucl.js.document;


public class Configuration {

    private String sourceDirectoryPath;
    private String headerFilePath;
    private String tokenFilePath;
    private String granularity;
    private String language;
    private int maximumLine;
    private int minimumLine;
    private int maximumToken;
    private int minimumToken;
    private int numberOfThread;

    public Configuration(String sourceDirectoryPath, String headerFilePath, String tokenFilePath,
                         String granularity, String language, int maximumLine, int minimumLine, int maximumToken,
                         int minimumToken, int numberOfThread) {
        this.sourceDirectoryPath = sourceDirectoryPath;
        this.headerFilePath = headerFilePath;
        this.tokenFilePath = tokenFilePath;
        this.granularity = granularity;
        this.language = language;
        this.maximumLine = maximumLine;
        this.minimumLine = minimumLine;
        this.maximumToken = maximumToken;
        this.minimumToken = minimumToken;
        this.numberOfThread = numberOfThread;
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

    public int getMaximumLine() {
        return maximumLine;
    }

    public int getMinimumLine() {
        return minimumLine;
    }

    public int getMaximumToken() {
        return maximumToken;
    }

    public int getMinimumToken() {
        return minimumToken;
    }

    public int getNumberOfThread() {
        return numberOfThread;
    }
}
