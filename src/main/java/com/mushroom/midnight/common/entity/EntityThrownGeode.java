package com.mushroom.midnight.common.entity;

import com.mushroom.midnight.client.particle.MidnightParticles;
import com.mushroom.midnight.common.registry.ModCriterion;
import com.mushroom.midnight.common.registry.ModItems;
import com.mushroom.midnight.common.registry.ModLootTables;
import com.mushroom.midnight.common.registry.ModSounds;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityThrownGeode extends EntityThrowable {
    private static final byte POPPED_STATE_ID = 3;

    public EntityThrownGeode(World world) {
        super(world);
    }

    public EntityThrownGeode(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EntityThrownGeode(World world, EntityLivingBase thrower) {
        super(world, thrower);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == POPPED_STATE_ID) {
            this.spawnPopParticles();
        }
    }

    private void spawnPopParticles() {
        this.world.playSound(this.posX, this.posY, this.posZ, ModSounds.EGG_CRACKED, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.2F + 0.85F, false);

        for (int i = 0; i < 8; i++) {
            double velX = (this.rand.nextDouble() - 0.5) * 0.1;
            double velY = (this.rand.nextDouble() - 0.5) * 0.1;
            this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY + 0.1, this.posZ, velX, 0.1, velY, Item.getIdFromItem(ModItems.GEODE));
        }

        for (int i = 0; i < 2; i++) {
            double offsetX = (this.rand.nextDouble() - 0.5) * 0.4;
            double offsetY = (this.rand.nextDouble() - 0.5) * 0.4;
            double offsetZ = (this.rand.nextDouble() - 0.5) * 0.4;
            MidnightParticles.FURNACE_FLAME.spawn(this.world, this.posX + offsetX, this.posY + offsetY, this.posZ + offsetZ, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null) {
            DamageSource source = DamageSource.causeThrownDamage(this, this.getThrower());
            result.entityHit.attackEntityFrom(source, 1.0F);
        }

        if (!this.world.isRemote) {
            BlockPos impactedPos = result.getBlockPos();
            IBlockState impactedState = impactedPos != null ? this.world.getBlockState(impactedPos) : Blocks.AIR.getDefaultState();

            if (this.canBreakOn(impactedState)) {
                EntityPlayerMP player = this.thrower instanceof EntityPlayerMP ? (EntityPlayerMP) this.thrower : null;
                if (player != null) {
                    ModCriterion.THROWN_GEODE.trigger(player);
                }
                dropLootWhenBroken(player);
                this.world.setEntityState(this, POPPED_STATE_ID);

            } else {
                this.dropItem(ModItems.GEODE, 1);
            }

            this.setDead();
        }
    }

    private boolean canBreakOn(IBlockState state) {
        return state.getMaterial() == Material.ROCK || state.getMaterial() == Material.IRON;
    }

    private void dropLootWhenBroken(@Nullable EntityPlayerMP player) {
        LootContext.Builder builder = new LootContext.Builder((WorldServer) this.world).withLootedEntity(this).withDamageSource(DamageSource.GENERIC);
        if (player != null) {
            builder = builder.withPlayer(player).withLuck(player.getLuck());
        }

        LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(ModLootTables.LOOT_TABLE_THROWN_GEODE);
        for (ItemStack itemstack : loottable.generateLootForPools(this.rand, builder.build())) {
            this.entityDropItem(itemstack, 0.1f);
        }
    }
}
