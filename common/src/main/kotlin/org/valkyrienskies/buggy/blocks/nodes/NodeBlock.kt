package org.valkyrienskies.buggy.blocks.nodes

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING
import net.minecraft.world.level.material.Material
import org.valkyrienskies.buggy.blocks.nodes.basic.EmitterNodeBlockEntity

open class NodeBlock : BaseEntityBlock(Properties.of(Material.BAMBOO)) {

    init {
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? { return null }

}