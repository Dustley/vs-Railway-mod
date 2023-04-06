package org.valkyrienskies.buggy.nodes.types.logic

import org.valkyrienskies.buggy.nodes.Node

// AddingNode class that overrides computeValue function to add all values
class AndNode: Node() {
    override fun computeValue(): Double {
        var sum = 1.0

        connectedValues.forEach {
            if(it.value != 1.0){
                sum = 0.0
            }
        }

        return sum
    }
}