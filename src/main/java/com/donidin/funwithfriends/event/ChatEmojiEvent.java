package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;

import java.util.LinkedHashMap;
import java.util.Map;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class ChatEmojiEvent {

    private static final Map<String, String> EMOJI_MAP = new LinkedHashMap<>();

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
        String text = event.getRawText();

        if (!text.contains(":")) {
            return;
        }

        boolean modified = false;
        for (Map.Entry<String, String> entry : EMOJI_MAP.entrySet()) {
            if (text.contains(entry.getKey())) {
                text = text.replace(entry.getKey(), entry.getValue());
                modified = true;
            }
        }

        if (modified) {
            event.setMessage(Component.literal(text));
        }
    }
}