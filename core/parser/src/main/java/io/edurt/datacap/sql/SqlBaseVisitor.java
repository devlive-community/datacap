package io.edurt.datacap.sql;

import io.edurt.datacap.sql.parser.SqlBaseParser;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Arrays;

public class SqlBaseVisitor
        implements ParseTreeVisitor<SqlBase>
{
    private SqlBase configure;

    public SqlBaseVisitor()
    {
        configure = new SqlBase();
    }

    @Override
    public SqlBase visit(ParseTree parseTree)
    {
        int childCount = parseTree.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ParseTree child = parseTree.getChild(i);
            if (child instanceof SqlBaseParser.StatementContext) {
                SqlBaseParser.StatementContext statementContext = (SqlBaseParser.StatementContext) child;
                this.handlerWithStatement(statementContext);
            }
        }
        return this.configure;
    }

    @Override
    public SqlBase visitChildren(RuleNode ruleNode)
    {
        return this.configure;
    }

    @Override
    public SqlBase visitTerminal(TerminalNode terminalNode)
    {
        return this.configure;
    }

    @Override
    public SqlBase visitErrorNode(ErrorNode errorNode)
    {
        this.configure.setSuccessful(false);
        this.configure.setMessage(errorNode.getText());
        return configure;
    }

    private void handlerWithStatement(SqlBaseParser.StatementContext statementContext)
    {
        int childCount = statementContext.getChildCount();
        int i = 0;
        for (; i < childCount; i++) {
            ParseTree child = statementContext.getChild(i);
            if (child instanceof SqlBaseParser.ColumnStatementContext) {
                configure.setColumns(Arrays.asList(child.getText()));
            }
            else if (child instanceof SqlBaseParser.FromClauseContext) {
                configure.setTable(child.getChild(1).getText());
            }
            else if (child instanceof TerminalNode) {
                try {
                    configure.setToken(SqlBaseToken.valueOf(child.getText()));
                    configure.setSuccessful(true);
                }
                catch (Exception exception) {
                    configure.setSuccessful(false);
                    configure.setMessage(ExceptionUtils.getMessage(exception));
                }
            }
        }
    }
}
