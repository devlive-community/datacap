// Generated from io/edurt/datacap/sql/parser/SqlBase.g4 by ANTLR 4.12.0
package io.edurt.datacap.sql.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SqlBaseParser}.
 */
public interface SqlBaseListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#singleStatement}.
	 * @param ctx the parse tree
	 */
	void enterSingleStatement(SqlBaseParser.SingleStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#singleStatement}.
	 * @param ctx the parse tree
	 */
	void exitSingleStatement(SqlBaseParser.SingleStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(SqlBaseParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(SqlBaseParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#kafkaQueryTopicStatement}.
	 * @param ctx the parse tree
	 */
	void enterKafkaQueryTopicStatement(SqlBaseParser.KafkaQueryTopicStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#kafkaQueryTopicStatement}.
	 * @param ctx the parse tree
	 */
	void exitKafkaQueryTopicStatement(SqlBaseParser.KafkaQueryTopicStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#kafkaQueryConsumerStatement}.
	 * @param ctx the parse tree
	 */
	void enterKafkaQueryConsumerStatement(SqlBaseParser.KafkaQueryConsumerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#kafkaQueryConsumerStatement}.
	 * @param ctx the parse tree
	 */
	void exitKafkaQueryConsumerStatement(SqlBaseParser.KafkaQueryConsumerStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#kafkaQueryStatement}.
	 * @param ctx the parse tree
	 */
	void enterKafkaQueryStatement(SqlBaseParser.KafkaQueryStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#kafkaQueryStatement}.
	 * @param ctx the parse tree
	 */
	void exitKafkaQueryStatement(SqlBaseParser.KafkaQueryStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#kafkaStatement}.
	 * @param ctx the parse tree
	 */
	void enterKafkaStatement(SqlBaseParser.KafkaStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#kafkaStatement}.
	 * @param ctx the parse tree
	 */
	void exitKafkaStatement(SqlBaseParser.KafkaStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#childPathStatement}.
	 * @param ctx the parse tree
	 */
	void enterChildPathStatement(SqlBaseParser.ChildPathStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#childPathStatement}.
	 * @param ctx the parse tree
	 */
	void exitChildPathStatement(SqlBaseParser.ChildPathStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#columnStatement}.
	 * @param ctx the parse tree
	 */
	void enterColumnStatement(SqlBaseParser.ColumnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#columnStatement}.
	 * @param ctx the parse tree
	 */
	void exitColumnStatement(SqlBaseParser.ColumnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void enterFromClause(SqlBaseParser.FromClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void exitFromClause(SqlBaseParser.FromClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(SqlBaseParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(SqlBaseParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(SqlBaseParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(SqlBaseParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterQuotedIdentifier(SqlBaseParser.QuotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitQuotedIdentifier(SqlBaseParser.QuotedIdentifierContext ctx);
}