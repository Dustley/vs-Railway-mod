package org.valkyrienskies.buggy.nodes.types.arithmetic

import org.valkyrienskies.buggy.nodes.Node
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// AddingNode class that overrides computeValue function to add all values
class CosNode: Node() {
    override fun computeValue(): Double {
        return cos(value)
    }
}