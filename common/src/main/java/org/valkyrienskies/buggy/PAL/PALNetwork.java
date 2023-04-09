package org.valkyrienskies.buggy.PAL;


import net.minecraft.core.BlockPos;
import org.valkyrienskies.buggy.PAL.PinTypes.*;

import java.util.*;
import java.util.function.Function;

public class PALNetwork {

    private final HashMap<Long, Pin> pins = new HashMap<Long, Pin>();
    private final HashMap<Long, Link> links = new HashMap<Long, Link>();
    private final HashMap<BlockPos, Pin> posToPin = new HashMap<BlockPos, Pin>();
    private final List<Link> tagRemoveLinks = new ArrayList<Link>();

    public Pin addPin(Long id, PinType type) {
        Pin pin = switch (type) {
            // MATH
            case ADDING         ->      new AddingPin(id);
            case SUBTRACTING    ->      new SubtractingPin(id);
            case MULTIPLYING    ->      new MultiplyingPin(id);

            // MISC
            case EMMITING       ->      new EmmitingPin(id);
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

    public Pin getPinFromId(Long id){
        return pins.get(id);
    }
    public Link getLinkFromId(Long id){
        return links.get(id);
    }

    public Pin getPinFromBlock(BlockPos pos){
        return posToPin.get(pos);
    }

    public void removePin(Pin pin){ pins.remove(pin.getId()); }
    public void removePin(Long id){ pins.remove(id); }

    public void lateRemoveLink(Link link){ tagRemoveLinks.add(link); }
    public void removeLink(Link link){ links.remove(link.getId()); }
    public void removeLink(Long id){ links.remove(id); }

    public void tick() {
        tagRemoveLinks.forEach(this::removeLink);

        links.forEach((id, link) -> {

            Pin source = getPinFromId(link.getSourceID());
            Pin output = getPinFromId(link.getTargetID());

            if (source != null && output != null) { output.calcValue(source.getValue()); } // check if valid link
            else { lateRemoveLink(link); } // destroy if invalid

        });

        pins.forEach((id, pin) -> {
            pin.updateValue();
        });
    }

    public void perSecond(){
        System.out.println("Pins: " + pins);
        System.out.println("Links: " + links);
    }

}
