package com.ucl.js.document;

public class CodeBlock {
    private long parentId;
    private long blockId;
    private String filePath;
    private int startLine;
    private int endLine;
    private String tokenStream;
    private int numberOfTokens;

    public CodeBlock(long parentId, long blockId, String filePath, int startLine, int endLine, String tokenStream, int numberOfTokens) {
        this.parentId = parentId;
        this.blockId = blockId;
        this.filePath = filePath;
        this.startLine = startLine;
        this.endLine = endLine;
        this.tokenStream = tokenStream;
        this.numberOfTokens = numberOfTokens;
    }

    public long getParentId() {
        return parentId;
    }

    public long getBlockId() {
        return blockId;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getTokenStream() {
        return tokenStream;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getNumberOfTokens() {
        return numberOfTokens;
    }

    public void setBlockId(long blockId) {
        this.blockId = blockId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getHeaderLine() {
        return this.blockId + "," + this.filePath + "," + this.getStartLine() + "," + this.getEndLine() + "\n";

    }

    public String getTokenLine() {
        return this.blockId + "," + this.parentId + "@#@" + this.getTokenStream() + "\n";
    }
}
