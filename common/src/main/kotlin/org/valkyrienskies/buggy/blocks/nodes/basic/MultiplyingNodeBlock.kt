package org.valkyrienskies.buggy.blocks.nodes.basic

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import org.valkyrienskies.buggy.PAL.PinType
import org.valkyrienskies.buggy.blocks.nodes.NodeBlock
import org.valkyrienskies.buggy.mixinducks.server.PALNetworkDuck

class MultiplyingNodeBlock : NodeBlock(Properties.of(Material.METAL)) {

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
        super.onPlace(state, level, pos, oldState, isMoving)

        if (level.isClientSide) return
        level as PALNetworkDuck

        level.network.addPin(PinType.MULTIPLYING, pos) // on place get pin and set shit

    }

}