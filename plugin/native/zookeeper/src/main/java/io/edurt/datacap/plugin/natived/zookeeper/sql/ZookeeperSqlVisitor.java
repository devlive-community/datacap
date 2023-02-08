package io.edurt.datacap.plugin.natived.zookeeper.sql;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Arrays;

public class ZookeeperSqlVisitor
        implements ParseTreeVisitor<ZookeeperSql>
{
    ZookeeperSql configure;

    public ZookeeperSqlVisitor()
    {
        configure = new ZookeeperSql();
    }

    @Override
    public ZookeeperSql visit(ParseTree tree)
    {
        int childCount = tree.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof ZookeeperSqlParser.StatementContext) {
                ZookeeperSqlParser.StatementContext statementContext = (ZookeeperSqlParser.StatementContext) child;
                handlerWithStatement(statementContext);
            }
        }
        return configure;
    }

    private void handlerWithStatement(ZookeeperSqlParser.StatementContext statementContext)
    {
        int childCount = statementContext.getChildCount();
        int i = 0;
        for (; i < childCount; i++) {
            ParseTree child = statementContext.getChild(i);
            if (child instanceof ZookeeperSqlParser.ColumnStatementContext) {
                configure.setColumns(Arrays.asList(child.getText()));
            }
            else if (child instanceof ZookeeperSqlParser.FromClauseContext) {
                configure.setTable(child.getChild(1).getText());
                configure.setFrom(true);
            }
            else if (child instanceof TerminalNode) {
                if (child.getText().equalsIgnoreCase("SELECT")) {
                    configure.setSupport(true);
                    configure.setToken(ZookeeperSqlToken.SELECT);
                }
            }
        }
    }

    @Override
    public ZookeeperSql visitChildren(RuleNode node)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ZookeeperSql visitTerminal(TerminalNode node)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ZookeeperSql visitErrorNode(ErrorNode node)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
