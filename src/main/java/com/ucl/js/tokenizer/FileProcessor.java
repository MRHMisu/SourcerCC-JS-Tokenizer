package com.ucl.js.tokenizer;

import com.ucl.js.document.CodeBlock;
import com.ucl.js.document.SourceFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.*;
import java.util.*;


public class FileProcessor {


    public static List<SourceFile> getJavaScriptSourceFilePaths(File rootDirectory) {
        List<SourceFile> sourceFiles = new ArrayList<>();
        Collection<File> files = FileUtils.listFilesAndDirs(rootDirectory,
                TrueFileFilter.INSTANCE,
                TrueFileFilter.INSTANCE);
        int parentIdCounter = 0;
        for (File file : files) {
            if (!file.isDirectory()) {
                if (file.getPath().endsWith(".js")) {
                    sourceFiles.add(new SourceFile(parentIdCounter, file.getAbsolutePath()));
                    parentIdCounter++;
                }
            }
        }
        return sourceFiles;
    }

    public static void writeHeaderFile(List<CodeBlock> codeBlocks, String headerFilePath) throws IOException {
        Writer headerWriter = new BufferedWriter(new FileWriter(headerFilePath, true));
        StringBuilder headerBuilder = new StringBuilder();
        for (CodeBlock cb : codeBlocks) {
            headerBuilder.append(cb.getHeaderLine());
        }
        headerWriter.append(headerBuilder.toString());
        headerWriter.close();

    }

    public static void writeTokenFile(List<CodeBlock> codeBlocks, String tokenFilePath) throws IOException {
        Writer headerWriter = new BufferedWriter(new FileWriter(tokenFilePath, true));
        StringBuilder tokenBuilder = new StringBuilder();
        for (CodeBlock cb : codeBlocks) {
            tokenBuilder.append(cb.getTokenLine());
        }
        headerWriter.append(tokenBuilder.toString());
        headerWriter.close();

    }

}
