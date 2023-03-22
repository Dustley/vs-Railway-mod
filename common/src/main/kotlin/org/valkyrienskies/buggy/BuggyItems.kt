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

    val TOOL_CONNECTOR                    = ITEMS.register("connector", ::ConnectorItem)
//    val TOOL_PULSEGUN           = ITEMS.register("pulse_gun", ::PulseGunItem)
//    val TOOL_DELETEWAND         = ITEMS.register("delete_wand", ::ShipDeleteWandItem)
//    val TOOL_GRABGUN            = ITEMS.register("grab_gun", ::GrabGunItem)
//    val UPGRADE_THRUSTER        = ITEMS.register("upgrade_thruster", ::ThrusterUpgradeItem)

    val TAB: CreativeModeTab = CreativeTabs.create(
        ResourceLocation(
            BuggyMod.MOD_ID,
            "buggy_tab"
        )
    ) { ItemStack(Blocks.IRON_BLOCK) }

    fun register() {
        BuggyBlocks.registerItems(ITEMS)
        ITEMS.applyAll()
    }

    private infix fun Item.byName(name: String) = ITEMS.register(name) { this }
}