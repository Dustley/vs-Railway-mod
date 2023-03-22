package org.valkyrienskies.buggy.items.tools

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import org.valkyrienskies.buggy.BuggyProperties
import org.valkyrienskies.buggy.blocks.NodeBlock
import org.valkyrienskies.core.api.ships.properties.ShipId
import org.valkyrienskies.mod.common.getShipObjectManagingPos

class ConnectorItem : Item(
    Properties().stacksTo(1)
) {

    private var clickedPosition: BlockPos? = null
    private var clickedShipId: ShipId? = null


    override fun useOn(context: UseOnContext): InteractionResult {

        val level = context.level
        val blockPos = context.clickedPos.immutable()
        val ship = context.level.getShipObjectManagingPos(blockPos)
        val player: Player? = context.player
        var shipID: ShipId?

        if(ship != null) {
            shipID = ship.id
        } else {
            shipID = null
        }

        if (level is ServerLevel) {

            // if its a hook block
            //if (level.getBlockState(blockPos).block == tournamentBlocks.ROPEHOOK.get()) {
            if (level.getBlockState(blockPos).block is NodeBlock != null) {

                println("connect" + level.getBlockState(blockPos))

            }
        }
        return super.useOn(context)
    }


}