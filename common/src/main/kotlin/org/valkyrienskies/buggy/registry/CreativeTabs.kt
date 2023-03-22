package org.valkyrienskies.buggy.registry

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import org.valkyrienskies.buggy.services.BuggyPlatformHelper
import java.util.ServiceLoader

class CreativeTabs {
    companion object {
        fun create(id: ResourceLocation, stack: () -> ItemStack): CreativeModeTab {
            return ServiceLoader.load(BuggyPlatformHelper::class.java)
                .findFirst()
                .get()
                .createCreativeTab(id, stack)
        }
    }
}