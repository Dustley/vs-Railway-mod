package org.valkyrienskies.buggy.nodes

import net.minecraft.server.level.ServerLevel
import org.valkyrienskies.buggy.mixin.server.ServerNodeLinkerDuck

// Node class with properties and functions
open class Node() {

    var storedValue:Double = 0.0
    var value: Double = 0.0

    val connectedNodes: MutableList<Node> = mutableListOf()

    fun connectLevel(level: ServerLevel){
        val linker = level as ServerNodeLinkerDuck

        linker.addServerNode(this)
    }

    fun connectTo(node: Node) {
        connectedNodes.add(node)
    }

    fun disconnectFrom(node: Node) {
        connectedNodes.remove(node)
    }

    open fun computeValue(): Double {
        return value
    }

    fun tick() {
        value = storedValue
        storedValue = computeValue()
    }
}