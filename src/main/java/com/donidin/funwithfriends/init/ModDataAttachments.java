package com.donidin.funwithfriends.init;

import com.donidin.funwithfriends.FunWithFriends;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, FunWithFriends.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<String>> SELECTED_NICK_COLOR =
            ATTACHMENT_TYPES.register("selected_nick_color", () ->
                    AttachmentType.builder(() -> "").serialize(Codec.STRING).build()
            );
}