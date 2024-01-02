package io.edurt.datacap.parser.mysql.companion.tree

import com.google.common.base.MoreObjects.toStringHelper
import com.google.common.collect.ImmutableList
import io.edurt.datacap.parser.mysql.companion.AstVisitor
import io.edurt.datacap.parser.mysql.companion.ast.Node
import io.edurt.datacap.parser.mysql.companion.ast.NodeLocation
import java.util.*

class Select : Node {
    val distinct: Boolean
    val selectItems: List<SelectItem>

    constructor(distinct: Boolean, selectItems: List<SelectItem>) : this(Optional.empty(), distinct, selectItems)

    constructor(location: NodeLocation, distinct: Boolean, selectItems: List<SelectItem>) : this(
            Optional.of(location),
            distinct,
            selectItems
    )

    private constructor(location: Optional<NodeLocation>, distinct: Boolean, selectItems: List<SelectItem>) : super(location) {
        this.distinct = distinct
        this.selectItems = ImmutableList.copyOf(Objects.requireNonNull(selectItems, "selectItems"))
    }

    fun isDistinct(): Boolean {
        return distinct
    }

    override fun <R, C> accept(visitor: AstVisitor<R, C>, context: C): R? {
        return visitor.visitSelect(this, context)
    }

    override fun getChildren(): List<Node> {
        return selectItems
    }

    override fun toString(): String {
        return toStringHelper(this)
                .add("distinct", distinct)
                .add("selectItems", selectItems)
                .omitNullValues()
                .toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val select = other as Select
        return distinct == select.distinct &&
                Objects.equals(selectItems, select.selectItems)
    }

    override fun hashCode(): Int {
        return Objects.hash(distinct, selectItems)
    }

    override fun shallowEquals(other: Node): Boolean {
        if (! sameClass(this, other)) {
            return false
        }
        return distinct == (other as Select).distinct
    }
}
