package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.advancement.ModTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class PlayerJoinAndTypingEvents {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.getServer() != null) {
                if (player.getServer().getPlayerList().getPlayerCount() > 1) {
                    for (ServerPlayer p : player.getServer().getPlayerList().getPlayers()) {
                        ModTriggers.FRIEND_JOINED.get().trigger(p);
                    }
                }
            }
        }
    }

    public static void checkLongTypingAdvancement(ServerPlayer player, long typingDurationTicks) {
        if (typingDurationTicks >= 300) {
            ModTriggers.WAR_AND_PEACE.get().trigger(player);
        }
    }
}