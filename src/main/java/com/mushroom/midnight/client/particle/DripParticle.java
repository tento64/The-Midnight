package com.mushroom.midnight.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DripParticle extends MidnightParticle {
    private int bobTimer;

    public DripParticle(World world, double x, double y, double z, float r, float g, float b) {
        super(world, x, y, z, 0d, 0d, 0d);
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
        setSize(0.01f, 0.01f);
        this.particleGravity = 0.04f;
        this.maxAge = (int) (64d / (Math.random() * 0.8d + 0.2d));
        this.bobTimer = this.maxAge * 3 / 4;
        this.motionX = this.motionY = this.motionZ = 0d;
        this.particleScale *= 2f;
    }

    @Override
    public void tick() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= (double) particleGravity;
        if (bobTimer-- > 0) {
            motionX *= 0.02d;
            motionY *= 0.02d;
            motionZ *= 0.02d;
        }
        move(motionX, motionY, motionZ);
        motionX *= 0.9800000190734863d;
        motionY *= 0.9800000190734863d;
        motionZ *= 0.9800000190734863d;
        if (maxAge-- <= 0) {
            setExpired();
        }
        if (onGround) {
            setExpired();
            motionX *= 0.699999988079071d;
            motionZ *= 0.699999988079071d;
        }
    }

    @Override
    public int getBrightnessForRender(float partialTick) {
        return bobTimer <= 0 ? 0xe000e0 : 0xe000e0 - (0x010001 * bobTimer);
    }

    @Override
    ResourceLocation getTexture() {
        return MidnightParticleSprites.DRAGON_NEST;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticle {
        @Override
        public Particle makeParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... params) {
            return new DripParticle(world, x, y, z, params.length > 0 ? params[0] / 255f : 0f, params.length > 1 ? params[1] / 255f : 0f, params.length > 2 ? params[2] / 255f : 0f);
        }
    }
}
