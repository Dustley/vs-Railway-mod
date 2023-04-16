package org.valkyrienskies.buggy.PAL.PinTypes;

import org.valkyrienskies.buggy.PAL.Pin;

public class MultiplyingPin extends Pin {
    public MultiplyingPin(Long id) {
        super(id);
    }

    private Boolean isO = false;

    @Override
    public void updateValue() {
        if (this.isO) {
            this.setValue(0.0);
        } else {
            this.setValue(this.getStoredValue());
        }
        this.isO = false;
        this.setStoredValue(0.0);
    }

    @Override
    public void calcValue(Double inValue){
        if(this.getStoredValue() == 0.0) { this.setStoredValue(1.0); }
        this.setStoredValue( this.getStoredValue() * inValue );

        if(inValue == 0.0) {this.isO = true;}
    }
}
