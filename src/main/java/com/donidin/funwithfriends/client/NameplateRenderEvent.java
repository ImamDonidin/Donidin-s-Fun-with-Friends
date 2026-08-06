package com.donidin.funwithfriends.client;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.cosmetics.NickColor;
import com.donidin.funwithfriends.init.ModDataAttachments;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID, value = Dist.CLIENT)
public class NameplateRenderEvent {

    @SubscribeEvent
    public static void onRenderNameTag(RenderNameTagEvent event) {
        if (!(event.getEntity() instanceof AbstractClientPlayer player)) return;

        String colorId = player.getData(ModDataAttachments.SELECTED_NICK_COLOR.get());
        NickColor customColor = colorId.isEmpty() ? null : NickColor.fromId(colorId);

        boolean isTyping = ClientTypingData.isTyping(player.getUUID());

        MutableComponent nameComponent;

        if (isTyping) {
            nameComponent = Component.translatable("chat.fun_with_friends.typing", player.getDisplayName().getString());
        } else if (customColor != null) {
            nameComponent = Component.literal(player.getScoreboardName());
        } else {
            return;
        }

        if (customColor != null) {
            nameComponent.setStyle(nameComponent.getStyle().withColor(TextColor.fromRgb(customColor.getHexColor())));
        }

        event.setContent(nameComponent);
    }
}