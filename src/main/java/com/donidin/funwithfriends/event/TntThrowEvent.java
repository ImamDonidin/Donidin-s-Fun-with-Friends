package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.advancement.ModTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class TntThrowEvent {

    private static final int THROW_COOLDOWN = 20;

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (stack.is(Items.TNT) && player.isShiftKeyDown()) {
            if (player.getCooldowns().isOnCooldown(Items.TNT)) {
                return;
            }

            Level level = event.getLevel();
            InteractionHand hand = event.getHand();

            level.playSound(
                    null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.TNT_PRIMED, SoundSource.PLAYERS,
                    1.0F, 1.0F
            );

            if (!level.isClientSide()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    ModTriggers.HOT_POTATO.get().trigger(serverPlayer);
                }

                PrimedTnt tnt = new PrimedTnt(level, player.getX(), player.getEyeY() - 0.1, player.getZ(), player);
                tnt.setFuse(60);

                Vec3 look = player.getLookAngle();
                tnt.setDeltaMovement(look.x * 1.2, look.y * 1.2 + 0.2, look.z * 1.2);

                level.addFreshEntity(tnt);

                player.awardStat(Stats.ITEM_USED.get(Items.TNT));
                player.getCooldowns().addCooldown(Items.TNT, THROW_COOLDOWN);

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