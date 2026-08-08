package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.cosmetics.NickColor;
import com.donidin.funwithfriends.init.ModDataAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class NickColorFormatHandler {

    @SubscribeEvent
    public static void onTabName(PlayerEvent.TabListNameFormat event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Component formattedName = applyColorToName(player, event.getDisplayName());
            if (formattedName != null) {
                event.setDisplayName(formattedName);
            }
        }
    }

    @SubscribeEvent
    public static void onNameFormat(PlayerEvent.NameFormat event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Component formattedName = applyColorToName(player, event.getDisplayname());
            if (formattedName != null) {
                event.setDisplayname(formattedName);
            }
        }
    }

    private static Component applyColorToName(ServerPlayer player, Component currentDisplayName) {
        String colorId = player.getData(ModDataAttachments.SELECTED_NICK_COLOR.get());

        if (!colorId.isEmpty()) {
            NickColor color = NickColor.fromId(colorId);
            if (color != null) {
                Component baseName = currentDisplayName != null ? currentDisplayName : player.getName();
                MutableComponent customName = baseName.copy();
                return customName.setStyle(customName.getStyle().withColor(TextColor.fromRgb(color.getHexColor())));
            }
        }
        return null;
    }
}