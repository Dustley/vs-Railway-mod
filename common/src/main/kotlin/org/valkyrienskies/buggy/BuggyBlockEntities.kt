package org.valkyrienskies.buggy

import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.util.datafix.fixes.References
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.buggy.blocks.nodes.basic.EmitterNodeBlockEntity
import org.valkyrienskies.buggy.registry.DeferredRegister
import org.valkyrienskies.buggy.registry.RegistrySupplier
import kotlin.reflect.KFunction3

@Suppress("unused")
object BuggyBlockEntities {
    private val BLOCKENTITIES = DeferredRegister.create(BuggyMod.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY)

    val EMITTER_BLOCK = BuggyBlocks.EMITTER_BLOCK withBE ::EmitterNodeBlockEntity byName "emitter"

    fun register() {
        BLOCKENTITIES.applyAll()
    }

    private infix fun <T : BlockEntity> Set<RegistrySupplier<out Block>>.withBE(blockEntity: (BlockPos, BlockState) -> T) =
        Pair(this, blockEntity)

    private infix fun <T : BlockEntity> RegistrySupplier<out Block>.withBE(blockEntity: (BlockPos, BlockState) -> T) =
        Pair(setOf(this), blockEntity)

    private infix fun <T : BlockEntity> Block.withBE(blockEntity: (BlockPos, BlockState) -> T) = Pair(this, blockEntity)
    private infix fun <T : BlockEntity> Pair<Set<RegistrySupplier<out Block>>, (BlockPos, BlockState) -> T>.byName(name: String): RegistrySupplier<BlockEntityType<T>> =
        BLOCKENTITIES.register(name) {
            val type = Util.fetchChoiceType(References.BLOCK_ENTITY, name)

            BlockEntityType.Builder.of(
                this.second,
                *this.first.map { it.get() }.toTypedArray()
            ).build(type)
        }
}
