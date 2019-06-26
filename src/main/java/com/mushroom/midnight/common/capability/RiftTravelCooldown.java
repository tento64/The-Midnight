package com.mushroom.midnight.common.capability;

import com.mushroom.midnight.Midnight;
import com.mushroom.midnight.common.entity.RiftEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RiftTravelCooldown implements ICapabilityProvider {
    private int cooldown;

    public void update(Entity entity) {
        if (this.cooldown > 0) {
            AxisAlignedBB bounds = entity.getBoundingBox();
            if (entity.world.getEntitiesWithinAABB(RiftEntity.class, bounds).isEmpty()) {
                this.cooldown--;
            }
        }
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isReady() {
        return this.cooldown <= 0;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing) {
        return capability == Midnight.RIFT_TRAVEL_COOLDOWN_CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == Midnight.RIFT_TRAVEL_COOLDOWN_CAP) {
            return Midnight.RIFT_TRAVEL_COOLDOWN_CAP.cast(this);
        }
        return null;
    }
}
