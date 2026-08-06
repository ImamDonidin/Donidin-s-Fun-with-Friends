package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class ChatEmojiEvent {

    private static final Map<String, String> EMOJI_MAP = new HashMap<>();

    static {
        EMOJI_MAP.put(":heart:", "❤");
        EMOJI_MAP.put(":star:", "★");
        EMOJI_MAP.put(":skull:", "☠");
        EMOJI_MAP.put(":check:", "✔");
        EMOJI_MAP.put(":cross:", "❌");
        EMOJI_MAP.put(":pick:", "⛏");
        EMOJI_MAP.put(":fire:", "🔥");
        EMOJI_MAP.put(":smile:", "☺");
        EMOJI_MAP.put(":swords:", "⚔");
        EMOJI_MAP.put(":trident:", "🔱");
        EMOJI_MAP.put(":shield:", "🛡");
    }

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        String originalText = event.getRawText();
        String updatedText = originalText;

        for (Map.Entry<String, String> entry : EMOJI_MAP.entrySet()) {
            if (updatedText.contains(entry.getKey())) {
                updatedText = updatedText.replace(entry.getKey(), entry.getValue());
            }
        }

        if (!updatedText.equals(originalText)) {
            event.setMessage(Component.literal(updatedText));
        }
    }
}