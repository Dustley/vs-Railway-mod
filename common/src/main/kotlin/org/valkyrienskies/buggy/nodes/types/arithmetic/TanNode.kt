package org.valkyrienskies.buggy.nodes.types.arithmetic

import org.valkyrienskies.buggy.nodes.Node
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tan

// AddingNode class that overrides computeValue function to add all values
class TanNode: Node() {
    override fun computeValue(): Double {
        return tan(value)
    }
}