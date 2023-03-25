package org.valkyrienskies.buggy

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import de.m_marvin.industria.core.physics.engine.commands.ContraptionCommand
import de.m_marvin.industria.core.physics.engine.commands.ContraptionIdArgument
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.commands.arguments.coordinates.Vec3Argument
import net.minecraft.core.Registry
import org.valkyrienskies.buggy.registry.DeferredRegister

object BuggyCommands {
    //val COMMANDS = DeferredRegister.create(BuggyMod.MOD_ID, Registry.)

//    fun register() {
//        CommandDispatcher.register(Commands.literal("contraption").requires { source: CommandSourceStack ->
//            source.hasPermission(
//                2
//            )
//        }
//            .then(
//                Commands.literal("create").then(
//                    Commands.argument("pos1", BlockPosArgument.blockPos())
//                        .executes { source: CommandContext<CommandSourceStack?>? ->
//                            ContraptionCommand.createContraption(
//                                source,
//                                BlockPosArgument.getLoadedBlockPos(source, "pos1"),
//                                BlockPosArgument.getLoadedBlockPos(source, "pos1"),
//                                1f
//                            )
//                        }.then(
//                            Commands.argument("pos2", BlockPosArgument.blockPos())
//                                .executes { source: CommandContext<CommandSourceStack?>? ->
//                                    ContraptionCommand.createContraption(
//                                        source,
//                                        BlockPosArgument.getLoadedBlockPos(source, "pos1"),
//                                        BlockPosArgument.getLoadedBlockPos(source, "pos2"),
//                                        1f
//                                    )
//                                }.then(
//                                    Commands.argument("scale", FloatArgumentType.floatArg(0.0625f, 8f))
//                                        .executes { source: CommandContext<CommandSourceStack?>? ->
//                                            ContraptionCommand.createContraption(
//                                                source,
//                                                BlockPosArgument.getLoadedBlockPos(source, "pos1"),
//                                                BlockPosArgument.getLoadedBlockPos(source, "pos2"),
//                                                FloatArgumentType.getFloat(source, "scale")
//                                            )
//                                        })
//                        )
//                )
//            )
//            .then(
//                Commands.literal("assemble").then(
//                    Commands.argument("startPos", BlockPosArgument.blockPos())
//                        .executes { source: CommandContext<CommandSourceStack?>? ->
//                            ContraptionCommand.assembleContraption(
//                                source,
//                                BlockPosArgument.getLoadedBlockPos(source, "startPos"),
//                                1f
//                            )
//                        }.then(
//                            Commands.argument("scale", FloatArgumentType.floatArg(0.0625f, 8f))
//                                .executes { source: CommandContext<CommandSourceStack?>? ->
//                                    ContraptionCommand.assembleContraption(
//                                        source,
//                                        BlockPosArgument.getLoadedBlockPos(source, "startPos"),
//                                        FloatArgumentType.getFloat(source, "scale")
//                                    )
//                                })
//                )
//            )
//            .then(
//                Commands.literal("remove").then(
//                    Commands.argument("contraption", ContraptionIdArgument.contraption())
//                        .executes { source: CommandContext<CommandSourceStack?>? ->
//                            ContraptionCommand.removeContraption(
//                                source,
//                                ContraptionIdArgument.getContraption(source, "contraption")
//                            )
//                        })
//            )
//            .then(
//                Commands.literal("teleport").then(
//                    Commands.argument("contraption", ContraptionIdArgument.contraption())
//                        .then(
//                            Commands.argument("position", Vec3Argument.vec3())
//                                .executes { source: CommandContext<CommandSourceStack?>? ->
//                                    ContraptionCommand.teleportContraption(
//                                        source,
//                                        ContraptionIdArgument.getContraption(source, "contraption"),
//                                        Vec3Argument.getVec3(source, "position"),
//                                        false,
//                                        0f,
//                                        0f,
//                                        0f
//                                    )
//                                })
//                )
//            )
//            .then(
//                Commands.literal("find")
//                    .then(
//                        Commands.argument("contraption", ContraptionIdArgument.contraption())
//                            .executes { source: CommandContext<CommandSourceStack?>? ->
//                                ContraptionCommand.findContraption(
//                                    source,
//                                    ContraptionIdArgument.getContraption(source, "contraption"),
//                                    false
//                                )
//                            }
//                            .then(
//                                Commands.literal("teleport").executes { source: CommandContext<CommandSourceStack?>? ->
//                                    ContraptionCommand.findContraption(
//                                        source,
//                                        ContraptionIdArgument.getContraption(source, "contraption"),
//                                        true
//                                    )
//                                })
//                    )
//            )
//            .then(
//                Commands.literal("name")
//                    .then(
//                        Commands.argument("contraption", ContraptionIdArgument.contraption())
//                            .then(
//                                Commands.argument("name", StringArgumentType.greedyString())
//                                    .executes { source: CommandContext<CommandSourceStack?>? ->
//                                        ContraptionCommand.setName(
//                                            source,
//                                            ContraptionIdArgument.getContraption(source, "contraption"),
//                                            StringArgumentType.getString(source, "name")
//                                        )
//                                    })
//                    )
//            )
//        )
//    }

}