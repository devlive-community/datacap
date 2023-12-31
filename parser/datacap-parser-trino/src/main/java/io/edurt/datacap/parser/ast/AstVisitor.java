package io.edurt.datacap.parser.ast;

import io.edurt.datacap.parser.node.Node;
import io.edurt.datacap.parser.tree.AddColumn;
import io.edurt.datacap.parser.tree.AliasedRelation;
import io.edurt.datacap.parser.tree.AllColumns;
import io.edurt.datacap.parser.tree.AllRows;
import io.edurt.datacap.parser.tree.Analyze;
import io.edurt.datacap.parser.tree.ArithmeticBinaryExpression;
import io.edurt.datacap.parser.tree.ArithmeticUnaryExpression;
import io.edurt.datacap.parser.tree.ArrayConstructor;
import io.edurt.datacap.parser.tree.AtTimeZone;
import io.edurt.datacap.parser.tree.BetweenPredicate;
import io.edurt.datacap.parser.tree.BinaryLiteral;
import io.edurt.datacap.parser.tree.BindExpression;
import io.edurt.datacap.parser.tree.BooleanLiteral;
import io.edurt.datacap.parser.tree.Call;
import io.edurt.datacap.parser.tree.CallArgument;
import io.edurt.datacap.parser.tree.Cast;
import io.edurt.datacap.parser.tree.CharLiteral;
import io.edurt.datacap.parser.tree.CoalesceExpression;
import io.edurt.datacap.parser.tree.ColumnDefinition;
import io.edurt.datacap.parser.tree.Comment;
import io.edurt.datacap.parser.tree.Commit;
import io.edurt.datacap.parser.tree.ComparisonExpression;
import io.edurt.datacap.parser.tree.CreateRole;
import io.edurt.datacap.parser.tree.CreateSchema;
import io.edurt.datacap.parser.tree.CreateTable;
import io.edurt.datacap.parser.tree.CreateTableAsSelect;
import io.edurt.datacap.parser.tree.CreateView;
import io.edurt.datacap.parser.tree.Cube;
import io.edurt.datacap.parser.tree.CurrentPath;
import io.edurt.datacap.parser.tree.CurrentTime;
import io.edurt.datacap.parser.tree.CurrentUser;
import io.edurt.datacap.parser.tree.DataType;
import io.edurt.datacap.parser.tree.DataTypeParameter;
import io.edurt.datacap.parser.tree.DateTimeDataType;
import io.edurt.datacap.parser.tree.Deallocate;
import io.edurt.datacap.parser.tree.DecimalLiteral;
import io.edurt.datacap.parser.tree.Delete;
import io.edurt.datacap.parser.tree.DereferenceExpression;
import io.edurt.datacap.parser.tree.DescribeInput;
import io.edurt.datacap.parser.tree.DescribeOutput;
import io.edurt.datacap.parser.tree.DoubleLiteral;
import io.edurt.datacap.parser.tree.DropColumn;
import io.edurt.datacap.parser.tree.DropRole;
import io.edurt.datacap.parser.tree.DropSchema;
import io.edurt.datacap.parser.tree.DropTable;
import io.edurt.datacap.parser.tree.DropView;
import io.edurt.datacap.parser.tree.Except;
import io.edurt.datacap.parser.tree.Execute;
import io.edurt.datacap.parser.tree.ExistsPredicate;
import io.edurt.datacap.parser.tree.Explain;
import io.edurt.datacap.parser.tree.ExplainOption;
import io.edurt.datacap.parser.tree.Expression;
import io.edurt.datacap.parser.tree.Extract;
import io.edurt.datacap.parser.tree.FetchFirst;
import io.edurt.datacap.parser.tree.FieldReference;
import io.edurt.datacap.parser.tree.Format;
import io.edurt.datacap.parser.tree.FrameBound;
import io.edurt.datacap.parser.tree.FunctionCall;
import io.edurt.datacap.parser.tree.GenericDataType;
import io.edurt.datacap.parser.tree.GenericLiteral;
import io.edurt.datacap.parser.tree.Grant;
import io.edurt.datacap.parser.tree.GrantRoles;
import io.edurt.datacap.parser.tree.GroupBy;
import io.edurt.datacap.parser.tree.GroupingElement;
import io.edurt.datacap.parser.tree.GroupingOperation;
import io.edurt.datacap.parser.tree.GroupingSets;
import io.edurt.datacap.parser.tree.Identifier;
import io.edurt.datacap.parser.tree.IfExpression;
import io.edurt.datacap.parser.tree.InListExpression;
import io.edurt.datacap.parser.tree.InPredicate;
import io.edurt.datacap.parser.tree.Insert;
import io.edurt.datacap.parser.tree.Intersect;
import io.edurt.datacap.parser.tree.IntervalDayTimeDataType;
import io.edurt.datacap.parser.tree.IntervalLiteral;
import io.edurt.datacap.parser.tree.IsNotNullPredicate;
import io.edurt.datacap.parser.tree.IsNullPredicate;
import io.edurt.datacap.parser.tree.Isolation;
import io.edurt.datacap.parser.tree.LambdaArgumentDeclaration;
import io.edurt.datacap.parser.tree.LambdaExpression;
import io.edurt.datacap.parser.tree.Lateral;
import io.edurt.datacap.parser.tree.LikeClause;
import io.edurt.datacap.parser.tree.LikePredicate;
import io.edurt.datacap.parser.tree.Limit;
import io.edurt.datacap.parser.tree.Literal;
import io.edurt.datacap.parser.tree.LogicalBinaryExpression;
import io.edurt.datacap.parser.tree.LongLiteral;
import io.edurt.datacap.parser.tree.NotExpression;
import io.edurt.datacap.parser.tree.NullIfExpression;
import io.edurt.datacap.parser.tree.NullLiteral;
import io.edurt.datacap.parser.tree.NumericParameter;
import io.edurt.datacap.parser.tree.Offset;
import io.edurt.datacap.parser.tree.OrderBy;
import io.edurt.datacap.parser.tree.Parameter;
import io.edurt.datacap.parser.tree.PathElement;
import io.edurt.datacap.parser.tree.PathSpecification;
import io.edurt.datacap.parser.tree.Prepare;
import io.edurt.datacap.parser.tree.Property;
import io.edurt.datacap.parser.tree.QuantifiedComparisonExpression;
import io.edurt.datacap.parser.tree.Relation;
import io.edurt.datacap.parser.tree.RenameColumn;
import io.edurt.datacap.parser.tree.RenameSchema;
import io.edurt.datacap.parser.tree.RenameTable;
import io.edurt.datacap.parser.tree.RenameView;
import io.edurt.datacap.parser.tree.ResetSession;
import io.edurt.datacap.parser.tree.Revoke;
import io.edurt.datacap.parser.tree.RevokeRoles;
import io.edurt.datacap.parser.tree.Rollback;
import io.edurt.datacap.parser.tree.Rollup;
import io.edurt.datacap.parser.tree.Row;
import io.edurt.datacap.parser.tree.RowDataType;
import io.edurt.datacap.parser.tree.SampledRelation;
import io.edurt.datacap.parser.tree.SearchedCaseExpression;
import io.edurt.datacap.parser.tree.Select;
import io.edurt.datacap.parser.tree.SelectItem;
import io.edurt.datacap.parser.tree.SetOperation;
import io.edurt.datacap.parser.tree.SetPath;
import io.edurt.datacap.parser.tree.SetRole;
import io.edurt.datacap.parser.tree.SetSchemaAuthorization;
import io.edurt.datacap.parser.tree.SetSession;
import io.edurt.datacap.parser.tree.ShowCatalogs;
import io.edurt.datacap.parser.tree.ShowColumns;
import io.edurt.datacap.parser.tree.ShowCreate;
import io.edurt.datacap.parser.tree.ShowFunctions;
import io.edurt.datacap.parser.tree.ShowGrants;
import io.edurt.datacap.parser.tree.ShowRoleGrants;
import io.edurt.datacap.parser.tree.ShowRoles;
import io.edurt.datacap.parser.tree.ShowSchemas;
import io.edurt.datacap.parser.tree.ShowSession;
import io.edurt.datacap.parser.tree.ShowStats;
import io.edurt.datacap.parser.tree.ShowTables;
import io.edurt.datacap.parser.tree.SimpleCaseExpression;
import io.edurt.datacap.parser.tree.SimpleGroupBy;
import io.edurt.datacap.parser.tree.SingleColumn;
import io.edurt.datacap.parser.tree.SortItem;
import io.edurt.datacap.parser.tree.StartTransaction;
import io.edurt.datacap.parser.tree.Statement;
import io.edurt.datacap.parser.tree.StringLiteral;
import io.edurt.datacap.parser.tree.SubqueryExpression;
import io.edurt.datacap.parser.tree.SubscriptExpression;
import io.edurt.datacap.parser.tree.SymbolReference;
import io.edurt.datacap.parser.tree.TimeLiteral;
import io.edurt.datacap.parser.tree.TimestampLiteral;
import io.edurt.datacap.parser.tree.TransactionAccessMode;
import io.edurt.datacap.parser.tree.TransactionMode;
import io.edurt.datacap.parser.tree.TryExpression;
import io.edurt.datacap.parser.tree.TypeParameter;
import io.edurt.datacap.parser.tree.Union;
import io.edurt.datacap.parser.tree.Unnest;
import io.edurt.datacap.parser.tree.Use;
import io.edurt.datacap.parser.tree.Values;
import io.edurt.datacap.parser.tree.WhenClause;
import io.edurt.datacap.parser.tree.Window;
import io.edurt.datacap.parser.tree.WindowFrame;
import io.edurt.datacap.parser.tree.With;
import io.edurt.datacap.parser.tree.WithQuery;
import io.edurt.datacap.parser.tree.join.Join;
import io.edurt.datacap.parser.tree.query.Query;
import io.edurt.datacap.parser.tree.query.QueryBody;
import io.edurt.datacap.parser.tree.query.QuerySpecification;
import io.edurt.datacap.parser.tree.table.Table;
import io.edurt.datacap.parser.tree.table.TableElement;
import io.edurt.datacap.parser.tree.table.TableSubQuery;

