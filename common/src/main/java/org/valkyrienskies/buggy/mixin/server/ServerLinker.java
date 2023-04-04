package org.valkyrienskies.buggy.mixin.server;

import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.buggy.nodes.Node;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class ServerLinker implements ServerNodeLinkerDuck {
    ArrayList<Node> nodeMap = new ArrayList<>();

    @Inject(method = "tick", at = @At("HEAD"))
    private void Tick(BooleanSupplier hasTimeLeft, CallbackInfo ci){
        nodeMap.forEach((e) -> {
            e.tick();
            System.out.print(e + " ");
        });
    }

    @Override
    public void addServerNode(Node node) {
        nodeMap.add(node);
    }

    @Override
    public void removeServerNode(Node node) {
        nodeMap.remove(node);
    }
}
