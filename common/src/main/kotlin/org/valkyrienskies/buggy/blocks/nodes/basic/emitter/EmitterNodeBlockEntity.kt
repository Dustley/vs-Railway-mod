package org.valkyrienskies.buggy.blocks.nodes.basic.emitter

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.piston.PistonMath
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.buggy.BuggyBlockEntities
import org.valkyrienskies.buggy.PAL.PALNetwork
import org.valkyrienskies.buggy.PAL.Pin
import org.valkyrienskies.buggy.PAL.PinType
import org.valkyrienskies.buggy.blocks.nodes.IHasNode
import org.valkyrienskies.buggy.mixinducks.server.PALNetworkDuck

class EmitterNodeBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(BuggyBlockEntities.EMITTER_BLOCK.get(), pos, state),
    IHasNode {

    override var pin: Pin? = null

    override fun createPin() {
        val network : PALNetwork = (level as PALNetworkDuck).network
        this.pin = network.addPin(PinType.EMMITING, worldPosition)
    }

    override fun removePin() {
        if (level != null) {
            val network : PALNetwork = (level as PALNetworkDuck).network
            network.removePin(this.pin)
            this.pin = null
        }
    }

    override fun load(tag: CompoundTag) {

        if (level != null) {
            val network : PALNetwork = (level as PALNetworkDuck).network
            if (this.pin == null) {
                this.pin = network.getPinFromBlock(worldPosition)
            }
        }

        super.load(tag)
    }


}