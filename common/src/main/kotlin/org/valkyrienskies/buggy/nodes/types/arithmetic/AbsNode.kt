package org.valkyrienskies.buggy.nodes.types.arithmetic

import org.valkyrienskies.buggy.nodes.Node
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sin

// AddingNode class that overrides computeValue function to add all values
class AbsNode: Node() {
    override fun computeValue(): Double {
        return abs(value)
    }
}