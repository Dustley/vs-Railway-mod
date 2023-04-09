package org.valkyrienskies.buggy.PAL.PinTypes;

import org.valkyrienskies.buggy.PAL.Pin;

public class DisplayPin extends Pin {
    public DisplayPin(Long id) {
        super(id);
    }

    @Override
    public void updateValue() {
        super.updateValue();
        System.out.println(this.getValue());
    }
}
