package org.valkyrienskies.buggy.blockentity.seat

import de.m_marvin.univec.impl.Vec3d
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.valkyrienskies.buggy.BuggyBlockEntities
import org.valkyrienskies.buggy.BuggyDebugHelper
import org.valkyrienskies.buggy.api.debug.DebugLine
import org.valkyrienskies.buggy.api.debug.DebugObjectID
import org.valkyrienskies.buggy.api.extension.fromPos
import org.valkyrienskies.buggy.api.helper.Helper3d
import org.valkyrienskies.buggy.ship.SimpleShipControl
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.mod.common.getShipObjectManagingPos
import java.awt.Color

class SeatBlockEntity (pos: BlockPos, state: BlockState) : BlockEntity(BuggyBlockEntities.BASIC_SEAT.get(), pos, state) {

    val TargetVec = Vec3d(200.0, 200.0, 200.0)

    var debugID : DebugObjectID = -1

    companion object {
        fun tick(level: Level, pos: BlockPos, state: BlockState, be: BlockEntity) {
            if (state.getValue(BlockStateProperties.POWER) < 1) { return }

            be as SeatBlockEntity

            val ship = level.getShipObjectManagingPos(pos) ?: return

            val pos = Vec3d().fromPos(pos)

            val worldPos = Helper3d.MaybeShipToWorldspace(level, pos)
            println("worldPos: $worldPos")

            val rotn = be.TargetVec.sub(worldPos).normalize()
            println("rotn: $rotn")

            if (!level.isClientSide) {
                ship as ServerShip

                val force = rotn.mul(1e4 * ship.inertiaData.mass)
                println("force: $force")

                be.debugID = BuggyDebugHelper.updateObject(be.debugID, DebugLine(worldPos, force.add(worldPos), Color.RED))

                SimpleShipControl.getOrCreate(ship).addInvariantForce(force)
            }
        }
    }


}