package io.edurt.datacap.parser.mysql.companion.tree

import io.edurt.datacap.parser.mysql.companion.ast.Node
import io.edurt.datacap.parser.mysql.companion.ast.NodeLocation
import java.util.*

class Limit(override val location: Optional<NodeLocation>,
            val children: Set<LimitItem>) : Node(location)
