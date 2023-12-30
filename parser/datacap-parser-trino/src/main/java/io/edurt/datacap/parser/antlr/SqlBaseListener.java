// Generated from io/edurt/datacap/parser/SqlBase.g4 by ANTLR 4.12.0
package io.edurt.datacap.parser.antlr;
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
	 * Enter a parse tree produced by {@link SqlBaseParser#standaloneExpression}.
	 * @param ctx the parse tree
	 */
	void enterStandaloneExpression(SqlBaseParser.StandaloneExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#standaloneExpression}.
	 * @param ctx the parse tree
	 */
	void exitStandaloneExpression(SqlBaseParser.StandaloneExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#standalonePathSpecification}.
	 * @param ctx the parse tree
	 */
	void enterStandalonePathSpecification(SqlBaseParser.StandalonePathSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#standalonePathSpecification}.
	 * @param ctx the parse tree
	 */
	void exitStandalonePathSpecification(SqlBaseParser.StandalonePathSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#standaloneType}.
	 * @param ctx the parse tree
	 */
	void enterStandaloneType(SqlBaseParser.StandaloneTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#standaloneType}.
	 * @param ctx the parse tree
	 */
	void exitStandaloneType(SqlBaseParser.StandaloneTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#standaloneRowPattern}.
	 * @param ctx the parse tree
	 */
	void enterStandaloneRowPattern(SqlBaseParser.StandaloneRowPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#standaloneRowPattern}.
	 * @param ctx the parse tree
	 */
	void exitStandaloneRowPattern(SqlBaseParser.StandaloneRowPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefault}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefault(SqlBaseParser.StatementDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefault}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefault(SqlBaseParser.StatementDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code use}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterUse(SqlBaseParser.UseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code use}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitUse(SqlBaseParser.UseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createSchema}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateSchema(SqlBaseParser.CreateSchemaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createSchema}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateSchema(SqlBaseParser.CreateSchemaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropSchema}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropSchema(SqlBaseParser.DropSchemaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropSchema}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropSchema(SqlBaseParser.DropSchemaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameSchema}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRenameSchema(SqlBaseParser.RenameSchemaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameSchema}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRenameSchema(SqlBaseParser.RenameSchemaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setSchemaAuthorization}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetSchemaAuthorization(SqlBaseParser.SetSchemaAuthorizationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setSchemaAuthorization}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetSchemaAuthorization(SqlBaseParser.SetSchemaAuthorizationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTableAsSelect}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateTableAsSelect(SqlBaseParser.CreateTableAsSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTableAsSelect}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateTableAsSelect(SqlBaseParser.CreateTableAsSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTable}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateTable(SqlBaseParser.CreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTable}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateTable(SqlBaseParser.CreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropTable}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropTable(SqlBaseParser.DropTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropTable}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropTable(SqlBaseParser.DropTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code insertInto}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterInsertInto(SqlBaseParser.InsertIntoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code insertInto}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitInsertInto(SqlBaseParser.InsertIntoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code delete}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDelete(SqlBaseParser.DeleteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code delete}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDelete(SqlBaseParser.DeleteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code truncateTable}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterTruncateTable(SqlBaseParser.TruncateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code truncateTable}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitTruncateTable(SqlBaseParser.TruncateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentTable}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCommentTable(SqlBaseParser.CommentTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentTable}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCommentTable(SqlBaseParser.CommentTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCommentView(SqlBaseParser.CommentViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCommentView(SqlBaseParser.CommentViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentColumn}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCommentColumn(SqlBaseParser.CommentColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentColumn}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCommentColumn(SqlBaseParser.CommentColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameTable}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRenameTable(SqlBaseParser.RenameTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameTable}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRenameTable(SqlBaseParser.RenameTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAddColumn(SqlBaseParser.AddColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAddColumn(SqlBaseParser.AddColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameColumn}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRenameColumn(SqlBaseParser.RenameColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameColumn}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRenameColumn(SqlBaseParser.RenameColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropColumn}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropColumn(SqlBaseParser.DropColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropColumn}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropColumn(SqlBaseParser.DropColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setTableAuthorization}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetTableAuthorization(SqlBaseParser.SetTableAuthorizationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setTableAuthorization}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetTableAuthorization(SqlBaseParser.SetTableAuthorizationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setTableProperties}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetTableProperties(SqlBaseParser.SetTablePropertiesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setTableProperties}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetTableProperties(SqlBaseParser.SetTablePropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableExecute}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterTableExecute(SqlBaseParser.TableExecuteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableExecute}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitTableExecute(SqlBaseParser.TableExecuteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code analyze}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAnalyze(SqlBaseParser.AnalyzeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code analyze}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAnalyze(SqlBaseParser.AnalyzeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createMaterializedView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateMaterializedView(SqlBaseParser.CreateMaterializedViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createMaterializedView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateMaterializedView(SqlBaseParser.CreateMaterializedViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateView(SqlBaseParser.CreateViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateView(SqlBaseParser.CreateViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code refreshMaterializedView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRefreshMaterializedView(SqlBaseParser.RefreshMaterializedViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code refreshMaterializedView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRefreshMaterializedView(SqlBaseParser.RefreshMaterializedViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropMaterializedView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropMaterializedView(SqlBaseParser.DropMaterializedViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropMaterializedView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropMaterializedView(SqlBaseParser.DropMaterializedViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameMaterializedView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRenameMaterializedView(SqlBaseParser.RenameMaterializedViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameMaterializedView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRenameMaterializedView(SqlBaseParser.RenameMaterializedViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setMaterializedViewProperties}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetMaterializedViewProperties(SqlBaseParser.SetMaterializedViewPropertiesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setMaterializedViewProperties}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetMaterializedViewProperties(SqlBaseParser.SetMaterializedViewPropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropView(SqlBaseParser.DropViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropView(SqlBaseParser.DropViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRenameView(SqlBaseParser.RenameViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRenameView(SqlBaseParser.RenameViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setViewAuthorization}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetViewAuthorization(SqlBaseParser.SetViewAuthorizationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setViewAuthorization}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetViewAuthorization(SqlBaseParser.SetViewAuthorizationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code call}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCall(SqlBaseParser.CallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code call}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCall(SqlBaseParser.CallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createRole}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateRole(SqlBaseParser.CreateRoleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createRole}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateRole(SqlBaseParser.CreateRoleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropRole}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropRole(SqlBaseParser.DropRoleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropRole}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropRole(SqlBaseParser.DropRoleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantRoles}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterGrantRoles(SqlBaseParser.GrantRolesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantRoles}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitGrantRoles(SqlBaseParser.GrantRolesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeRoles}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRevokeRoles(SqlBaseParser.RevokeRolesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeRoles}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRevokeRoles(SqlBaseParser.RevokeRolesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setRole}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetRole(SqlBaseParser.SetRoleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setRole}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetRole(SqlBaseParser.SetRoleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grant}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterGrant(SqlBaseParser.GrantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grant}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitGrant(SqlBaseParser.GrantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code deny}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDeny(SqlBaseParser.DenyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code deny}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDeny(SqlBaseParser.DenyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revoke}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRevoke(SqlBaseParser.RevokeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revoke}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRevoke(SqlBaseParser.RevokeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowGrants(SqlBaseParser.ShowGrantsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowGrants(SqlBaseParser.ShowGrantsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code explain}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExplain(SqlBaseParser.ExplainContext ctx);
	/**
	 * Exit a parse tree produced by the {@code explain}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExplain(SqlBaseParser.ExplainContext ctx);
	/**
	 * Enter a parse tree produced by the {@code explainAnalyze}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExplainAnalyze(SqlBaseParser.ExplainAnalyzeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code explainAnalyze}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExplainAnalyze(SqlBaseParser.ExplainAnalyzeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateTable(SqlBaseParser.ShowCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateTable(SqlBaseParser.ShowCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateSchema}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateSchema(SqlBaseParser.ShowCreateSchemaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateSchema}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateSchema(SqlBaseParser.ShowCreateSchemaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateView(SqlBaseParser.ShowCreateViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateView(SqlBaseParser.ShowCreateViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateMaterializedView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateMaterializedView(SqlBaseParser.ShowCreateMaterializedViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateMaterializedView}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateMaterializedView(SqlBaseParser.ShowCreateMaterializedViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowTables(SqlBaseParser.ShowTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowTables(SqlBaseParser.ShowTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSchemas}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowSchemas(SqlBaseParser.ShowSchemasContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSchemas}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowSchemas(SqlBaseParser.ShowSchemasContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCatalogs}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowCatalogs(SqlBaseParser.ShowCatalogsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCatalogs}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowCatalogs(SqlBaseParser.ShowCatalogsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowColumns(SqlBaseParser.ShowColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowColumns(SqlBaseParser.ShowColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showStats}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowStats(SqlBaseParser.ShowStatsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showStats}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowStats(SqlBaseParser.ShowStatsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showStatsForQuery}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowStatsForQuery(SqlBaseParser.ShowStatsForQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showStatsForQuery}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowStatsForQuery(SqlBaseParser.ShowStatsForQueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showRoles}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowRoles(SqlBaseParser.ShowRolesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showRoles}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowRoles(SqlBaseParser.ShowRolesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showRoleGrants}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowRoleGrants(SqlBaseParser.ShowRoleGrantsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showRoleGrants}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowRoleGrants(SqlBaseParser.ShowRoleGrantsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showFunctions}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowFunctions(SqlBaseParser.ShowFunctionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showFunctions}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowFunctions(SqlBaseParser.ShowFunctionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSession}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowSession(SqlBaseParser.ShowSessionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSession}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowSession(SqlBaseParser.ShowSessionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setSession}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetSession(SqlBaseParser.SetSessionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setSession}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetSession(SqlBaseParser.SetSessionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code resetSession}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterResetSession(SqlBaseParser.ResetSessionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code resetSession}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitResetSession(SqlBaseParser.ResetSessionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code startTransaction}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStartTransaction(SqlBaseParser.StartTransactionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code startTransaction}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStartTransaction(SqlBaseParser.StartTransactionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commit}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCommit(SqlBaseParser.CommitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commit}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCommit(SqlBaseParser.CommitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rollback}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRollback(SqlBaseParser.RollbackContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rollback}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRollback(SqlBaseParser.RollbackContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prepare}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterPrepare(SqlBaseParser.PrepareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prepare}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitPrepare(SqlBaseParser.PrepareContext ctx);
	/**
	 * Enter a parse tree produced by the {@code deallocate}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDeallocate(SqlBaseParser.DeallocateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code deallocate}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDeallocate(SqlBaseParser.DeallocateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code execute}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExecute(SqlBaseParser.ExecuteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code execute}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExecute(SqlBaseParser.ExecuteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeInput}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDescribeInput(SqlBaseParser.DescribeInputContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeInput}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDescribeInput(SqlBaseParser.DescribeInputContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeOutput}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDescribeOutput(SqlBaseParser.DescribeOutputContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeOutput}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDescribeOutput(SqlBaseParser.DescribeOutputContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setPath}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetPath(SqlBaseParser.SetPathContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setPath}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetPath(SqlBaseParser.SetPathContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setTimeZone}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetTimeZone(SqlBaseParser.SetTimeZoneContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setTimeZone}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetTimeZone(SqlBaseParser.SetTimeZoneContext ctx);
	/**
	 * Enter a parse tree produced by the {@code update}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterUpdate(SqlBaseParser.UpdateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code update}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitUpdate(SqlBaseParser.UpdateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code merge}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterMerge(SqlBaseParser.MergeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code merge}
	 * labeled alternative in {@link SqlBaseParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitMerge(SqlBaseParser.MergeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(SqlBaseParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(SqlBaseParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#with}.
	 * @param ctx the parse tree
	 */
	void enterWith(SqlBaseParser.WithContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#with}.
	 * @param ctx the parse tree
	 */
	void exitWith(SqlBaseParser.WithContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#tableElement}.
	 * @param ctx the parse tree
	 */
	void enterTableElement(SqlBaseParser.TableElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#tableElement}.
	 * @param ctx the parse tree
	 */
	void exitTableElement(SqlBaseParser.TableElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterColumnDefinition(SqlBaseParser.ColumnDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitColumnDefinition(SqlBaseParser.ColumnDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#likeClause}.
	 * @param ctx the parse tree
	 */
	void enterLikeClause(SqlBaseParser.LikeClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#likeClause}.
	 * @param ctx the parse tree
	 */
	void exitLikeClause(SqlBaseParser.LikeClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#properties}.
	 * @param ctx the parse tree
	 */
	void enterProperties(SqlBaseParser.PropertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#properties}.
	 * @param ctx the parse tree
	 */
	void exitProperties(SqlBaseParser.PropertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#propertyAssignments}.
	 * @param ctx the parse tree
	 */
	void enterPropertyAssignments(SqlBaseParser.PropertyAssignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#propertyAssignments}.
	 * @param ctx the parse tree
	 */
	void exitPropertyAssignments(SqlBaseParser.PropertyAssignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(SqlBaseParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(SqlBaseParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code defaultPropertyValue}
	 * labeled alternative in {@link SqlBaseParser#propertyValue}.
	 * @param ctx the parse tree
	 */
	void enterDefaultPropertyValue(SqlBaseParser.DefaultPropertyValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code defaultPropertyValue}
	 * labeled alternative in {@link SqlBaseParser#propertyValue}.
	 * @param ctx the parse tree
	 */
	void exitDefaultPropertyValue(SqlBaseParser.DefaultPropertyValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nonDefaultPropertyValue}
	 * labeled alternative in {@link SqlBaseParser#propertyValue}.
	 * @param ctx the parse tree
	 */
	void enterNonDefaultPropertyValue(SqlBaseParser.NonDefaultPropertyValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nonDefaultPropertyValue}
	 * labeled alternative in {@link SqlBaseParser#propertyValue}.
	 * @param ctx the parse tree
	 */
	void exitNonDefaultPropertyValue(SqlBaseParser.NonDefaultPropertyValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#queryNoWith}.
	 * @param ctx the parse tree
	 */
	void enterQueryNoWith(SqlBaseParser.QueryNoWithContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#queryNoWith}.
	 * @param ctx the parse tree
	 */
	void exitQueryNoWith(SqlBaseParser.QueryNoWithContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#limitRowCount}.
	 * @param ctx the parse tree
	 */
	void enterLimitRowCount(SqlBaseParser.LimitRowCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#limitRowCount}.
	 * @param ctx the parse tree
	 */
	void exitLimitRowCount(SqlBaseParser.LimitRowCountContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#rowCount}.
	 * @param ctx the parse tree
	 */
	void enterRowCount(SqlBaseParser.RowCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#rowCount}.
	 * @param ctx the parse tree
	 */
	void exitRowCount(SqlBaseParser.RowCountContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryTermDefault}
	 * labeled alternative in {@link SqlBaseParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void enterQueryTermDefault(SqlBaseParser.QueryTermDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryTermDefault}
	 * labeled alternative in {@link SqlBaseParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void exitQueryTermDefault(SqlBaseParser.QueryTermDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setOperation}
	 * labeled alternative in {@link SqlBaseParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void enterSetOperation(SqlBaseParser.SetOperationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setOperation}
	 * labeled alternative in {@link SqlBaseParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void exitSetOperation(SqlBaseParser.SetOperationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryPrimaryDefault}
	 * labeled alternative in {@link SqlBaseParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterQueryPrimaryDefault(SqlBaseParser.QueryPrimaryDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryPrimaryDefault}
	 * labeled alternative in {@link SqlBaseParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitQueryPrimaryDefault(SqlBaseParser.QueryPrimaryDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code table}
	 * labeled alternative in {@link SqlBaseParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterTable(SqlBaseParser.TableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code table}
	 * labeled alternative in {@link SqlBaseParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitTable(SqlBaseParser.TableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inlineTable}
	 * labeled alternative in {@link SqlBaseParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterInlineTable(SqlBaseParser.InlineTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inlineTable}
	 * labeled alternative in {@link SqlBaseParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitInlineTable(SqlBaseParser.InlineTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subquery}
	 * labeled alternative in {@link SqlBaseParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterSubquery(SqlBaseParser.SubqueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subquery}
	 * labeled alternative in {@link SqlBaseParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitSubquery(SqlBaseParser.SubqueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#sortItem}.
	 * @param ctx the parse tree
	 */
	void enterSortItem(SqlBaseParser.SortItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#sortItem}.
	 * @param ctx the parse tree
	 */
	void exitSortItem(SqlBaseParser.SortItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecification(SqlBaseParser.QuerySpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecification(SqlBaseParser.QuerySpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#groupBy}.
	 * @param ctx the parse tree
	 */
	void enterGroupBy(SqlBaseParser.GroupByContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#groupBy}.
	 * @param ctx the parse tree
	 */
	void exitGroupBy(SqlBaseParser.GroupByContext ctx);
	/**
	 * Enter a parse tree produced by the {@code singleGroupingSet}
	 * labeled alternative in {@link SqlBaseParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void enterSingleGroupingSet(SqlBaseParser.SingleGroupingSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code singleGroupingSet}
	 * labeled alternative in {@link SqlBaseParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void exitSingleGroupingSet(SqlBaseParser.SingleGroupingSetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rollup}
	 * labeled alternative in {@link SqlBaseParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void enterRollup(SqlBaseParser.RollupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rollup}
	 * labeled alternative in {@link SqlBaseParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void exitRollup(SqlBaseParser.RollupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cube}
	 * labeled alternative in {@link SqlBaseParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void enterCube(SqlBaseParser.CubeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cube}
	 * labeled alternative in {@link SqlBaseParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void exitCube(SqlBaseParser.CubeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code multipleGroupingSets}
	 * labeled alternative in {@link SqlBaseParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void enterMultipleGroupingSets(SqlBaseParser.MultipleGroupingSetsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code multipleGroupingSets}
	 * labeled alternative in {@link SqlBaseParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void exitMultipleGroupingSets(SqlBaseParser.MultipleGroupingSetsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#groupingSet}.
	 * @param ctx the parse tree
	 */
	void enterGroupingSet(SqlBaseParser.GroupingSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#groupingSet}.
	 * @param ctx the parse tree
	 */
	void exitGroupingSet(SqlBaseParser.GroupingSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#windowDefinition}.
	 * @param ctx the parse tree
	 */
	void enterWindowDefinition(SqlBaseParser.WindowDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#windowDefinition}.
	 * @param ctx the parse tree
	 */
	void exitWindowDefinition(SqlBaseParser.WindowDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#windowSpecification}.
	 * @param ctx the parse tree
	 */
	void enterWindowSpecification(SqlBaseParser.WindowSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#windowSpecification}.
	 * @param ctx the parse tree
	 */
	void exitWindowSpecification(SqlBaseParser.WindowSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#namedQuery}.
	 * @param ctx the parse tree
	 */
	void enterNamedQuery(SqlBaseParser.NamedQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#namedQuery}.
	 * @param ctx the parse tree
	 */
	void exitNamedQuery(SqlBaseParser.NamedQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#setQuantifier}.
	 * @param ctx the parse tree
	 */
	void enterSetQuantifier(SqlBaseParser.SetQuantifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#setQuantifier}.
	 * @param ctx the parse tree
	 */
	void exitSetQuantifier(SqlBaseParser.SetQuantifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectSingle}
	 * labeled alternative in {@link SqlBaseParser#selectItem}.
	 * @param ctx the parse tree
	 */
	void enterSelectSingle(SqlBaseParser.SelectSingleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectSingle}
	 * labeled alternative in {@link SqlBaseParser#selectItem}.
	 * @param ctx the parse tree
	 */
	void exitSelectSingle(SqlBaseParser.SelectSingleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectAll}
	 * labeled alternative in {@link SqlBaseParser#selectItem}.
	 * @param ctx the parse tree
	 */
	void enterSelectAll(SqlBaseParser.SelectAllContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectAll}
	 * labeled alternative in {@link SqlBaseParser#selectItem}.
	 * @param ctx the parse tree
	 */
	void exitSelectAll(SqlBaseParser.SelectAllContext ctx);
	/**
	 * Enter a parse tree produced by the {@code relationDefault}
	 * labeled alternative in {@link SqlBaseParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelationDefault(SqlBaseParser.RelationDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code relationDefault}
	 * labeled alternative in {@link SqlBaseParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelationDefault(SqlBaseParser.RelationDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code joinRelation}
	 * labeled alternative in {@link SqlBaseParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterJoinRelation(SqlBaseParser.JoinRelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code joinRelation}
	 * labeled alternative in {@link SqlBaseParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitJoinRelation(SqlBaseParser.JoinRelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#joinType}.
	 * @param ctx the parse tree
	 */
	void enterJoinType(SqlBaseParser.JoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#joinType}.
	 * @param ctx the parse tree
	 */
	void exitJoinType(SqlBaseParser.JoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#joinCriteria}.
	 * @param ctx the parse tree
	 */
	void enterJoinCriteria(SqlBaseParser.JoinCriteriaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#joinCriteria}.
	 * @param ctx the parse tree
	 */
	void exitJoinCriteria(SqlBaseParser.JoinCriteriaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#sampledRelation}.
	 * @param ctx the parse tree
	 */
	void enterSampledRelation(SqlBaseParser.SampledRelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#sampledRelation}.
	 * @param ctx the parse tree
	 */
	void exitSampledRelation(SqlBaseParser.SampledRelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#sampleType}.
	 * @param ctx the parse tree
	 */
	void enterSampleType(SqlBaseParser.SampleTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#sampleType}.
	 * @param ctx the parse tree
	 */
	void exitSampleType(SqlBaseParser.SampleTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#trimsSpecification}.
	 * @param ctx the parse tree
	 */
	void enterTrimsSpecification(SqlBaseParser.TrimsSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#trimsSpecification}.
	 * @param ctx the parse tree
	 */
	void exitTrimsSpecification(SqlBaseParser.TrimsSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#listAggOverflowBehavior}.
	 * @param ctx the parse tree
	 */
	void enterListAggOverflowBehavior(SqlBaseParser.ListAggOverflowBehaviorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#listAggOverflowBehavior}.
	 * @param ctx the parse tree
	 */
	void exitListAggOverflowBehavior(SqlBaseParser.ListAggOverflowBehaviorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#listaggCountIndication}.
	 * @param ctx the parse tree
	 */
	void enterListaggCountIndication(SqlBaseParser.ListaggCountIndicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#listaggCountIndication}.
	 * @param ctx the parse tree
	 */
	void exitListaggCountIndication(SqlBaseParser.ListaggCountIndicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#patternRecognition}.
	 * @param ctx the parse tree
	 */
	void enterPatternRecognition(SqlBaseParser.PatternRecognitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#patternRecognition}.
	 * @param ctx the parse tree
	 */
	void exitPatternRecognition(SqlBaseParser.PatternRecognitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#measureDefinition}.
	 * @param ctx the parse tree
	 */
	void enterMeasureDefinition(SqlBaseParser.MeasureDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#measureDefinition}.
	 * @param ctx the parse tree
	 */
	void exitMeasureDefinition(SqlBaseParser.MeasureDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#rowsPerMatch}.
	 * @param ctx the parse tree
	 */
	void enterRowsPerMatch(SqlBaseParser.RowsPerMatchContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#rowsPerMatch}.
	 * @param ctx the parse tree
	 */
	void exitRowsPerMatch(SqlBaseParser.RowsPerMatchContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#emptyMatchHandling}.
	 * @param ctx the parse tree
	 */
	void enterEmptyMatchHandling(SqlBaseParser.EmptyMatchHandlingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#emptyMatchHandling}.
	 * @param ctx the parse tree
	 */
	void exitEmptyMatchHandling(SqlBaseParser.EmptyMatchHandlingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#skipTo}.
	 * @param ctx the parse tree
	 */
	void enterSkipTo(SqlBaseParser.SkipToContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#skipTo}.
	 * @param ctx the parse tree
	 */
	void exitSkipTo(SqlBaseParser.SkipToContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#subsetDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubsetDefinition(SqlBaseParser.SubsetDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#subsetDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubsetDefinition(SqlBaseParser.SubsetDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#variableDefinition}.
	 * @param ctx the parse tree
	 */
	void enterVariableDefinition(SqlBaseParser.VariableDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#variableDefinition}.
	 * @param ctx the parse tree
	 */
	void exitVariableDefinition(SqlBaseParser.VariableDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#aliasedRelation}.
	 * @param ctx the parse tree
	 */
	void enterAliasedRelation(SqlBaseParser.AliasedRelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#aliasedRelation}.
	 * @param ctx the parse tree
	 */
	void exitAliasedRelation(SqlBaseParser.AliasedRelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#columnAliases}.
	 * @param ctx the parse tree
	 */
	void enterColumnAliases(SqlBaseParser.ColumnAliasesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#columnAliases}.
	 * @param ctx the parse tree
	 */
	void exitColumnAliases(SqlBaseParser.ColumnAliasesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableName}
	 * labeled alternative in {@link SqlBaseParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterTableName(SqlBaseParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableName}
	 * labeled alternative in {@link SqlBaseParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitTableName(SqlBaseParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryRelation}
	 * labeled alternative in {@link SqlBaseParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryRelation(SqlBaseParser.SubqueryRelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryRelation}
	 * labeled alternative in {@link SqlBaseParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryRelation(SqlBaseParser.SubqueryRelationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unnest}
	 * labeled alternative in {@link SqlBaseParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterUnnest(SqlBaseParser.UnnestContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unnest}
	 * labeled alternative in {@link SqlBaseParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitUnnest(SqlBaseParser.UnnestContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lateral}
	 * labeled alternative in {@link SqlBaseParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterLateral(SqlBaseParser.LateralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lateral}
	 * labeled alternative in {@link SqlBaseParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitLateral(SqlBaseParser.LateralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableFunctionInvocation}
	 * labeled alternative in {@link SqlBaseParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterTableFunctionInvocation(SqlBaseParser.TableFunctionInvocationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableFunctionInvocation}
	 * labeled alternative in {@link SqlBaseParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitTableFunctionInvocation(SqlBaseParser.TableFunctionInvocationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesizedRelation}
	 * labeled alternative in {@link SqlBaseParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedRelation(SqlBaseParser.ParenthesizedRelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesizedRelation}
	 * labeled alternative in {@link SqlBaseParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedRelation(SqlBaseParser.ParenthesizedRelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#tableFunctionCall}.
	 * @param ctx the parse tree
	 */
	void enterTableFunctionCall(SqlBaseParser.TableFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#tableFunctionCall}.
	 * @param ctx the parse tree
	 */
	void exitTableFunctionCall(SqlBaseParser.TableFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#tableFunctionArgument}.
	 * @param ctx the parse tree
	 */
	void enterTableFunctionArgument(SqlBaseParser.TableFunctionArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#tableFunctionArgument}.
	 * @param ctx the parse tree
	 */
	void exitTableFunctionArgument(SqlBaseParser.TableFunctionArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#tableArgument}.
	 * @param ctx the parse tree
	 */
	void enterTableArgument(SqlBaseParser.TableArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#tableArgument}.
	 * @param ctx the parse tree
	 */
	void exitTableArgument(SqlBaseParser.TableArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableArgumentTable}
	 * labeled alternative in {@link SqlBaseParser#tableArgumentRelation}.
	 * @param ctx the parse tree
	 */
	void enterTableArgumentTable(SqlBaseParser.TableArgumentTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableArgumentTable}
	 * labeled alternative in {@link SqlBaseParser#tableArgumentRelation}.
	 * @param ctx the parse tree
	 */
	void exitTableArgumentTable(SqlBaseParser.TableArgumentTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableArgumentQuery}
	 * labeled alternative in {@link SqlBaseParser#tableArgumentRelation}.
	 * @param ctx the parse tree
	 */
	void enterTableArgumentQuery(SqlBaseParser.TableArgumentQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableArgumentQuery}
	 * labeled alternative in {@link SqlBaseParser#tableArgumentRelation}.
	 * @param ctx the parse tree
	 */
	void exitTableArgumentQuery(SqlBaseParser.TableArgumentQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#descriptorArgument}.
	 * @param ctx the parse tree
	 */
	void enterDescriptorArgument(SqlBaseParser.DescriptorArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#descriptorArgument}.
	 * @param ctx the parse tree
	 */
	void exitDescriptorArgument(SqlBaseParser.DescriptorArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#descriptorField}.
	 * @param ctx the parse tree
	 */
	void enterDescriptorField(SqlBaseParser.DescriptorFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#descriptorField}.
	 * @param ctx the parse tree
	 */
	void exitDescriptorField(SqlBaseParser.DescriptorFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#copartitionTables}.
	 * @param ctx the parse tree
	 */
	void enterCopartitionTables(SqlBaseParser.CopartitionTablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#copartitionTables}.
	 * @param ctx the parse tree
	 */
	void exitCopartitionTables(SqlBaseParser.CopartitionTablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(SqlBaseParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(SqlBaseParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalNot}
	 * labeled alternative in {@link SqlBaseParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalNot(SqlBaseParser.LogicalNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalNot}
	 * labeled alternative in {@link SqlBaseParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalNot(SqlBaseParser.LogicalNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicated}
	 * labeled alternative in {@link SqlBaseParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterPredicated(SqlBaseParser.PredicatedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicated}
	 * labeled alternative in {@link SqlBaseParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitPredicated(SqlBaseParser.PredicatedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code or}
	 * labeled alternative in {@link SqlBaseParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterOr(SqlBaseParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code or}
	 * labeled alternative in {@link SqlBaseParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitOr(SqlBaseParser.OrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code and}
	 * labeled alternative in {@link SqlBaseParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterAnd(SqlBaseParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code and}
	 * labeled alternative in {@link SqlBaseParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitAnd(SqlBaseParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comparison}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterComparison(SqlBaseParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comparison}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitComparison(SqlBaseParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code quantifiedComparison}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterQuantifiedComparison(SqlBaseParser.QuantifiedComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code quantifiedComparison}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitQuantifiedComparison(SqlBaseParser.QuantifiedComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code between}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterBetween(SqlBaseParser.BetweenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code between}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitBetween(SqlBaseParser.BetweenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inList}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterInList(SqlBaseParser.InListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inList}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitInList(SqlBaseParser.InListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inSubquery}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterInSubquery(SqlBaseParser.InSubqueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inSubquery}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitInSubquery(SqlBaseParser.InSubqueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code like}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterLike(SqlBaseParser.LikeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code like}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitLike(SqlBaseParser.LikeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullPredicate}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterNullPredicate(SqlBaseParser.NullPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullPredicate}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitNullPredicate(SqlBaseParser.NullPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code distinctFrom}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterDistinctFrom(SqlBaseParser.DistinctFromContext ctx);
	/**
	 * Exit a parse tree produced by the {@code distinctFrom}
	 * labeled alternative in {@link SqlBaseParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitDistinctFrom(SqlBaseParser.DistinctFromContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueExpressionDefault}
	 * labeled alternative in {@link SqlBaseParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterValueExpressionDefault(SqlBaseParser.ValueExpressionDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueExpressionDefault}
	 * labeled alternative in {@link SqlBaseParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitValueExpressionDefault(SqlBaseParser.ValueExpressionDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code concatenation}
	 * labeled alternative in {@link SqlBaseParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterConcatenation(SqlBaseParser.ConcatenationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code concatenation}
	 * labeled alternative in {@link SqlBaseParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitConcatenation(SqlBaseParser.ConcatenationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmeticBinary}
	 * labeled alternative in {@link SqlBaseParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticBinary(SqlBaseParser.ArithmeticBinaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmeticBinary}
	 * labeled alternative in {@link SqlBaseParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticBinary(SqlBaseParser.ArithmeticBinaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmeticUnary}
	 * labeled alternative in {@link SqlBaseParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticUnary(SqlBaseParser.ArithmeticUnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmeticUnary}
	 * labeled alternative in {@link SqlBaseParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticUnary(SqlBaseParser.ArithmeticUnaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atTimeZone}
	 * labeled alternative in {@link SqlBaseParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterAtTimeZone(SqlBaseParser.AtTimeZoneContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atTimeZone}
	 * labeled alternative in {@link SqlBaseParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitAtTimeZone(SqlBaseParser.AtTimeZoneContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dereference}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterDereference(SqlBaseParser.DereferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dereference}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitDereference(SqlBaseParser.DereferenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeConstructor}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterTypeConstructor(SqlBaseParser.TypeConstructorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeConstructor}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitTypeConstructor(SqlBaseParser.TypeConstructorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonValue}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterJsonValue(SqlBaseParser.JsonValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonValue}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitJsonValue(SqlBaseParser.JsonValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specialDateTimeFunction}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSpecialDateTimeFunction(SqlBaseParser.SpecialDateTimeFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specialDateTimeFunction}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSpecialDateTimeFunction(SqlBaseParser.SpecialDateTimeFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code substring}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSubstring(SqlBaseParser.SubstringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code substring}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSubstring(SqlBaseParser.SubstringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cast}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCast(SqlBaseParser.CastContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cast}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCast(SqlBaseParser.CastContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lambda}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterLambda(SqlBaseParser.LambdaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lambda}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitLambda(SqlBaseParser.LambdaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesizedExpression}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpression(SqlBaseParser.ParenthesizedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesizedExpression}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpression(SqlBaseParser.ParenthesizedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code trim}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterTrim(SqlBaseParser.TrimContext ctx);
	/**
	 * Exit a parse tree produced by the {@code trim}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitTrim(SqlBaseParser.TrimContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parameter}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterParameter(SqlBaseParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parameter}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitParameter(SqlBaseParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code normalize}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterNormalize(SqlBaseParser.NormalizeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code normalize}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitNormalize(SqlBaseParser.NormalizeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonObject}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterJsonObject(SqlBaseParser.JsonObjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonObject}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitJsonObject(SqlBaseParser.JsonObjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalLiteral}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterIntervalLiteral(SqlBaseParser.IntervalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalLiteral}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitIntervalLiteral(SqlBaseParser.IntervalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numericLiteral}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterNumericLiteral(SqlBaseParser.NumericLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numericLiteral}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitNumericLiteral(SqlBaseParser.NumericLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(SqlBaseParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(SqlBaseParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonArray}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterJsonArray(SqlBaseParser.JsonArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonArray}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitJsonArray(SqlBaseParser.JsonArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleCase}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSimpleCase(SqlBaseParser.SimpleCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleCase}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSimpleCase(SqlBaseParser.SimpleCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code columnReference}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterColumnReference(SqlBaseParser.ColumnReferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code columnReference}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitColumnReference(SqlBaseParser.ColumnReferenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterNullLiteral(SqlBaseParser.NullLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitNullLiteral(SqlBaseParser.NullLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rowConstructor}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterRowConstructor(SqlBaseParser.RowConstructorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rowConstructor}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitRowConstructor(SqlBaseParser.RowConstructorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subscript}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSubscript(SqlBaseParser.SubscriptContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subscript}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSubscript(SqlBaseParser.SubscriptContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonExists}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterJsonExists(SqlBaseParser.JsonExistsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonExists}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitJsonExists(SqlBaseParser.JsonExistsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentPath}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCurrentPath(SqlBaseParser.CurrentPathContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentPath}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCurrentPath(SqlBaseParser.CurrentPathContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryExpression}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryExpression(SqlBaseParser.SubqueryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryExpression}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryExpression(SqlBaseParser.SubqueryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryLiteral}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryLiteral(SqlBaseParser.BinaryLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryLiteral}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryLiteral(SqlBaseParser.BinaryLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentUser}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCurrentUser(SqlBaseParser.CurrentUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentUser}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCurrentUser(SqlBaseParser.CurrentUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonQuery}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterJsonQuery(SqlBaseParser.JsonQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonQuery}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitJsonQuery(SqlBaseParser.JsonQueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code measure}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterMeasure(SqlBaseParser.MeasureContext ctx);
	/**
	 * Exit a parse tree produced by the {@code measure}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitMeasure(SqlBaseParser.MeasureContext ctx);
	/**
	 * Enter a parse tree produced by the {@code extract}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterExtract(SqlBaseParser.ExtractContext ctx);
	/**
	 * Exit a parse tree produced by the {@code extract}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitExtract(SqlBaseParser.ExtractContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(SqlBaseParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(SqlBaseParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayConstructor}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterArrayConstructor(SqlBaseParser.ArrayConstructorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayConstructor}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitArrayConstructor(SqlBaseParser.ArrayConstructorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(SqlBaseParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(SqlBaseParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentSchema}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCurrentSchema(SqlBaseParser.CurrentSchemaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentSchema}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCurrentSchema(SqlBaseParser.CurrentSchemaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exists}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterExists(SqlBaseParser.ExistsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exists}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitExists(SqlBaseParser.ExistsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code position}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPosition(SqlBaseParser.PositionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code position}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPosition(SqlBaseParser.PositionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code listagg}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterListagg(SqlBaseParser.ListaggContext ctx);
	/**
	 * Exit a parse tree produced by the {@code listagg}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitListagg(SqlBaseParser.ListaggContext ctx);
	/**
	 * Enter a parse tree produced by the {@code searchedCase}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSearchedCase(SqlBaseParser.SearchedCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code searchedCase}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSearchedCase(SqlBaseParser.SearchedCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentCatalog}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCurrentCatalog(SqlBaseParser.CurrentCatalogContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentCatalog}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCurrentCatalog(SqlBaseParser.CurrentCatalogContext ctx);
	/**
	 * Enter a parse tree produced by the {@code groupingOperation}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterGroupingOperation(SqlBaseParser.GroupingOperationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code groupingOperation}
	 * labeled alternative in {@link SqlBaseParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitGroupingOperation(SqlBaseParser.GroupingOperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#jsonPathInvocation}.
	 * @param ctx the parse tree
	 */
	void enterJsonPathInvocation(SqlBaseParser.JsonPathInvocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#jsonPathInvocation}.
	 * @param ctx the parse tree
	 */
	void exitJsonPathInvocation(SqlBaseParser.JsonPathInvocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#jsonValueExpression}.
	 * @param ctx the parse tree
	 */
	void enterJsonValueExpression(SqlBaseParser.JsonValueExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#jsonValueExpression}.
	 * @param ctx the parse tree
	 */
	void exitJsonValueExpression(SqlBaseParser.JsonValueExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#jsonRepresentation}.
	 * @param ctx the parse tree
	 */
	void enterJsonRepresentation(SqlBaseParser.JsonRepresentationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#jsonRepresentation}.
	 * @param ctx the parse tree
	 */
	void exitJsonRepresentation(SqlBaseParser.JsonRepresentationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#jsonArgument}.
	 * @param ctx the parse tree
	 */
	void enterJsonArgument(SqlBaseParser.JsonArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#jsonArgument}.
	 * @param ctx the parse tree
	 */
	void exitJsonArgument(SqlBaseParser.JsonArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#jsonExistsErrorBehavior}.
	 * @param ctx the parse tree
	 */
	void enterJsonExistsErrorBehavior(SqlBaseParser.JsonExistsErrorBehaviorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#jsonExistsErrorBehavior}.
	 * @param ctx the parse tree
	 */
	void exitJsonExistsErrorBehavior(SqlBaseParser.JsonExistsErrorBehaviorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#jsonValueBehavior}.
	 * @param ctx the parse tree
	 */
	void enterJsonValueBehavior(SqlBaseParser.JsonValueBehaviorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#jsonValueBehavior}.
	 * @param ctx the parse tree
	 */
	void exitJsonValueBehavior(SqlBaseParser.JsonValueBehaviorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#jsonQueryWrapperBehavior}.
	 * @param ctx the parse tree
	 */
	void enterJsonQueryWrapperBehavior(SqlBaseParser.JsonQueryWrapperBehaviorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#jsonQueryWrapperBehavior}.
	 * @param ctx the parse tree
	 */
	void exitJsonQueryWrapperBehavior(SqlBaseParser.JsonQueryWrapperBehaviorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#jsonQueryBehavior}.
	 * @param ctx the parse tree
	 */
	void enterJsonQueryBehavior(SqlBaseParser.JsonQueryBehaviorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#jsonQueryBehavior}.
	 * @param ctx the parse tree
	 */
	void exitJsonQueryBehavior(SqlBaseParser.JsonQueryBehaviorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#jsonObjectMember}.
	 * @param ctx the parse tree
	 */
	void enterJsonObjectMember(SqlBaseParser.JsonObjectMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#jsonObjectMember}.
	 * @param ctx the parse tree
	 */
	void exitJsonObjectMember(SqlBaseParser.JsonObjectMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#processingMode}.
	 * @param ctx the parse tree
	 */
	void enterProcessingMode(SqlBaseParser.ProcessingModeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#processingMode}.
	 * @param ctx the parse tree
	 */
	void exitProcessingMode(SqlBaseParser.ProcessingModeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#nullTreatment}.
	 * @param ctx the parse tree
	 */
	void enterNullTreatment(SqlBaseParser.NullTreatmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#nullTreatment}.
	 * @param ctx the parse tree
	 */
	void exitNullTreatment(SqlBaseParser.NullTreatmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code basicStringLiteral}
	 * labeled alternative in {@link SqlBaseParser#string}.
	 * @param ctx the parse tree
	 */
	void enterBasicStringLiteral(SqlBaseParser.BasicStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code basicStringLiteral}
	 * labeled alternative in {@link SqlBaseParser#string}.
	 * @param ctx the parse tree
	 */
	void exitBasicStringLiteral(SqlBaseParser.BasicStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unicodeStringLiteral}
	 * labeled alternative in {@link SqlBaseParser#string}.
	 * @param ctx the parse tree
	 */
	void enterUnicodeStringLiteral(SqlBaseParser.UnicodeStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unicodeStringLiteral}
	 * labeled alternative in {@link SqlBaseParser#string}.
	 * @param ctx the parse tree
	 */
	void exitUnicodeStringLiteral(SqlBaseParser.UnicodeStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code timeZoneInterval}
	 * labeled alternative in {@link SqlBaseParser#timeZoneSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterTimeZoneInterval(SqlBaseParser.TimeZoneIntervalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code timeZoneInterval}
	 * labeled alternative in {@link SqlBaseParser#timeZoneSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitTimeZoneInterval(SqlBaseParser.TimeZoneIntervalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code timeZoneString}
	 * labeled alternative in {@link SqlBaseParser#timeZoneSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterTimeZoneString(SqlBaseParser.TimeZoneStringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code timeZoneString}
	 * labeled alternative in {@link SqlBaseParser#timeZoneSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitTimeZoneString(SqlBaseParser.TimeZoneStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(SqlBaseParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(SqlBaseParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#comparisonQuantifier}.
	 * @param ctx the parse tree
	 */
	void enterComparisonQuantifier(SqlBaseParser.ComparisonQuantifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#comparisonQuantifier}.
	 * @param ctx the parse tree
	 */
	void exitComparisonQuantifier(SqlBaseParser.ComparisonQuantifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void enterBooleanValue(SqlBaseParser.BooleanValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void exitBooleanValue(SqlBaseParser.BooleanValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#interval}.
	 * @param ctx the parse tree
	 */
	void enterInterval(SqlBaseParser.IntervalContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#interval}.
	 * @param ctx the parse tree
	 */
	void exitInterval(SqlBaseParser.IntervalContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#intervalField}.
	 * @param ctx the parse tree
	 */
	void enterIntervalField(SqlBaseParser.IntervalFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#intervalField}.
	 * @param ctx the parse tree
	 */
	void exitIntervalField(SqlBaseParser.IntervalFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#normalForm}.
	 * @param ctx the parse tree
	 */
	void enterNormalForm(SqlBaseParser.NormalFormContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#normalForm}.
	 * @param ctx the parse tree
	 */
	void exitNormalForm(SqlBaseParser.NormalFormContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rowType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void enterRowType(SqlBaseParser.RowTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rowType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void exitRowType(SqlBaseParser.RowTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void enterIntervalType(SqlBaseParser.IntervalTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void exitIntervalType(SqlBaseParser.IntervalTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(SqlBaseParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(SqlBaseParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doublePrecisionType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void enterDoublePrecisionType(SqlBaseParser.DoublePrecisionTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doublePrecisionType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void exitDoublePrecisionType(SqlBaseParser.DoublePrecisionTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code legacyArrayType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void enterLegacyArrayType(SqlBaseParser.LegacyArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code legacyArrayType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void exitLegacyArrayType(SqlBaseParser.LegacyArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code genericType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void enterGenericType(SqlBaseParser.GenericTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code genericType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void exitGenericType(SqlBaseParser.GenericTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dateTimeType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void enterDateTimeType(SqlBaseParser.DateTimeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dateTimeType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void exitDateTimeType(SqlBaseParser.DateTimeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code legacyMapType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void enterLegacyMapType(SqlBaseParser.LegacyMapTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code legacyMapType}
	 * labeled alternative in {@link SqlBaseParser#type}.
	 * @param ctx the parse tree
	 */
	void exitLegacyMapType(SqlBaseParser.LegacyMapTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#rowField}.
	 * @param ctx the parse tree
	 */
	void enterRowField(SqlBaseParser.RowFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#rowField}.
	 * @param ctx the parse tree
	 */
	void exitRowField(SqlBaseParser.RowFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameter(SqlBaseParser.TypeParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameter(SqlBaseParser.TypeParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#whenClause}.
	 * @param ctx the parse tree
	 */
	void enterWhenClause(SqlBaseParser.WhenClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#whenClause}.
	 * @param ctx the parse tree
	 */
	void exitWhenClause(SqlBaseParser.WhenClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilter(SqlBaseParser.FilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilter(SqlBaseParser.FilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mergeUpdate}
	 * labeled alternative in {@link SqlBaseParser#mergeCase}.
	 * @param ctx the parse tree
	 */
	void enterMergeUpdate(SqlBaseParser.MergeUpdateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mergeUpdate}
	 * labeled alternative in {@link SqlBaseParser#mergeCase}.
	 * @param ctx the parse tree
	 */
	void exitMergeUpdate(SqlBaseParser.MergeUpdateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mergeDelete}
	 * labeled alternative in {@link SqlBaseParser#mergeCase}.
	 * @param ctx the parse tree
	 */
	void enterMergeDelete(SqlBaseParser.MergeDeleteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mergeDelete}
	 * labeled alternative in {@link SqlBaseParser#mergeCase}.
	 * @param ctx the parse tree
	 */
	void exitMergeDelete(SqlBaseParser.MergeDeleteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mergeInsert}
	 * labeled alternative in {@link SqlBaseParser#mergeCase}.
	 * @param ctx the parse tree
	 */
	void enterMergeInsert(SqlBaseParser.MergeInsertContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mergeInsert}
	 * labeled alternative in {@link SqlBaseParser#mergeCase}.
	 * @param ctx the parse tree
	 */
	void exitMergeInsert(SqlBaseParser.MergeInsertContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#over}.
	 * @param ctx the parse tree
	 */
	void enterOver(SqlBaseParser.OverContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#over}.
	 * @param ctx the parse tree
	 */
	void exitOver(SqlBaseParser.OverContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#windowFrame}.
	 * @param ctx the parse tree
	 */
	void enterWindowFrame(SqlBaseParser.WindowFrameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#windowFrame}.
	 * @param ctx the parse tree
	 */
	void exitWindowFrame(SqlBaseParser.WindowFrameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#frameExtent}.
	 * @param ctx the parse tree
	 */
	void enterFrameExtent(SqlBaseParser.FrameExtentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#frameExtent}.
	 * @param ctx the parse tree
	 */
	void exitFrameExtent(SqlBaseParser.FrameExtentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unboundedFrame}
	 * labeled alternative in {@link SqlBaseParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void enterUnboundedFrame(SqlBaseParser.UnboundedFrameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unboundedFrame}
	 * labeled alternative in {@link SqlBaseParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void exitUnboundedFrame(SqlBaseParser.UnboundedFrameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentRowBound}
	 * labeled alternative in {@link SqlBaseParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void enterCurrentRowBound(SqlBaseParser.CurrentRowBoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentRowBound}
	 * labeled alternative in {@link SqlBaseParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void exitCurrentRowBound(SqlBaseParser.CurrentRowBoundContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boundedFrame}
	 * labeled alternative in {@link SqlBaseParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void enterBoundedFrame(SqlBaseParser.BoundedFrameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boundedFrame}
	 * labeled alternative in {@link SqlBaseParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void exitBoundedFrame(SqlBaseParser.BoundedFrameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code quantifiedPrimary}
	 * labeled alternative in {@link SqlBaseParser#rowPattern}.
	 * @param ctx the parse tree
	 */
	void enterQuantifiedPrimary(SqlBaseParser.QuantifiedPrimaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code quantifiedPrimary}
	 * labeled alternative in {@link SqlBaseParser#rowPattern}.
	 * @param ctx the parse tree
	 */
	void exitQuantifiedPrimary(SqlBaseParser.QuantifiedPrimaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code patternConcatenation}
	 * labeled alternative in {@link SqlBaseParser#rowPattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternConcatenation(SqlBaseParser.PatternConcatenationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code patternConcatenation}
	 * labeled alternative in {@link SqlBaseParser#rowPattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternConcatenation(SqlBaseParser.PatternConcatenationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code patternAlternation}
	 * labeled alternative in {@link SqlBaseParser#rowPattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternAlternation(SqlBaseParser.PatternAlternationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code patternAlternation}
	 * labeled alternative in {@link SqlBaseParser#rowPattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternAlternation(SqlBaseParser.PatternAlternationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code patternVariable}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void enterPatternVariable(SqlBaseParser.PatternVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code patternVariable}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void exitPatternVariable(SqlBaseParser.PatternVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyPattern}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void enterEmptyPattern(SqlBaseParser.EmptyPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyPattern}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void exitEmptyPattern(SqlBaseParser.EmptyPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code patternPermutation}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void enterPatternPermutation(SqlBaseParser.PatternPermutationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code patternPermutation}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void exitPatternPermutation(SqlBaseParser.PatternPermutationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code groupedPattern}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void enterGroupedPattern(SqlBaseParser.GroupedPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code groupedPattern}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void exitGroupedPattern(SqlBaseParser.GroupedPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionStartAnchor}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void enterPartitionStartAnchor(SqlBaseParser.PartitionStartAnchorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionStartAnchor}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void exitPartitionStartAnchor(SqlBaseParser.PartitionStartAnchorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionEndAnchor}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void enterPartitionEndAnchor(SqlBaseParser.PartitionEndAnchorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionEndAnchor}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void exitPartitionEndAnchor(SqlBaseParser.PartitionEndAnchorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code excludedPattern}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void enterExcludedPattern(SqlBaseParser.ExcludedPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code excludedPattern}
	 * labeled alternative in {@link SqlBaseParser#patternPrimary}.
	 * @param ctx the parse tree
	 */
	void exitExcludedPattern(SqlBaseParser.ExcludedPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code zeroOrMoreQuantifier}
	 * labeled alternative in {@link SqlBaseParser#patternQuantifier}.
	 * @param ctx the parse tree
	 */
	void enterZeroOrMoreQuantifier(SqlBaseParser.ZeroOrMoreQuantifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code zeroOrMoreQuantifier}
	 * labeled alternative in {@link SqlBaseParser#patternQuantifier}.
	 * @param ctx the parse tree
	 */
	void exitZeroOrMoreQuantifier(SqlBaseParser.ZeroOrMoreQuantifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code oneOrMoreQuantifier}
	 * labeled alternative in {@link SqlBaseParser#patternQuantifier}.
	 * @param ctx the parse tree
	 */
	void enterOneOrMoreQuantifier(SqlBaseParser.OneOrMoreQuantifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code oneOrMoreQuantifier}
	 * labeled alternative in {@link SqlBaseParser#patternQuantifier}.
	 * @param ctx the parse tree
	 */
	void exitOneOrMoreQuantifier(SqlBaseParser.OneOrMoreQuantifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code zeroOrOneQuantifier}
	 * labeled alternative in {@link SqlBaseParser#patternQuantifier}.
	 * @param ctx the parse tree
	 */
	void enterZeroOrOneQuantifier(SqlBaseParser.ZeroOrOneQuantifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code zeroOrOneQuantifier}
	 * labeled alternative in {@link SqlBaseParser#patternQuantifier}.
	 * @param ctx the parse tree
	 */
	void exitZeroOrOneQuantifier(SqlBaseParser.ZeroOrOneQuantifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rangeQuantifier}
	 * labeled alternative in {@link SqlBaseParser#patternQuantifier}.
	 * @param ctx the parse tree
	 */
	void enterRangeQuantifier(SqlBaseParser.RangeQuantifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rangeQuantifier}
	 * labeled alternative in {@link SqlBaseParser#patternQuantifier}.
	 * @param ctx the parse tree
	 */
	void exitRangeQuantifier(SqlBaseParser.RangeQuantifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#updateAssignment}.
	 * @param ctx the parse tree
	 */
	void enterUpdateAssignment(SqlBaseParser.UpdateAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#updateAssignment}.
	 * @param ctx the parse tree
	 */
	void exitUpdateAssignment(SqlBaseParser.UpdateAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code explainFormat}
	 * labeled alternative in {@link SqlBaseParser#explainOption}.
	 * @param ctx the parse tree
	 */
	void enterExplainFormat(SqlBaseParser.ExplainFormatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code explainFormat}
	 * labeled alternative in {@link SqlBaseParser#explainOption}.
	 * @param ctx the parse tree
	 */
	void exitExplainFormat(SqlBaseParser.ExplainFormatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code explainType}
	 * labeled alternative in {@link SqlBaseParser#explainOption}.
	 * @param ctx the parse tree
	 */
	void enterExplainType(SqlBaseParser.ExplainTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code explainType}
	 * labeled alternative in {@link SqlBaseParser#explainOption}.
	 * @param ctx the parse tree
	 */
	void exitExplainType(SqlBaseParser.ExplainTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isolationLevel}
	 * labeled alternative in {@link SqlBaseParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void enterIsolationLevel(SqlBaseParser.IsolationLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isolationLevel}
	 * labeled alternative in {@link SqlBaseParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void exitIsolationLevel(SqlBaseParser.IsolationLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code transactionAccessMode}
	 * labeled alternative in {@link SqlBaseParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void enterTransactionAccessMode(SqlBaseParser.TransactionAccessModeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code transactionAccessMode}
	 * labeled alternative in {@link SqlBaseParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void exitTransactionAccessMode(SqlBaseParser.TransactionAccessModeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code readUncommitted}
	 * labeled alternative in {@link SqlBaseParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void enterReadUncommitted(SqlBaseParser.ReadUncommittedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code readUncommitted}
	 * labeled alternative in {@link SqlBaseParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void exitReadUncommitted(SqlBaseParser.ReadUncommittedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code readCommitted}
	 * labeled alternative in {@link SqlBaseParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void enterReadCommitted(SqlBaseParser.ReadCommittedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code readCommitted}
	 * labeled alternative in {@link SqlBaseParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void exitReadCommitted(SqlBaseParser.ReadCommittedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code repeatableRead}
	 * labeled alternative in {@link SqlBaseParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void enterRepeatableRead(SqlBaseParser.RepeatableReadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code repeatableRead}
	 * labeled alternative in {@link SqlBaseParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void exitRepeatableRead(SqlBaseParser.RepeatableReadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code serializable}
	 * labeled alternative in {@link SqlBaseParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void enterSerializable(SqlBaseParser.SerializableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code serializable}
	 * labeled alternative in {@link SqlBaseParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void exitSerializable(SqlBaseParser.SerializableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code positionalArgument}
	 * labeled alternative in {@link SqlBaseParser#callArgument}.
	 * @param ctx the parse tree
	 */
	void enterPositionalArgument(SqlBaseParser.PositionalArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code positionalArgument}
	 * labeled alternative in {@link SqlBaseParser#callArgument}.
	 * @param ctx the parse tree
	 */
	void exitPositionalArgument(SqlBaseParser.PositionalArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code namedArgument}
	 * labeled alternative in {@link SqlBaseParser#callArgument}.
	 * @param ctx the parse tree
	 */
	void enterNamedArgument(SqlBaseParser.NamedArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code namedArgument}
	 * labeled alternative in {@link SqlBaseParser#callArgument}.
	 * @param ctx the parse tree
	 */
	void exitNamedArgument(SqlBaseParser.NamedArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code qualifiedArgument}
	 * labeled alternative in {@link SqlBaseParser#pathElement}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedArgument(SqlBaseParser.QualifiedArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code qualifiedArgument}
	 * labeled alternative in {@link SqlBaseParser#pathElement}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedArgument(SqlBaseParser.QualifiedArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unqualifiedArgument}
	 * labeled alternative in {@link SqlBaseParser#pathElement}.
	 * @param ctx the parse tree
	 */
	void enterUnqualifiedArgument(SqlBaseParser.UnqualifiedArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unqualifiedArgument}
	 * labeled alternative in {@link SqlBaseParser#pathElement}.
	 * @param ctx the parse tree
	 */
	void exitUnqualifiedArgument(SqlBaseParser.UnqualifiedArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#pathSpecification}.
	 * @param ctx the parse tree
	 */
	void enterPathSpecification(SqlBaseParser.PathSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#pathSpecification}.
	 * @param ctx the parse tree
	 */
	void exitPathSpecification(SqlBaseParser.PathSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#privilege}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege(SqlBaseParser.PrivilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#privilege}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege(SqlBaseParser.PrivilegeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(SqlBaseParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(SqlBaseParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#queryPeriod}.
	 * @param ctx the parse tree
	 */
	void enterQueryPeriod(SqlBaseParser.QueryPeriodContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#queryPeriod}.
	 * @param ctx the parse tree
	 */
	void exitQueryPeriod(SqlBaseParser.QueryPeriodContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#rangeType}.
	 * @param ctx the parse tree
	 */
	void enterRangeType(SqlBaseParser.RangeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#rangeType}.
	 * @param ctx the parse tree
	 */
	void exitRangeType(SqlBaseParser.RangeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specifiedPrincipal}
	 * labeled alternative in {@link SqlBaseParser#grantor}.
	 * @param ctx the parse tree
	 */
	void enterSpecifiedPrincipal(SqlBaseParser.SpecifiedPrincipalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specifiedPrincipal}
	 * labeled alternative in {@link SqlBaseParser#grantor}.
	 * @param ctx the parse tree
	 */
	void exitSpecifiedPrincipal(SqlBaseParser.SpecifiedPrincipalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentUserGrantor}
	 * labeled alternative in {@link SqlBaseParser#grantor}.
	 * @param ctx the parse tree
	 */
	void enterCurrentUserGrantor(SqlBaseParser.CurrentUserGrantorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentUserGrantor}
	 * labeled alternative in {@link SqlBaseParser#grantor}.
	 * @param ctx the parse tree
	 */
	void exitCurrentUserGrantor(SqlBaseParser.CurrentUserGrantorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentRoleGrantor}
	 * labeled alternative in {@link SqlBaseParser#grantor}.
	 * @param ctx the parse tree
	 */
	void enterCurrentRoleGrantor(SqlBaseParser.CurrentRoleGrantorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentRoleGrantor}
	 * labeled alternative in {@link SqlBaseParser#grantor}.
	 * @param ctx the parse tree
	 */
	void exitCurrentRoleGrantor(SqlBaseParser.CurrentRoleGrantorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unspecifiedPrincipal}
	 * labeled alternative in {@link SqlBaseParser#principal}.
	 * @param ctx the parse tree
	 */
	void enterUnspecifiedPrincipal(SqlBaseParser.UnspecifiedPrincipalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unspecifiedPrincipal}
	 * labeled alternative in {@link SqlBaseParser#principal}.
	 * @param ctx the parse tree
	 */
	void exitUnspecifiedPrincipal(SqlBaseParser.UnspecifiedPrincipalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code userPrincipal}
	 * labeled alternative in {@link SqlBaseParser#principal}.
	 * @param ctx the parse tree
	 */
	void enterUserPrincipal(SqlBaseParser.UserPrincipalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code userPrincipal}
	 * labeled alternative in {@link SqlBaseParser#principal}.
	 * @param ctx the parse tree
	 */
	void exitUserPrincipal(SqlBaseParser.UserPrincipalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rolePrincipal}
	 * labeled alternative in {@link SqlBaseParser#principal}.
	 * @param ctx the parse tree
	 */
	void enterRolePrincipal(SqlBaseParser.RolePrincipalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rolePrincipal}
	 * labeled alternative in {@link SqlBaseParser#principal}.
	 * @param ctx the parse tree
	 */
	void exitRolePrincipal(SqlBaseParser.RolePrincipalContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#roles}.
	 * @param ctx the parse tree
	 */
	void enterRoles(SqlBaseParser.RolesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#roles}.
	 * @param ctx the parse tree
	 */
	void exitRoles(SqlBaseParser.RolesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unquotedIdentifier}
	 * labeled alternative in {@link SqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterUnquotedIdentifier(SqlBaseParser.UnquotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unquotedIdentifier}
	 * labeled alternative in {@link SqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitUnquotedIdentifier(SqlBaseParser.UnquotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code quotedIdentifier}
	 * labeled alternative in {@link SqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterQuotedIdentifier(SqlBaseParser.QuotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code quotedIdentifier}
	 * labeled alternative in {@link SqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitQuotedIdentifier(SqlBaseParser.QuotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code backQuotedIdentifier}
	 * labeled alternative in {@link SqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterBackQuotedIdentifier(SqlBaseParser.BackQuotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code backQuotedIdentifier}
	 * labeled alternative in {@link SqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitBackQuotedIdentifier(SqlBaseParser.BackQuotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code digitIdentifier}
	 * labeled alternative in {@link SqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterDigitIdentifier(SqlBaseParser.DigitIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code digitIdentifier}
	 * labeled alternative in {@link SqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitDigitIdentifier(SqlBaseParser.DigitIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link SqlBaseParser#number}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(SqlBaseParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link SqlBaseParser#number}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(SqlBaseParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doubleLiteral}
	 * labeled alternative in {@link SqlBaseParser#number}.
	 * @param ctx the parse tree
	 */
	void enterDoubleLiteral(SqlBaseParser.DoubleLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doubleLiteral}
	 * labeled alternative in {@link SqlBaseParser#number}.
	 * @param ctx the parse tree
	 */
	void exitDoubleLiteral(SqlBaseParser.DoubleLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integerLiteral}
	 * labeled alternative in {@link SqlBaseParser#number}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(SqlBaseParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integerLiteral}
	 * labeled alternative in {@link SqlBaseParser#number}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(SqlBaseParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlBaseParser#nonReserved}.
	 * @param ctx the parse tree
	 */
	void enterNonReserved(SqlBaseParser.NonReservedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlBaseParser#nonReserved}.
	 * @param ctx the parse tree
	 */
	void exitNonReserved(SqlBaseParser.NonReservedContext ctx);
}