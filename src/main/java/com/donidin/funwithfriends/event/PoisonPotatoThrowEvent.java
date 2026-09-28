package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.advancement.ModTriggers;
import com.donidin.funwithfriends.config.ModConfig;
import com.donidin.funwithfriends.entity.PoisonPotatoEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class PoisonPotatoThrowEvent {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!ModConfig.INSTANCE.enablePotatoThrowing) {
            return;
        }

        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (stack.is(Items.POISONOUS_POTATO)) {
            if (player.getCooldowns().isOnCooldown(Items.POISONOUS_POTATO)) {
                return;
            }

            Level level = event.getLevel();
            InteractionHand hand = event.getHand();

            level.playSound(
                    null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL,
                    0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
            );

            if (!level.isClientSide()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    ModTriggers.BOO.get().trigger(serverPlayer);
                }

                PoisonPotatoEntity potato = new PoisonPotatoEntity(player, level);
                potato.setItem(stack.copyWithCount(1));
                potato.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(potato);

                player.awardStat(Stats.ITEM_USED.get(Items.POISONOUS_POTATO));

                player.getCooldowns().addCooldown(Items.POISONOUS_POTATO, ModConfig.INSTANCE.potatoCooldown);

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }

            player.swing(hand, true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
        }
    }
}