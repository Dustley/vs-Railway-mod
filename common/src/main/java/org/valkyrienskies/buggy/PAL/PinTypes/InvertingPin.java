package org.valkyrienskies.buggy.PAL.PinTypes;

import org.valkyrienskies.buggy.PAL.Pin;

public class InvertingPin extends Pin {
    public InvertingPin(Long id) {
        super(id);
    }

    @Override
    public void calcValue(Double inValue){
        this.setStoredValue( -this.getStoredValue() );
    }
}
