package org.valkyrienskies.buggy

import net.minecraft.core.Registry
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import org.valkyrienskies.buggy.blocks.*
import org.valkyrienskies.buggy.blocks.nodes.NodeBlock
import org.valkyrienskies.buggy.blocks.nodes.basic.*
import org.valkyrienskies.buggy.registry.DeferredRegister

@Suppress("unused")
object BuggyBlocks {
    private val BLOCKS = DeferredRegister.create(BuggyMod.MOD_ID, Registry.BLOCK_REGISTRY)


//    val BASIC_SEAT                  = BLOCKS.register("basic_seat", ::SeatBlock)
//
//    val BEARING                     = BLOCKS.register("bearing", ::BearingBaseBlock)
//    val ENGINE                      = BLOCKS.register<Block>("motor", ::MotorBaseBlock)
//    val SPRING                      = BLOCKS.register("spring", ::SpringBaseBlock)
//    val MECHANICAL_TOP              = BLOCKS.register("mechanical_top", ::MechanicalTopBlock)
//
//    val BASIC_TREAD                 = BLOCKS.register("basic_tread", ::TreadBlock)
//
//    // NODES
//        // Basic
//    val EMMITOR                     = BLOCKS.register("emmitor", ::PowerEmitBlock)
//    val WEAKEMMITOR                 = BLOCKS.register("weak_emmitor", ::NodeBlock)
//    val SWITCH                      = BLOCKS.register("switch", ::SwitchEmitBlock)
//    val DISPLAYBLOCK                = BLOCKS.register("display", ::DisplayBlock)
//
//        // Math
//    val ADDINGBLOCK                 = BLOCKS.register("adder", ::AddingBlock)
//    val SUBTRACTBLOCK               = BLOCKS.register("subtract", ::AddingBlock)
//    val MULTIPLYBLOCK               = BLOCKS.register("multiplier", ::MultiplyingBlock)
//    val INVERTINGBLOCK              = BLOCKS.register("inverter", ::InvertingBlock)

    val EMMITINGBLOCK                = BLOCKS.register("emmitor", ::EmmitingNodeBlock)
    val ADDINGBLOCK                  = BLOCKS.register("adder", ::AddingNodeBlock)
    val SUBTRACTINGBLOCK             = BLOCKS.register("subtractor", ::SubtractingNodeBlock)
    val MULTIPLYINGBLOCK             = BLOCKS.register("multiplier", ::MultiplyingNodeBlock)
    val DISPLAYBLOCK                 = BLOCKS.register("display", ::DisplayNodeBlock)


    fun register() {
        BLOCKS.applyAll()
    }

    fun registerItems(items: DeferredRegister<Item>) {
        BLOCKS.forEach {
                if (it.get() is NodeBlock) {
                    items.register(it.name) { BlockItem(it.get(), Item.Properties().tab(BuggyItems.NODE_TAB)) }
                } else {
                    items.register(it.name) { BlockItem(it.get(), Item.Properties().tab(BuggyItems.TAB)) }
                }
        }
    }

}
