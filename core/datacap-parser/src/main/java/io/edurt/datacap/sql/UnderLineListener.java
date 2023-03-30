package io.edurt.datacap.sql;

import com.google.common.base.Preconditions;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class UnderLineListener
        extends BaseErrorListener
{
    public void syntaxError(Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line, int charPositionInLine,
            String msg,
            RecognitionException e)
    {
        Preconditions.checkState(false, "line " + line + ":" + charPositionInLine + " " + msg);
    }
}
