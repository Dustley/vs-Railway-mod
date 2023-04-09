package org.valkyrienskies.buggy.PAL.PinTypes;

import org.valkyrienskies.buggy.PAL.Pin;

public class AddingPin extends Pin {
    public AddingPin(Long id) {
        super(id);
    }

    @Override
    public void calcValue(Double inValue){
        this.setStoredValue( this.getStoredValue() + inValue );
    }
}
