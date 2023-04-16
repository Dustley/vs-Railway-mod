package org.valkyrienskies.buggy.PAL.PinTypes;

import org.valkyrienskies.buggy.BuggyConfig;
import org.valkyrienskies.buggy.PAL.Pin;

public class TogglePin extends Pin {
    public TogglePin(Long id) {
        super(id);
    }

    private Boolean enabled = false;

    public void setTValue(Boolean value) {this.enabled = value;}
    public  Boolean getToggle() {return this.enabled;}

    @Override
    public void updateValue() {
        if(this.enabled) {
            this.setValue(BuggyConfig.SERVER.getBaseEmmition());
        } else {
            this.setValue(0.0);
        }
    }
}
