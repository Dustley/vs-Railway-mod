package org.valkyrienskies.buggy.nodes.types

import org.valkyrienskies.buggy.nodes.Node

class AdditionNode(Data: NodeData) : Node(Data) {

    override fun numberOperation(inData: ArrayList<Double>):Double {
        var total:Double = 0.0
        inData.forEach { total += it }
        return total
    }

}