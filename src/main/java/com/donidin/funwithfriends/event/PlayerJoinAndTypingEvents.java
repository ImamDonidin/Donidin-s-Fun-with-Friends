package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.advancement.ModTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class PlayerJoinAndTypingEvents {

    private static final Map<UUID, Long> TYPING_START_TIMES = new ConcurrentHashMap<>();

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

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        TYPING_START_TIMES.remove(event.getEntity().getUUID());
    }

    public static void onPlayerTypingStateChanged(ServerPlayer player, boolean isTyping) {
        UUID uuid = player.getUUID();
        if (isTyping) {
            TYPING_START_TIMES.put(uuid, System.currentTimeMillis());
        } else {
            Long startTime = TYPING_START_TIMES.remove(uuid);
            if (startTime != null) {
                long durationMs = System.currentTimeMillis() - startTime;
                if (durationMs >= 15000) {
                    ModTriggers.WAR_AND_PEACE.get().trigger(player);
                }
            }
        }
    }
}