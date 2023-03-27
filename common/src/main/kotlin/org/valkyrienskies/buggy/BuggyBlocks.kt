package org.valkyrienskies.buggy

import net.minecraft.core.Registry
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import org.valkyrienskies.buggy.blocks.*
import org.valkyrienskies.buggy.blocks.bearings.BearingBaseBlock
import org.valkyrienskies.buggy.blocks.MechanicalTopBlock
import org.valkyrienskies.buggy.blocks.logic.PowerEmitBlock
import org.valkyrienskies.buggy.blocks.motors.EngineBlock
import org.valkyrienskies.buggy.blocks.movement.TreadBlock
import org.valkyrienskies.buggy.blocks.seats.SeatBlock
import org.valkyrienskies.buggy.blocks.springs.SpringBaseBlock
import org.valkyrienskies.buggy.registry.DeferredRegister

@Suppress("unused")
object BuggyBlocks {
    private val BLOCKS = DeferredRegister.create(BuggyMod.MOD_ID, Registry.BLOCK_REGISTRY)

//    val SHIP_ASSEMBLER           = BLOCKS.register("ship_assembler", ::ShipAssemblerBlock)
//    val BALLAST                  = BLOCKS.register("ballast", ::BallastBlock)
//    val BALLOON                  = BLOCKS.register("balloon", ::BalloonBlock)
//    val THRUSTER                 = BLOCKS.register("thruster", ::ThrusterBlock)
//    val THRUSTER_TINY            = BLOCKS.register("tiny_thruster", ::TinyThrusterBlock)
//    val SPINNER                  = BLOCKS.register("spinner", ::SpinnerBlock)
//    val SEAT                     = BLOCKS.register("seat", ::SeatBlock)
//    val ROPE_HOOK                = BLOCKS.register("rope_hook", ::RopeHookBlock)

    // DEBUG BLOCKS:
//    val TARGETER                 = BLOCKS.register("targeter", ::TargeterBlock)

    val BASIC_SEAT                  = BLOCKS.register("basic_seat", ::SeatBlock)
    val BASIC_ENGINE                = BLOCKS.register("basic_engine", ::EngineBlock)

    val BEARING                     = BLOCKS.register("bearing", ::BearingBaseBlock)
    val SPRING                      = BLOCKS.register("spring", ::SpringBaseBlock)
    val MECHANICAL_TOP              = BLOCKS.register("mechanical_top", ::MechanicalTopBlock)

    val BASIC_TREAD                 = BLOCKS.register("basic_tread", ::TreadBlock)
    val EMMITOR                     = BLOCKS.register("emmitor", ::PowerEmitBlock)

    fun register() {
        BLOCKS.applyAll()
    }

    fun registerItems(items: DeferredRegister<Item>) {
        BLOCKS.forEach {
            if (it != MECHANICAL_TOP ){
                items.register(it.name) { BlockItem(it.get(), Item.Properties().tab(BuggyItems.TAB)) }
            }
        }
    }

}
