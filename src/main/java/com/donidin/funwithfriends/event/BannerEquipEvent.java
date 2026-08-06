package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class BannerEquipEvent {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.getItem() instanceof BannerItem && player.isShiftKeyDown()) {
            ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);

            if (!player.level().isClientSide()) {
                ItemStack singleBanner = heldItem.split(1);

                player.setItemSlot(EquipmentSlot.HEAD, singleBanner);

                if (!headItem.isEmpty()) {
                    if (heldItem.isEmpty()) {
                        player.setItemInHand(hand, headItem);
                    } else {
                        if (!player.getInventory().add(headItem)) {
                            player.drop(headItem, false);
                        }
                    }
                }

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            player.swing(hand, true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(player.level().isClientSide()));
        }
    }
}