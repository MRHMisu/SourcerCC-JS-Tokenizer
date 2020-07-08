package com.ucl.js.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * An error listener that immediately bails out of the parse (does not recover)
 * and throws a runtime exception with a descriptive error message.
 */
public class ErrorListener extends BaseErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg, RecognitionException e) {

        String entireMessage = String.format("Syntax error occurs at " + "source: %s, line: %s, index: %s, error message: %s",
                recognizer.getInputStream().getSourceName(), line, charPositionInLine, msg);

        throw new RuntimeException(entireMessage);
    }
}