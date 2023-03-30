package io.edurt.datacap.sql;

import com.google.common.base.Preconditions;
import io.edurt.datacap.sql.parser.SqlBaseLexer;
import io.edurt.datacap.sql.parser.SqlBaseParser;
import lombok.Getter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.ObjectUtils;

public class SqlBaseFormatter
{
    private final String sql;

    @Getter
    private SqlBase parseResult;

    public SqlBaseFormatter(String sql)
    {
        this.sql = sql;
        this.parseResult = new SqlBase();
        this.initialize();
    }

    private SqlBase initialize()
    {
        CharStream stream = CharStreams.fromString(this.sql);
        SqlBaseLexer lexer = new SqlBaseLexer(new SqlCaseInsensitiveStream(stream));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new UnderLineListener());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SqlBaseParser parser = new SqlBaseParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new UnderLineListener());
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

        ParseTree tree = null;
        try {
            tree = parser.singleStatement();
        }
        catch (Exception ex) {
            this.parseResult.setMessage(ex.getMessage());
            this.parseResult.setSuccessful(false);
            return this.parseResult;
        }

        SqlBaseVisitor sqlBaseVisitor = new SqlBaseVisitor();
        if (ObjectUtils.isNotEmpty(tree)) {
            this.parseResult = sqlBaseVisitor.visit(tree);
        }
        else {
            Preconditions.checkArgument(false, "Not support sql");
        }
        return this.parseResult;
    }
}
