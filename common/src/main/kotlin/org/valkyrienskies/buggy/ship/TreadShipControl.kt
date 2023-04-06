package org.valkyrienskies.buggy.ship

import com.fasterxml.jackson.annotation.JsonAutoDetect
import de.m_marvin.univec.impl.Vec3d
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.Property
import org.apache.commons.math3.analysis.function.Power
import org.joml.Vector3d
import org.joml.Vector3i
import org.valkyrienskies.core.api.ships.PhysShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.core.api.ships.saveAttachment
import org.valkyrienskies.core.impl.api.ShipForcesInducer
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl
import org.valkyrienskies.core.impl.pipelines.SegmentUtils
import org.valkyrienskies.mod.common.util.toJOML
import org.valkyrienskies.buggy.BuggyConfig
import org.valkyrienskies.buggy.api.extension.toPos
import org.valkyrienskies.buggy.api.utilities.Quadruple
import org.valkyrienskies.buggy.nodes.INodeBlock
import org.valkyrienskies.buggy.nodes.Node
import org.valkyrienskies.mod.common.util.toBlockPos
import org.valkyrienskies.mod.common.util.toJOMLD
import java.util.concurrent.CopyOnWriteArrayList

@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
class TreadShipControl : ShipForcesInducer {

    private val Treads = mutableListOf<Quadruple<Vector3i, Direction, Level, Node>>()

    override fun applyForces(physShip: PhysShip) {
        if (physShip == null) return
        physShip as PhysShipImpl

        val mass = physShip.inertia.shipMass
        val segment = physShip.segments.segments[0]?.segmentDisplacement!!
        val vel = SegmentUtils.getVelocity(physShip.poseVel, segment, Vector3d())

        // look here to see how to set value
        // https://github.com/ConstantDust/VS2_tournament/blob/main/common/src/main/kotlin/org/valkyrienskies/tournament/blocks/ThrusterBlock.kt

        Treads.forEach {
            val (pos, dir, level, node) = it

            if(level.isClientSide) return

            var apForce = Vector3d()

            val tDir = physShip.transform.shipToWorld.transformDirection(dir.normal.toJOMLD())
            val tPos = Vec3d(pos).add(0.5, 0.5, 0.5).sub(Vec3d().readFrom(physShip.transform.positionInShip))
            val wPos = Vector3d(physShip.transform.shipToWorld.transformPosition(Vector3d(pos.x.toDouble() + 0.5,pos.y.toDouble()+ 0.5 ,pos.z.toDouble() + 0.5)))

            //calculate apForce
            if(!level.isEmptyBlock(Vec3d(wPos.x, wPos.y - 1.0, wPos.z).toPos())){
                apForce = apForce.add(tDir.mul(BuggyConfig.SERVER.TreadSpeed * node.value, Vector3d()))
            }


            //move
            if (apForce.isFinite) {
                physShip.applyInvariantForceToPos(apForce, tPos.conv())
                //println("tread: " + apForce + " " + node.value)
            }
        }
    }

    fun addTread(pos: BlockPos, level: Level, force: Direction, bclass: Node) {
        Treads.add(Quadruple(pos.toJOML(), force, level, bclass))
    }

    fun removeTread(pos: BlockPos, level: Level, force: Direction, bclass:Node) {
        Treads.remove(Quadruple(pos.toJOML(), force, level, bclass))
    }

    fun forceStopTread(pos: BlockPos) {
        Treads.removeAll { it.first == pos }
    }

    companion object {
        fun getOrCreate(ship: ServerShip): TreadShipControl {
            return ship.getAttachment<TreadShipControl>()
                ?: TreadShipControl().also { ship.saveAttachment(it) }
        }
    }

}