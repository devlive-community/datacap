package io.edurt.datacap.parser.tree.table;

import com.google.common.collect.ImmutableList;
import io.edurt.datacap.parser.ast.AstVisitor;
import io.edurt.datacap.parser.node.Node;
import io.edurt.datacap.parser.node.NodeLocation;
import io.edurt.datacap.parser.tree.QualifiedName;
import io.edurt.datacap.parser.tree.query.QueryBody;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;

public class Table
        extends QueryBody
{
    private final QualifiedName name;

    public Table(QualifiedName name)
    {
        this(Optional.empty(), name);
    }

    public Table(NodeLocation location, QualifiedName name)
    {
        this(Optional.of(location), name);
    }

    private Table(Optional<NodeLocation> location, QualifiedName name)
    {
        super(location);
        this.name = name;
    }

    public QualifiedName getName()
    {
        return name;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context)
    {
        return visitor.visitTable(this, context);
    }

    @Override
    public List<Node> getChildren()
    {
        return ImmutableList.of();
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .addValue(name)
                .toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Table table = (Table) o;
        return Objects.equals(name, table.name);
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public boolean shallowEquals(Node other)
    {
        if (!sameClass(this, other)) {
            return false;
        }
        Table otherTable = (Table) other;
        return name.equals(otherTable.name);
    }
}
