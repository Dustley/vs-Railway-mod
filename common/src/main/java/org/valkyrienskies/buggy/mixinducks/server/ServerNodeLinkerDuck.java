package org.valkyrienskies.buggy.mixinducks.server;


import net.minecraft.core.BlockPos;
import org.valkyrienskies.buggy.nodes.Node;

public interface ServerNodeLinkerDuck {

    void addServerNode(Node node, BlockPos pos);
    void removeServerNode(Node node, BlockPos pos);

}
