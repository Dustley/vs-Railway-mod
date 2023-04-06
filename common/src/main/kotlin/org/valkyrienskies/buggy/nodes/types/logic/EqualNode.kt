package org.valkyrienskies.buggy.nodes.types.logic

import org.valkyrienskies.buggy.nodes.Node

// AddingNode class that overrides computeValue function to add all values
class EqualNode: Node() {
    override fun computeValue(): Double {
        var sum = 1.0

        connectedValues.forEach {
            if(connectedValues[0].value != it.value){
                sum = 0.0
            }
        }

        return sum
    }
}