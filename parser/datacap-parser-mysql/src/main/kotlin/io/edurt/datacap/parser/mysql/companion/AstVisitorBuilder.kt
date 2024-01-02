package io.edurt.datacap.parser.mysql.companion

import io.edurt.datacap.parser.mysql.MySqlParser
import io.edurt.datacap.parser.mysql.MySqlParser.*
import io.edurt.datacap.parser.mysql.MySqlParserVisitor
import io.edurt.datacap.parser.mysql.companion.ast.Node
import io.edurt.datacap.parser.mysql.companion.ast.NodeLocation
import io.edurt.datacap.parser.mysql.companion.ast.ParserOptions
import io.edurt.datacap.parser.mysql.companion.tree.Relation
import io.edurt.datacap.parser.mysql.companion.tree.SelectItem
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.RuleNode
import org.antlr.v4.runtime.tree.TerminalNode
import java.util.*
import java.util.stream.Collectors.toList


class AstVisitorBuilder(options: ParserOptions.DecimalLiteralTreatment) : MySqlParserVisitor<Node> {
    private fun <T> visitSelectElements(context: SelectElementsContext, clazz: Class<T>): List<T> {
        return context.selectElement().stream()
                .map<Any>(this::visitChildren)
                .map { o: Any? -> clazz.cast(o) }
                .collect(toList())
    }

    private fun getLocation(parserRuleContext: ParserRuleContext): Optional<NodeLocation> {
        Objects.requireNonNull(parserRuleContext, "parserRuleContext is null")
        return Optional.ofNullable(getLocation(parserRuleContext.getStart()))
    }

    private fun getLocation(token: Token): NodeLocation {
        Objects.requireNonNull(token, "token is null")
        return NodeLocation(token.line, token.charPositionInLine)
    }

    override fun visit(tree: ParseTree): Node {
        return when (tree) {
            is MySqlParser.RootContext -> visitRoot(tree)
            is MySqlParser.SqlStatementsContext -> visitSqlStatements(tree)
            is MySqlParser.SqlStatementContext -> visitSqlStatement(tree)
            else -> throw UnsupportedOperationException("Unsupported ParseTree type: ${tree?.javaClass?.simpleName}")
        }
    }

    override fun visitChildren(p0: RuleNode?): Node {
        return when (p0) {
            is SelectExpressionElementContext -> visitSelectExpressionElement(p0)
            else -> throw UnsupportedOperationException("Unsupported RuleNode type: ${p0?.javaClass?.simpleName}")
        }
    }

    override fun visitTerminal(p0: TerminalNode?): Node {
        TODO("Not yet implemented")
    }

