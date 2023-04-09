package org.valkyrienskies.buggy.mixin.server;

import org.valkyrienskies.buggy.mixinducks.server.PALNetworkDuck;
import org.valkyrienskies.buggy.PAL.PALNetwork;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;


@Mixin(ServerLevel.class)
public class PALNetworkMixin implements PALNetworkDuck {

    private static final PALNetwork network = new PALNetwork();
    private static Integer offset = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void Tick(BooleanSupplier hasTimeLeft, CallbackInfo ci){
        network.tick();

        offset++;
        if(offset >= 40){
            offset = 0;
            //network.perSecond();
        }
    }

    @Override
    public PALNetwork getNetwork() {
        return network;
    }
}