import javax.annotation.Nullable;

public abstract class AstVisitor<R, C>
{
    public R process(Node node)
    {
        return process(node, null);
    }

    public R process(Node node, @Nullable C context)
    {
        return node.accept(this, context);
    }

    public R visitNode(Node node, C context)
    {
        return null;
    }

    public R visitExpression(Expression node, C context)
    {
        return visitNode(node, context);
    }

    public R visitCurrentTime(CurrentTime node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitExtract(Extract node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitArithmeticBinary(ArithmeticBinaryExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitBetweenPredicate(BetweenPredicate node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitCoalesceExpression(CoalesceExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitComparisonExpression(ComparisonExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitLiteral(Literal node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitDoubleLiteral(DoubleLiteral node, C context)
    {
        return visitLiteral(node, context);
    }

    public R visitDecimalLiteral(DecimalLiteral node, C context)
    {
        return visitLiteral(node, context);
    }

    public R visitStatement(Statement node, C context)
    {
        return visitNode(node, context);
    }

    public R visitPrepare(Prepare node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitDeallocate(Deallocate node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitExecute(Execute node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitDescribeOutput(DescribeOutput node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitDescribeInput(DescribeInput node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitQuery(Query node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitExplain(Explain node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitShowTables(ShowTables node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitShowSchemas(ShowSchemas node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitShowCatalogs(ShowCatalogs node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitShowColumns(ShowColumns node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitShowStats(ShowStats node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitShowCreate(ShowCreate node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitShowFunctions(ShowFunctions node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitUse(Use node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitShowSession(ShowSession node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitSetSession(SetSession node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitResetSession(ResetSession node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitGenericLiteral(GenericLiteral node, C context)
    {
        return visitLiteral(node, context);
    }

    public R visitTimeLiteral(TimeLiteral node, C context)
    {
        return visitLiteral(node, context);
    }

    public R visitExplainOption(ExplainOption node, C context)
    {
        return visitNode(node, context);
    }

    public R visitWith(With node, C context)
    {
        return visitNode(node, context);
    }

    public R visitWithQuery(WithQuery node, C context)
    {
        return visitNode(node, context);
    }

    public R visitSelect(Select node, C context)
    {
        return visitNode(node, context);
    }

    public R visitRelation(Relation node, C context)
    {
        return visitNode(node, context);
    }

    public R visitQueryBody(QueryBody node, C context)
    {
        return visitRelation(node, context);
    }

    public R visitOrderBy(OrderBy node, C context)
    {
        return visitNode(node, context);
    }

    public R visitOffset(Offset node, C context)
    {
        return visitNode(node, context);
    }

    public R visitFetchFirst(FetchFirst node, C context)
    {
        return visitNode(node, context);
    }

    public R visitLimit(Limit node, C context)
    {
        return visitNode(node, context);
    }

    public R visitAllRows(AllRows node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitQuerySpecification(QuerySpecification node, C context)
    {
        return visitQueryBody(node, context);
    }

    public R visitSetOperation(SetOperation node, C context)
    {
        return visitQueryBody(node, context);
    }

    public R visitUnion(Union node, C context)
    {
        return visitSetOperation(node, context);
    }

    public R visitIntersect(Intersect node, C context)
    {
        return visitSetOperation(node, context);
    }

    public R visitExcept(Except node, C context)
    {
        return visitSetOperation(node, context);
    }

    public R visitTimestampLiteral(TimestampLiteral node, C context)
    {
        return visitLiteral(node, context);
    }

    public R visitWhenClause(WhenClause node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitIntervalLiteral(IntervalLiteral node, C context)
    {
        return visitLiteral(node, context);
    }

    public R visitInPredicate(InPredicate node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitFunctionCall(FunctionCall node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitLambdaExpression(LambdaExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitSimpleCaseExpression(SimpleCaseExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitStringLiteral(StringLiteral node, C context)
    {
        return visitLiteral(node, context);
    }

    public R visitCharLiteral(CharLiteral node, C context)
    {
        return visitLiteral(node, context);
    }

    public R visitBinaryLiteral(BinaryLiteral node, C context)
    {
        return visitLiteral(node, context);
    }

    public R visitBooleanLiteral(BooleanLiteral node, C context)
    {
        return visitLiteral(node, context);
    }

    public R visitInListExpression(InListExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitIdentifier(Identifier node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitDereferenceExpression(DereferenceExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitNullIfExpression(NullIfExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitIfExpression(IfExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitNullLiteral(NullLiteral node, C context)
    {
        return visitLiteral(node, context);
    }

    public R visitArithmeticUnary(ArithmeticUnaryExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitNotExpression(NotExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitSelectItem(SelectItem node, C context)
    {
        return visitNode(node, context);
    }

    public R visitSingleColumn(SingleColumn node, C context)
    {
        return visitSelectItem(node, context);
    }

    public R visitAllColumns(AllColumns node, C context)
    {
        return visitSelectItem(node, context);
    }

    public R visitSearchedCaseExpression(SearchedCaseExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitLikePredicate(LikePredicate node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitIsNotNullPredicate(IsNotNullPredicate node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitIsNullPredicate(IsNullPredicate node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitArrayConstructor(ArrayConstructor node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitSubscriptExpression(SubscriptExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitLongLiteral(LongLiteral node, C context)
    {
        return visitLiteral(node, context);
    }

    public R visitParameter(Parameter node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitLogicalBinaryExpression(LogicalBinaryExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitSubqueryExpression(SubqueryExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitSortItem(SortItem node, C context)
    {
        return visitNode(node, context);
    }

    public R visitTable(Table node, C context)
    {
        return visitQueryBody(node, context);
    }

    public R visitUnnest(Unnest node, C context)
    {
        return visitRelation(node, context);
    }

    public R visitLateral(Lateral node, C context)
    {
        return visitRelation(node, context);
    }

    public R visitValues(Values node, C context)
    {
        return visitQueryBody(node, context);
    }

    public R visitRow(Row node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitTableSubquery(TableSubQuery node, C context)
    {
        return visitQueryBody(node, context);
    }

    public R visitAliasedRelation(AliasedRelation node, C context)
    {
        return visitRelation(node, context);
    }

    public R visitSampledRelation(SampledRelation node, C context)
    {
        return visitRelation(node, context);
    }

    public R visitJoin(Join node, C context)
    {
        return visitRelation(node, context);
    }

    public R visitExists(ExistsPredicate node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitTryExpression(TryExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitCast(Cast node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitFieldReference(FieldReference node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitWindow(Window node, C context)
    {
        return visitNode(node, context);
    }

    public R visitWindowFrame(WindowFrame node, C context)
    {
        return visitNode(node, context);
    }

    public R visitFrameBound(FrameBound node, C context)
    {
        return visitNode(node, context);
    }

    public R visitCallArgument(CallArgument node, C context)
    {
        return visitNode(node, context);
    }

    public R visitTableElement(TableElement node, C context)
    {
        return visitNode(node, context);
    }

    public R visitColumnDefinition(ColumnDefinition node, C context)
    {
        return visitTableElement(node, context);
    }

    public R visitLikeClause(LikeClause node, C context)
    {
        return visitTableElement(node, context);
    }

    public R visitCreateSchema(CreateSchema node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitDropSchema(DropSchema node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitRenameSchema(RenameSchema node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitSetSchemaAuthorization(SetSchemaAuthorization node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitCreateTable(CreateTable node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitCreateTableAsSelect(CreateTableAsSelect node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitProperty(Property node, C context)
    {
        return visitNode(node, context);
    }

    public R visitDropTable(DropTable node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitRenameTable(RenameTable node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitRenameView(RenameView node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitComment(Comment node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitRenameColumn(RenameColumn node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitDropColumn(DropColumn node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitAddColumn(AddColumn node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitAnalyze(Analyze node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitCreateView(CreateView node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitDropView(DropView node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitInsert(Insert node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitCall(Call node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitDelete(Delete node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitStartTransaction(StartTransaction node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitCreateRole(CreateRole node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitDropRole(DropRole node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitGrantRoles(GrantRoles node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitRevokeRoles(RevokeRoles node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitSetRole(SetRole node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitGrant(Grant node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitRevoke(Revoke node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitShowGrants(ShowGrants node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitShowRoles(ShowRoles node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitShowRoleGrants(ShowRoleGrants node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitSetPath(SetPath node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitPathSpecification(PathSpecification node, C context)
    {
        return visitNode(node, context);
    }

    public R visitPathElement(PathElement node, C context)
    {
        return visitNode(node, context);
    }

    public R visitTransactionMode(TransactionMode node, C context)
    {
        return visitNode(node, context);
    }

    public R visitIsolationLevel(Isolation node, C context)
    {
        return visitTransactionMode(node, context);
    }

    public R visitTransactionAccessMode(TransactionAccessMode node, C context)
    {
        return visitTransactionMode(node, context);
    }

    public R visitCommit(Commit node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitRollback(Rollback node, C context)
    {
        return visitStatement(node, context);
    }

    public R visitAtTimeZone(AtTimeZone node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitGroupBy(GroupBy node, C context)
    {
        return visitNode(node, context);
    }

    public R visitGroupingElement(GroupingElement node, C context)
    {
        return visitNode(node, context);
    }

    public R visitCube(Cube node, C context)
    {
        return visitGroupingElement(node, context);
    }

    public R visitGroupingSets(GroupingSets node, C context)
    {
        return visitGroupingElement(node, context);
    }

    public R visitRollup(Rollup node, C context)
    {
        return visitGroupingElement(node, context);
    }

    public R visitSimpleGroupBy(SimpleGroupBy node, C context)
    {
        return visitGroupingElement(node, context);
    }

    public R visitSymbolReference(SymbolReference node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitQuantifiedComparisonExpression(QuantifiedComparisonExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitLambdaArgumentDeclaration(LambdaArgumentDeclaration node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitBindExpression(BindExpression node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitGroupingOperation(GroupingOperation node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitCurrentUser(CurrentUser node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitCurrentPath(CurrentPath node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitFormat(Format node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitDataType(DataType node, C context)
    {
        return visitExpression(node, context);
    }

    public R visitRowDataType(RowDataType node, C context)
    {
        return visitDataType(node, context);
    }

    public R visitGenericDataType(GenericDataType node, C context)
    {
        return visitDataType(node, context);
    }

    public R visitRowField(RowDataType.Field node, C context)
    {
        return visitNode(node, context);
    }

    public R visitDataTypeParameter(DataTypeParameter node, C context)
    {
        return visitNode(node, context);
    }

    public R visitNumericTypeParameter(NumericParameter node, C context)
    {
        return visitDataTypeParameter(node, context);
    }

    public R visitTypeParameter(TypeParameter node, C context)
    {
        return visitDataTypeParameter(node, context);
    }

    public R visitIntervalDataType(IntervalDayTimeDataType node, C context)
    {
        return visitDataType(node, context);
    }

    public R visitDateTimeType(DateTimeDataType node, C context)
    {
        return visitDataType(node, context);
    }
}
