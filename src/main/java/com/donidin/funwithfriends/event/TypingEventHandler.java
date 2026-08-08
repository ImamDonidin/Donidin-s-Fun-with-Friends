package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.network.TypingPayload;
import com.donidin.funwithfriends.util.TypingData;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class TypingEventHandler {

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof ServerPlayer targetPlayer && event.getEntity() instanceof ServerPlayer tracker) {
            if (TypingData.isTyping(targetPlayer.getUUID())) {
                PacketDistributor.sendToPlayer(tracker, new TypingPayload.StateUpdate(targetPlayer.getUUID(), true));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        TypingData.setTyping(event.getEntity().getUUID(), false);
    }
}