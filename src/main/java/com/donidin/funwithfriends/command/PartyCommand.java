package com.donidin.funwithfriends.command;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.coop.CoopPartyManager;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.UUID;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class PartyCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("party")
                        .then(Commands.literal("invite")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(ctx -> invitePlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "target")))))
                        .then(Commands.literal("leave")
                                .executes(ctx -> leaveParty(ctx.getSource())))
                        .then(Commands.literal("kick")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(ctx -> kickPlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "target")))))
                        .then(Commands.literal("list")
                                .executes(ctx -> listParty(ctx.getSource())))
        );
    }

    private static int invitePlayer(CommandSourceStack source, ServerPlayer target) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if (player.equals(target)) {
            source.sendFailure(Component.translatable("command.fun_with_friends.party.cannot_invite_self"));
            return 0;
        }

        boolean success = CoopPartyManager.addPlayerToParty(player, target);
        return success ? 1 : 0;
    }

    private static int leaveParty(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        CoopPartyManager.CoopParty party = CoopPartyManager.getParty(player.getUUID());

        if (party == null) {
            source.sendFailure(Component.translatable("command.fun_with_friends.party.not_in_party"));
            return 0;
        }

        CoopPartyManager.leaveParty(player);
        return 1;
    }

    private static int kickPlayer(CommandSourceStack source, ServerPlayer target) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        CoopPartyManager.CoopParty party = CoopPartyManager.getParty(player.getUUID());

        if (party == null) {
            source.sendFailure(Component.translatable("command.fun_with_friends.party.not_in_party"));
            return 0;
        }

        if (!party.getLeaderId().equals(player.getUUID())) {
            source.sendFailure(Component.translatable("command.fun_with_friends.party.not_leader"));
            return 0;
        }

        if (player.equals(target)) {
            source.sendFailure(Component.translatable("command.fun_with_friends.party.cannot_kick_self"));
            return 0;
        }

        if (!party.getMembers().contains(target.getUUID())) {
            source.sendFailure(Component.translatable("command.fun_with_friends.party.target_not_in_party"));
            return 0;
        }

        CoopPartyManager.leaveParty(target);
        CoopPartyManager.broadcastToParty(party, Component.translatable("coop.fun_with_friends.party_kicked", target.getDisplayName()));
        target.displayClientMessage(Component.translatable("coop.fun_with_friends.party_kicked_self"), true);
        return 1;
    }

    private static int listParty(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        CoopPartyManager.CoopParty party = CoopPartyManager.getParty(player.getUUID());

        if (party == null) {
            source.sendFailure(Component.translatable("command.fun_with_friends.party.not_in_party"));
            return 0;
        }

        source.sendSuccess(() -> Component.translatable("command.fun_with_friends.party.list_header", party.getMembers().size(), CoopPartyManager.MAX_PARTY_SIZE), false);

        var server = source.getServer();
        for (UUID memberId : party.getMembers()) {
            ServerPlayer member = server.getPlayerList().getPlayer(memberId);
            boolean isLeader = memberId.equals(party.getLeaderId());
            String roleTag = isLeader ? " ★" : "";

            if (member != null) {
                source.sendSuccess(() -> Component.literal(" §a• " + member.getScoreboardName() + roleTag), false);
            } else {
                source.sendSuccess(() -> Component.literal(" §7• Offline" + roleTag), false);
            }
        }
        return 1;
    }
}