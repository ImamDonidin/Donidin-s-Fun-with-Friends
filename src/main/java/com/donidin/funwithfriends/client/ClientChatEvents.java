package com.donidin.funwithfriends.client;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.network.TypingPayload;
import com.donidin.funwithfriends.util.TypingData;
import net.minecraft.client.gui.screens.ChatScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID, value = Dist.CLIENT)
public class ClientChatEvents {
    private static boolean wasTyping = false;

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        if (event.getNewScreen() instanceof ChatScreen) {
            updateTypingState(true);
        }
    }

    @SubscribeEvent
    public static void onScreenClose(ScreenEvent.Closing event) {
        if (event.getScreen() instanceof ChatScreen) {
            updateTypingState(false);
        }
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        wasTyping = false;
        TypingData.clear();
    }

    private static void updateTypingState(boolean isTyping) {
        if (wasTyping != isTyping) {
            wasTyping = isTyping;
            PacketDistributor.sendToServer(new TypingPayload(isTyping));
        }
    }
}