package org.valkyrienskies.buggy.PAL.PinTypes;

import org.valkyrienskies.buggy.PAL.Pin;

public class MultiplyingPin extends Pin {
    public MultiplyingPin(Long id) {
        super(id);
    }

    @Override
    public void calcValue(Double inValue){
        Double tempStore = 1.0;
        if(this.getStoredValue() != 0.0) tempStore = this.getStoredValue() * inValue;
        this.setStoredValue(tempStore);
    }
}
