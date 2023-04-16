package org.valkyrienskies.buggy.blocks.nodes.basic

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.piston.PistonMath
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.buggy.BuggyBlockEntities
import org.valkyrienskies.buggy.PAL.PALNetwork
import org.valkyrienskies.buggy.PAL.Pin
import org.valkyrienskies.buggy.mixinducks.server.PALNetworkDuck

class EmitterNodeBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(BuggyBlockEntities.EMITTER_BLOCK.get(), pos, state){

    private var pin : Pin? = null

    override fun load(tag: CompoundTag) {

        if (level != null) {
            var network : PALNetwork = (level as PALNetworkDuck).network
            if (pin == null) {
                pin = network.getPinFromBlock(worldPosition)
            }
        }

        super.load(tag)
    }

}