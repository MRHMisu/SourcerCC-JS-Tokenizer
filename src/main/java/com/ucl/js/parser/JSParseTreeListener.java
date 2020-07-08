package com.ucl.js.parser;


import com.ucl.js.document.CodeBlock;
import com.ucl.js.document.SourceFile;
import com.ucl.js.tokenizer.Tokenizer;
import javascript.JavaScriptParser;
import javascript.JavaScriptParserBaseListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class works for collecting method blocks by traversing ANTLR4 generated Parse Tree
 * based on the ANTLR4 recommended Listener pattern.
 */

public class JSParseTreeListener extends JavaScriptParserBaseListener {

    private SourceFile sourceFile;
    private Map<Integer, Integer> sourceStartEndMap;
    private Map<Integer, String> sourceCodeMap;
    private List<CodeBlock> codeBlocks;
    private long blockIdCounter = 0;

    /**
     * Constructor to build JSParseTreeListener
     *
     * @param sourceFile JavaScript source file path
     */
    public JSParseTreeListener(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
        this.sourceStartEndMap = new HashMap<>();
        this.sourceCodeMap = new HashMap<>();
        codeBlocks = new ArrayList<>();
    }

    public List<CodeBlock> getCodeBlocks() {
        return this.codeBlocks;
    }

    @Override
    public void enterFunctionDeclaration(JavaScriptParser.FunctionDeclarationContext ctx) {
        buildCodeBlock(ctx);
    }

    @Override
    public void enterFunctionExpression(JavaScriptParser.FunctionExpressionContext ctx) {
        buildCodeBlock(ctx);
    }

    @Override
    public void enterMethodDefinition(JavaScriptParser.MethodDefinitionContext ctx) {
        buildCodeBlock(ctx);
    }

    /**
     * This method builds a Complete Method from the ANTLR4 generated Parse Tree.
     * It collects function name, parameters, headers source, start and end line.
     * Moreover, it helps to detect duplicate extracted methods from the Parse Tree.sss
     *
     * @param tree ANTLR4 generated Parse Tree
     */
    protected void buildCodeBlock(ParseTree tree) {
        ParserRuleContext ctx = (ParserRuleContext) tree;
        int startLine = ctx.getStart().getLine();
        int endLine = ctx.getStop().getLine();
        if ((sourceStartEndMap.containsKey(startLine) && sourceStartEndMap.get(startLine).equals(endLine))) {
            String src = getSourceCode(tree);
            if (sourceCodeMap.get(startLine).equals(src)) {
                return;
            }
        }
        String codeStream = getSourceCode(tree);
        Tokenizer tokenizer = new Tokenizer(codeStream);
        String tokenStream = tokenizer.getToken();
        int numberOfTokens = tokenizer.getNumberOfToken();
        CodeBlock codeBlock = new CodeBlock(this.sourceFile.getParentId(), blockIdCounter,
                this.sourceFile.getFilePath(), startLine, endLine, tokenStream, numberOfTokens);
        this.codeBlocks.add(codeBlock);
        this.blockIdCounter++;
    }

    /**
     * This method builds a CodeBlock object using the complete source code of the file.
     *
     * @param tree ANTLR4 generated Parse Tree
     * @return A CodeBlock Object containing the whole source code of the file block.
     */
    public CodeBlock getFileLevelCodeBlock(ParseTree tree) {
        ParserRuleContext ctx = (ParserRuleContext) tree;
        int startLine = ctx.getStart().getLine();
        int endLine = ctx.getStop().getLine();
        String codeStream = getSourceCode(tree);
        Tokenizer tokenizer = new Tokenizer(codeStream);
        String tokenStream = tokenizer.getToken();
        int numberOfTokens = tokenizer.getNumberOfToken();
        CodeBlock codeBlock = new CodeBlock(this.sourceFile.getParentId(), blockIdCounter,
                this.sourceFile.getFilePath(), startLine, endLine, tokenStream, numberOfTokens);
        return codeBlock;
    }


    /**
     * This method is responsible for retrieving the source code from the ANTLR4 generated Parse Tree.
     * It travers ANTLR4 generated Parse Tree to get the list of terminal nodes and then concatenating
     * the terminal nodes values as string to produce the tokenize source code.
     *
     * @param tree ANTLR4 generated Parse Tree
     * @return Extract function block source code.
     */
    protected String getSourceCode(ParseTree tree) {
        StringBuilder builder = new StringBuilder();
        List<String> terminalNodes = traverseParseTree(tree);
        for (String tm : terminalNodes) {
            builder.append(tm).append("\\s");
        }
        return builder.toString().trim();
    }

    /**
     * Travers ANTLR4 generated Parse Tree using Depth-First-Search and collect all the terminal nodes.
     *
     * @param tree tree ANTLR4 generated Parse Tree
     * @return A list of TerminalNodeImpl derived from function block.
     */
    protected List<String> traverseParseTree(ParseTree tree) {
        List<String> terminalNodes = new ArrayList<>();
        List<ParseTree> firstStack = new ArrayList<>();
        firstStack.add(tree);
        List<List<ParseTree>> childListStack = new ArrayList<>();
        childListStack.add(firstStack);
        while (!childListStack.isEmpty()) {
            List<ParseTree> childStack = childListStack.get(childListStack.size() - 1);
            if (childStack.isEmpty()) {
                childListStack.remove(childListStack.size() - 1);
            } else {
                tree = childStack.remove(0);
                if (tree instanceof TerminalNodeImpl) {
                    terminalNodes.add(tree.getText());
                }
                if (tree.getChildCount() > 0) {
                    List<ParseTree> children = new ArrayList<>();
                    for (int i = 0; i < tree.getChildCount(); i++) {
                        children.add(tree.getChild(i));
                    }
                    childListStack.add(children);
                }
            }
        }
        return terminalNodes;
    }

}