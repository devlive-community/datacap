package io.edurt.datacap.parser.tree.query;

import com.google.common.collect.ImmutableList;
import io.edurt.datacap.parser.ast.AstVisitor;
import io.edurt.datacap.parser.node.Node;
import io.edurt.datacap.parser.node.NodeLocation;
import io.edurt.datacap.parser.tree.Expression;
import io.edurt.datacap.parser.tree.FetchFirst;
import io.edurt.datacap.parser.tree.GroupBy;
import io.edurt.datacap.parser.tree.Limit;
import io.edurt.datacap.parser.tree.Offset;
import io.edurt.datacap.parser.tree.OrderBy;
import io.edurt.datacap.parser.tree.Relation;
import io.edurt.datacap.parser.tree.Select;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public class QuerySpecification
        extends QueryBody
{
    private final Select select;
    private final Optional<Relation> from;
    private final Optional<Expression> where;
    private final Optional<GroupBy> groupBy;
    private final Optional<Expression> having;
    private final Optional<OrderBy> orderBy;
    private final Optional<Offset> offset;
    private final Optional<Node> limit;

    public QuerySpecification(
            Select select,
            Optional<Relation> from,
            Optional<Expression> where,
            Optional<GroupBy> groupBy,
            Optional<Expression> having,
            Optional<OrderBy> orderBy,
            Optional<Offset> offset,
            Optional<Node> limit)
    {
        this(Optional.empty(), select, from, where, groupBy, having, orderBy, offset, limit);
    }

    public QuerySpecification(
            NodeLocation location,
            Select select,
            Optional<Relation> from,
            Optional<Expression> where,
            Optional<GroupBy> groupBy,
            Optional<Expression> having,
            Optional<OrderBy> orderBy,
            Optional<Offset> offset,
            Optional<Node> limit)
    {
        this(Optional.of(location), select, from, where, groupBy, having, orderBy, offset, limit);
    }

    private QuerySpecification(
            Optional<NodeLocation> location,
            Select select,
            Optional<Relation> from,
            Optional<Expression> where,
            Optional<GroupBy> groupBy,
            Optional<Expression> having,
            Optional<OrderBy> orderBy,
            Optional<Offset> offset,
            Optional<Node> limit)
    {
        super(location);
        requireNonNull(select, "select is null");
        requireNonNull(from, "from is null");
        requireNonNull(where, "where is null");
        requireNonNull(groupBy, "groupBy is null");
        requireNonNull(having, "having is null");
        requireNonNull(orderBy, "orderBy is null");
        requireNonNull(offset, "offset is null");
        requireNonNull(limit, "limit is null");
        checkArgument(
                !limit.isPresent()
                        || limit.get() instanceof FetchFirst
                        || limit.get() instanceof Limit,
                "limit must be optional of either FetchFirst or Limit type");
        this.select = select;
        this.from = from;
        this.where = where;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.offset = offset;
        this.limit = limit;
    }

    public Select getSelect()
    {
        return select;
    }

    public Optional<Relation> getFrom()
    {
        return from;
    }

    public Optional<Expression> getWhere()
    {
        return where;
    }

    public Optional<GroupBy> getGroupBy()
    {
        return groupBy;
    }

    public Optional<Expression> getHaving()
    {
        return having;
    }

    public Optional<OrderBy> getOrderBy()
    {
        return orderBy;
    }

    public Optional<Offset> getOffset()
    {
        return offset;
    }

    public Optional<Node> getLimit()
    {
        return limit;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context)
    {
        return visitor.visitQuerySpecification(this, context);
    }

    @Override
    public List<Node> getChildren()
    {
        ImmutableList.Builder<Node> nodes = ImmutableList.builder();
        nodes.add(select);
        from.ifPresent(nodes::add);
        where.ifPresent(nodes::add);
        groupBy.ifPresent(nodes::add);
        having.ifPresent(nodes::add);
        orderBy.ifPresent(nodes::add);
        offset.ifPresent(nodes::add);
        limit.ifPresent(nodes::add);
        return nodes.build();
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("select", select)
                .add("from", from)
                .add("where", where.orElse(null))
                .add("groupBy", groupBy)
                .add("having", having.orElse(null))
                .add("orderBy", orderBy)
                .add("offset", offset.orElse(null))
                .add("limit", limit.orElse(null))
                .toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        QuerySpecification o = (QuerySpecification) obj;
        return Objects.equals(select, o.select) &&
                Objects.equals(from, o.from) &&
                Objects.equals(where, o.where) &&
                Objects.equals(groupBy, o.groupBy) &&
                Objects.equals(having, o.having) &&
                Objects.equals(orderBy, o.orderBy) &&
                Objects.equals(offset, o.offset) &&
                Objects.equals(limit, o.limit);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(select, from, where, groupBy, having, orderBy, offset, limit);
    }

    @Override
    public boolean shallowEquals(Node other)
    {
        return sameClass(this, other);
    }
}
