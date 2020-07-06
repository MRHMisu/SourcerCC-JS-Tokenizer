package com.ucl.js.document;

public class CodeBlock {
    private long parentId;
    private long blockId;
    private String filePath;
    private String tokenStream;
    private int startLine;
    private int endLine;

    public CodeBlock(long parentId, long blockId, String filePath, int startLine, int endLine, String tokenStream) {
        this.parentId = parentId;
        this.blockId = blockId;
        this.filePath = filePath;
        this.startLine = startLine;
        this.endLine = endLine;
        this.tokenStream = tokenStream;
    }


}
