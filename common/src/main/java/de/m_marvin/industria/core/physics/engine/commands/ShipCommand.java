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
import de.m_marvin.industria.core.physics.types.ShipPosition;
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

public class ShipCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ship").requires((source) -> {
                    return source.hasPermission(2);
                })
                .then(
                        Commands.literal("create")
                                .then(
                                        Commands.argument("pos1", BlockPosArgument.blockPos())
                                                .executes((source) ->
                                                        createShip(source, BlockPosArgument.getLoadedBlockPos(source, "pos1"), BlockPosArgument.getLoadedBlockPos(source, "pos1"), 1F)
                                                )
                                                .then(
                                                        Commands.argument("pos2", BlockPosArgument.blockPos())
                                                                .executes((source) ->
                                                                        createShip(source, BlockPosArgument.getLoadedBlockPos(source, "pos1"), BlockPosArgument.getLoadedBlockPos(source, "pos2"), 1F)
                                                                )
                                                                .then(
                                                                        Commands.argument("scale", FloatArgumentType.floatArg(0.0625F, 8F))
                                                                                .executes((source) ->
                                                                                        createShip(source, BlockPosArgument.getLoadedBlockPos(source, "pos1"), BlockPosArgument.getLoadedBlockPos(source, "pos2"), FloatArgumentType.getFloat(source, "scale"))
                                                                                )
                                                                )
                                                )
                                )
                )
                .then(
                        Commands.literal("assemble")
                                .then(
                                        Commands.argument("startPos", BlockPosArgument.blockPos())
                                                .executes((source) ->
                                                        assembleShip(source, BlockPosArgument.getLoadedBlockPos(source, "startPos"), 1F)
                                                )
                                                .then(
                                                        Commands.argument("scale", FloatArgumentType.floatArg(0.0625F, 8F))
                                                                .executes((source) ->
                                                                        assembleShip(source, BlockPosArgument.getLoadedBlockPos(source, "startPos"), FloatArgumentType.getFloat(source, "scale"))
                                                                )
                                                )
                                )
                )
                .then(
                        Commands.literal("remove")
                                .then(
                                        Commands.argument("ship", ShipIdArgument.ship())
                                                .executes((source) ->
                                                        removeShip(source, ShipIdArgument.getShip(source, "ship"))
                                                )
                                )
                )
                .then(
                        Commands.literal("teleport")
                                .then(
                                        Commands.argument("ship", ShipIdArgument.ship())
                                                .then(
                                                        Commands.argument("position", Vec3Argument.vec3())
                                                                .executes((source) ->
                                                                        teleportShip(source, ShipIdArgument.getShip(source, "ship"), Vec3Argument.getVec3(source, "position"), false, 0F, 0F, 0F)
                                                                )
                                                )
                                )
                )
                .then(
                        Commands.literal("find")
                                .then(
                                        Commands.argument("ship", ShipIdArgument.ship())
                                                .executes((source) ->
                                                        findShip(source, ShipIdArgument.getShip(source, "ship"), false)
                                                )
                                                .then(
                                                        Commands.literal("teleport")
                                                                .executes((source) ->
                                                                        findShip(source, ShipIdArgument.getShip(source, "ship"), true)
                                                                )
                                                )
                                )
                )
                .then(
                        Commands.literal("name")
                                .then(
                                        Commands.argument("ship", ShipIdArgument.ship())
                                                .then(
                                                        Commands.argument("name", StringArgumentType.greedyString())
                                                                .executes((source) ->
                                                                        setName(source, ShipIdArgument.getShip(source, "ship"), StringArgumentType.getString(source, "name"))
                                                                )
                                                )
                                )
                ));
    }

    public static int setName(CommandContext<CommandSourceStack> source, Ship ship, String name) {

        PhysicUtility.setShipName(ship.getId(), name);

        source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.ship.name.set", name), true);
        return Command.SINGLE_SUCCESS;

    }

    public static int findShip(CommandContext<CommandSourceStack> source, Ship ship, boolean teleport) {

        Vec3d shipPosition = PhysicUtility.getPosition((ServerShip) ship, false).getPosition();

        if (teleport) {

            if (source.getSource().getEntity() instanceof Player player) {

                player.teleportTo(shipPosition.x, shipPosition.y, shipPosition.z);

                source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.ship.find.teleported", shipPosition.x().intValue(), shipPosition.y().intValue(), shipPosition.z().intValue()), true);
                return Command.SINGLE_SUCCESS;

            } else {

                source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.ship.find.noplayer"), true);
                return 0;

            }

        } else {

            Component coordMsgComp = new TranslatableComponent("buggy.commands.coordinates", shipPosition.x().intValue(), shipPosition.y().intValue(), shipPosition.z().intValue()).withStyle((style) -> {
                return style
                        .withColor(ChatFormatting.DARK_GRAY)
                        .withClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/tp @s " + shipPosition.x() + " " + shipPosition.y() + " " + shipPosition.z()))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("chat.coordinates.tooltip")));
            });

            source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.ship.find.found", coordMsgComp), true);
            return Command.SINGLE_SUCCESS;

        }

    }

    public static int teleportShip(CommandContext<CommandSourceStack> source, Ship ship, Vec3 position, boolean rotate, float rotationX, float rotationY, float rotationZ) {

        ShipPosition shipPos = null;
        if (rotate) {
            shipPos = new ShipPosition(Quaternion.fromXYZDegrees(rotationX, rotationY, rotationZ), Vec3d.fromVec(position));
        } else {
            shipPos = PhysicUtility.getPosition((ServerShip) ship, false);
            shipPos.getPosition().setI(Vec3d.fromVec(position));
        }

        PhysicUtility.setPosition((ServerShip) ship, shipPos, false);

        source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.ship.teleport.success", (int) position.x(), (int) position.y(), (int) position.z()), true);
        return Command.SINGLE_SUCCESS;

    }

    public static int createShip(CommandContext<CommandSourceStack> source, BlockPos pos1, BlockPos pos2, float scale) {

        AABB bounds = new AABB(MathUtility.getMinCorner(pos1, pos2), MathUtility.getMaxCorner(pos1, pos2));

        if (bounds.getXsize() > 32 || bounds.getYsize() > 32 || bounds.getZsize() > 32) {
            source.getSource().sendFailure(new TranslatableComponent("buggy.commands.ship.create.tolarge", (int) bounds.getXsize(), (int) bounds.getYsize(), (int) bounds.getZsize(), 32, 32, 32));
            return 0;
        }

        Ship ship = PhysicUtility.convertToShip(bounds, true, scale, source.getSource().getLevel());

        BlockPos minCorner = MathUtility.getMinCorner(pos1, pos2);

        if (ship != null) {
            source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.ship.create.success", (int) minCorner.getX(), (int) minCorner.getY(), (int) minCorner.getZ()), true);
            return Command.SINGLE_SUCCESS;
        }

        source.getSource().sendFailure(new TranslatableComponent("buggy.commands.ship.create.failed"));
        return 0;

    }

    public static int assembleShip(CommandContext<CommandSourceStack> source, BlockPos startPos, float scale) {

        Optional<List<BlockPos>> structureBlocks = StructureFinder.findStructure(source.getSource().getLevel(), startPos, 16 * 16 * 16, state -> PhysicUtility.isValidShipBlock(state));

        if (structureBlocks.isEmpty()) {

            source.getSource().sendFailure(new TranslatableComponent("buggy.commands.ship.assemble.toLarge", 16 * 16 * 16));
            return 0;

        }

        ServerShip ship = PhysicUtility.assembleToShip(source.getSource().getLevel(), structureBlocks.get(), true, scale);

        if (ship != null) {
            source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.ship.assemble.success", startPos.getX(), startPos.getY(), startPos.getZ()), true);
            return Command.SINGLE_SUCCESS;
        }

        source.getSource().sendFailure(new TranslatableComponent("buggy.commands.ship.assemble.failed"));
        return 0;

    }

    public static int removeShip(CommandContext<CommandSourceStack> source, Ship ship) {

        PhysicUtility.removeShip(source.getSource().getLevel(), ship);

        source.getSource().sendSuccess(new TranslatableComponent("buggy.commands.ship.remove.success"), true);
        return Command.SINGLE_SUCCESS;

    }

}