    override fun visitErrorNode(p0: ErrorNode?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRoot(ctx: MySqlParser.RootContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSqlStatements(ctx: MySqlParser.SqlStatementsContext): Node {
        // TODO: In the later stage, multiple SQL statements can be parsed
        return visit(ctx.sqlStatement()[0])
    }

    override fun visitSqlStatement(ctx: MySqlParser.SqlStatementContext): Node {
        if (ctx.dmlStatement() != null) {
            return visitDmlStatement(ctx.dmlStatement())
        }
        else if (ctx.ddlStatement() != null) {
            return visitDdlStatement(ctx.ddlStatement())
        }
        else if (ctx.transactionStatement() != null) {
            return visitTransactionStatement(ctx.transactionStatement())
        }
        else if (ctx.replicationStatement() != null) {
            return visitReplicationStatement(ctx.replicationStatement())
        }
        else if (ctx.utilityStatement() != null) {
            return visitUtilityStatement(ctx.utilityStatement())
        }
        else if (ctx.preparedStatement() != null) {
            return visitPreparedStatement(ctx.preparedStatement())
        }
        else if (ctx.administrationStatement() != null) {
            return visitAdministrationStatement(ctx.administrationStatement())
        }
        else {
            throw UnsupportedOperationException("Unsupported SqlStatement type: ${ctx?.javaClass?.simpleName}")
        }
    }

    override fun visitEmptyStatement_(ctx: MySqlParser.EmptyStatement_Context?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDdlStatement(ctx: MySqlParser.DdlStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDmlStatement(ctx: MySqlParser.DmlStatementContext): Node {
        ctx.selectStatement()?.let {
            return visitSimpleSelect(ctx.selectStatement() as SimpleSelectContext)
        }
        TODO("Not yet implemented")
    }

    override fun visitTransactionStatement(ctx: MySqlParser.TransactionStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitReplicationStatement(ctx: MySqlParser.ReplicationStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPreparedStatement(ctx: MySqlParser.PreparedStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCompoundStatement(ctx: MySqlParser.CompoundStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAdministrationStatement(ctx: MySqlParser.AdministrationStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUtilityStatement(ctx: MySqlParser.UtilityStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateDatabase(ctx: MySqlParser.CreateDatabaseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateEvent(ctx: MySqlParser.CreateEventContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateIndex(ctx: MySqlParser.CreateIndexContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateLogfileGroup(ctx: MySqlParser.CreateLogfileGroupContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateProcedure(ctx: MySqlParser.CreateProcedureContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateFunction(ctx: MySqlParser.CreateFunctionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateRole(ctx: MySqlParser.CreateRoleContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateServer(ctx: MySqlParser.CreateServerContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCopyCreateTable(ctx: MySqlParser.CopyCreateTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitQueryCreateTable(ctx: MySqlParser.QueryCreateTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitColumnCreateTable(ctx: MySqlParser.ColumnCreateTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateTablespaceInnodb(ctx: MySqlParser.CreateTablespaceInnodbContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateTablespaceNdb(ctx: MySqlParser.CreateTablespaceNdbContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateTrigger(ctx: MySqlParser.CreateTriggerContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitWithClause(ctx: MySqlParser.WithClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCommonTableExpressions(ctx: MySqlParser.CommonTableExpressionsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCteName(ctx: MySqlParser.CteNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCteColumnName(ctx: MySqlParser.CteColumnNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateView(ctx: MySqlParser.CreateViewContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateDatabaseOption(ctx: MySqlParser.CreateDatabaseOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCharSet(ctx: MySqlParser.CharSetContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCurrentUserExpression(ctx: MySqlParser.CurrentUserExpressionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitOwnerStatement(ctx: MySqlParser.OwnerStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPreciseSchedule(ctx: MySqlParser.PreciseScheduleContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIntervalSchedule(ctx: MySqlParser.IntervalScheduleContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTimestampValue(ctx: MySqlParser.TimestampValueContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIntervalExpr(ctx: MySqlParser.IntervalExprContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIntervalType(ctx: MySqlParser.IntervalTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitEnableType(ctx: MySqlParser.EnableTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIndexType(ctx: MySqlParser.IndexTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIndexOption(ctx: MySqlParser.IndexOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitProcedureParameter(ctx: MySqlParser.ProcedureParameterContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFunctionParameter(ctx: MySqlParser.FunctionParameterContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRoutineComment(ctx: MySqlParser.RoutineCommentContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRoutineLanguage(ctx: MySqlParser.RoutineLanguageContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRoutineBehavior(ctx: MySqlParser.RoutineBehaviorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRoutineData(ctx: MySqlParser.RoutineDataContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRoutineSecurity(ctx: MySqlParser.RoutineSecurityContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitServerOption(ctx: MySqlParser.ServerOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateDefinitions(ctx: MySqlParser.CreateDefinitionsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitColumnDeclaration(ctx: MySqlParser.ColumnDeclarationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitConstraintDeclaration(ctx: MySqlParser.ConstraintDeclarationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIndexDeclaration(ctx: MySqlParser.IndexDeclarationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitColumnDefinition(ctx: MySqlParser.ColumnDefinitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitNullColumnConstraint(ctx: MySqlParser.NullColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDefaultColumnConstraint(ctx: MySqlParser.DefaultColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitVisibilityColumnConstraint(ctx: MySqlParser.VisibilityColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitInvisibilityColumnConstraint(ctx: MySqlParser.InvisibilityColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAutoIncrementColumnConstraint(ctx: MySqlParser.AutoIncrementColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPrimaryKeyColumnConstraint(ctx: MySqlParser.PrimaryKeyColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUniqueKeyColumnConstraint(ctx: MySqlParser.UniqueKeyColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCommentColumnConstraint(ctx: MySqlParser.CommentColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFormatColumnConstraint(ctx: MySqlParser.FormatColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitStorageColumnConstraint(ctx: MySqlParser.StorageColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitReferenceColumnConstraint(ctx: MySqlParser.ReferenceColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCollateColumnConstraint(ctx: MySqlParser.CollateColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitGeneratedColumnConstraint(ctx: MySqlParser.GeneratedColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSerialDefaultColumnConstraint(ctx: MySqlParser.SerialDefaultColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCheckColumnConstraint(ctx: MySqlParser.CheckColumnConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPrimaryKeyTableConstraint(ctx: MySqlParser.PrimaryKeyTableConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUniqueKeyTableConstraint(ctx: MySqlParser.UniqueKeyTableConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitForeignKeyTableConstraint(ctx: MySqlParser.ForeignKeyTableConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCheckTableConstraint(ctx: MySqlParser.CheckTableConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitReferenceDefinition(ctx: MySqlParser.ReferenceDefinitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitReferenceAction(ctx: MySqlParser.ReferenceActionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitReferenceControlType(ctx: MySqlParser.ReferenceControlTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSimpleIndexDeclaration(ctx: MySqlParser.SimpleIndexDeclarationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSpecialIndexDeclaration(ctx: MySqlParser.SpecialIndexDeclarationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionEngine(ctx: MySqlParser.TableOptionEngineContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionEngineAttribute(ctx: MySqlParser.TableOptionEngineAttributeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionAutoextendSize(ctx: MySqlParser.TableOptionAutoextendSizeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionAutoIncrement(ctx: MySqlParser.TableOptionAutoIncrementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionAverage(ctx: MySqlParser.TableOptionAverageContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionCharset(ctx: MySqlParser.TableOptionCharsetContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionChecksum(ctx: MySqlParser.TableOptionChecksumContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionCollate(ctx: MySqlParser.TableOptionCollateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionComment(ctx: MySqlParser.TableOptionCommentContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionCompression(ctx: MySqlParser.TableOptionCompressionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionConnection(ctx: MySqlParser.TableOptionConnectionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionDataDirectory(ctx: MySqlParser.TableOptionDataDirectoryContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionDelay(ctx: MySqlParser.TableOptionDelayContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionEncryption(ctx: MySqlParser.TableOptionEncryptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionPageCompressed(ctx: MySqlParser.TableOptionPageCompressedContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionPageCompressionLevel(ctx: MySqlParser.TableOptionPageCompressionLevelContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionEncryptionKeyId(ctx: MySqlParser.TableOptionEncryptionKeyIdContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionIndexDirectory(ctx: MySqlParser.TableOptionIndexDirectoryContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionInsertMethod(ctx: MySqlParser.TableOptionInsertMethodContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionKeyBlockSize(ctx: MySqlParser.TableOptionKeyBlockSizeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionMaxRows(ctx: MySqlParser.TableOptionMaxRowsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionMinRows(ctx: MySqlParser.TableOptionMinRowsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionPackKeys(ctx: MySqlParser.TableOptionPackKeysContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionPassword(ctx: MySqlParser.TableOptionPasswordContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionRowFormat(ctx: MySqlParser.TableOptionRowFormatContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionStartTransaction(ctx: MySqlParser.TableOptionStartTransactionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionSecondaryEngineAttribute(ctx: MySqlParser.TableOptionSecondaryEngineAttributeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionRecalculation(ctx: MySqlParser.TableOptionRecalculationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionPersistent(ctx: MySqlParser.TableOptionPersistentContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionSamplePage(ctx: MySqlParser.TableOptionSamplePageContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionTablespace(ctx: MySqlParser.TableOptionTablespaceContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionTableType(ctx: MySqlParser.TableOptionTableTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionTransactional(ctx: MySqlParser.TableOptionTransactionalContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableOptionUnion(ctx: MySqlParser.TableOptionUnionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableType(ctx: MySqlParser.TableTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTablespaceStorage(ctx: MySqlParser.TablespaceStorageContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionDefinitions(ctx: MySqlParser.PartitionDefinitionsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionFunctionHash(ctx: MySqlParser.PartitionFunctionHashContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionFunctionKey(ctx: MySqlParser.PartitionFunctionKeyContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionFunctionRange(ctx: MySqlParser.PartitionFunctionRangeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionFunctionList(ctx: MySqlParser.PartitionFunctionListContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSubPartitionFunctionHash(ctx: MySqlParser.SubPartitionFunctionHashContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSubPartitionFunctionKey(ctx: MySqlParser.SubPartitionFunctionKeyContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionComparison(ctx: MySqlParser.PartitionComparisonContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionListAtom(ctx: MySqlParser.PartitionListAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionListVector(ctx: MySqlParser.PartitionListVectorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionSimple(ctx: MySqlParser.PartitionSimpleContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionDefinerAtom(ctx: MySqlParser.PartitionDefinerAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionDefinerVector(ctx: MySqlParser.PartitionDefinerVectorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSubpartitionDefinition(ctx: MySqlParser.SubpartitionDefinitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionOptionEngine(ctx: MySqlParser.PartitionOptionEngineContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionOptionComment(ctx: MySqlParser.PartitionOptionCommentContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionOptionDataDirectory(ctx: MySqlParser.PartitionOptionDataDirectoryContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionOptionIndexDirectory(ctx: MySqlParser.PartitionOptionIndexDirectoryContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionOptionMaxRows(ctx: MySqlParser.PartitionOptionMaxRowsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionOptionMinRows(ctx: MySqlParser.PartitionOptionMinRowsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionOptionTablespace(ctx: MySqlParser.PartitionOptionTablespaceContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionOptionNodeGroup(ctx: MySqlParser.PartitionOptionNodeGroupContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterSimpleDatabase(ctx: MySqlParser.AlterSimpleDatabaseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterUpgradeName(ctx: MySqlParser.AlterUpgradeNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterEvent(ctx: MySqlParser.AlterEventContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterFunction(ctx: MySqlParser.AlterFunctionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterInstance(ctx: MySqlParser.AlterInstanceContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterLogfileGroup(ctx: MySqlParser.AlterLogfileGroupContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterProcedure(ctx: MySqlParser.AlterProcedureContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterServer(ctx: MySqlParser.AlterServerContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterTable(ctx: MySqlParser.AlterTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterTablespace(ctx: MySqlParser.AlterTablespaceContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterView(ctx: MySqlParser.AlterViewContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByTableOption(ctx: MySqlParser.AlterByTableOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAddColumn(ctx: MySqlParser.AlterByAddColumnContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAddColumns(ctx: MySqlParser.AlterByAddColumnsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAddIndex(ctx: MySqlParser.AlterByAddIndexContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAddPrimaryKey(ctx: MySqlParser.AlterByAddPrimaryKeyContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAddUniqueKey(ctx: MySqlParser.AlterByAddUniqueKeyContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAddSpecialIndex(ctx: MySqlParser.AlterByAddSpecialIndexContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAddForeignKey(ctx: MySqlParser.AlterByAddForeignKeyContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAddCheckTableConstraint(ctx: MySqlParser.AlterByAddCheckTableConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAlterCheckTableConstraint(ctx: MySqlParser.AlterByAlterCheckTableConstraintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterBySetAlgorithm(ctx: MySqlParser.AlterBySetAlgorithmContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByChangeDefault(ctx: MySqlParser.AlterByChangeDefaultContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByChangeColumn(ctx: MySqlParser.AlterByChangeColumnContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByRenameColumn(ctx: MySqlParser.AlterByRenameColumnContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByLock(ctx: MySqlParser.AlterByLockContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByModifyColumn(ctx: MySqlParser.AlterByModifyColumnContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByDropColumn(ctx: MySqlParser.AlterByDropColumnContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByDropConstraintCheck(ctx: MySqlParser.AlterByDropConstraintCheckContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByDropPrimaryKey(ctx: MySqlParser.AlterByDropPrimaryKeyContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByDropIndex(ctx: MySqlParser.AlterByDropIndexContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByRenameIndex(ctx: MySqlParser.AlterByRenameIndexContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAlterColumnDefault(ctx: MySqlParser.AlterByAlterColumnDefaultContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAlterIndexVisibility(ctx: MySqlParser.AlterByAlterIndexVisibilityContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByDropForeignKey(ctx: MySqlParser.AlterByDropForeignKeyContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByDisableKeys(ctx: MySqlParser.AlterByDisableKeysContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByEnableKeys(ctx: MySqlParser.AlterByEnableKeysContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByRename(ctx: MySqlParser.AlterByRenameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByOrder(ctx: MySqlParser.AlterByOrderContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByConvertCharset(ctx: MySqlParser.AlterByConvertCharsetContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByDefaultCharset(ctx: MySqlParser.AlterByDefaultCharsetContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByDiscardTablespace(ctx: MySqlParser.AlterByDiscardTablespaceContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByImportTablespace(ctx: MySqlParser.AlterByImportTablespaceContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByForce(ctx: MySqlParser.AlterByForceContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByValidate(ctx: MySqlParser.AlterByValidateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAddDefinitions(ctx: MySqlParser.AlterByAddDefinitionsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterPartition(ctx: MySqlParser.AlterPartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAddPartition(ctx: MySqlParser.AlterByAddPartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByDropPartition(ctx: MySqlParser.AlterByDropPartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByDiscardPartition(ctx: MySqlParser.AlterByDiscardPartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByImportPartition(ctx: MySqlParser.AlterByImportPartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByTruncatePartition(ctx: MySqlParser.AlterByTruncatePartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByCoalescePartition(ctx: MySqlParser.AlterByCoalescePartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByReorganizePartition(ctx: MySqlParser.AlterByReorganizePartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByExchangePartition(ctx: MySqlParser.AlterByExchangePartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByAnalyzePartition(ctx: MySqlParser.AlterByAnalyzePartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByCheckPartition(ctx: MySqlParser.AlterByCheckPartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByOptimizePartition(ctx: MySqlParser.AlterByOptimizePartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByRebuildPartition(ctx: MySqlParser.AlterByRebuildPartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByRepairPartition(ctx: MySqlParser.AlterByRepairPartitionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByRemovePartitioning(ctx: MySqlParser.AlterByRemovePartitioningContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterByUpgradePartitioning(ctx: MySqlParser.AlterByUpgradePartitioningContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropDatabase(ctx: MySqlParser.DropDatabaseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropEvent(ctx: MySqlParser.DropEventContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropIndex(ctx: MySqlParser.DropIndexContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropLogfileGroup(ctx: MySqlParser.DropLogfileGroupContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropProcedure(ctx: MySqlParser.DropProcedureContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropFunction(ctx: MySqlParser.DropFunctionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropServer(ctx: MySqlParser.DropServerContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropTable(ctx: MySqlParser.DropTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropTablespace(ctx: MySqlParser.DropTablespaceContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropTrigger(ctx: MySqlParser.DropTriggerContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropView(ctx: MySqlParser.DropViewContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropRole(ctx: MySqlParser.DropRoleContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSetRole(ctx: MySqlParser.SetRoleContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRenameTable(ctx: MySqlParser.RenameTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRenameTableClause(ctx: MySqlParser.RenameTableClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTruncateTable(ctx: MySqlParser.TruncateTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCallStatement(ctx: MySqlParser.CallStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDeleteStatement(ctx: MySqlParser.DeleteStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDoStatement(ctx: MySqlParser.DoStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHandlerStatement(ctx: MySqlParser.HandlerStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitInsertStatement(ctx: MySqlParser.InsertStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLoadDataStatement(ctx: MySqlParser.LoadDataStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLoadXmlStatement(ctx: MySqlParser.LoadXmlStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitReplaceStatement(ctx: MySqlParser.ReplaceStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSimpleSelect(ctx: SimpleSelectContext): Node {
        ctx.querySpecification()?.let {
            return visitQuerySpecification(it)
        }
        TODO("Not yet implemented")
    }

    override fun visitParenthesisSelect(ctx: MySqlParser.ParenthesisSelectContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUnionSelect(ctx: MySqlParser.UnionSelectContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUnionParenthesisSelect(ctx: MySqlParser.UnionParenthesisSelectContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitWithLateralStatement(ctx: MySqlParser.WithLateralStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUpdateStatement(ctx: MySqlParser.UpdateStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitValuesStatement(ctx: MySqlParser.ValuesStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitInsertStatementValue(ctx: MySqlParser.InsertStatementValueContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUpdatedElement(ctx: MySqlParser.UpdatedElementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAssignmentField(ctx: MySqlParser.AssignmentFieldContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLockClause(ctx: MySqlParser.LockClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSingleDeleteStatement(ctx: MySqlParser.SingleDeleteStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitMultipleDeleteStatement(ctx: MySqlParser.MultipleDeleteStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHandlerOpenStatement(ctx: MySqlParser.HandlerOpenStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHandlerReadIndexStatement(ctx: MySqlParser.HandlerReadIndexStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHandlerReadStatement(ctx: MySqlParser.HandlerReadStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHandlerCloseStatement(ctx: MySqlParser.HandlerCloseStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSingleUpdateStatement(ctx: MySqlParser.SingleUpdateStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitMultipleUpdateStatement(ctx: MySqlParser.MultipleUpdateStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitOrderByClause(ctx: MySqlParser.OrderByClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitOrderByExpression(ctx: MySqlParser.OrderByExpressionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableSources(ctx: MySqlParser.TableSourcesContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableSourceBase(ctx: MySqlParser.TableSourceBaseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableSourceNested(ctx: MySqlParser.TableSourceNestedContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableJson(ctx: MySqlParser.TableJsonContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAtomTableItem(ctx: MySqlParser.AtomTableItemContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSubqueryTableItem(ctx: MySqlParser.SubqueryTableItemContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableSourcesItem(ctx: MySqlParser.TableSourcesItemContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIndexHint(ctx: MySqlParser.IndexHintContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIndexHintType(ctx: MySqlParser.IndexHintTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitInnerJoin(ctx: MySqlParser.InnerJoinContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitStraightJoin(ctx: MySqlParser.StraightJoinContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitOuterJoin(ctx: MySqlParser.OuterJoinContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitNaturalJoin(ctx: MySqlParser.NaturalJoinContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitJoinSpec(ctx: MySqlParser.JoinSpecContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitQueryExpression(ctx: MySqlParser.QueryExpressionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitQueryExpressionNointo(ctx: MySqlParser.QueryExpressionNointoContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitQuerySpecification(ctx: MySqlParser.QuerySpecificationContext): Node {
        val from: Optional<Relation> = Optional.empty()
        val selectItems: List<SelectItem> = visitSelectElements(ctx.selectElements(), SelectItem::class.java)
        println(selectItems)

        TODO("Not yet implemented")
    }

    override fun visitQuerySpecificationNointo(ctx: MySqlParser.QuerySpecificationNointoContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUnionParenthesis(ctx: MySqlParser.UnionParenthesisContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUnionStatement(ctx: MySqlParser.UnionStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLateralStatement(ctx: MySqlParser.LateralStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitJsonTable(ctx: MySqlParser.JsonTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitJsonColumnList(ctx: MySqlParser.JsonColumnListContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitJsonColumn(ctx: MySqlParser.JsonColumnContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitJsonOnEmpty(ctx: MySqlParser.JsonOnEmptyContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitJsonOnError(ctx: MySqlParser.JsonOnErrorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSelectSpec(ctx: MySqlParser.SelectSpecContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSelectElements(ctx: MySqlParser.SelectElementsContext): Node {
        TODO("Not yet implemented")
    }

    override fun visitSelectStarElement(ctx: MySqlParser.SelectStarElementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSelectColumnElement(ctx: MySqlParser.SelectColumnElementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSelectFunctionElement(ctx: MySqlParser.SelectFunctionElementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSelectExpressionElement(ctx: MySqlParser.SelectExpressionElementContext): Node {
        return SelectItem(getLocation(ctx.expression()), ctx.expression().text)
    }

    override fun visitSelectIntoVariables(ctx: MySqlParser.SelectIntoVariablesContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSelectIntoDumpFile(ctx: MySqlParser.SelectIntoDumpFileContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSelectIntoTextFile(ctx: MySqlParser.SelectIntoTextFileContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSelectFieldsInto(ctx: MySqlParser.SelectFieldsIntoContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSelectLinesInto(ctx: MySqlParser.SelectLinesIntoContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFromClause(ctx: MySqlParser.FromClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitGroupByClause(ctx: MySqlParser.GroupByClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHavingClause(ctx: MySqlParser.HavingClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitWindowClause(ctx: MySqlParser.WindowClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitGroupByItem(ctx: MySqlParser.GroupByItemContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLimitClause(ctx: MySqlParser.LimitClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLimitClauseAtom(ctx: MySqlParser.LimitClauseAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitStartTransaction(ctx: MySqlParser.StartTransactionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitBeginWork(ctx: MySqlParser.BeginWorkContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCommitWork(ctx: MySqlParser.CommitWorkContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRollbackWork(ctx: MySqlParser.RollbackWorkContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSavepointStatement(ctx: MySqlParser.SavepointStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRollbackStatement(ctx: MySqlParser.RollbackStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitReleaseStatement(ctx: MySqlParser.ReleaseStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLockTables(ctx: MySqlParser.LockTablesContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUnlockTables(ctx: MySqlParser.UnlockTablesContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSetAutocommitStatement(ctx: MySqlParser.SetAutocommitStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSetTransactionStatement(ctx: MySqlParser.SetTransactionStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTransactionMode(ctx: MySqlParser.TransactionModeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLockTableElement(ctx: MySqlParser.LockTableElementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLockAction(ctx: MySqlParser.LockActionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTransactionOption(ctx: MySqlParser.TransactionOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTransactionLevel(ctx: MySqlParser.TransactionLevelContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitChangeMaster(ctx: MySqlParser.ChangeMasterContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitChangeReplicationFilter(ctx: MySqlParser.ChangeReplicationFilterContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPurgeBinaryLogs(ctx: MySqlParser.PurgeBinaryLogsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitResetMaster(ctx: MySqlParser.ResetMasterContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitResetSlave(ctx: MySqlParser.ResetSlaveContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitStartSlave(ctx: MySqlParser.StartSlaveContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitStopSlave(ctx: MySqlParser.StopSlaveContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitStartGroupReplication(ctx: MySqlParser.StartGroupReplicationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitStopGroupReplication(ctx: MySqlParser.StopGroupReplicationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitMasterStringOption(ctx: MySqlParser.MasterStringOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitMasterDecimalOption(ctx: MySqlParser.MasterDecimalOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitMasterBoolOption(ctx: MySqlParser.MasterBoolOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitMasterRealOption(ctx: MySqlParser.MasterRealOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitMasterUidListOption(ctx: MySqlParser.MasterUidListOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitStringMasterOption(ctx: MySqlParser.StringMasterOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDecimalMasterOption(ctx: MySqlParser.DecimalMasterOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitBoolMasterOption(ctx: MySqlParser.BoolMasterOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitChannelOption(ctx: MySqlParser.ChannelOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDoDbReplication(ctx: MySqlParser.DoDbReplicationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIgnoreDbReplication(ctx: MySqlParser.IgnoreDbReplicationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDoTableReplication(ctx: MySqlParser.DoTableReplicationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIgnoreTableReplication(ctx: MySqlParser.IgnoreTableReplicationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitWildDoTableReplication(ctx: MySqlParser.WildDoTableReplicationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitWildIgnoreTableReplication(ctx: MySqlParser.WildIgnoreTableReplicationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRewriteDbReplication(ctx: MySqlParser.RewriteDbReplicationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTablePair(ctx: MySqlParser.TablePairContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitThreadType(ctx: MySqlParser.ThreadTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitGtidsUntilOption(ctx: MySqlParser.GtidsUntilOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitMasterLogUntilOption(ctx: MySqlParser.MasterLogUntilOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRelayLogUntilOption(ctx: MySqlParser.RelayLogUntilOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSqlGapsUntilOption(ctx: MySqlParser.SqlGapsUntilOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUserConnectionOption(ctx: MySqlParser.UserConnectionOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPasswordConnectionOption(ctx: MySqlParser.PasswordConnectionOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDefaultAuthConnectionOption(ctx: MySqlParser.DefaultAuthConnectionOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPluginDirConnectionOption(ctx: MySqlParser.PluginDirConnectionOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitGtuidSet(ctx: MySqlParser.GtuidSetContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitXaStartTransaction(ctx: MySqlParser.XaStartTransactionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitXaEndTransaction(ctx: MySqlParser.XaEndTransactionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitXaPrepareStatement(ctx: MySqlParser.XaPrepareStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitXaCommitWork(ctx: MySqlParser.XaCommitWorkContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitXaRollbackWork(ctx: MySqlParser.XaRollbackWorkContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitXaRecoverWork(ctx: MySqlParser.XaRecoverWorkContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPrepareStatement(ctx: MySqlParser.PrepareStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitExecuteStatement(ctx: MySqlParser.ExecuteStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDeallocatePrepare(ctx: MySqlParser.DeallocatePrepareContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRoutineBody(ctx: MySqlParser.RoutineBodyContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitBlockStatement(ctx: MySqlParser.BlockStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCaseStatement(ctx: MySqlParser.CaseStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIfStatement(ctx: MySqlParser.IfStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIterateStatement(ctx: MySqlParser.IterateStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLeaveStatement(ctx: MySqlParser.LeaveStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLoopStatement(ctx: MySqlParser.LoopStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRepeatStatement(ctx: MySqlParser.RepeatStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitReturnStatement(ctx: MySqlParser.ReturnStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitWhileStatement(ctx: MySqlParser.WhileStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCloseCursor(ctx: MySqlParser.CloseCursorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFetchCursor(ctx: MySqlParser.FetchCursorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitOpenCursor(ctx: MySqlParser.OpenCursorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDeclareVariable(ctx: MySqlParser.DeclareVariableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDeclareCondition(ctx: MySqlParser.DeclareConditionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDeclareCursor(ctx: MySqlParser.DeclareCursorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDeclareHandler(ctx: MySqlParser.DeclareHandlerContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHandlerConditionCode(ctx: MySqlParser.HandlerConditionCodeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHandlerConditionState(ctx: MySqlParser.HandlerConditionStateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHandlerConditionName(ctx: MySqlParser.HandlerConditionNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHandlerConditionWarning(ctx: MySqlParser.HandlerConditionWarningContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHandlerConditionNotfound(ctx: MySqlParser.HandlerConditionNotfoundContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHandlerConditionException(ctx: MySqlParser.HandlerConditionExceptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitProcedureSqlStatement(ctx: MySqlParser.ProcedureSqlStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCaseAlternative(ctx: MySqlParser.CaseAlternativeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitElifAlternative(ctx: MySqlParser.ElifAlternativeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterUserMysqlV56(ctx: MySqlParser.AlterUserMysqlV56Context?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAlterUserMysqlV80(ctx: MySqlParser.AlterUserMysqlV80Context?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateUserMysqlV56(ctx: MySqlParser.CreateUserMysqlV56Context?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateUserMysqlV80(ctx: MySqlParser.CreateUserMysqlV80Context?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDropUser(ctx: MySqlParser.DropUserContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitGrantStatement(ctx: MySqlParser.GrantStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRoleOption(ctx: MySqlParser.RoleOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitGrantProxy(ctx: MySqlParser.GrantProxyContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRenameUser(ctx: MySqlParser.RenameUserContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDetailRevoke(ctx: MySqlParser.DetailRevokeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShortRevoke(ctx: MySqlParser.ShortRevokeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRoleRevoke(ctx: MySqlParser.RoleRevokeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRevokeProxy(ctx: MySqlParser.RevokeProxyContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSetPasswordStatement(ctx: MySqlParser.SetPasswordStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUserSpecification(ctx: MySqlParser.UserSpecificationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHashAuthOption(ctx: MySqlParser.HashAuthOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRandomAuthOption(ctx: MySqlParser.RandomAuthOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitStringAuthOption(ctx: MySqlParser.StringAuthOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitModuleAuthOption(ctx: MySqlParser.ModuleAuthOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSimpleAuthOption(ctx: MySqlParser.SimpleAuthOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAuthOptionClause(ctx: MySqlParser.AuthOptionClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitModule(ctx: MySqlParser.ModuleContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPasswordModuleOption(ctx: MySqlParser.PasswordModuleOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTlsOption(ctx: MySqlParser.TlsOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUserResourceOption(ctx: MySqlParser.UserResourceOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUserPasswordOption(ctx: MySqlParser.UserPasswordOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUserLockOption(ctx: MySqlParser.UserLockOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPrivelegeClause(ctx: MySqlParser.PrivelegeClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPrivilege(ctx: MySqlParser.PrivilegeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCurrentSchemaPriviLevel(ctx: MySqlParser.CurrentSchemaPriviLevelContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitGlobalPrivLevel(ctx: MySqlParser.GlobalPrivLevelContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDefiniteSchemaPrivLevel(ctx: MySqlParser.DefiniteSchemaPrivLevelContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDefiniteFullTablePrivLevel(ctx: MySqlParser.DefiniteFullTablePrivLevelContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDefiniteFullTablePrivLevel2(ctx: MySqlParser.DefiniteFullTablePrivLevel2Context?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDefiniteTablePrivLevel(ctx: MySqlParser.DefiniteTablePrivLevelContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRenameUserClause(ctx: MySqlParser.RenameUserClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAnalyzeTable(ctx: MySqlParser.AnalyzeTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCheckTable(ctx: MySqlParser.CheckTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitChecksumTable(ctx: MySqlParser.ChecksumTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitOptimizeTable(ctx: MySqlParser.OptimizeTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRepairTable(ctx: MySqlParser.RepairTableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCheckTableOption(ctx: MySqlParser.CheckTableOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCreateUdfunction(ctx: MySqlParser.CreateUdfunctionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitInstallPlugin(ctx: MySqlParser.InstallPluginContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUninstallPlugin(ctx: MySqlParser.UninstallPluginContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSetVariable(ctx: MySqlParser.SetVariableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSetCharset(ctx: MySqlParser.SetCharsetContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSetNames(ctx: MySqlParser.SetNamesContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSetPassword(ctx: MySqlParser.SetPasswordContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSetTransaction(ctx: MySqlParser.SetTransactionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSetAutocommit(ctx: MySqlParser.SetAutocommitContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSetNewValueInsideTrigger(ctx: MySqlParser.SetNewValueInsideTriggerContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowMasterLogs(ctx: MySqlParser.ShowMasterLogsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowLogEvents(ctx: MySqlParser.ShowLogEventsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowObjectFilter(ctx: MySqlParser.ShowObjectFilterContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowColumns(ctx: MySqlParser.ShowColumnsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowCreateDb(ctx: MySqlParser.ShowCreateDbContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowCreateFullIdObject(ctx: MySqlParser.ShowCreateFullIdObjectContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowCreateUser(ctx: MySqlParser.ShowCreateUserContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowEngine(ctx: MySqlParser.ShowEngineContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowGlobalInfo(ctx: MySqlParser.ShowGlobalInfoContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowErrors(ctx: MySqlParser.ShowErrorsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowCountErrors(ctx: MySqlParser.ShowCountErrorsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowSchemaFilter(ctx: MySqlParser.ShowSchemaFilterContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowRoutine(ctx: MySqlParser.ShowRoutineContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowGrants(ctx: MySqlParser.ShowGrantsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowIndexes(ctx: MySqlParser.ShowIndexesContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowOpenTables(ctx: MySqlParser.ShowOpenTablesContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowProfile(ctx: MySqlParser.ShowProfileContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowSlaveStatus(ctx: MySqlParser.ShowSlaveStatusContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitVariableClause(ctx: MySqlParser.VariableClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowCommonEntity(ctx: MySqlParser.ShowCommonEntityContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowFilter(ctx: MySqlParser.ShowFilterContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowGlobalInfoClause(ctx: MySqlParser.ShowGlobalInfoClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowSchemaEntity(ctx: MySqlParser.ShowSchemaEntityContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShowProfileType(ctx: MySqlParser.ShowProfileTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitBinlogStatement(ctx: MySqlParser.BinlogStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCacheIndexStatement(ctx: MySqlParser.CacheIndexStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFlushStatement(ctx: MySqlParser.FlushStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitKillStatement(ctx: MySqlParser.KillStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLoadIndexIntoCache(ctx: MySqlParser.LoadIndexIntoCacheContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitResetStatement(ctx: MySqlParser.ResetStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitShutdownStatement(ctx: MySqlParser.ShutdownStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableIndexes(ctx: MySqlParser.TableIndexesContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSimpleFlushOption(ctx: MySqlParser.SimpleFlushOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitChannelFlushOption(ctx: MySqlParser.ChannelFlushOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableFlushOption(ctx: MySqlParser.TableFlushOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFlushTableOption(ctx: MySqlParser.FlushTableOptionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLoadedTableIndexes(ctx: MySqlParser.LoadedTableIndexesContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSimpleDescribeStatement(ctx: MySqlParser.SimpleDescribeStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFullDescribeStatement(ctx: MySqlParser.FullDescribeStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHelpStatement(ctx: MySqlParser.HelpStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUseStatement(ctx: MySqlParser.UseStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSignalStatement(ctx: MySqlParser.SignalStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitResignalStatement(ctx: MySqlParser.ResignalStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSignalConditionInformation(ctx: MySqlParser.SignalConditionInformationContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitWithStatement(ctx: MySqlParser.WithStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableStatement(ctx: MySqlParser.TableStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDiagnosticsStatement(ctx: MySqlParser.DiagnosticsStatementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDiagnosticsConditionInformationName(ctx: MySqlParser.DiagnosticsConditionInformationNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDescribeStatements(ctx: MySqlParser.DescribeStatementsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDescribeConnection(ctx: MySqlParser.DescribeConnectionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFullId(ctx: MySqlParser.FullIdContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTableName(ctx: MySqlParser.TableNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRoleName(ctx: MySqlParser.RoleNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFullColumnName(ctx: MySqlParser.FullColumnNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIndexColumnName(ctx: MySqlParser.IndexColumnNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSimpleUserName(ctx: MySqlParser.SimpleUserNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHostName(ctx: MySqlParser.HostNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUserName(ctx: MySqlParser.UserNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitMysqlVariable(ctx: MySqlParser.MysqlVariableContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCharsetName(ctx: MySqlParser.CharsetNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCollationName(ctx: MySqlParser.CollationNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitEngineName(ctx: MySqlParser.EngineNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitEngineNameBase(ctx: MySqlParser.EngineNameBaseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUuidSet(ctx: MySqlParser.UuidSetContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitXid(ctx: MySqlParser.XidContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitXuidStringId(ctx: MySqlParser.XuidStringIdContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAuthPlugin(ctx: MySqlParser.AuthPluginContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUid(ctx: MySqlParser.UidContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSimpleId(ctx: MySqlParser.SimpleIdContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDottedId(ctx: MySqlParser.DottedIdContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDecimalLiteral(ctx: MySqlParser.DecimalLiteralContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFileSizeLiteral(ctx: MySqlParser.FileSizeLiteralContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitStringLiteral(ctx: MySqlParser.StringLiteralContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitBooleanLiteral(ctx: MySqlParser.BooleanLiteralContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitHexadecimalLiteral(ctx: MySqlParser.HexadecimalLiteralContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitNullNotnull(ctx: MySqlParser.NullNotnullContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitConstant(ctx: MySqlParser.ConstantContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitStringDataType(ctx: MySqlParser.StringDataTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitNationalVaryingStringDataType(ctx: MySqlParser.NationalVaryingStringDataTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitNationalStringDataType(ctx: MySqlParser.NationalStringDataTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDimensionDataType(ctx: MySqlParser.DimensionDataTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSimpleDataType(ctx: MySqlParser.SimpleDataTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCollectionDataType(ctx: MySqlParser.CollectionDataTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSpatialDataType(ctx: MySqlParser.SpatialDataTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLongVarcharDataType(ctx: MySqlParser.LongVarcharDataTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLongVarbinaryDataType(ctx: MySqlParser.LongVarbinaryDataTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCollectionOptions(ctx: MySqlParser.CollectionOptionsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitConvertedDataType(ctx: MySqlParser.ConvertedDataTypeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLengthOneDimension(ctx: MySqlParser.LengthOneDimensionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLengthTwoDimension(ctx: MySqlParser.LengthTwoDimensionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLengthTwoOptionalDimension(ctx: MySqlParser.LengthTwoOptionalDimensionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUidList(ctx: MySqlParser.UidListContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFullColumnNameList(ctx: MySqlParser.FullColumnNameListContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTables(ctx: MySqlParser.TablesContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIndexColumnNames(ctx: MySqlParser.IndexColumnNamesContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitExpressions(ctx: MySqlParser.ExpressionsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitExpressionsWithDefaults(ctx: MySqlParser.ExpressionsWithDefaultsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitConstants(ctx: MySqlParser.ConstantsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSimpleStrings(ctx: MySqlParser.SimpleStringsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUserVariables(ctx: MySqlParser.UserVariablesContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDefaultValue(ctx: MySqlParser.DefaultValueContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCurrentTimestamp(ctx: MySqlParser.CurrentTimestampContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitExpressionOrDefault(ctx: MySqlParser.ExpressionOrDefaultContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIfExists(ctx: MySqlParser.IfExistsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIfNotExists(ctx: MySqlParser.IfNotExistsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitOrReplace(ctx: MySqlParser.OrReplaceContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitWaitNowaitClause(ctx: MySqlParser.WaitNowaitClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSpecificFunctionCall(ctx: MySqlParser.SpecificFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAggregateFunctionCall(ctx: MySqlParser.AggregateFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitNonAggregateFunctionCall(ctx: MySqlParser.NonAggregateFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitScalarFunctionCall(ctx: MySqlParser.ScalarFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUdfFunctionCall(ctx: MySqlParser.UdfFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPasswordFunctionCall(ctx: MySqlParser.PasswordFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSimpleFunctionCall(ctx: MySqlParser.SimpleFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCurrentUser(ctx: MySqlParser.CurrentUserContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDataTypeFunctionCall(ctx: MySqlParser.DataTypeFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitValuesFunctionCall(ctx: MySqlParser.ValuesFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCaseExpressionFunctionCall(ctx: MySqlParser.CaseExpressionFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCaseFunctionCall(ctx: MySqlParser.CaseFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCharFunctionCall(ctx: MySqlParser.CharFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPositionFunctionCall(ctx: MySqlParser.PositionFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSubstrFunctionCall(ctx: MySqlParser.SubstrFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTrimFunctionCall(ctx: MySqlParser.TrimFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitWeightFunctionCall(ctx: MySqlParser.WeightFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitExtractFunctionCall(ctx: MySqlParser.ExtractFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitGetFormatFunctionCall(ctx: MySqlParser.GetFormatFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitJsonValueFunctionCall(ctx: MySqlParser.JsonValueFunctionCallContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCaseFuncAlternative(ctx: MySqlParser.CaseFuncAlternativeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLevelWeightList(ctx: MySqlParser.LevelWeightListContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLevelWeightRange(ctx: MySqlParser.LevelWeightRangeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLevelInWeightListElement(ctx: MySqlParser.LevelInWeightListElementContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAggregateWindowedFunction(ctx: MySqlParser.AggregateWindowedFunctionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitNonAggregateWindowedFunction(ctx: MySqlParser.NonAggregateWindowedFunctionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitOverClause(ctx: MySqlParser.OverClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitWindowSpec(ctx: MySqlParser.WindowSpecContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitWindowName(ctx: MySqlParser.WindowNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFrameClause(ctx: MySqlParser.FrameClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFrameUnits(ctx: MySqlParser.FrameUnitsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFrameExtent(ctx: MySqlParser.FrameExtentContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFrameBetween(ctx: MySqlParser.FrameBetweenContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFrameRange(ctx: MySqlParser.FrameRangeContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPartitionClause(ctx: MySqlParser.PartitionClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitScalarFunctionName(ctx: MySqlParser.ScalarFunctionNameContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPasswordFunctionClause(ctx: MySqlParser.PasswordFunctionClauseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFunctionArgs(ctx: MySqlParser.FunctionArgsContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFunctionArg(ctx: MySqlParser.FunctionArgContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIsExpression(ctx: MySqlParser.IsExpressionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitNotExpression(ctx: MySqlParser.NotExpressionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLogicalExpression(ctx: MySqlParser.LogicalExpressionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPredicateExpression(ctx: MySqlParser.PredicateExpressionContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSoundsLikePredicate(ctx: MySqlParser.SoundsLikePredicateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitExpressionAtomPredicate(ctx: MySqlParser.ExpressionAtomPredicateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSubqueryComparisonPredicate(ctx: MySqlParser.SubqueryComparisonPredicateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitJsonMemberOfPredicate(ctx: MySqlParser.JsonMemberOfPredicateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitBinaryComparisonPredicate(ctx: MySqlParser.BinaryComparisonPredicateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitInPredicate(ctx: MySqlParser.InPredicateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitBetweenPredicate(ctx: MySqlParser.BetweenPredicateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIsNullPredicate(ctx: MySqlParser.IsNullPredicateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLikePredicate(ctx: MySqlParser.LikePredicateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitRegexpPredicate(ctx: MySqlParser.RegexpPredicateContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUnaryExpressionAtom(ctx: MySqlParser.UnaryExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCollateExpressionAtom(ctx: MySqlParser.CollateExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitVariableAssignExpressionAtom(ctx: MySqlParser.VariableAssignExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitMysqlVariableExpressionAtom(ctx: MySqlParser.MysqlVariableExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitNestedExpressionAtom(ctx: MySqlParser.NestedExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitNestedRowExpressionAtom(ctx: MySqlParser.NestedRowExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitMathExpressionAtom(ctx: MySqlParser.MathExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitExistsExpressionAtom(ctx: MySqlParser.ExistsExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIntervalExpressionAtom(ctx: MySqlParser.IntervalExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitJsonExpressionAtom(ctx: MySqlParser.JsonExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitSubqueryExpressionAtom(ctx: MySqlParser.SubqueryExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitConstantExpressionAtom(ctx: MySqlParser.ConstantExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFunctionCallExpressionAtom(ctx: MySqlParser.FunctionCallExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitBinaryExpressionAtom(ctx: MySqlParser.BinaryExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFullColumnNameExpressionAtom(ctx: MySqlParser.FullColumnNameExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitBitExpressionAtom(ctx: MySqlParser.BitExpressionAtomContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitUnaryOperator(ctx: MySqlParser.UnaryOperatorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitComparisonOperator(ctx: MySqlParser.ComparisonOperatorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitLogicalOperator(ctx: MySqlParser.LogicalOperatorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitBitOperator(ctx: MySqlParser.BitOperatorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitMultOperator(ctx: MySqlParser.MultOperatorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitAddOperator(ctx: MySqlParser.AddOperatorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitJsonOperator(ctx: MySqlParser.JsonOperatorContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitCharsetNameBase(ctx: MySqlParser.CharsetNameBaseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitTransactionLevelBase(ctx: MySqlParser.TransactionLevelBaseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitPrivilegesBase(ctx: MySqlParser.PrivilegesBaseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitIntervalTypeBase(ctx: MySqlParser.IntervalTypeBaseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitDataTypeBase(ctx: MySqlParser.DataTypeBaseContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitKeywordsCanBeId(ctx: MySqlParser.KeywordsCanBeIdContext?): Node {
        TODO("Not yet implemented")
    }

    override fun visitFunctionNameBase(ctx: MySqlParser.FunctionNameBaseContext?): Node {
        TODO("Not yet implemented")
    }
}
