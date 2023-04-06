package org.valkyrienskies.buggy.nodes.types.arithmetic

import org.valkyrienskies.buggy.nodes.Node
import kotlin.math.roundToInt
import kotlin.math.sin

// AddingNode class that overrides computeValue function to add all values
class InvertNode: Node() {
    override fun computeValue(): Double {
        return -value
    }
}