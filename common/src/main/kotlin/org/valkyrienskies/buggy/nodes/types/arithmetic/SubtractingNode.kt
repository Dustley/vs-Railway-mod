package org.valkyrienskies.buggy.nodes.types.arithmetic

import org.valkyrienskies.buggy.nodes.Node

// AddingNode class that overrides computeValue function to add all values
class SubtractingNode: Node() {
    override fun computeValue(): Double {
        var sum = 0.0

        connectedValues.forEach {
            sum -= it.value
        }

        return sum
    }
}