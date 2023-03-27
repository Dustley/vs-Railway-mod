package org.valkyrienskies.buggy.items.tools

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import org.valkyrienskies.buggy.BuggyItems
import org.valkyrienskies.buggy.nodes.INodeBlock
import org.valkyrienskies.buggy.nodes.Node

class ConnectorItem : Item(
    Properties().stacksTo(1).tab(BuggyItems.TAB)
) {

    private var clickedName:String? = null
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
                        clickedName = pos.toString()
                    } else if (clickedB == null) {
                        clickedB = node
                        clickedName = pos.toString()
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
        if (clickedA != null && clickedB != null) { connectNodes(clickedA!!, clickedB!!) }

        return super.useOn(context)
    }

    override fun getDescriptionId(): String {
        return if (clickedName != null) {
            ("Connector Tool || " + clickedName)
        } else {
            ("Connector Tool || Nodeless")
        }
    }

    fun connectNodes(nodeA:Node, nodeB:Node){
        if (nodeA != nodeB) {
            nodeA.connectOut(nodeB)
            nodeB.connectIn(nodeA)
        }

        clickedA = null
        clickedB = null
        clickedName = null
    }

}