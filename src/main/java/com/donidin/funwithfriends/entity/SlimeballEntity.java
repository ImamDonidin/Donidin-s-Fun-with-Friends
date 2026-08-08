package com.donidin.funwithfriends.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class SlimeballEntity extends ThrowableItemProjectile {

    public SlimeballEntity(EntityType<? extends SlimeballEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SlimeballEntity(Level level, LivingEntity shooter) {
        super(ModEntities.SLIME_BALL.get(), shooter, level);
    }

    public SlimeballEntity(Level level, double x, double y, double z) {
        super(ModEntities.SLIME_BALL.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();

        if (target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ItemStack stack = this.getItem();
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(
                        new ItemParticleOption(ParticleTypes.ITEM, stack),
                        this.getX(), this.getY(), this.getZ(),
                        ((double) this.random.nextFloat() - 0.5D) * 0.08D,
                        ((double) this.random.nextFloat() - 0.5D) * 0.08D,
                        ((double) this.random.nextFloat() - 0.5D) * 0.08D
                );
            }
        }
    }
}