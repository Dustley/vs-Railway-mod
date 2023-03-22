package org.valkyrienskies.buggy.blocks.movement

import de.m_marvin.univec.impl.Vec3d
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
import org.valkyrienskies.buggy.BuggyProperties
import org.valkyrienskies.buggy.blocks.NodeBlock
import org.valkyrienskies.buggy.ship.TreadShipControl
import org.valkyrienskies.buggy.util.DirectionalShape
import org.valkyrienskies.buggy.util.Node
import org.valkyrienskies.buggy.util.RotShapes
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.getShipObjectManagingPos

class TreadBlock : DirectionalBlock(
    Properties.of(Material.STONE)
        .sound(SoundType.STONE).strength(1.0f, 2.0f)
), NodeBlock {

    val SHAPE = RotShapes.box(3.0, 5.0, 4.0, 13.0, 11.0, 16.0)
    val Tread_SHAPE = DirectionalShape.south(SHAPE)

    override var ConnectedNode: Node
        get() = ConnectedNode
        set(value) {}

    init {
//        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(BlockStateProperties.POWER, 0).setValue(BuggyProperties.Energy, 0))
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(BlockStateProperties.POWER, 0))
    }

    override fun getRenderShape(blockState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return Tread_SHAPE[state.getValue(BlockStateProperties.FACING)]
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
        builder.add(BlockStateProperties.POWER)
        //builder.add(BuggyProperties.Energy)
        super.createBlockStateDefinition(builder)
    }

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
        super.onPlace(state, level, pos, oldState, isMoving)

        if (level.isClientSide) return
        level as ServerLevel

        val signal = level.getBestNeighborSignal(pos)
        level.setBlock(pos, state.setValue(BlockStateProperties.POWER, signal), 2)

        setNode(Node())

        TreadShipControl.getOrCreate(level.getShipObjectManagingPos(pos) ?: level.getShipManagingPos(pos) ?: return
        )?.addTread(pos, level, state.getValue(FACING))
    }

    override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        super.onRemove(state, level, pos, newState, isMoving)

        if (level.isClientSide) return
        level as ServerLevel

        state.setValue(BlockStateProperties.POWER, 0)
        level.getShipManagingPos(pos)?.getAttachment<TreadShipControl>()?.removeTread(pos, level,state.getValue(FACING))
        level.getShipManagingPos(pos)?.getAttachment<TreadShipControl>()?.forceStopTread( pos )
    }

    override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        isMoving: Boolean
    ) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving)

        if (level as? ServerLevel == null) return

        val signal = level.getBestNeighborSignal(pos)
        level.setBlock(pos, state.setValue(BlockStateProperties.POWER, signal), 2)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        return defaultBlockState()
            .setValue(FACING, ctx.nearestLookingDirection)
    }


//    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: Random) {
//        super.animateTick(state, level, pos, random)
//        if (state.getValue(BlockStateProperties.POWER) > 0) {
//            val dir = state.getValue(FACING)
//
//            val x = pos.x.toDouble() + (0.5 * (dir.stepX + 1));
//            val y = pos.y.toDouble() + (0.5 * (dir.stepY + 1));
//            val z = pos.z.toDouble() + (0.5 * (dir.stepZ + 1));
//
//            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0.0, 0.0, 0.0)
//        }
//    }

}