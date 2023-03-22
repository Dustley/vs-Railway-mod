package org.valkyrienskies.buggy.util

import net.minecraft.world.level.Level
import org.joml.Vector3d
import org.joml.Vector3i

class Node {

    companion object Values{

    }

    private val Inputs = mutableListOf<Connection>()
    private val Outputs = mutableListOf<Connection>()

    fun getInputs(): MutableList<Connection> {
        return Inputs
    }
    fun getOutputs(): MutableList<Connection> {
        return Outputs
    }

    fun createConnection(isInput:Boolean, From:Node, To:Node):Connection{
        val connection = Connection(From, To)

        if(isInput){
            Inputs.add(connection)
        } else {
            Outputs.add(connection)
        }

        connection.updateConnection()
        return connection
    }

}