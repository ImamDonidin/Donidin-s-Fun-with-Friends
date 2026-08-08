package com.donidin.funwithfriends.command;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.cosmetics.NickColor;
import com.donidin.funwithfriends.init.ModDataAttachments;
import com.donidin.funwithfriends.network.SyncNickColorPayload;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class NickColorCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("nickcolor")
                        .then(Commands.literal("reset")
                                .executes(NickColorCommand::resetColor))
                        .then(Commands.argument("color", StringArgumentType.word())
                                .suggests(NickColorCommand::suggestColors)
                                .executes(NickColorCommand::setColor))
        );
    }

    private static CompletableFuture<Suggestions> suggestColors(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        if (context.getSource().getEntity() instanceof ServerPlayer player) {
            for (NickColor color : NickColor.VALUES) {
                AdvancementHolder advancement = player.getServer().getAdvancements().get(color.getAdvancementId());
                if (advancement != null && player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    builder.suggest(color.getId());
                }
            }
        }
        return builder.buildFuture();
    }

    private static int setColor(CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getEntity() instanceof ServerPlayer player)) return 0;

        String colorId = StringArgumentType.getString(context, "color");
        NickColor selected = NickColor.fromId(colorId);

        if (selected == null) {
            player.sendSystemMessage(Component.translatable("command.fun_with_friends.nickcolor.unknown"));
            return 0;
        }

        AdvancementHolder advancement = player.getServer().getAdvancements().get(selected.getAdvancementId());
        if (advancement == null || !player.getAdvancements().getOrStartProgress(advancement).isDone()) {
            player.sendSystemMessage(Component.translatable("command.fun_with_friends.nickcolor.locked"));
            return 0;
        }

        player.setData(ModDataAttachments.SELECTED_NICK_COLOR.get(), selected.getId());
        PacketDistributor.sendToAllPlayers(new SyncNickColorPayload(player.getUUID(), selected.getId()));
        player.refreshTabListName();

        Component colorName = Component.translatable("color.fun_with_friends." + selected.getId())
                .withStyle(style -> style.withColor(TextColor.fromRgb(selected.getHexColor())));

        player.sendSystemMessage(Component.translatable("command.fun_with_friends.nickcolor.success", colorName));
        return 1;
    }

    private static int resetColor(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getEntity() instanceof ServerPlayer player) {
            player.setData(ModDataAttachments.SELECTED_NICK_COLOR.get(), "");
            PacketDistributor.sendToAllPlayers(new SyncNickColorPayload(player.getUUID(), ""));
            player.refreshTabListName();

            player.sendSystemMessage(Component.translatable("command.fun_with_friends.nickcolor.reset"));
            return 1;
        }
        return 0;
    }
}