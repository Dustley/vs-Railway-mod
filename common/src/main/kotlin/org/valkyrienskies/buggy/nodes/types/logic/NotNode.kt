package org.valkyrienskies.buggy.nodes.types.logic

import org.valkyrienskies.buggy.nodes.Node

// AddingNode class that overrides computeValue function to add all values
class NotNode: Node() {
    override fun computeValue(): Double {
        if(value == 0.0){
            return 1.0
        } else if (value == 1.0){
            return 0.0
        }

        return 0.0
    }
}