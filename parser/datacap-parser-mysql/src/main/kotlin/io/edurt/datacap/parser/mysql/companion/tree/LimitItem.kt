package io.edurt.datacap.parser.mysql.companion.tree

import io.edurt.datacap.parser.mysql.companion.ast.Node
import io.edurt.datacap.parser.mysql.companion.ast.NodeLocation
import java.util.*

data class LimitItem(override val location: Optional<NodeLocation>,
                     val limit: Long) : Node(location)
