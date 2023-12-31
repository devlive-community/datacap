package io.edurt.datacap.parser.tree.query;

import io.edurt.datacap.parser.ast.AstVisitor;
import io.edurt.datacap.parser.node.NodeLocation;
import io.edurt.datacap.parser.tree.Relation;

import java.util.Optional;

public abstract class QueryBody
        extends Relation
{
    protected QueryBody(Optional<NodeLocation> location)
    {
        super(location);
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context)
    {
        return visitor.visitQueryBody(this, context);
    }
}
