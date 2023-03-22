package org.valkyrienskies.buggy.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.valkyrienskies.buggy.BuggyProperties
import org.valkyrienskies.buggy.util.Node

interface NodeBlock{

    var ConnectedNode:Node

    fun getNode():Node {
        return ConnectedNode
    }

    fun setNode(node: Node) {
        ConnectedNode = node
    }

    fun powerFunction(power:Double):Double{
        return 0.0
    }

}