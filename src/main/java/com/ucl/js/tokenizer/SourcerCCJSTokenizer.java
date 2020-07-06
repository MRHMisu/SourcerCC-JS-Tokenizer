package com.ucl.js.tokenizer;

import com.ucl.js.document.SourceFile;
import com.ucl.js.parser.Builder;
import com.ucl.js.parser.JSParseTreeListener;
import com.ucl.js.document.CodeBlock;
import javascript.JavaScriptParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.collections4.ListUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public final class SourcerCCJSTokenizer {

    private final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    private final int NUMBER_OF_FILES_IN_A_BATCH = 10;

    private List<CodeBlock> tokenizeSourceFilesConcurrently(String rootDirectory) {

        List<CodeBlock> tokenizedCodeBlocks = new ArrayList<>();
        File rootDir = new File(rootDirectory);
        List<SourceFile> filePaths = FileProcessor.getJavaScriptSourceFilePaths(rootDir);
        List<List<SourceFile>> filePathBatches = ListUtils.partition(filePaths, NUMBER_OF_FILES_IN_A_BATCH);

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        // Callable, return a future, submit and run the task async
        List<Callable<List<CodeBlock>>> listOfCallable = new ArrayList<>();
        for (List<SourceFile> sourceFileList : filePathBatches) {
            Callable<List<CodeBlock>> c = () -> {
                return parseCodeBlocks(sourceFileList);
            };
            listOfCallable.add(c);
        }
        try {
            List<Future<List<CodeBlock>>> futures = executor.invokeAll(listOfCallable);
            for (Future<List<CodeBlock>> future : futures) {
                if (future.isDone()) {
                    tokenizedCodeBlocks.addAll(future.get());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            // thread was interrupted
            e.printStackTrace();
        } finally {
            // shut down the executor manually
            executor.shutdown();
        }
        return tokenizedCodeBlocks;
    }

    private List<CodeBlock> parseCodeBlocks(List<SourceFile> sourceFiles) {
        List<CodeBlock> codeBlocks = new ArrayList<>();
        for (SourceFile sf : sourceFiles) {
            codeBlocks.addAll(parseCodeBlock(sf));
        }
        return codeBlocks;
    }

    private List<CodeBlock> parseCodeBlock(SourceFile sourceFile) {
        List<CodeBlock> codeBlock = new ArrayList<>();
        try {
            CharStream sourceStream = CharStreams.fromFileName(sourceFile.getFilePath());
            JavaScriptParser parser = new Builder.Parser(sourceStream).build();
            ParseTree parseTree = parser.program();
            JSParseTreeListener jsParseTreeListener = new JSParseTreeListener(sourceFile, parseTree);
            ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
            codeBlock.addAll(jsParseTreeListener.getCodeBlocks());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            System.err.println("Source file->" + sourceFile.getFilePath() + "-cannot be parsed ");
        }
        return codeBlock;
    }
}
