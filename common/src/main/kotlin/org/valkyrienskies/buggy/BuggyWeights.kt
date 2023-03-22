package org.valkyrienskies.buggy

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.valkyrienskies.core.apigame.world.chunks.BlockType
import org.valkyrienskies.mod.common.BlockStateInfo
import org.valkyrienskies.mod.common.BlockStateInfoProvider
import org.valkyrienskies.mod.common.vsCore

object BuggyWeights : BlockStateInfoProvider {
    override val priority: Int
        get() = 400

    override fun getBlockStateMass(blockState: BlockState): Double? {
        return null
    }

    override fun getBlockStateType(blockState: BlockState): BlockType? {
        if (blockState.block == BuggyBlocks.BEARING_TOP.get())
            return vsCore.blockTypes.air

        return null
    }

    fun register() {
        Registry.register(BlockStateInfo.REGISTRY, ResourceLocation(BuggyMod.MOD_ID, "bearing"), BuggyWeights)
        Registry.register(BlockStateInfo.REGISTRY, ResourceLocation(BuggyMod.MOD_ID, "bearing"), BuggyWeights)
    }
}
