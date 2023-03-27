package org.valkyrienskies.buggy.nodes

import org.valkyrienskies.buggy.nodes.types.NodeData

open class Node(Data:NodeData){

    private val data: NodeData = Data

    private val nodeId:Long = 0L // not used rn implement later
    private val nodesIn = ArrayList<Node>()
    private val nodesOut = ArrayList<Node>()

    // Create connections
    fun connectIn(node: Node){
        nodesIn.add(node)
    }
    fun connectOut(node: Node){
        nodesOut.add(node)
    }

    // Break connections
        // By reference
    fun breadkIn(node: Node){
        nodesIn.remove(node)
    }
    fun breakOut(node: Node){
        nodesOut.remove(node)
    }
        // By Array Id
    fun breadkIn(node: Int){
        nodesIn.remove(nodesIn[node])
    }
    fun breakOut(node: Int){
        nodesOut.remove(nodesOut[node])
    }

    // Global Data
    fun getData():NodeData {
        return data
    }
    fun getId():Long {
        return nodeId
    }

    // Fed in value
    open fun numberOperation(inData: ArrayList<Double>):Double { return inData[0] }
    open fun stringOperation(inData: ArrayList<String>):String { return inData[0] }
    open fun booleanOperation(inData: ArrayList<Boolean>):Boolean { return inData[0] }
}