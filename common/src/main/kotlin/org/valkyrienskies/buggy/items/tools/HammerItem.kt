package org.valkyrienskies.buggy.items.tools

import de.m_marvin.univec.impl.Vec3d
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.context.UseOnContext
import org.valkyrienskies.buggy.BuggyConfig
import org.valkyrienskies.buggy.BuggyItems
import org.valkyrienskies.buggy.api.extension.fromVVec
import org.valkyrienskies.buggy.ship.PulseShipControl
import org.valkyrienskies.mod.common.getShipObjectManagingPos
import kotlin.random.Random

class HammerItem : Item(
    Properties().stacksTo(1).durability(240).tab(BuggyItems.TAB).defaultDurability(240).fireResistant()
){

    private var pulseForce : Vec3d? = null

    override fun useOn(context: UseOnContext): InteractionResult {
        val force = BuggyConfig.SERVER.HammerForce

        val particleForce = 0.2
        val particleCount = 10

        val player = context.player
        player?.cooldowns?.addCooldown(this, 15)
        val blockPosition = context.clickedPos
        val Location = Vec3d().fromVVec(context.clickLocation)

        repeat(particleCount) { i ->
            context.level.addParticle(ParticleTypes.ELECTRIC_SPARK, Location.x, Location.y, Location.z, Random.nextDouble(-particleForce,particleForce),Random.nextDouble(-particleForce,particleForce),Random.nextDouble(-particleForce,particleForce))
            context.level.addParticle(ParticleTypes.SMOKE, Location.x, Location.y, Location.z, Random.nextDouble(-particleForce,particleForce),Random.nextDouble(-particleForce,particleForce),Random.nextDouble(-particleForce,particleForce))
        }


        if(context.level.isClientSide || player == null) {
            return InteractionResult.PASS
        }

        val level = context.level
        if(level !is ServerLevel){
            return InteractionResult.PASS
        }

        val ship = level.getShipObjectManagingPos(blockPosition) ?: return InteractionResult.PASS

        pulseForce = Vec3d().fromVVec(player.lookAngle).normalize().mul(force)

        PulseShipControl.getOrCreate(ship).addPulse(Location, pulseForce!!)

        return super.useOn(context)
    }

    override fun getUseAnimation(stack: ItemStack): UseAnim {
        return UseAnim.SPEAR
    }

    override fun getRarity(stack: ItemStack): Rarity {
        return Rarity.EPIC
    }

    override fun getBarColor(stack: ItemStack): Int {
        return 1
    }

}