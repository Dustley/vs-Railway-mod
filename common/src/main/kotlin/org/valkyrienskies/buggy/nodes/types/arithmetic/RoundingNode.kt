package org.valkyrienskies.buggy.nodes.types.arithmetic

import org.valkyrienskies.buggy.nodes.Node
import kotlin.math.roundToInt

// AddingNode class that overrides computeValue function to add all values
class RoundingNode: Node() {
    override fun computeValue(): Double {
        return value.roundToInt().toDouble()
    }
}