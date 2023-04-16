package org.valkyrienskies.buggy

import de.m_marvin.univec.impl.Vec3d
import de.m_marvin.univec.impl.Vec4i
import net.minecraft.client.Minecraft
import net.minecraft.util.Tuple
import org.valkyrienskies.buggy.api.debug.DebugObject
import org.valkyrienskies.buggy.api.debug.DebugObjectID
import org.valkyrienskies.buggy.api.utilities.Quadruple
import org.valkyrienskies.buggy.api.utilities.Quintuple
import java.awt.Color

class BuggyDebugHelper {
    companion object {

        private var debugLines = ArrayList<Triple<Vec3d, Vec3d, Vec4i>>()
        private var tickDebugLines = ArrayList<Quadruple<Vec3d, Vec3d, Int, Vec4i>>()
        private var tickIDDebugLines = ArrayList<Quintuple<Vec3d, Vec3d, Int, Int, Vec4i>>()

        fun addConstantDebugLine( p1 : Vec3d, p2 : Vec3d, color: Vec4i) {
            debugLines.add(Triple(p1, p2, color))
        }

        fun addConstantDebugBox( center : Vec3d, sideLength: Double, color: Vec4i) {
            val halfSideLength = sideLength / 2.0

            // Define the eight vertices of the cube
            val vertices = listOf(
                center.add(Vec3d(-halfSideLength, -halfSideLength, halfSideLength)),
                center.add(Vec3d(-halfSideLength, halfSideLength, halfSideLength)),
                center.add(Vec3d(halfSideLength, halfSideLength, halfSideLength)),
                center.add(Vec3d(halfSideLength, -halfSideLength, halfSideLength)),
                center.add(Vec3d(-halfSideLength, -halfSideLength, -halfSideLength)),
                center.add(Vec3d(-halfSideLength, halfSideLength, -halfSideLength)),
                center.add(Vec3d(halfSideLength, halfSideLength, -halfSideLength)),
                center.add(Vec3d(halfSideLength, -halfSideLength, -halfSideLength))
            )

            // Define the twelve edges of the cube by connecting its vertices
            val edges = listOf(
                Pair(vertices[0], vertices[1]),
                Pair(vertices[1], vertices[2]),
                Pair(vertices[2], vertices[3]),
                Pair(vertices[3], vertices[0]),
                Pair(vertices[4], vertices[5]),
                Pair(vertices[5], vertices[6]),
                Pair(vertices[6], vertices[7]),
                Pair(vertices[7], vertices[4]),
                Pair(vertices[0], vertices[4]),
                Pair(vertices[1], vertices[5]),
                Pair(vertices[2], vertices[6]),
                Pair(vertices[3], vertices[7])
            )

            // Draw each edge of the cube
            for (edge in edges) {
                addConstantDebugLine(edge.first, edge.second, color)
            }
        }

        fun addConstantDebugArrow(start: Vec3d, end: Vec3d, arrowLength: Double, arrowAngle: Double, color: Vec4i) {

            // Draw the main line of the arrow
            addConstantDebugLine(start, end, color)

            // Draw the arrowhead
            val arrowDirection = end.sub(start).normalize()
            val arrowTip = end.sub(arrowDirection.mul(arrowLength))
            val arrowSide1 = arrowDirection.cross(Vec3d(0.0, 1.0, 0.0)).normalize()
            val arrowSide2 = arrowDirection.cross(Vec3d(0.0, -1.0, 0.0)).normalize()
            val arrowBase1 = arrowTip.add(arrowSide1.mul(arrowLength * arrowAngle))
            val arrowBase2 = arrowTip.add(arrowSide2.mul(arrowLength * arrowAngle))
            addConstantDebugLine(end, arrowBase1, color)
            addConstantDebugLine(end, arrowBase2, color)
            addConstantDebugLine(arrowBase1, arrowTip, color)
            addConstantDebugLine(arrowBase2, arrowTip, color)

        }

        fun addTickDebugBox( center : Vec3d, sideLength: Double, ticks : Int, color: Vec4i) {
            val halfSideLength = sideLength / 2.0

            // Define the eight vertices of the cube
            val vertices = listOf(
                center.add(Vec3d(-halfSideLength, -halfSideLength, halfSideLength)),
                center.add(Vec3d(-halfSideLength, halfSideLength, halfSideLength)),
                center.add(Vec3d(halfSideLength, halfSideLength, halfSideLength)),
                center.add(Vec3d(halfSideLength, -halfSideLength, halfSideLength)),
                center.add(Vec3d(-halfSideLength, -halfSideLength, -halfSideLength)),
                center.add(Vec3d(-halfSideLength, halfSideLength, -halfSideLength)),
                center.add(Vec3d(halfSideLength, halfSideLength, -halfSideLength)),
                center.add(Vec3d(halfSideLength, -halfSideLength, -halfSideLength))
            )

            // Define the twelve edges of the cube by connecting its vertices
            val edges = listOf(
                Pair(vertices[0], vertices[1]),
                Pair(vertices[1], vertices[2]),
                Pair(vertices[2], vertices[3]),
                Pair(vertices[3], vertices[0]),
                Pair(vertices[4], vertices[5]),
                Pair(vertices[5], vertices[6]),
                Pair(vertices[6], vertices[7]),
                Pair(vertices[7], vertices[4]),
                Pair(vertices[0], vertices[4]),
                Pair(vertices[1], vertices[5]),
                Pair(vertices[2], vertices[6]),
                Pair(vertices[3], vertices[7])
            )

            // Draw each edge of the cube
            for (edge in edges) {
                addTickDebugLine(edge.first, edge.second, ticks, color)
            }
        }

        fun addTickDebugPyramid(center: Vec3d, direction: Vec3d, ticks : Int, color: Vec4i) {
            // Define the vertices of the pyramid
            val apex = center.add(direction)
            val baseVertex1 = Vec3d(center.x + 1.0, center.y, center.z)
            val baseVertex2 = Vec3d(center.x, center.y + 1.0, center.z)
            val baseVertex3 = Vec3d(center.x - 1.0, center.y, center.z)
            val baseVertex4 = Vec3d(center.x, center.y - 1.0, center.z)

            // Draw the base of the pyramid
            addTickDebugLine(baseVertex1, baseVertex2, ticks, color)
            addTickDebugLine(baseVertex2, baseVertex3, ticks, color)
            addTickDebugLine(baseVertex3, baseVertex4, ticks, color)
            addTickDebugLine(baseVertex4, baseVertex1, ticks, color)

            // Draw the lines from the apex to the base vertices
            addTickDebugLine(apex, baseVertex1, ticks, color)
            addTickDebugLine(apex, baseVertex2, ticks, color)
            addTickDebugLine(apex, baseVertex3, ticks, color)
            addTickDebugLine(apex, baseVertex4, ticks, color)
        }

        fun addTickDebugLine( p1 : Vec3d, p2 : Vec3d, ticks : Int, color: Vec4i) {
            tickDebugLines.add(Quadruple(p1, p2, ticks, color))
        }

        fun addTickedIDDebugLine( p1 : Vec3d, p2 : Vec3d, ticks : Int, id : Int, color: Vec4i) {
            var cont = false
            tickIDDebugLines.forEach {
                if(it.fourth == id) {cont = true}
            }
            if (!cont) {
                tickIDDebugLines.add(Quintuple(p1, p2, ticks, id, color))
                println("Added tick debug line from: $p1 to: $p2 for $ticks ticks with the ID $id!")
            } else {
                println("Already added tick debug line with id $id")
            }
        }

        fun updateIDDebugLine(idIn : Int, p1In : Vec3d, p2In : Vec3d, ticks : Int, color: Vec4i) {
            if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
                try {
                    tickIDDebugLines.forEach {
                        try {
                            val (p1, p2, tick, id, color) = it
                            if (id == idIn) {
                                tickIDDebugLines.remove(it)
                            }
                        } catch (e: Exception) {
                        }
                    }
                } catch (e: Exception) {
                }
                tickIDDebugLines.add(Quintuple(p1In, p2In, ticks, idIn, color))
                println("Updated debug line with id: $idIn")
            }
        }

