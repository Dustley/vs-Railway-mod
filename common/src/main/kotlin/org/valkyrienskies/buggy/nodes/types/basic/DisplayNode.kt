package org.valkyrienskies.buggy.nodes.types.basic

import org.valkyrienskies.buggy.nodes.Node

class DisplayNode: Node() {
    override fun computeValue(): Double {
        println("DISPLAY VALUE IS: " + value)
        return 0.0
    }
}