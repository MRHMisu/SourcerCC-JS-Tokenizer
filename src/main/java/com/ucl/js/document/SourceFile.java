package com.ucl.js.document;

public class SourceFile {

    private long parentId;
    private String filePath;

    public SourceFile(long parentId, String filePath) {
        this.parentId = parentId;
        this.filePath = filePath;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