        fun removeIDDebugLine(idIn : Int) {
            try {
                tickIDDebugLines.forEach {
                    try {
                        val id = it.fourth
                        if (id == idIn) {
                            tickIDDebugLines.remove(it)
                        }
                    } catch (e : Exception) {}
                }
            } catch (e : Exception) {}
            println("Removed debug line with id: $idIn")
        }

        fun queryLines() : List<Triple<Vec3d, Vec3d, Vec4i>> {
            val lines = ArrayList<Triple<Vec3d, Vec3d, Vec4i>>()
            lines.addAll(debugLines)

            try {
                tickDebugLines.forEach {
                    try {
                        val (p1, p2, tick, color) = it
                        lines.add(Triple(p1, p2, color))
                        tickDebugLines.remove(it)
                        if (tick > 0) {
                            tickDebugLines.add(Quadruple(p1, p2, tick, color))
                        }
                    } catch (e : Exception) {}
                }
            } catch (e : Exception) {}
            try {
                tickIDDebugLines.forEach {
                    try {
                        val (p1, p2, tick, id, color) = it
                        lines.add(Triple(p1, p2, color))
                        tickIDDebugLines.remove(it)
                        if (tick > 0) {
                            tickIDDebugLines.add(Quintuple(p1, p2, tick, id, color))
                        }
                    } catch (e : Exception) {}
                }
            } catch (e : Exception) {}

            //println("query ${lines.size} total lines")

            return lines
        }

    }

}