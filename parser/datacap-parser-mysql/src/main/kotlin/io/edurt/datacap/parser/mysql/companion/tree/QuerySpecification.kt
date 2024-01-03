package io.edurt.datacap.parser.mysql.companion.tree

import io.edurt.datacap.parser.mysql.companion.ast.NodeLocation
import java.util.*

data class QuerySpecification(override val location: Optional<NodeLocation>,
                              val select: Select,
                              val from: Relation,
                              val limit: Limit) : Statement(location)
