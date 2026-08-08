package com.donidin.funwithfriends;

import com.donidin.funwithfriends.advancement.ModTriggers;
import com.donidin.funwithfriends.datagen.ModAdvancementProvider;
import com.donidin.funwithfriends.entity.ModEntities;
import com.donidin.funwithfriends.init.ModDataAttachments;
import com.donidin.funwithfriends.network.SyncNickColorPayload;
import com.donidin.funwithfriends.network.TypingPayload;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.concurrent.CompletableFuture;

@Mod(FunWithFriends.MOD_ID)
public class FunWithFriends {
    public static final String MOD_ID = "fun_with_friends";

    public FunWithFriends(IEventBus modEventBus) {
        modEventBus.addListener(this::registerPayloads);
        modEventBus.addListener(this::onGatherData);

        ModTriggers.TRIGGERS.register(modEventBus);
        ModDataAttachments.ATTACHMENT_TYPES.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
    }

    private void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1")
                .optional();

        registrar.playToServer(
                TypingPayload.TYPE,
                TypingPayload.STREAM_CODEC,
                TypingPayload::handleOnServer
        );

        registrar.playToClient(
                TypingPayload.StateUpdate.TYPE,
                TypingPayload.StateUpdate.STREAM_CODEC,
                TypingPayload.StateUpdate::handleOnClient
        );

        registrar.playToClient(
                SyncNickColorPayload.TYPE,
                SyncNickColorPayload.STREAM_CODEC,
                SyncNickColorPayload::handleOnClient
        );
    }

    private void onGatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(
                event.includeServer(),
                new ModAdvancementProvider(packOutput, lookupProvider, existingFileHelper)
        );
    }
}