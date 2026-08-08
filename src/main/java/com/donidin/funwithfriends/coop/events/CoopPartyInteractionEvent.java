package com.donidin.funwithfriends.coop.events;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.advancement.ModTriggers;
import com.donidin.funwithfriends.coop.CoopPartyManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class CoopPartyInteractionEvent {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        if (event.getLevel().isClientSide()) {
            if (event.getTarget() instanceof net.minecraft.world.entity.player.Player && event.getEntity().isShiftKeyDown() && event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
            return;
        }

        if (!(event.getTarget() instanceof ServerPlayer targetPlayer) || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.isShiftKeyDown() && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {

            if (CoopPartyManager.inSameParty(player.getUUID(), targetPlayer.getUUID())) {
                if (!player.hasEffect(MobEffects.REGENERATION)) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0, false, true));
                    targetPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0, false, true));

                    if (player.level() instanceof ServerLevel serverLevel) {
                        serverLevel.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 0.7F, 1.4F);
                        serverLevel.sendParticles(ParticleTypes.CRIT, targetPlayer.getX(), targetPlayer.getY() + 1.2, targetPlayer.getZ(), 8, 0.2, 0.2, 0.2, 0.1);
                    }

                    player.displayClientMessage(Component.translatable("coop.fun_with_friends.high_five", targetPlayer.getDisplayName()), true);
                    targetPlayer.displayClientMessage(Component.translatable("coop.fun_with_friends.high_five", player.getDisplayName()), true);

                    ModTriggers.HIGH_FIVE.get().trigger(player);
                    ModTriggers.HIGH_FIVE.get().trigger(targetPlayer);
                } else {
                    CoopPartyManager.leaveParty(player);
                }
            } else {
                boolean added = CoopPartyManager.addPlayerToParty(player, targetPlayer);
                if (added && player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
                }
            }

            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}