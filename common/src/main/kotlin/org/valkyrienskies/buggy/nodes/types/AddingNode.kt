package org.valkyrienskies.buggy.nodes.types

import net.minecraft.server.level.ServerLevel
import org.valkyrienskies.buggy.nodes.Node

// AddingNode class that overrides computeValue function to add all values
class AddingNode: Node() {
    override fun computeValue(): Double {
        var result = value
        if (connectedNodes.isNotEmpty()) {
            connectedNodes.forEach { result += it.value }
        }
        return result
    }
}