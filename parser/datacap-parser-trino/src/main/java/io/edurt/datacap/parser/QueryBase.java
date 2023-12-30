package io.edurt.datacap.parser;

import com.google.common.collect.ImmutableList;
import io.edurt.datacap.parser.node.Node;
import io.edurt.datacap.parser.tree.AliasedRelation;
import io.edurt.datacap.parser.tree.AllColumns;
import io.edurt.datacap.parser.tree.BooleanLiteral;
import io.edurt.datacap.parser.tree.CoalesceExpression;
import io.edurt.datacap.parser.tree.ComparisonExpression;
import io.edurt.datacap.parser.tree.Expression;
import io.edurt.datacap.parser.tree.FunctionCall;
import io.edurt.datacap.parser.tree.GroupBy;
import io.edurt.datacap.parser.tree.Identifier;
import io.edurt.datacap.parser.tree.LogicalBinaryExpression;
import io.edurt.datacap.parser.tree.Offset;
import io.edurt.datacap.parser.tree.OrderBy;
import io.edurt.datacap.parser.tree.QualifiedName;
import io.edurt.datacap.parser.tree.Relation;
import io.edurt.datacap.parser.tree.Row;
import io.edurt.datacap.parser.tree.SearchedCaseExpression;
import io.edurt.datacap.parser.tree.Select;
import io.edurt.datacap.parser.tree.SelectItem;
import io.edurt.datacap.parser.tree.SingleColumn;
import io.edurt.datacap.parser.tree.SortItem;
import io.edurt.datacap.parser.tree.StringLiteral;
import io.edurt.datacap.parser.tree.Values;
import io.edurt.datacap.parser.tree.WhenClause;
import io.edurt.datacap.parser.tree.query.Query;
import io.edurt.datacap.parser.tree.query.QueryBody;
import io.edurt.datacap.parser.tree.query.QuerySpecification;
import io.edurt.datacap.parser.tree.table.Table;
import io.edurt.datacap.parser.tree.table.TableSubQuery;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public final class QueryBase
{
    private QueryBase()
    {
    }

    public static Identifier identifier(String name)
    {
        return new Identifier(name);
    }

    public static Identifier quotedIdentifier(String name)
    {
        return new Identifier(name, true);
    }

    public static SelectItem unaliasedName(String name)
    {
        return new SingleColumn(identifier(name));
    }

    public static SelectItem aliasedName(String name, String alias)
    {
        return new SingleColumn(identifier(name), identifier(alias));
    }

    public static Select selectList(Expression... expressions)
    {
        return selectList(asList(expressions));
    }

    public static Select selectList(List<Expression> expressions)
    {
        ImmutableList.Builder<SelectItem> items = ImmutableList.builder();
        for (Expression expression : expressions) {
            items.add(new SingleColumn(expression));
        }
        return new Select(false, items.build());
    }

    public static Select selectList(SelectItem... items)
    {
        return new Select(false, ImmutableList.copyOf(items));
    }

    public static Select selectAll(List<SelectItem> items)
    {
        return new Select(false, items);
    }

    public static Table table(QualifiedName name)
    {
        return new Table(name);
    }

    public static Relation subquery(Query queryBase)
    {
        return new TableSubQuery(queryBase);
    }

    public static SortItem ascending(String name)
    {
        return new SortItem(identifier(name), SortItem.Ordering.ASCENDING, SortItem.NullOrdering.UNDEFINED);
    }

    public static Expression logicalAnd(Expression left, Expression right)
    {
        return new LogicalBinaryExpression(LogicalBinaryExpression.Operator.AND, left, right);
    }

    public static Expression equal(Expression left, Expression right)
    {
        return new ComparisonExpression(ComparisonExpression.Operator.EQUAL, left, right);
    }

    public static Expression caseWhen(Expression operand, Expression result)
    {
        return new SearchedCaseExpression(ImmutableList.of(new WhenClause(operand, result)), Optional.empty());
    }

    public static Expression functionCall(String name, Expression... arguments)
    {
        return new FunctionCall(QualifiedName.of(name), ImmutableList.copyOf(arguments));
    }

    public static Values values(Row... row)
    {
        return new Values(ImmutableList.copyOf(row));
    }

    public static Row row(Expression... values)
    {
        return new Row(ImmutableList.copyOf(values));
    }

    public static Relation aliased(Relation relation, String alias, List<String> columnAliases)
    {
        return new AliasedRelation(
                relation,
                identifier(alias),
                columnAliases.stream()
                        .map(QueryBase::identifier)
                        .collect(Collectors.toList()));
    }

    public static SelectItem aliasedNullToEmpty(String column, String alias)
    {
        return new SingleColumn(new CoalesceExpression(identifier(column), new StringLiteral("")), identifier(alias));
    }

    public static OrderBy ordering(SortItem... items)
    {
        return new OrderBy(ImmutableList.copyOf(items));
    }

    public static Query simpleQuery(Select select)
    {
        return query(new QuerySpecification(
                select,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()));
    }

    public static Query simpleQuery(Select select, Relation from)
    {
        return simpleQuery(select, from, Optional.empty(), Optional.empty());
    }

    public static Query simpleQuery(Select select, Relation from, OrderBy orderBy)
    {
        return simpleQuery(select, from, Optional.empty(), Optional.of(orderBy));
    }

    public static Query simpleQuery(Select select, Relation from, Expression where)
    {
        return simpleQuery(select, from, Optional.of(where), Optional.empty());
    }

    public static Query simpleQuery(Select select, Relation from, Expression where, OrderBy orderBy)
    {
        return simpleQuery(select, from, Optional.of(where), Optional.of(orderBy));
    }

    public static Query simpleQuery(Select select, Relation from, Optional<Expression> where, Optional<OrderBy> orderBy)
    {
        return simpleQuery(select, from, where, Optional.empty(), Optional.empty(), orderBy, Optional.empty(), Optional.empty());
    }

    public static Query simpleQuery(
            Select select,
            Relation from,
            Optional<Expression> where,
            Optional<GroupBy> groupBy,
            Optional<Expression> having,
            Optional<OrderBy> orderBy,
            Optional<Offset> offset,
            Optional<Node> limit)
    {
        return query(new QuerySpecification(
                select,
                Optional.of(from),
                where,
                groupBy,
                having,
                orderBy,
                offset,
                limit));
    }

    public static Query singleValueQuery(String columnName, String value)
    {
        Relation values = values(row(new StringLiteral((value))));
        return simpleQuery(
                selectList(new AllColumns()),
                aliased(values, "t", ImmutableList.of(columnName)));
    }

    public static Query singleValueQuery(String columnName, boolean value)
    {
        Relation values = values(row(value ? BooleanLiteral.TRUE_LITERAL : BooleanLiteral.FALSE_LITERAL));
        return simpleQuery(
                selectList(new AllColumns()),
                aliased(values, "t", ImmutableList.of(columnName)));
    }

    public static Query query(QueryBody body)
    {
        return new Query(
                Optional.empty(),
                body,
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
}
