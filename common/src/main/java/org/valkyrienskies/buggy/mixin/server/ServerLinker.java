package org.valkyrienskies.buggy.mixin.server;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.buggy.mixinducks.server.ServerNodeLinkerDuck;
import org.valkyrienskies.buggy.nodes.Node;

import java.util.HashMap;
import java.util.function.BooleanSupplier;


@Mixin(ServerLevel.class)
public class ServerLinker implements ServerNodeLinkerDuck {
    HashMap<Long, Node> nodeMap = new HashMap<>();

    @Inject(method = "tick", at = @At("HEAD"))
    private void Tick(BooleanSupplier hasTimeLeft, CallbackInfo ci){
        nodeMap.forEach((e, i) -> i.tick());
        // ticks all nodes
    }

    @Override
    public void addServerNode(Node node, BlockPos pos) {
        nodeMap.put(pos.asLong(), node);
    }

    @Override
    public void removeServerNode(Node node, BlockPos pos) {
        nodeMap.remove(pos.asLong(), node);
    }
}
