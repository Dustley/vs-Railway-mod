package org.valkyrienskies.buggy

import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.util.datafix.fixes.References
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.buggy.blockentity.*
import org.valkyrienskies.buggy.blockentity.bearing.BearingBlockEntity
import org.valkyrienskies.buggy.blockentity.motors.EngineBlockEntity
import org.valkyrienskies.buggy.blockentity.seat.SeatBlockEntity
import org.valkyrienskies.buggy.blockentity.springs.SpringBlockEntity
import org.valkyrienskies.buggy.registry.DeferredRegister
import org.valkyrienskies.buggy.registry.RegistrySupplier

@Suppress("unused")
object BuggyBlockEntities {
    private val BLOCKENTITIES = DeferredRegister.create(BuggyMod.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY)

//    val TARGETER = BuggyBlocks.TARGETER withBE ::TargeterBlockEntity byName "targeter"
    val BASIC_SEAT          = BuggyBlocks.BASIC_SEAT withBE ::SeatBlockEntity byName "basic_seat"
    val BASIC_ENGINE        = BuggyBlocks.BASIC_ENGINE withBE ::EngineBlockEntity byName "basic_engine"

    val BEARING             = BuggyBlocks.BEARING withBE ::BearingBlockEntity byName "bearing"
    val SPRING              = BuggyBlocks.SPRING withBE ::SpringBlockEntity byName "spring"


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
