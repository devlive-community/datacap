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
            /* Kafka Statement */
            if (child instanceof SqlBaseParser.KafkaStatementContext) {
                this.handlerWithKafkaStatement((SqlBaseParser.KafkaStatementContext) child);
            }
            else if (child instanceof SqlBaseParser.ColumnStatementContext) {
                configure.setColumns(Arrays.asList(child.getText()));
            }
            else if (child instanceof SqlBaseParser.FromClauseContext) {
                configure.setTable(child.getChild(1).getText());
            }
            else if (child instanceof SqlBaseParser.ChildPathStatementContext) {
                this.handlerWithChildPathStatementContext((SqlBaseParser.ChildPathStatementContext) child);
            }
            else if (child instanceof TerminalNode) {
                this.applyToken(child.getText(), false);
            }
        }
    }

    private void handlerWithChildPathStatementContext(SqlBaseParser.ChildPathStatementContext context)
    {
        int childCount = context.getChildCount();
        int i = 0;
        for (; i < childCount; i++) {
            ParseTree child = context.getChild(i);
            if (child instanceof SqlBaseParser.FromClauseContext) {
                this.configure.setTable(child.getChild(1).getText());
            }
            else if (child instanceof TerminalNode) {
                this.applyToken(child.getText(), true);
            }
        }
    }

    private void handlerWithKafkaStatement(SqlBaseParser.KafkaStatementContext context)
    {
        ParseTree node = context.getChild(0);
        if (node instanceof SqlBaseParser.KafkaQueryStatementContext) {
            ParseTree queryNode = node.getChild(0);
            if (queryNode instanceof SqlBaseParser.KafkaQueryTopicStatementContext
                    | queryNode instanceof SqlBaseParser.KafkaQueryConsumerStatementContext) {
                int count = queryNode.getChildCount();
                this.applyToken(queryNode.getChild(0).getText(), false);
                this.applyToken(queryNode.getChild(1).getText(), true);
                // If the total number is greater than 2, the mark specifies the topic, which is the table name
                if (count > 2) {
                    ParseTree fromNode = queryNode.getChild(count - 1);
                    if (fromNode instanceof SqlBaseParser.FromClauseContext) {
                        configure.setTable(fromNode.getChild(1).getText());
                    }
                }
            }
        }
    }

    private void applyToken(String token, boolean isChild)
    {
        try {
            if (isChild) {
                this.configure.setChildToken(token.toUpperCase());
            }
            else {
                this.configure.setToken(token.toUpperCase());
            }
            configure.setSuccessful(true);
        }
        catch (Exception exception) {
            configure.setSuccessful(false);
            configure.setMessage(ExceptionUtils.getMessage(exception));
        }
    }
}
