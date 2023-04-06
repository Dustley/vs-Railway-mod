package org.valkyrienskies.buggy.nodes.types.basic

import net.minecraft.server.level.ServerLevel
import org.valkyrienskies.buggy.nodes.Node

// EmitterNode class that outputs 10 if enabled, otherwise outputs 0
class EmitterNode: Node() {
    var enabled: Boolean = true
    var power:Double = 1.0

    override fun computeValue(): Double {
        return if (enabled) power else 0.0
    }
}