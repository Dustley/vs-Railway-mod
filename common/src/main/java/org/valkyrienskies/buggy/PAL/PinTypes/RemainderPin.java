package org.valkyrienskies.buggy.PAL.PinTypes;

import org.valkyrienskies.buggy.PAL.Pin;

public class RemainderPin extends Pin {
    public RemainderPin(Long id) {
        super(id);
    }

    @Override
    public void calcValue(Double inValue){
        this.setStoredValue( this.getStoredValue() % inValue );
    }
}
