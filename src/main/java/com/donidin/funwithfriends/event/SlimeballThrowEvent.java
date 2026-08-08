package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class SlimeballThrowEvent {

    private static final int COOLDOWN = 10;

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (stack.is(Items.SLIME_BALL)) {
            if (player.getCooldowns().isOnCooldown(Items.SLIME_BALL)) return;

            Level level = event.getLevel();
            InteractionHand hand = event.getHand();

            level.playSound(
                    null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.SLIME_ATTACK, SoundSource.PLAYERS,
                    0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
            );

            if (!level.isClientSide()) {
                Snowball slimeball = new Snowball(level, player);
                slimeball.setItem(stack.copyWithCount(1));
                slimeball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.2F, 1.0F);
                level.addFreshEntity(slimeball);

                player.awardStat(Stats.ITEM_USED.get(Items.SLIME_BALL));
                player.getCooldowns().addCooldown(Items.SLIME_BALL, COOLDOWN);

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