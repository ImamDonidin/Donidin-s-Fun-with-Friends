package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.advancement.ModTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class CoopInteractionsEvent {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (!(event.getTarget() instanceof ServerPlayer targetPlayer)) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();

        if (player.isShiftKeyDown()) {
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);

            if (!heldItem.isEmpty()) {
                if (event.getLevel().isClientSide()) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }

                Component itemName = heldItem.getHoverName();
                ItemStack itemToGive = heldItem.copyWithCount(1);

                if (targetPlayer.getInventory().add(itemToGive)) {
                    heldItem.shrink(1);

                    if (player.level() instanceof ServerLevel serverLevel) {
                        serverLevel.playSound(null, targetPlayer.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.8F, 1.0F);
                    }

                    player.displayClientMessage(Component.translatable("coop.fun_with_friends.item_sent", itemName, targetPlayer.getDisplayName()), true);
                    targetPlayer.displayClientMessage(Component.translatable("coop.fun_with_friends.item_received", itemName, player.getDisplayName()), true);

                    ModTriggers.SHINY_QUARTER.get().trigger(player);

                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                } else {
                    player.displayClientMessage(Component.translatable("coop.fun_with_friends.inventory_full", targetPlayer.getDisplayName()), true);
                    event.setCancellationResult(InteractionResult.FAIL);
                    event.setCanceled(true);
                }
                return;
            }

            if (event.getLevel().isClientSide()) {
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
                return;
            }

            if (player.getHealth() <= 4.0F) {
                player.displayClientMessage(Component.translatable("coop.fun_with_friends.too_low_health"), true);
                event.setCancellationResult(InteractionResult.FAIL);
                event.setCanceled(true);
                return;
            }

            if (targetPlayer.getHealth() >= targetPlayer.getMaxHealth()) {
                player.displayClientMessage(Component.translatable("coop.fun_with_friends.target_full_health", targetPlayer.getDisplayName()), true);
                event.setCancellationResult(InteractionResult.FAIL);
                event.setCanceled(true);
                return;
            }

            player.setHealth(player.getHealth() - 2.0F);
            targetPlayer.heal(2.0F);

            if (player.level() instanceof ServerLevel serverLevel) {
                serverLevel.playSound(null, targetPlayer.blockPosition(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, 1.5F);
                serverLevel.sendParticles(ParticleTypes.HEART, targetPlayer.getX(), targetPlayer.getY() + 1.0, targetPlayer.getZ(), 3, 0.2, 0.2, 0.2, 0.1);
            }

            player.displayClientMessage(Component.translatable("coop.fun_with_friends.health_given", targetPlayer.getDisplayName()), true);
            targetPlayer.displayClientMessage(Component.translatable("coop.fun_with_friends.health_received", player.getDisplayName()), true);

            ModTriggers.MEDIC.get().trigger(player);

            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }

        else {
            double yDiff = targetPlayer.getY() - player.getY();

            if (yDiff >= 1.8 && yDiff <= 4.0) {
                if (event.getLevel().isClientSide()) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }

                Vec3 pullDirection = new Vec3(
                        (targetPlayer.getX() - player.getX()) * 0.2,
                        0.9,
                        (targetPlayer.getZ() - player.getZ()) * 0.2
                );

                player.setDeltaMovement(pullDirection);
                player.hurtMarked = true;
                player.connection.send(new ClientboundSetEntityMotionPacket(player));

                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.PLAYERS, 1.0F, 1.2F);
                    serverLevel.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY() + 1.0, player.getZ(), 5, 0.2, 0.2, 0.2, 0.05);
                }

                player.displayClientMessage(Component.translatable("coop.fun_with_friends.pulled_by", targetPlayer.getDisplayName()), true);
                targetPlayer.displayClientMessage(Component.translatable("coop.fun_with_friends.pulled_other", player.getDisplayName()), true);

                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}