package com.mushroom.midnight.common.world.template;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public interface TemplateMarkerProcessor {
    void process(IWorld world, BlockPos pos, String key);
}
