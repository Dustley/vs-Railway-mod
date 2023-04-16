package org.valkyrienskies.buggy.blocks.nodes.basic

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.buggy.blocks.nodes.NodeBlock

class EmitterNodeBlock : NodeBlock() {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity { return EmitterNodeBlockEntity(pos, state) }
}