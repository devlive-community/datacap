package io.edurt.datacap.parser.tree.table;

import io.edurt.datacap.parser.ast.AstVisitor;
import io.edurt.datacap.parser.node.Node;
import io.edurt.datacap.parser.node.NodeLocation;

import java.util.Optional;

public abstract class TableElement
        extends Node
{
    public TableElement(Optional<NodeLocation> location)
    {
        super(location);
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context)
    {
        return visitor.visitTableElement(this, context);
    }
}
