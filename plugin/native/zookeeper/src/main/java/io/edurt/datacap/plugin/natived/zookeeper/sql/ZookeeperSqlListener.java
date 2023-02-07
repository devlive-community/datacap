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
     * Enter a parse tree produced by {@link ZookeeperSqlParser#selectElements}.
     *
     * @param ctx the parse tree
     */
    void enterSelectElements(ZookeeperSqlParser.SelectElementsContext ctx);

    /**
     * Exit a parse tree produced by {@link ZookeeperSqlParser#selectElements}.
     *
     * @param ctx the parse tree
     */
    void exitSelectElements(ZookeeperSqlParser.SelectElementsContext ctx);

    /**
     * Enter a parse tree produced by {@link ZookeeperSqlParser#qualifiedName}.
     *
     * @param ctx the parse tree
     */
    void enterQualifiedName(ZookeeperSqlParser.QualifiedNameContext ctx);

    /**
     * Exit a parse tree produced by {@link ZookeeperSqlParser#qualifiedName}.
     *
     * @param ctx the parse tree
     */
    void exitQualifiedName(ZookeeperSqlParser.QualifiedNameContext ctx);

    /**
     * Enter a parse tree produced by the {@code unquotedIdentifier}
     * labeled alternative in {@link ZookeeperSqlParser#identifier}.
     *
     * @param ctx the parse tree
     */
    void enterUnquotedIdentifier(ZookeeperSqlParser.UnquotedIdentifierContext ctx);

    /**
     * Exit a parse tree produced by the {@code unquotedIdentifier}
     * labeled alternative in {@link ZookeeperSqlParser#identifier}.
     *
     * @param ctx the parse tree
     */
    void exitUnquotedIdentifier(ZookeeperSqlParser.UnquotedIdentifierContext ctx);

    /**
     * Enter a parse tree produced by the {@code quotedIdentifier}
     * labeled alternative in {@link ZookeeperSqlParser#identifier}.
     *
     * @param ctx the parse tree
     */
    void enterQuotedIdentifier(ZookeeperSqlParser.QuotedIdentifierContext ctx);

    /**
     * Exit a parse tree produced by the {@code quotedIdentifier}
     * labeled alternative in {@link ZookeeperSqlParser#identifier}.
     *
     * @param ctx the parse tree
     */
    void exitQuotedIdentifier(ZookeeperSqlParser.QuotedIdentifierContext ctx);

    /**
     * Enter a parse tree produced by the {@code backQuotedIdentifier}
     * labeled alternative in {@link ZookeeperSqlParser#identifier}.
     *
     * @param ctx the parse tree
     */
    void enterBackQuotedIdentifier(ZookeeperSqlParser.BackQuotedIdentifierContext ctx);

    /**
     * Exit a parse tree produced by the {@code backQuotedIdentifier}
     * labeled alternative in {@link ZookeeperSqlParser#identifier}.
     *
     * @param ctx the parse tree
     */
    void exitBackQuotedIdentifier(ZookeeperSqlParser.BackQuotedIdentifierContext ctx);

    /**
     * Enter a parse tree produced by the {@code digitIdentifier}
     * labeled alternative in {@link ZookeeperSqlParser#identifier}.
     *
     * @param ctx the parse tree
     */
    void enterDigitIdentifier(ZookeeperSqlParser.DigitIdentifierContext ctx);

    /**
     * Exit a parse tree produced by the {@code digitIdentifier}
     * labeled alternative in {@link ZookeeperSqlParser#identifier}.
     *
     * @param ctx the parse tree
     */
    void exitDigitIdentifier(ZookeeperSqlParser.DigitIdentifierContext ctx);
}
