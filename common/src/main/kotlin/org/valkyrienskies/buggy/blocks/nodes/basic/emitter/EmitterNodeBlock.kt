package org.valkyrienskies.buggy.blocks.nodes.basic.emitter

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.buggy.blocks.nodes.IHasNode
import org.valkyrienskies.buggy.blocks.nodes.NodeBlock

class EmitterNodeBlock : NodeBlock() {

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity { return EmitterNodeBlockEntity(pos, state) }

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
        super.onPlace(state, level, pos, oldState, isMoving)

        (level.getBlockEntity(pos) as IHasNode).createPin()
    }

    override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        (level.getBlockEntity(pos) as IHasNode).removePin()

        super.onRemove(state, level, pos, newState, isMoving)
    }

}