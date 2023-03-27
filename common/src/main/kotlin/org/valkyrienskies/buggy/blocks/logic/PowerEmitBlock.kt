package org.valkyrienskies.buggy.blocks.logic

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.Material
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import org.valkyrienskies.buggy.nodes.INodeBlock
import org.valkyrienskies.buggy.nodes.Node
import org.valkyrienskies.buggy.nodes.types.NodeData
import org.valkyrienskies.buggy.util.DirectionalShape
import org.valkyrienskies.buggy.util.RotShapes

class PowerEmitBlock : DirectionalBlock(
    Properties.of(Material.STONE)
        .sound(SoundType.STONE).strength(1.0f, 2.0f)
), INodeBlock {

    val SHAPE = RotShapes.box(3.0, 5.0, 4.0, 13.0, 11.0, 16.0)
    val Emmitor_SHAPE = DirectionalShape.south(SHAPE)

    init {
//        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(BlockStateProperties.POWER, 0).setValue(BuggyProperties.Energy, 0))
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(BlockStateProperties.POWER, 0))
    }

    override fun getRenderShape(blockState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return Emmitor_SHAPE[state.getValue(BlockStateProperties.FACING)]
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
        builder.add(BlockStateProperties.POWER)
        //builder.add(BuggyProperties.Energy)
        super.createBlockStateDefinition(builder)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        return defaultBlockState()
            .setValue(FACING, ctx.nearestLookingDirection)
    }

    override var node: Node = Node(NodeData())

}