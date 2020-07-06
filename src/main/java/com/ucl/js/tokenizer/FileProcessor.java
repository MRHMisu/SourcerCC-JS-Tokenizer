package com.ucl.js.tokenizer;

import com.ucl.js.document.SourceFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.*;
import java.util.*;


public class FileProcessor {


    protected static List<SourceFile> getJavaScriptSourceFilePaths(File rootDirectory) {
        List<SourceFile> sourceFiles = new ArrayList<>();
        Collection<File> files = FileUtils.listFilesAndDirs(rootDirectory,
                TrueFileFilter.INSTANCE,
                TrueFileFilter.INSTANCE);
        int parentIdCounter = 0;
        for (File file : files) {
            if (!file.isDirectory()) {
                if (!file.getPath().endsWith(".js")) {
                    sourceFiles.add(new SourceFile(parentIdCounter, file.getAbsolutePath()));
                    parentIdCounter++;
                }
            }
        }
        return sourceFiles;
    }

    public static void writeHeaderFile(List<String> tokens, String outputPath) throws IOException {
        String result = "";
        Writer output = new BufferedWriter(new FileWriter(outputPath, true));

        output.append(result);
        output.close();

    }

    public static void writeTokenFile(List<String> tokens, String outputPath) throws IOException {
        String result = "";
        Writer output = new BufferedWriter(new FileWriter(outputPath, true));

        output.append(result);
        output.close();
    }
}
