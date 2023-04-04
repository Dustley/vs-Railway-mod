package org.valkyrienskies.buggy.nodes.types

import net.minecraft.server.level.ServerLevel
import org.valkyrienskies.buggy.nodes.Node

// EmitterNode class that outputs 10 if enabled, otherwise outputs 0
class EmitterNode: Node() {
    var enabled: Boolean = true

    override fun computeValue(): Double {
        return if (enabled) 10.0 else 0.0
    }
}