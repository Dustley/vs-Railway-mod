package org.valkyrienskies.buggy.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import de.m_marvin.industria.core.physics.engine.commands.ShipCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.valkyrienskies.core.impl.config.VSConfigClass;
import org.valkyrienskies.buggy.BuggyConfig;
import org.valkyrienskies.buggy.BuggyMod;
import org.valkyrienskies.mod.compat.clothconfig.VSClothConfig;
import org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric;

public class BuggyModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // force VS2 to load before Buggy
        new ValkyrienSkiesModFabric().onInitialize();

        // commands
        CommandRegistrationCallback.EVENT.register((dispatcher, environment) -> { ShipCommand.register(dispatcher); });

        BuggyMod.init();
    }

    @Environment(EnvType.CLIENT)
    public static class Client implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
            BuggyMod.initClient();
        }
    }

    public static class ModMenu implements ModMenuApi {
        @Override
        public ConfigScreenFactory<?> getModConfigScreenFactory() {
            return (parent) -> VSClothConfig.createConfigScreenFor(
                    parent,
                    VSConfigClass.Companion.getRegisteredConfig(BuggyConfig.class)
            );
        }
    }
}
