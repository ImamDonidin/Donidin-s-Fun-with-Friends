package com.donidin.funwithfriends.entity;

import com.donidin.funwithfriends.FunWithFriends;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, FunWithFriends.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<PoisonPotatoEntity>> POISON_POTATO =
            ENTITY_TYPES.register("poison_potato", () ->
                    EntityType.Builder.<PoisonPotatoEntity>of(PoisonPotatoEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("poison_potato")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<SlimeballEntity>> SLIME_BALL =
            ENTITY_TYPES.register("slimeball", () ->
                    EntityType.Builder.<SlimeballEntity>of(SlimeballEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("slimeball")
            );
}