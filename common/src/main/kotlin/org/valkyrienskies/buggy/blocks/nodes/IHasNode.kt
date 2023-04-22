package org.valkyrienskies.buggy.blocks.nodes

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import org.valkyrienskies.buggy.PAL.PALNetwork
import org.valkyrienskies.buggy.PAL.Pin
import org.valkyrienskies.buggy.PAL.PinType
import org.valkyrienskies.buggy.mixinducks.server.PALNetworkDuck

interface IHasNode {

    var pin : Pin?

    fun createPin()
    fun removePin()
}