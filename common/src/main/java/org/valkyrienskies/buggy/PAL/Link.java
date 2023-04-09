package org.valkyrienskies.buggy.PAL;

import org.spongepowered.asm.mixin.injection.Desc;

public class Link {
    private final Long id;
    private final Long source;
    private final Long target;

    public Link(Long id, Pin source, Pin target) {
        this.id = id;
        this.source = source.getId();
        this.target = target.getId();
    }

    public Long getId() {
        return id;
    }

    public Long getSourceID() {
        return source;
    }
    public Long getTargetID() {
        return target;
    }


    public Link get() { return this; }
}
