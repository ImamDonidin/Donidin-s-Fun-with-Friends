package com.donidin.funwithfriends.entity;

import com.donidin.funwithfriends.advancement.ModTriggers;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class PoisonPotatoEntity extends ThrowableItemProjectile {

    public PoisonPotatoEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public PoisonPotatoEntity(LivingEntity shooter, Level level) {
        super(ModEntities.POISON_POTATO.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.POISONOUS_POTATO;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        if (!this.level().isClientSide()) {
            if (result.getEntity() instanceof LivingEntity target) {
                target.hurt(this.damageSources().thrown(this, this.getOwner()), 0.5F);
                target.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 0));

                if (target instanceof ServerPlayer targetPlayer
                        && this.getOwner() instanceof ServerPlayer shooter
                        && targetPlayer != shooter) {
                    ModTriggers.BOO.get().trigger(shooter);
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (this.level().isClientSide()) {
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(
                        new ItemParticleOption(ParticleTypes.ITEM, this.getItem()),
                        this.getX(), this.getY(), this.getZ(),
                        (this.random.nextFloat() - 0.5D) * 0.08D,
                        this.random.nextFloat() * 0.05D,
                        (this.random.nextFloat() - 0.5D) * 0.08D
                );
            }
        } else {
            this.discard();
        }
    }
}