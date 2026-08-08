package com.donidin.funwithfriends.coop.events;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.advancement.ModTriggers;
import com.donidin.funwithfriends.coop.CoopPartyManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class CoopPartyTickEvent {

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerTickEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            CoopPartyManager.handlePlayerDisconnect(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || player.level().isClientSide()) {
            return;
        }

        if (player.tickCount % 10 != 0) return;

        if (player.isPassenger()) {
            List<ServerPlayer> nearbyPartyMembers = CoopPartyManager.getNearbyPartyMembers(player, 25.0);

            boolean partnerIsPassenger = nearbyPartyMembers.stream().anyMatch(Entity::isPassenger);

            if (partnerIsPassenger) {
                Entity mount = player.getVehicle();
                if (mount instanceof LivingEntity livingMount) {
                    livingMount.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 1, false, false, true));

                    if (player.level() instanceof ServerLevel serverLevel && player.tickCount % 20 == 0) {
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, mount.getX(), mount.getY() + 0.5, mount.getZ(), 2, 0.2, 0.2, 0.2, 0.05);
                    }
                }

                ModTriggers.ROAD_TRIP.get().trigger(player);
            }
        }
    }
}