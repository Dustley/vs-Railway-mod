package org.valkyrienskies.buggy.mixin.server;

import org.valkyrienskies.buggy.nodes.Node;

public interface ServerNodeLinkerDuck {

    void addServerNode(Node node);
    void removeServerNode(Node node);

}
