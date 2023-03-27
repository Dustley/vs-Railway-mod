package de.m_marvin.industria.core.physics.engine.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import org.valkyrienskies.core.api.ships.Ship;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import de.m_marvin.industria.core.physics.PhysicUtility;
import de.m_marvin.industria.core.physics.types.ShipHitResult;
import de.m_marvin.univec.impl.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult.Type;

public class ShipIdArgument implements ArgumentType<de.m_marvin.industria.core.physics.engine.commands.ShipIdArgument.ShipSelector> {

    public static final String SHIP_PREFIX = "";
    public static final Collection<String> EXAMPLES = Arrays.asList("ship1", "ship42", "examplename");
    public static final DynamicCommandExceptionType ERROR_NON_EXISTING_SHIP = new DynamicCommandExceptionType((object) -> {
        return new TranslatableComponent("buggy.argument.ship.notFound", object);
    });

    public static ShipIdArgument ship() {
        return new ShipIdArgument();
    }

    public static ShipSelector getShipSelector(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return context.getArgument(name, ShipSelector.class);
    }

    public static Long getShipId(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        ShipSelector selector = getShipSelector(context, name);
        Long id = selector.findShipId(context.getSource().getLevel());
        if (id != null && selector.isNamed()) {
            throw ERROR_NON_EXISTING_SHIP.create(selector.getName());
        }
        return id;
    }

    public static Ship getShip(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        Long id = getShipId(context, name);
        if (id == null) {
            throw ERROR_NON_EXISTING_SHIP.create(id);
        }
        Ship ship = PhysicUtility.getShipById(context.getSource().getLevel(), id);
        if (ship == null) {
            throw ERROR_NON_EXISTING_SHIP.create(id);
        }
        return ship;
    }

    @Override
    public ShipSelector parse(StringReader reader) throws CommandSyntaxException {
        String inputStr = reader.readString();
        long id = tryParseId(inputStr);
        if (id == 0) {
            return ShipSelector.byName(inputStr);
        } else {
            return ShipSelector.byId(id);
        }
    }

    public Long tryParseId(String input) {
        try {
            if (input.startsWith(SHIP_PREFIX)) {
                String idStr = input.substring(SHIP_PREFIX.length());
                return Long.parseLong(idStr);
            }
            return 0L;
        } catch (Exception e) {}
        return 0L;
    }

    @SuppressWarnings("resource")
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {

        if (context.getSource() instanceof SharedSuggestionProvider) {

            ClientLevel level = Minecraft.getInstance().level;
            Player player = Minecraft.getInstance().player;
            Vec3d eyePos = Vec3d.fromVec(player.getEyePosition());
            Vec3d direction = Vec3d.fromVec(player.getViewVector(0));

            ShipHitResult result = PhysicUtility.clipForShip(level, eyePos, direction, 10000000.0);

            if (result.getType() != Type.MISS) {
                long shipId = result.getShip().getId();
                builder.suggest(SHIP_PREFIX + shipId);
            } else {
                for (Ship ship : PhysicUtility.getLoadedShips(level)) {
                    builder.suggest(SHIP_PREFIX + ship.getId());
                }
            }

        }

        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static class ShipSelector {

        protected String name;
        protected Long id;

        public ShipSelector(String name, Long id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public Long getId() {
            return id;
        }

        public boolean isNamed() {
            return this.name != null;
        }

        public boolean hasId() {
            return this.id != null;
        }

        public static ShipSelector byName(String name) {
            return new ShipSelector(name, 0L);
        }

        public static ShipSelector byId(long id) {
            return new ShipSelector(null, id);
        }

        public Long findShipId(ServerLevel level) {
            if (this.hasId()) {
                return this.id;
            } else {
                return PhysicUtility.getShipIdByName(level, this.name);
            }
        }

    }

}
