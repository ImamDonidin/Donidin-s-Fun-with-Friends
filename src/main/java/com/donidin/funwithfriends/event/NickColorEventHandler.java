package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.cosmetics.NickColor;
import com.donidin.funwithfriends.init.ModDataAttachments;
import com.donidin.funwithfriends.network.SyncNickColorPayload;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class NickColorEventHandler {

    @SubscribeEvent
    public static void onTabName(PlayerEvent.TabListNameFormat event) {
        Player player = event.getEntity();
        String colorId = player.getData(ModDataAttachments.SELECTED_NICK_COLOR.get());

        if (!colorId.isEmpty()) {
            NickColor color = NickColor.fromId(colorId);
            if (color != null) {
                MutableComponent customName = player.getDisplayName().copy();
                customName.setStyle(customName.getStyle().withColor(TextColor.fromRgb(color.getHexColor())));

                event.setDisplayName(customName);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            String savedColor = player.getData(ModDataAttachments.SELECTED_NICK_COLOR.get());
            if (!savedColor.isEmpty()) {
                PacketDistributor.sendToAllPlayers(new SyncNickColorPayload(player.getUUID(), savedColor));
            }
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof ServerPlayer targetPlayer && event.getEntity() instanceof ServerPlayer player) {
            String savedColor = targetPlayer.getData(ModDataAttachments.SELECTED_NICK_COLOR.get());
            if (!savedColor.isEmpty()) {
                PacketDistributor.sendToPlayer(player, new SyncNickColorPayload(targetPlayer.getUUID(), savedColor));
            }
        }
    }
}