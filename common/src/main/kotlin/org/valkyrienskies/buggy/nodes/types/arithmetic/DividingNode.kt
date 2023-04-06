package org.valkyrienskies.buggy.nodes.types.arithmetic

import org.valkyrienskies.buggy.nodes.Node

// AddingNode class that overrides computeValue function to add all values
class DividingNode: Node() {
    override fun computeValue(): Double {
        var sum = 1.0

        connectedValues.forEach {
            sum /= it.value
        }

        return sum
    }
}