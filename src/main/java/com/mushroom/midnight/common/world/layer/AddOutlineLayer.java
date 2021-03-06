package com.mushroom.midnight.common.world.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public class AddOutlineLayer implements ICastleTransformer {
    private final int outline;

    public AddOutlineLayer(int outline) {
        this.outline = outline;
    }

    @Override
    public int apply(INoiseRandom random, int top, int right, int bottom, int left, int value) {
        if (right != value || bottom != value) {
            return this.outline;
        } else {
            return value;
        }
    }
}
