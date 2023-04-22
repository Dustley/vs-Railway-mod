package org.valkyrienskies.buggy.PAL.PinTypes;

import org.valkyrienskies.buggy.PAL.Pin;

public class DisplayPin extends Pin {
    public DisplayPin(Long id) {
        super(id);
    }

    @Override
    public void calcValue(Double inValue){
        this.setStoredValue( inValue ); // would be for adding node -> this.setStoredValue( this.storedValue + inValue );
        System.out.println(inValue);
    }

    @Override
    public void updateValue() {
        super.updateValue();
        //System.out.println(this.getValue());
    }
}
