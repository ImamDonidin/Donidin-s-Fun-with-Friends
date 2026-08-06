package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.entity.PoisonPotatoEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
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

    private static final int THROW_COOLDOWN = 15;

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (stack.is(Items.POISONOUS_POTATO)) {
            if (player.getCooldowns().isOnCooldown(Items.POISONOUS_POTATO)) {
                return;
            }

            Level level = event.getLevel();

            Player exceptPlayer = (player instanceof ServerPlayer) ? player : null;
            level.playSound(exceptPlayer, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

            if (!level.isClientSide()) {
                PoisonPotatoEntity potato = new PoisonPotatoEntity(player, level);
                potato.setItem(stack.copyWithCount(1));
                potato.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(potato);
            }

            player.swing(event.getHand(), true);
            player.awardStat(Stats.ITEM_USED.get(Items.POISONOUS_POTATO));

            player.getCooldowns().addCooldown(Items.POISONOUS_POTATO, THROW_COOLDOWN);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
            event.setCanceled(true);
        }
    }
}