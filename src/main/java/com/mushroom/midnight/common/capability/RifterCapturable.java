package com.mushroom.midnight.common.capability;

import com.mushroom.midnight.Midnight;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RifterCapturable implements ICapabilityProvider {
    private boolean captured;

    public static boolean isCaptured(Entity entity) {
        LazyOptional<RifterCapturable> capability = entity.getCapability(Midnight.RIFTER_CAPTURABLE_CAP);
        return capability.map(RifterCapturable::isCaptured).orElse(false);
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    public boolean isCaptured() {
        return this.captured;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing) {
        return capability == Midnight.RIFTER_CAPTURABLE_CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == Midnight.RIFTER_CAPTURABLE_CAP) {
            return Midnight.RIFTER_CAPTURABLE_CAP.cast(this);
        }
        return null;
    }
}
