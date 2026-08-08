package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.warden.Warden;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.List;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class BossScalingHandler {

    private static final String SCALED_TAG = "fwf_health_scaled";

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof LivingEntity entity)) {
            return;
        }

        if (!isBossOrRaidMember(entity) || entity.getTags().contains(SCALED_TAG)) {
            return;
        }

        ServerLevel level = (ServerLevel) event.getLevel();

        List<ServerPlayer> nearbyPlayers = level.getPlayers(player ->
                player.isAlive() && !player.isSpectator() && player.distanceToSqr(entity) <= 1024.0
        );

        int playerCount = nearbyPlayers.size();

        if (playerCount > 1) {
            double multiplier = Math.min(3.0, 1.0 + (playerCount - 1) * 0.5);

            AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttr != null) {
                double baseHealth = maxHealthAttr.getBaseValue();
                double newHealth = baseHealth * multiplier;

                maxHealthAttr.setBaseValue(newHealth);
                entity.setHealth((float) newHealth);
                entity.addTag(SCALED_TAG);
            }
        }
    }

    private static boolean isBossOrRaidMember(LivingEntity entity) {
        return entity instanceof EnderDragon ||
                entity instanceof WitherBoss ||
                entity instanceof Warden ||
                entity instanceof ElderGuardian ||
                (entity instanceof net.minecraft.world.entity.raid.Raider raider && raider.hasRaid());
    }
}