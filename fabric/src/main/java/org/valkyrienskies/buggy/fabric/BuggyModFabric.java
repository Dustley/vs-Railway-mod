package org.valkyrienskies.buggy.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
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
