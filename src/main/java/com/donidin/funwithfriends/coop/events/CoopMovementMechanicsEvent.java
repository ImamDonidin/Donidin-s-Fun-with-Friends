package com.donidin.funwithfriends.coop.events;

import com.donidin.funwithfriends.FunWithFriends;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class CoopMovementMechanicsEvent {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != InteractionHand.MAIN_HAND || event.getEntity().isShiftKeyDown()) return;

        if (event.getLevel().isClientSide()) {
            if (event.getTarget() instanceof net.minecraft.world.entity.player.Player) {
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
            return;
        }

        if (!(event.getTarget() instanceof ServerPlayer targetPlayer) || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        double yDiff = targetPlayer.getY() - player.getY();

        if (yDiff >= 1.8 && yDiff <= 4.0) {
            Vec3 pullDirection = new Vec3(
                    (targetPlayer.getX() - player.getX()) * 0.25,
                    0.85,
                    (targetPlayer.getZ() - player.getZ()) * 0.25
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