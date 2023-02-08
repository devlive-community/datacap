package io.edurt.datacap.plugin.natived.zookeeper.sql;
// Generated from ZookeeperSql.g4 by ANTLR 4.9.3

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ZookeeperSqlParser}.
 */
public interface ZookeeperSqlListener
        extends ParseTreeListener
{
    /**
     * Enter a parse tree produced by {@link ZookeeperSqlParser#singleStatement}.
     *
     * @param ctx the parse tree
     */
    void enterSingleStatement(ZookeeperSqlParser.SingleStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link ZookeeperSqlParser#singleStatement}.
     *
     * @param ctx the parse tree
     */
    void exitSingleStatement(ZookeeperSqlParser.SingleStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link ZookeeperSqlParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterStatement(ZookeeperSqlParser.StatementContext ctx);

    /**
     * Exit a parse tree produced by {@link ZookeeperSqlParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitStatement(ZookeeperSqlParser.StatementContext ctx);

    /**
     * Enter a parse tree produced by {@link ZookeeperSqlParser#columnStatement}.
     *
     * @param ctx the parse tree
     */
    void enterColumnStatement(ZookeeperSqlParser.ColumnStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link ZookeeperSqlParser#columnStatement}.
     *
     * @param ctx the parse tree
     */
    void exitColumnStatement(ZookeeperSqlParser.ColumnStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link ZookeeperSqlParser#fromClause}.
     *
     * @param ctx the parse tree
     */
    void enterFromClause(ZookeeperSqlParser.FromClauseContext ctx);

    /**
     * Exit a parse tree produced by {@link ZookeeperSqlParser#fromClause}.
     *
     * @param ctx the parse tree
     */
    void exitFromClause(ZookeeperSqlParser.FromClauseContext ctx);

    /**
     * Enter a parse tree produced by {@link ZookeeperSqlParser#tableName}.
     *
     * @param ctx the parse tree
     */
    void enterTableName(ZookeeperSqlParser.TableNameContext ctx);

    /**
     * Exit a parse tree produced by {@link ZookeeperSqlParser#tableName}.
     *
     * @param ctx the parse tree
     */
    void exitTableName(ZookeeperSqlParser.TableNameContext ctx);

    /**
     * Enter a parse tree produced by {@link ZookeeperSqlParser#identifier}.
     *
     * @param ctx the parse tree
     */
    void enterIdentifier(ZookeeperSqlParser.IdentifierContext ctx);

    /**
     * Exit a parse tree produced by {@link ZookeeperSqlParser#identifier}.
     *
     * @param ctx the parse tree
     */
    void exitIdentifier(ZookeeperSqlParser.IdentifierContext ctx);

    /**
     * Enter a parse tree produced by {@link ZookeeperSqlParser#quotedIdentifier}.
     *
     * @param ctx the parse tree
     */
    void enterQuotedIdentifier(ZookeeperSqlParser.QuotedIdentifierContext ctx);

    /**
     * Exit a parse tree produced by {@link ZookeeperSqlParser#quotedIdentifier}.
     *
     * @param ctx the parse tree
     */
    void exitQuotedIdentifier(ZookeeperSqlParser.QuotedIdentifierContext ctx);
}
