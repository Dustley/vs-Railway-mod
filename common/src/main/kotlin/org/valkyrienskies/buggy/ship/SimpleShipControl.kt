package org.valkyrienskies.buggy.ship

import com.fasterxml.jackson.annotation.JsonAutoDetect
import de.m_marvin.univec.impl.Vec3d
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import org.joml.Vector3d
import org.joml.Vector3i
import org.valkyrienskies.buggy.BuggyConfig
import org.valkyrienskies.core.api.ships.PhysShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.core.api.ships.saveAttachment
import org.valkyrienskies.core.impl.api.ShipForcesInducer
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl
import org.valkyrienskies.buggy.api.extension.conv
import org.valkyrienskies.buggy.api.extension.toPos
import org.valkyrienskies.mod.common.util.toJOML
import java.util.concurrent.CopyOnWriteArrayList

@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
class SimpleShipControl : ShipForcesInducer {

    private val Treads = CopyOnWriteArrayList<Triple<Vector3i, Vector3d, Level>>()
    private val Forces = CopyOnWriteArrayList<Vector3d>()

    override fun applyForces(physShip: PhysShip) {
        if (physShip == null) return
        physShip as PhysShipImpl

        Forces.forEach {
            val force = it

            println("force to apply: $force")

            physShip.applyInvariantForce(force)

        }
        Forces.clear()

        Treads.forEach {
            val (pos, force, level) = it

            if(level != null && level.isClientSide) return

            var apForce = Vector3d()

            val tDir = physShip.transform.shipToWorld.transformDirection(force.normalize())
            val tForce = Vec3d(physShip.transform.shipToWorld.transformDirection(force, Vec3d().conv()))
            val tPos = Vec3d(pos).add(0.5, 0.5, 0.5).sub(Vec3d().readFrom(physShip.transform.positionInShip))
            val wPos: Vector3d? = physShip.transform.shipToWorld.transformPosition(Vector3d(pos.x.toDouble(),pos.y.toDouble(),pos.z.toDouble()))

            val fPos: BlockPos = Vec3d(wPos?.let { it1 -> Vec3d(it1.x, wPos.y -1.0, wPos.z) }).toPos()

            //move forward
            if(!level.isEmptyBlock(fPos)){
                apForce = tForce.mul(BuggyConfig.SERVER.TreadSpeed /* * level.getBlockState(pos.toBlockPos()).getValue(BuggyProperties.Energy) */).conv()
            }

            if (apForce.isFinite) {
                physShip.applyInvariantForceToPos(apForce, tPos.conv())
            }
        }
    }

    fun addInvariantForce(force: Vec3d) {
        Forces.add(force.conv())
    }


    fun addTread(pos: BlockPos, level: Level, force: Vec3d) {
        Treads.add(Triple(pos.toJOML(), force.conv(), level))
    }

    fun removeTread(pos: BlockPos, level: Level, force: Vec3d) {
        Treads.remove(Triple(pos.toJOML(), force.conv(), level))
    }

    companion object {
        fun getOrCreate(ship: ServerShip): SimpleShipControl {
            return ship.getAttachment<SimpleShipControl>()
                ?: SimpleShipControl().also { ship.saveAttachment(it) }
        }
    }

}