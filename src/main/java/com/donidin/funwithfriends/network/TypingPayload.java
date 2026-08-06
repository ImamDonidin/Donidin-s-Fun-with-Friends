package com.donidin.funwithfriends.network;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.client.ClientTypingData;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record TypingPayload(boolean isTyping) implements CustomPacketPayload {
    public static final Type<TypingPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(FunWithFriends.MOD_ID, "typing_state"));

    public static final StreamCodec<ByteBuf, TypingPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, TypingPayload::isTyping,
            TypingPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnServer(TypingPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                PacketDistributor.sendToAllPlayers(new StateUpdate(player.getUUID(), payload.isTyping()));
            }
        });
    }

    public record StateUpdate(UUID playerUuid, boolean isTyping) implements CustomPacketPayload {
        public static final Type<StateUpdate> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(FunWithFriends.MOD_ID, "typing_update"));

        public static final StreamCodec<ByteBuf, StateUpdate> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, StateUpdate::playerUuid,
                ByteBufCodecs.BOOL, StateUpdate::isTyping,
                StateUpdate::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handleOnClient(StateUpdate payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ClientTypingData.setTyping(payload.playerUuid(), payload.isTyping());
            });
        }
    }
}