package org.valkyrienskies.buggy.ship

import com.fasterxml.jackson.annotation.JsonAutoDetect
import de.m_marvin.univec.impl.Vec3d
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3ic
import org.valkyrienskies.buggy.BuggyConfig
import org.valkyrienskies.buggy.api.utilities.Quadruple
import org.valkyrienskies.buggy.mixinducks.server.PALNetworkDuck
import org.valkyrienskies.core.api.ships.PhysShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.core.api.ships.saveAttachment
import org.valkyrienskies.core.impl.api.ShipForcesInducer
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl
import org.valkyrienskies.mod.common.util.toBlockPos
import org.valkyrienskies.mod.common.util.toJOML
import java.util.concurrent.CopyOnWriteArrayList

@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
class BuggyShipControl : ShipForcesInducer {

    private val Forces = CopyOnWriteArrayList<Vector3d>()
    private val Thrusters = CopyOnWriteArrayList<Triple<Vector3ic, Vector3dc, Level>>()
    private val Treads = CopyOnWriteArrayList<Triple<Vector3ic, Vector3dc, Level>>()

    override fun applyForces(physShip: PhysShip) {
        if (physShip == null) return
        physShip as PhysShipImpl

        Forces.forEach {
            val force = it

            println("force to apply: $force")

            physShip.applyInvariantForce(force)

        }
        Forces.clear()

        Thrusters.forEach {
            val (pos, force, level) = it
            level as PALNetworkDuck

            val tForce = physShip.transform.shipToWorld.transformDirection(force, Vector3d()) //.shipToWorld.transformDirection(force, Vector3d())
            val tPos = Vector3d(pos).add(0.5, 0.5, 0.5).sub(physShip.transform.positionInShip)

            if (force.isFinite && physShip.poseVel.vel.length() < BuggyConfig.SERVER.SpeedCutoff) {
                physShip.applyInvariantForceToPos(tForce.mul(BuggyConfig.SERVER.TreadSpeed * level.network.getPinFromBlock(pos.toBlockPos()).value, Vector3d()), tPos)
            }
        }

        Treads.forEach {
            val (pos, force, level) = it

            val tForce = physShip.transform.shipToWorld.transformDirection(force, Vector3d()) //.shipToWorld.transformDirection(force, Vector3d())
            val tPos = Vector3d(pos).add(0.5, 0.5, 0.5).sub(physShip.transform.positionInShip)

            if (force.isFinite && physShip.poseVel.vel.length() < BuggyConfig.SERVER.SpeedCutoff) {
                physShip.applyInvariantForceToPos(tForce.mul(BuggyConfig.SERVER.TreadSpeed * 2.0, Vector3d()), tPos)
            }
        }


    }

    fun addInvariantForce(force: Vec3d) {
        Forces.add(force.conv())
    }

    fun addThruster(pos: BlockPos, force: Vector3dc, level: Level) {
        Thrusters.add(Triple(pos.toJOML(), force, level))
    }
    fun removeThruster(pos: BlockPos, force: Vector3dc, level:Level) {
        Thrusters.remove(Triple(pos.toJOML(), force, level))
    }

    fun addTread(pos: BlockPos, power: Double, force: Vector3dc, level: Level) {
        Treads.add(Triple(pos.toJOML(), force, level))
    }
    fun removeTread(pos: BlockPos, power: Double,force: Vector3dc, level: Level) {
        Treads.remove(Triple(pos.toJOML(), force, level))
    }


    companion object {
        fun getOrCreate(ship: ServerShip): BuggyShipControl {
            return ship.getAttachment<BuggyShipControl>()
                ?: BuggyShipControl().also { ship.saveAttachment(it) }
        }
    }

}