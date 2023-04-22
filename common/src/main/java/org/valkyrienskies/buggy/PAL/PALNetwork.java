package org.valkyrienskies.buggy.PAL;


import de.m_marvin.univec.impl.Vec3d;
import de.m_marvin.univec.impl.Vec4i;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.saveddata.SavedData;
import org.joml.Vector3d;
import org.valkyrienskies.buggy.BuggyConfig;
import org.valkyrienskies.buggy.BuggyDebugHelper;
import org.valkyrienskies.buggy.PAL.PinTypes.*;

import java.util.*;
import java.util.function.Function;

public class PALNetwork{

    private PALNetworkData NetworkData = new PALNetworkData();

    private final HashMap<Long, Pin> pins = NetworkData.pins;
    private final HashMap<Long, Link> links = NetworkData.links;
    private final HashMap<BlockPos, Pin> posToPin = NetworkData.posToPin;

    private final List<Link> tagRemoveLinks = NetworkData.tagRemoveLinks;

    public Pin addPin(Long id, PinType type) {
        Pin pin = switch (type) {
            // MATH
            case ADDING         ->      new AddingPin(id);
            case SUBTRACTING    ->      new SubtractingPin(id);
            case MULTIPLYING    ->      new MultiplyingPin(id);

            // MISC
            case EMMITING       ->      new EmmitingPin(id);
            case TOGGLE         ->      new TogglePin(id);
            case DISPLAY        ->      new DisplayPin(id);

            // NONE
            default -> new Pin(id);
        };

        pins.put(id, pin);
        return pin;
    }
    public Pin addPin(PinType type) {
        return addPin(GetNextPinID(), type);
    }
    public Pin addPin(PinType type, BlockPos pos) {
        Pin i = addPin(GetNextPinID(), type);
        posToPin.put(pos, i);
        return i;
    }
    public Pin addPin() {
        return addPin(GetNextPinID(), PinType.BASIC);
    }

    public Link addLink(Long id, Pin source, Pin target) {
        Link link = new Link(id, source, target);
        links.put(id, link);
        return link;
    }
    public Link addLink(Pin source, Pin target) {
        return addLink(GetNextLinkID(), source, target);
    }

    public Long GetNextPinID() {
        Long nextID = 0L;
        while (pins.containsKey(nextID)) {
            nextID++;
        }
        return nextID;
    }
    public Long GetNextLinkID() {
        Long nextID = 0L;
        while (links.containsKey(nextID)) {
            nextID++;
        }
        return nextID;
    }

    public Pin getPinFromId(Long id){ return pins.get(id); }
    public Link getLinkFromId(Long id){
        return links.get(id);
    }

    public Pin getPinFromBlock(BlockPos pos){
        return posToPin.get(pos);
    }
    /*
    public BlockPos getPosFromPin(Pin apin) {
        final BlockPos[] current = new BlockPos[1];
        posToPin.forEach((i, p) -> {
            if ((p.get() != null) && (apin.get() != null) && (p.get() == apin.get())){
                current[0] = i;
            }
        });
        return current[0];
    }
    */

    public void removePin(Pin pin){ removePin(pin.getId()); }
    public void removePin(Long id){
        pins.remove(id);
    }

    public void lateRemoveLink(Link link){ tagRemoveLinks.add(link); }
    public void removeLink(Link link){ removeLink(link.getId()); }
    public void removeLink(Long id){ links.remove(id); }

    public void tick() {

        tagRemoveLinks.forEach(this::removeLink);
        tagRemoveLinks.clear();

        links.forEach((id, link) -> {

            Pin source = getPinFromId(link.getSourceID());
            Pin output = getPinFromId(link.getTargetID());

            if (source != null && output != null) { output.calcValue(source.getValue()); } // check if valid link
            else { lateRemoveLink(link); } // destroy if invalid

            // debug lines
//            if(BuggyConfig.CLIENT.getDebugRenderer()) {
//                if(getPosFromPin(source) != null && getPosFromPin(output) != null) {
//                    Vec3d sourcePos = new Vec3d(getPosFromPin(source)).add(new Vec3d(0.5, 0.5, 0.5));
//                    Vec3d targetPos = new Vec3d(getPosFromPin(output)).add(new Vec3d(0.5, 0.5, 0.5));
//                    Vec3d dir = targetPos.sub(sourcePos).normalize();
//
//                    BuggyDebugHelper.Companion.addTickDebugLine(sourcePos, targetPos, 25, new Vec4i(255, 0, 255, 155));
//
//                    //BuggyDebugHelper.Companion.addTickDebugBox(sourcePos.add(dir.mul( 0.75)), 0.25, 25, new Vec4i(255,0,0,255));
//                    //BuggyDebugHelper.Companion.addTickDebugBox(targetPos.add(dir.mul(-0.75)), 0.25, 25, new Vec4i(0,0,255,255));
//                }
//            }
        });

        pins.forEach((id, pin) -> {
            pin.updateValue();
        });
    }

    public void perSecond(){
        System.out.println("Pins: " + pins);
        System.out.println("Links: " + links);
    }

    public PALNetworkData getData(){
        return NetworkData;
    }

    public void loadData(PALNetworkData data){
        NetworkData = data;
    }

}
