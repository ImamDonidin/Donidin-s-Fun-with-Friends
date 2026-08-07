package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class HatEquipEvent {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack heldItem = player.getItemInHand(hand);

        if (player.isShiftKeyDown() && isAllowedHat(heldItem)) {
            ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);

            if (!player.level().isClientSide()) {
                boolean isCandle = heldItem.getItem() instanceof BlockItem blockItem
                        && blockItem.getBlock() instanceof CandleBlock;

                if (isCandle) {
                    if (headItem.isEmpty()) {
                        int countToEquip = Math.min(heldItem.getCount(), 4);
                        ItemStack candleHat = heldItem.split(countToEquip);
                        player.setItemSlot(EquipmentSlot.HEAD, candleHat);
                    } else if (ItemStack.isSameItemSameComponents(headItem, heldItem) && headItem.getCount() < 4) {
                        int spaceLeft = 4 - headItem.getCount();
                        int amountToAdd = Math.min(heldItem.getCount(), spaceLeft);

                        heldItem.shrink(amountToAdd);
                        headItem.grow(amountToAdd);
                        swapItems(player, hand, heldItem, headItem);
                    }
                } else {
                    swapItems(player, hand, heldItem, headItem);
                }

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            player.swing(hand, true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(player.level().isClientSide()));
        }
    }

    private static void swapItems(Player player, InteractionHand hand, ItemStack heldItem, ItemStack headItem) {
        ItemStack singleHat = heldItem.split(1);
        player.setItemSlot(EquipmentSlot.HEAD, singleHat);

        if (!headItem.isEmpty()) {
            if (heldItem.isEmpty()) {
                player.setItemInHand(hand, headItem);
            } else {
                if (!player.getInventory().add(headItem)) {
                    player.drop(headItem, false);
                }
            }
        }
    }

    /**
     * Checks whether this item can be worn as a hat (Glass, Ice, Flowers, Decor, etc.).
     */
    private static boolean isAllowedHat(ItemStack stack) {
        if (stack.is(Items.FEATHER)) {
            return true;
        }

        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return false;
        }

        Block block = blockItem.getBlock();

        return block instanceof TransparentBlock
                || block instanceof HalfTransparentBlock
                || block instanceof FlowerBlock
                || block instanceof TallFlowerBlock
                || block instanceof BushBlock
                || block instanceof FlowerPotBlock
                || block instanceof CarvedPumpkinBlock
                || block instanceof AbstractChestBlock
                || block instanceof CandleBlock
                || block instanceof PointedDripstoneBlock
                || block instanceof EndRodBlock
                || block instanceof BasePressurePlateBlock
                || stack.is(Items.PACKED_ICE)
                || stack.is(Items.BLUE_ICE)
                || stack.is(Items.SEA_PICKLE)
                || stack.is(Items.CHORUS_FLOWER);
    }
}