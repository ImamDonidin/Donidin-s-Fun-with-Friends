package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.cosmetics.NickColor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class NickColorUnlockNotifier {

    @SubscribeEvent
    public static void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        NickColor unlockedColor = NickColor.fromAdvancement(event.getAdvancement().id());
        if (unlockedColor != null) {
            notifyUnlock(player, unlockedColor);
        }
    }

    public static void notifyUnlock(ServerPlayer player, NickColor color) {
        Component colorName = Component.translatable("color.fun_with_friends." + color.getId())
                .withStyle(style -> style.withColor(TextColor.fromRgb(color.getHexColor())));

        player.sendSystemMessage(
                Component.translatable("message.fun_with_friends.nickcolor.unlocked", colorName)
        );
    }
}