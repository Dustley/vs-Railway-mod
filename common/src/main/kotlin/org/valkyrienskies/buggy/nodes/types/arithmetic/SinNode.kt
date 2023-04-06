package org.valkyrienskies.buggy.nodes.types.arithmetic

import org.valkyrienskies.buggy.nodes.Node
import kotlin.math.roundToInt
import kotlin.math.sin

class SinNode: Node() {
    override fun computeValue(): Double {
        return sin(value)
    }
}