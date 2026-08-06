package com.donidin.funwithfriends.network;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.init.ModDataAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record SyncNickColorPayload(UUID playerUuid, String colorId) implements CustomPacketPayload {

    public static final Type<SyncNickColorPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(FunWithFriends.MOD_ID, "sync_nick_color"));

    public static final StreamCodec<ByteBuf, SyncNickColorPayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, SyncNickColorPayload::playerUuid,
            ByteBufCodecs.STRING_UTF8, SyncNickColorPayload::colorId,
            SyncNickColorPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnClient(SyncNickColorPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Player player = Minecraft.getInstance().level.getPlayerByUUID(payload.playerUuid());
                if (player != null) {
                    player.setData(ModDataAttachments.SELECTED_NICK_COLOR.get(), payload.colorId());
                }
            }
        });
    }
}