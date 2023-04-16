package org.valkyrienskies.buggy.PAL.PinTypes;

import org.valkyrienskies.buggy.PAL.Pin;

public class DividingPin extends Pin {
    public DividingPin(Long id) {
        super(id);
    }

    @Override
    public void calcValue(Double inValue){
        if(this.getStoredValue() == 0.0 && inValue != 0.0) { this.setStoredValue(1.0); }
        this.setStoredValue( this.getStoredValue() / inValue );
    }
}
