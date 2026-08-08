package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.init.ModDataAttachments;
import com.donidin.funwithfriends.network.SyncNickColorPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class NickColorSyncHandler {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            String colorId = player.getData(ModDataAttachments.SELECTED_NICK_COLOR.get());

            if (!colorId.isEmpty()) {
                PacketDistributor.sendToAllPlayers(new SyncNickColorPayload(player.getUUID(), colorId));
            }

            if (player.getServer() != null) {
                for (ServerPlayer other : player.getServer().getPlayerList().getPlayers()) {
                    if (other != player) {
                        syncColorData(player, other);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof ServerPlayer targetPlayer && event.getEntity() instanceof ServerPlayer tracker) {
            syncColorData(tracker, targetPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            String savedColor = event.getOriginal().getData(ModDataAttachments.SELECTED_NICK_COLOR.get());
            event.getEntity().setData(ModDataAttachments.SELECTED_NICK_COLOR.get(), savedColor);
        }
    }

    private static void syncColorData(ServerPlayer receiver, ServerPlayer source) {
        String savedColor = source.getData(ModDataAttachments.SELECTED_NICK_COLOR.get());
        if (!savedColor.isEmpty()) {
            PacketDistributor.sendToPlayer(receiver, new SyncNickColorPayload(source.getUUID(), savedColor));
        }
    }
}