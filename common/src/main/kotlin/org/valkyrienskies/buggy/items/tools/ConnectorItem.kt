package org.valkyrienskies.buggy.items.tools

import de.m_marvin.univec.impl.Vec3d
import de.m_marvin.univec.impl.Vec4i
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import org.joml.Vector3d
import org.valkyrienskies.buggy.BuggyDebugHelper
import org.valkyrienskies.buggy.BuggyItems
import org.valkyrienskies.buggy.PAL.Pin
import org.valkyrienskies.buggy.api.extension.fromPos
import org.valkyrienskies.buggy.blocks.nodes.NodeBlock
import org.valkyrienskies.buggy.mixinducks.server.PALNetworkDuck
import kotlin.random.Random

class ConnectorItem : Item(
    Properties().stacksTo(1).tab(BuggyItems.TAB)
) {

    private var clickedName:BlockPos? = null
    private var clickedA: Pin? = null
    private var clickedB: Pin? = null
    private var clickedPosA: Vector3d? = null
    private var clickedPosB: Vector3d? = null


    override fun useOn(context: UseOnContext): InteractionResult {

        val level = context.level
        val pos = context.clickedPos
        val player = context.player

        if (player?.isCrouching == false) {
            if (level is PALNetworkDuck) {
                if (level.getBlockState(pos).block is NodeBlock) { // if our block implements the pin holder
                    val it: Pin = level.network.getPinFromBlock(pos) // the pin at the block we clicked
                    println("pin: " + it.get())
                    // if block has a pin
                    if (this.clickedA == null) {
                        this.clickedA = it.get()
                        this.clickedPosA = Vec3d().fromPos(pos).conv()
                        this.clickedName = pos
                    } else if (this.clickedB == null) {
                        this.clickedB = it.get()
                        this.clickedPosB = Vec3d().fromPos(pos).conv()
                        this.clickedName = pos
                    }
                }
            }
        } else {
            // if crouching
            resetValues()
        }

        // reset after both pins linked
        if (this.clickedA != null && this.clickedB != null) {
            level.addParticle(ParticleTypes.FLAME,
                pos.x.toDouble() + 0.5,
                pos.y.toDouble() + 1.1,
                pos.z.toDouble() + 0.5,
                0.0, 2.0, 0.0)

            if(this.clickedPosA != null && this.clickedPosB != null) {
                val A = this.clickedPosA!!.add(Vector3d(0.5), Vector3d())
                val B = this.clickedPosB!!.add(Vector3d(0.5), Vector3d())

                BuggyDebugHelper.addConstantDebugLine(Vec3d(A), Vec3d(B), Vec4i(0,255,0,127))
                val dir:Vector3d = this.clickedPosB!!.sub(this.clickedPosA, Vector3d()).normalize()

                BuggyDebugHelper.addConstantDebugArrow(Vec3d( A.add(dir.mul( 1.5, Vector3d()), Vector3d())), Vec3d( B.add(dir.mul(-1.5, Vector3d()), Vector3d())), 0.1, 0.25,  Vec4i(255,0,255,155))
                BuggyDebugHelper.addConstantDebugBox(Vec3d( A.add(dir.mul( 0.75, Vector3d()), Vector3d())), 0.25, Vec4i(255,0,0,255))
                BuggyDebugHelper.addConstantDebugBox(Vec3d( B.add(dir.mul(-0.75, Vector3d()), Vector3d())), 0.25, Vec4i(0,0,255,255))
            }

            connectPins(this.clickedA!!, this.clickedB!!, level as ServerLevel)
        }

        return super.useOn(context)
    }

    override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
        super.inventoryTick(stack, level, entity, slotId, isSelected)

        if(this.clickedName != null) {
            level.addParticle(ParticleTypes.ELECTRIC_SPARK,
                this.clickedName!!.x.toDouble() + 0.5,
                this.clickedName!!.y.toDouble() + 1.1,
                this.clickedName!!.z.toDouble() + 0.5,
                Random.nextDouble(-0.5,0.5), Random.nextDouble(-0.5,0.5), Random.nextDouble(-0.5,0.5))
        }
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {

        if (player.isCrouching) {
            // if crouching
            resetValues()
        }

        return super.use(level, player, usedHand)
    }

    override fun getDescriptionId(): String {
        return if (this.clickedName != null) {
            ("Connector Tool || " + this.clickedName.toString())
        } else {
            ("Connector Tool || Pinless")
        }
    }

    fun connectPins(pinA:Pin, pinB:Pin, level: ServerLevel){
        level as PALNetworkDuck

        if (pinA.id != pinB.id) {
            level.network.addLink(pinA.get(), pinB.get())
        }

        resetValues()
    }

    fun resetValues(){
        this.clickedA = null
        this.clickedB = null
        this.clickedPosA = null
        this.clickedPosB = null
        this.clickedName = null
    }

}