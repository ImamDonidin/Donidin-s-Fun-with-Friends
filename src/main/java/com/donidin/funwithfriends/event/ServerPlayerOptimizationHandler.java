package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.coop.CoopPartyManager;
import com.donidin.funwithfriends.util.TypingData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class ServerPlayerOptimizationHandler {

    private static final Map<UUID, Integer> PLAYER_TICK_COUNTERS = new ConcurrentHashMap<>();
    private static final Map<UUID, Boolean> FAR_AWAY_PARTY_NOTIFIED = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        UUID uuid = player.getUUID();
        int ticks = PLAYER_TICK_COUNTERS.getOrDefault(uuid, 0) + 1;

        if (ticks % 10 == 0) {
            runThrottledChecks(player);
        }

        PLAYER_TICK_COUNTERS.put(uuid, ticks % 1200);
    }

    private static void runThrottledChecks(ServerPlayer player) {
        if (TypingData.isTyping(player.getUUID()) && player.isSpectator()) {
            TypingData.setTyping(player.getUUID(), false);
        }

        CoopPartyManager.CoopParty party = CoopPartyManager.getParty(player.getUUID());
        if (party != null) {
            List<ServerPlayer> nearbyMembersFar = CoopPartyManager.getNearbyPartyMembers(player, 128.0);

            if (nearbyMembersFar.isEmpty() && party.getMembers().size() > 1) {
                if (!FAR_AWAY_PARTY_NOTIFIED.getOrDefault(player.getUUID(), false)) {
                    player.displayClientMessage(
                            Component.translatable("coop.fun_with_friends.party_members_far"),
                            true
                    );
                    FAR_AWAY_PARTY_NOTIFIED.put(player.getUUID(), true);
                }
            } else {
                FAR_AWAY_PARTY_NOTIFIED.put(player.getUUID(), false);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID uuid = event.getEntity().getUUID();
        PLAYER_TICK_COUNTERS.remove(uuid);
        FAR_AWAY_PARTY_NOTIFIED.remove(uuid);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            TypingData.setTyping(player.getUUID(), false);
        }
    }
}