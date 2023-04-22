package org.valkyrienskies.buggy

import net.minecraft.core.Registry
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import org.valkyrienskies.buggy.blocks.nodes.NodeBlock
import org.valkyrienskies.buggy.blocks.nodes.basic.*
import org.valkyrienskies.buggy.blocks.nodes.basic.display.DisplayNodeBlock
import org.valkyrienskies.buggy.blocks.nodes.basic.emitter.EmitterNodeBlock
import org.valkyrienskies.buggy.registry.DeferredRegister

@Suppress("unused")
object BuggyBlocks {
    private val BLOCKS = DeferredRegister.create(BuggyMod.MOD_ID, Registry.BLOCK_REGISTRY)

    val EMITTER_BLOCK                = BLOCKS.register("emitter", ::EmitterNodeBlock)
    val DISPLAY_BLOCK                = BLOCKS.register("display", ::DisplayNodeBlock)


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
