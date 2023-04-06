package org.valkyrienskies.buggy.items.tools

import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import org.valkyrienskies.buggy.BuggyItems
import org.valkyrienskies.buggy.nodes.INodeBlock
import org.valkyrienskies.buggy.nodes.Node
import kotlin.random.Random

class ConnectorItem : Item(
    Properties().stacksTo(1).tab(BuggyItems.TAB)
) {

    private var clickedName:BlockPos? = null
    private var clickedA: Node? = null
    private var clickedB: Node? = null


    override fun useOn(context: UseOnContext): InteractionResult {

        val level = context.level
        val pos = context.clickedPos.immutable()
        val player = context.player

        if (player?.isCrouching == false) {
            if (level is ServerLevel) {
                if (level.getBlockState(pos).block is INodeBlock) { // if our block implements the node holder
                    val nodeBlock = level.getBlockState(pos).block as INodeBlock // the block as a node holder
                    val node: Node = nodeBlock.node // the node at the block we clicked
                    // if block has a node
                    if (clickedA == null) {
                        clickedA = node
                        clickedName = pos
                    } else if (clickedB == null) {
                        clickedB = node
                        clickedName = pos
                    }
                }
            }
        } else {
            // if crouching
            clickedA = null
            clickedB = null
            clickedName = null
        }

        // reset after both nodes linked
        if (clickedA != null && clickedB != null) {
            level.addParticle(ParticleTypes.ENCHANTED_HIT,
                pos.x.toDouble() + 0.5,
                pos.y.toDouble() + 1.0,
                pos.z.toDouble() + 0.5,
                0.0, 2.0, 0.0)

            connectNodes(clickedA!!, clickedB!!)
        }

        return super.useOn(context)
    }

    override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
        super.inventoryTick(stack, level, entity, slotId, isSelected)

        if(clickedName != null) {
            level.addParticle(ParticleTypes.ELECTRIC_SPARK,
                clickedName!!.x.toDouble() + Random.nextInt(0,1).toDouble(),
                clickedName!!.y.toDouble() + Random.nextInt(0,1).toDouble(),
                clickedName!!.z.toDouble() + Random.nextInt(0,1).toDouble(),
                Random.nextDouble(-0.5,0.5), Random.nextDouble(-0.5,0.5), Random.nextDouble(-0.5,0.5))
        }
    }

    override fun getDescriptionId(): String {
        return if (clickedName != null) {
            ("Connector Tool || " + clickedName.toString())
        } else {
            ("Connector Tool || Nodeless")
        }
    }

    fun connectNodes(nodeA:Node, nodeB:Node){
        if (nodeA != nodeB) {
            nodeA.connectTo(nodeB)
        }

        clickedA = null
        clickedB = null
        clickedName = null
    }

}