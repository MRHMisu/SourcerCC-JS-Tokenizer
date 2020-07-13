package com.ucl.js.tokenizer;

import com.ucl.js.JavaScriptParser;
import com.ucl.js.document.Configuration;
import com.ucl.js.document.SourceFile;
import com.ucl.js.extractor.Builder;
import com.ucl.js.extractor.JSParseTreeListener;
import com.ucl.js.document.CodeBlock;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.collections4.ListUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class SourcerCCJSTokenizer {

    private final int NUMBER_OF_CORE = Runtime.getRuntime().availableProcessors();
    private final Configuration configuration;

    public SourcerCCJSTokenizer(Configuration configuration) {
        this.configuration = configuration;
    }

    public List<CodeBlock> tokenizeSourceFiles() {
        List<CodeBlock> codeBlocks = tokenizeSourceFilesConcurrently();
        long blockId = 0;
        for (CodeBlock cb : codeBlocks) {
            cb.setParentId(-1);
            cb.setBlockId(blockId);
            blockId++;
        }
        return codeBlocks;
    }

    public List<CodeBlock> tokenizeSourceFilesConcurrently() {
        List<CodeBlock> tokenizedCodeBlocks = new ArrayList<>();
        File rootDir = new File(this.configuration.getSourceDirectoryPath());
        List<SourceFile> filePaths = FileProcessor.getJavaScriptSourceFilePaths(rootDir);
        List<List<SourceFile>> filePathBatches = ListUtils.partition(filePaths, this.configuration.getNumberOfThreads());

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_CORE);
        // Callable, return a future, submit and run the task async
        List<Callable<List<CodeBlock>>> listOfCallable = new ArrayList<>();
        for (List<SourceFile> sourceFileList : filePathBatches) {
            Callable<List<CodeBlock>> c = () -> parseCodeBlocks(sourceFileList);
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
            JSParseTreeListener jsParseTreeListener = new JSParseTreeListener(sourceFile);
            if (this.configuration.getGranularity().equals("file")) {
                codeBlock.add(jsParseTreeListener.getFileLevelCodeBlock(parseTree));
            } else {
                ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
                List<CodeBlock> codeBlocks = jsParseTreeListener.getCodeBlocks();
                codeBlock.addAll(filterCodeBlock(codeBlocks));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.err.println("Source file->" + sourceFile.getFilePath() + "-cannot be parsed ");
        }
        return codeBlock;
    }

    private List<CodeBlock> filterCodeBlock(List<CodeBlock> codeBlocks) {
        List<CodeBlock> filteredCodeBlocks = new ArrayList<>();
        for (CodeBlock cb : codeBlocks) {
            int range = cb.getEndLine() - cb.getStartLine();
            int tokenLength = cb.getNumberOfTokens();
            if (checkCodeBlockSize(range) && checkCodeBlockTokenLength(tokenLength)) {
                filteredCodeBlocks.add(cb);
            }
        }
        return filteredCodeBlocks;
    }

    private boolean checkCodeBlockSize(int range) {
        return range >= this.configuration.getMinimumLines() && range <= this.configuration.getMaximumLines();
    }

    private boolean checkCodeBlockTokenLength(int tokenLength) {
        return tokenLength >= this.configuration.getMinimumTokens() && tokenLength <= this.configuration.getMaximumTokens();
    }
}
