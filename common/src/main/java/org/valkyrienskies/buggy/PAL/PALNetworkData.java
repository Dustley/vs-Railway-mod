package org.valkyrienskies.buggy.PAL;


import net.minecraft.core.BlockPos;
import org.valkyrienskies.buggy.PAL.PinTypes.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PALNetworkData implements Serializable {

    public final HashMap<Long, Pin> pins = new HashMap<Long, Pin>();
    public final HashMap<Long, Link> links = new HashMap<Long, Link>();
    public final HashMap<BlockPos, Pin> posToPin = new HashMap<BlockPos, Pin>();
    public final List<Link> tagRemoveLinks = new ArrayList<Link>();

}
