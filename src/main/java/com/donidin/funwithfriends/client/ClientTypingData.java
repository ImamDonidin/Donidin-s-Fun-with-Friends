package com.donidin.funwithfriends.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.UUID;

public class ClientTypingData {
    private static final Set<UUID> TYPING_PLAYERS = ConcurrentHashMap.newKeySet();

    public static void setTyping(UUID uuid, boolean isTyping) {
        if (isTyping) {
            TYPING_PLAYERS.add(uuid);
        } else {
            TYPING_PLAYERS.remove(uuid);
        }
    }

    public static boolean isTyping(UUID uuid) {
        return TYPING_PLAYERS.contains(uuid);
    }
}