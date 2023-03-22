package org.valkyrienskies.buggy.services

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

interface BuggyPlatformHelper {
    fun createCreativeTab(id: ResourceLocation, stack: () -> ItemStack): CreativeModeTab
}