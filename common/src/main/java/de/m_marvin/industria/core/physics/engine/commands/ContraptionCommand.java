package de.m_marvin.industria.core.physics.engine.commands;

import java.util.List;
import java.util.Optional;

import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import de.m_marvin.industria.core.physics.PhysicUtility;
import de.m_marvin.industria.core.physics.types.ContraptionPosition;
import de.m_marvin.industria.core.util.MathUtility;
import de.m_marvin.industria.core.util.StructureFinder;
import de.m_marvin.unimat.impl.Quaternion;
import de.m_marvin.univec.impl.Vec3d;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ContraptionCommand {

    public static int setName(CommandContext<CommandSourceStack> source, Ship contraption, String name) {

        PhysicUtility.setContraptionName(source.getSource().getLevel(), contraption, name);

        source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.contraption.name.set", name), true);
        return Command.SINGLE_SUCCESS;

    }

    public static int findContraption(CommandContext<CommandSourceStack> source, Ship contraption, boolean teleport) {

        Vec3d contraptionPosition = PhysicUtility.getPosition((ServerShip) contraption, false).getPosition();

        if (teleport) {

            if (source.getSource().getEntity() instanceof Player player) {

                player.teleportTo(contraptionPosition.x, contraptionPosition.y, contraptionPosition.z);

                source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.contraption.find.teleported", contraptionPosition.x().intValue(), contraptionPosition.y().intValue(), contraptionPosition.z().intValue()), true);
                return Command.SINGLE_SUCCESS;

            } else {

                source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.contraption.find.noplayer"), true);
                return 0;

            }

        } else {

            Component coordMsgComp = new TranslatableComponent("buggy.commands.coordinates", contraptionPosition.x().intValue(), contraptionPosition.y().intValue(), contraptionPosition.z().intValue()).withStyle((style) -> {
                return style
                        .withColor(ChatFormatting.GREEN)
                        .withClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/tp @s " + contraptionPosition.x() + " " + contraptionPosition.y() + " " + contraptionPosition.z()))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("chat.coordinates.tooltip")));
            });

            source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.contraption.find.found", coordMsgComp), true);
            return Command.SINGLE_SUCCESS;

        }

    }

    public static int teleportContraption(CommandContext<CommandSourceStack> source, Ship contraption, Vec3 position, boolean rotate, float rotationX, float rotationY, float rotationZ) {

        ContraptionPosition contraptionPos = null;
        if (rotate) {
            contraptionPos = new ContraptionPosition(Quaternion.fromXYZDegrees(rotationX, rotationY, rotationZ), Vec3d.fromVec(position));
        } else {
            contraptionPos = PhysicUtility.getPosition((ServerShip) contraption, false);
            contraptionPos.getPosition().setI(Vec3d.fromVec(position));
        }

        PhysicUtility.setPosition((ServerShip) contraption, contraptionPos, false);

        source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.contraption.teleport.success", (int) position.x(), (int) position.y(), (int) position.z()), true);
        return Command.SINGLE_SUCCESS;

    }

    public static int createContraption(CommandContext<CommandSourceStack> source, BlockPos pos1, BlockPos pos2, float scale) {

        AABB bounds = new AABB(MathUtility.getMinCorner(pos1, pos2), MathUtility.getMaxCorner(pos1, pos2));

        if (bounds.getXsize() > 32 || bounds.getYsize() > 32 || bounds.getZsize() > 32) {
            source.getSource().sendFailure(new TranslatableComponent("buggy.commands.contraption.create.tolarge", (int) bounds.getXsize(), (int) bounds.getYsize(), (int) bounds.getZsize(), 32, 32, 32));
            return 0;
        }

        Ship contraption = PhysicUtility.convertToContraption(bounds, true, scale, source.getSource().getLevel());

        BlockPos minCorner = MathUtility.getMinCorner(pos1, pos2);

        if (contraption != null) {
            source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.contraption.create.success", (int) minCorner.getX(), (int) minCorner.getY(), (int) minCorner.getZ()), true);
            return Command.SINGLE_SUCCESS;
        }

        source.getSource().sendFailure(new TranslatableComponent("buggy.commands.contraption.create.failed"));
        return 0;

    }

    public static int assembleContraption(CommandContext<CommandSourceStack> source, BlockPos startPos, float scale) {

        Optional<List<BlockPos>> structureBlocks = StructureFinder.findStructure(source.getSource().getLevel(), startPos, 16 * 16 * 16, state -> PhysicUtility.isValidContraptionBlock(state));

        if (structureBlocks.isEmpty()) {

            source.getSource().sendFailure(new TranslatableComponent("buggy.commands.contraption.assemble.toLarge", 16 * 16 * 16));
            return 0;

        }

        ServerShip contraption = PhysicUtility.assembleToContraption(source.getSource().getLevel(), structureBlocks.get(), true, scale);

        if (contraption != null) {
            source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.contraption.assemble.success", startPos.getX(), startPos.getY(), startPos.getZ(), startPos), true);
            return Command.SINGLE_SUCCESS;
        }

        source.getSource().sendFailure(new TranslatableComponent("buggy.commands.contraption.assemble.failed"));
        return 0;

    }

    public static int removeContraption(CommandContext<CommandSourceStack> source, Ship contraption) {

        PhysicUtility.removeContraption(source.getSource().getLevel(), contraption);

        source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.contraption.remove.success", contraption.getId()), true);
        return Command.SINGLE_SUCCESS;

    }

}