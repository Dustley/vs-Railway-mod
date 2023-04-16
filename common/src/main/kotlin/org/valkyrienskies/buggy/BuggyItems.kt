package org.valkyrienskies.buggy

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.*
import net.minecraft.world.level.block.Blocks
import org.valkyrienskies.buggy.items.*
import org.valkyrienskies.buggy.items.tools.ConnectorItem
import org.valkyrienskies.buggy.items.tools.HammerItem
import org.valkyrienskies.buggy.registry.CreativeTabs
import org.valkyrienskies.buggy.registry.DeferredRegister

@Suppress("unused")
object BuggyItems {
    private val ITEMS = DeferredRegister.create(BuggyMod.MOD_ID, Registry.ITEM_REGISTRY)

    val TAB: CreativeModeTab = CreativeTabs.create(
        ResourceLocation(
            BuggyMod.MOD_ID,
            "buggy_tab"
        )
    ) { ItemStack(Items.BUCKET) }

    val TOOL_CONNECTOR                    = ITEMS.register("connector", ::ConnectorItem)
    val TOOL_HAMMER                       = ITEMS.register("hammer", ::HammerItem)


    val NODE_TAB: CreativeModeTab = CreativeTabs.create(
        ResourceLocation(
            BuggyMod.MOD_ID,
            "buggy_node_tab"
        )
    ) { ItemStack(Blocks.CHAIN_COMMAND_BLOCK) }

    fun register() {
        BuggyBlocks.registerItems(ITEMS)

        ITEMS.applyAll()
    }

    private infix fun Item.byName(name: String) = ITEMS.register(name) { this }
}
