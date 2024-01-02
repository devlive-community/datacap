package io.edurt.datacap.parser.mysql.companion.tree

import io.edurt.datacap.parser.mysql.companion.AstVisitor
import io.edurt.datacap.parser.mysql.companion.ast.Node
import io.edurt.datacap.parser.mysql.companion.ast.NodeLocation
import java.util.*

abstract class Statement internal constructor(location: Optional<NodeLocation>) : Node(location) {
    override fun <R, C> accept(visitor: AstVisitor<R, C>, context: C): R? {
        return visitor.visitStatement(this, context)
    }
}

