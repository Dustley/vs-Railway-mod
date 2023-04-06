package org.valkyrienskies.buggy

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import org.valkyrienskies.buggy.items.*
import org.valkyrienskies.buggy.items.tools.ConnectorItem
import org.valkyrienskies.buggy.registry.CreativeTabs
import org.valkyrienskies.buggy.registry.DeferredRegister

@Suppress("unused")
object BuggyItems {
    private val ITEMS = DeferredRegister.create(BuggyMod.MOD_ID, Registry.ITEM_REGISTRY)

    // moved so items can be in tab
    val TAB: CreativeModeTab = CreativeTabs.create(
        ResourceLocation(
            BuggyMod.MOD_ID,
            "buggy_tab"
        )
    ) { ItemStack(BuggyBlocks.EMMITOR.get()) }


    val TOOL_CONNECTOR                    = ITEMS.register("connector", ::ConnectorItem)


    val NODE_TAB: CreativeModeTab = CreativeTabs.create(
        ResourceLocation(
            BuggyMod.MOD_ID,
            "buggy_node_tab"
        )
    ) { ItemStack(TOOL_CONNECTOR.get()) }

    fun register() {
        BuggyBlocks.registerItems(ITEMS)
        ITEMS.applyAll()
    }

    private infix fun Item.byName(name: String) = ITEMS.register(name) { this }
}
