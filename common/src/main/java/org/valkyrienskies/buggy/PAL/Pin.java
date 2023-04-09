package org.valkyrienskies.buggy.PAL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Pin {
    private final Long id;
    private double value;
    private double storedValue;

    public Pin(Long id ) {
        this.id = id;
    }

    //public void tick() { value = calculateValue.apply(inputs.stream().map(Link::getValue).toArray(Pin[]::new)); }

    public void calcValue(Double inValue){
        this.setStoredValue( inValue ); // would be for adding node -> this.setStoredValue( this.storedValue + inValue );
    }

    public Long getId() {
        return id;
    }

    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }

    public Pin get() { return this; }

    public void setStoredValue(double storedValue) {
        this.storedValue = storedValue;
    }
    public double getStoredValue() { return storedValue; }


    public void updateValue() {
        this.value = this.storedValue;
        this.storedValue = 0.0;
    }
}
