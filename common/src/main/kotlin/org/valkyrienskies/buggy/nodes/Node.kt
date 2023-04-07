package org.valkyrienskies.buggy.nodes

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.valkyrienskies.buggy.mixinducks.server.ServerNodeLinkerDuck

// Node class with properties and functions
@Deprecated("java nodes new")
open class Node() {

    var storedValue:Double = 0.0
    var Pos:BlockPos = BlockPos.ZERO
    var value: Double = 0.0

    val connectedNodes: MutableList<Node> = mutableListOf()
    val connectedValues: MutableList<Node> = mutableListOf()

    fun connectLevel(level: ServerLevel, pos: BlockPos){
        val linker = level as ServerNodeLinkerDuck

        Pos = pos
        linker.addServerNode(this, pos)
    }

    fun destroyNode(level: ServerLevel, pos: BlockPos){
        connectedNodes.forEach { disconnectFrom(it) }

        val linker = level as ServerNodeLinkerDuck
        linker.removeServerNode(this, pos)
    }

    fun connectTo(node: Node) {
        connectedNodes.add(node)
        node.connectedValues.add(this)
    }

    fun disconnectFrom(node: Node) {
        connectedNodes.remove(node)
        node.connectedValues.remove(this)
    }

    open fun computeValue(): Double {
        return value
    }

    fun tick() {
        value = storedValue
        storedValue = computeValue()

        connectedNodes.forEach {
            it.storedValue = computeValue()
        }

        //println("NODE -> " + Pos.toString() + " " + value)
    }
}