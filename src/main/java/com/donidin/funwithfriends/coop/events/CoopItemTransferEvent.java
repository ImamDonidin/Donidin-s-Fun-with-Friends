package com.donidin.funwithfriends.coop.events;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.advancement.ModTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class CoopItemTransferEvent {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        if (event.getLevel().isClientSide()) {
            if (event.getTarget() instanceof net.minecraft.world.entity.player.Player targetPlayer && targetPlayer.isAlive()) {
                if (event.getEntity().isShiftKeyDown() && !event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }
            }
            return;
        }

        if (!(event.getTarget() instanceof ServerPlayer targetPlayer) || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.isShiftKeyDown()) {
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);

            if (!heldItem.isEmpty()) {
                Component itemName = heldItem.getHoverName();
                ItemStack itemToGive = heldItem.copyWithCount(1);

                if (targetPlayer.getInventory().add(itemToGive)) {
                    heldItem.shrink(1);

                    if (player.level() instanceof ServerLevel serverLevel) {
                        serverLevel.playSound(null, targetPlayer.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.8F, 1.0F);
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, targetPlayer.getX(), targetPlayer.getY() + 1.0, targetPlayer.getZ(), 3, 0.2, 0.2, 0.2, 0.05);
                    }

                    player.displayClientMessage(Component.translatable("coop.fun_with_friends.item_sent", itemName, targetPlayer.getDisplayName()), true);
                    targetPlayer.displayClientMessage(Component.translatable("coop.fun_with_friends.item_received", itemName, player.getDisplayName()), true);

                    ModTriggers.SHINY_QUARTER.get().trigger(player);
                    ModTriggers.TRADE_DEAL.get().trigger(targetPlayer);

                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                } else {
                    player.displayClientMessage(Component.translatable("coop.fun_with_friends.inventory_full", targetPlayer.getDisplayName()), true);
                    event.setCancellationResult(InteractionResult.FAIL);
                    event.setCanceled(true);
                }
            }
        }
    }
}