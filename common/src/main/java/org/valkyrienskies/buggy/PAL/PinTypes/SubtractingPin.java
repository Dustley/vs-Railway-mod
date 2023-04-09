package org.valkyrienskies.buggy.PAL.PinTypes;

import org.valkyrienskies.buggy.PAL.Pin;

public class SubtractingPin extends Pin {
    public SubtractingPin(Long id) {
        super(id);
    }

    @Override
    public void calcValue(Double inValue){
        this.setStoredValue( this.getStoredValue() - inValue );
    }
}
