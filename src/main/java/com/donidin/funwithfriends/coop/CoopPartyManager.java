package com.donidin.funwithfriends.coop;

import com.donidin.funwithfriends.advancement.ModTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CoopPartyManager {

    public static final int MAX_PARTY_SIZE = 4;

    public static class CoopParty {
        private final UUID partyId;
        private UUID leaderId;
        private final Set<UUID> members = ConcurrentHashMap.newKeySet();

        public CoopParty(ServerPlayer leader) {
            this.partyId = UUID.randomUUID();
            this.leaderId = leader.getUUID();
            this.members.add(leader.getUUID());
        }

        public UUID getPartyId() { return partyId; }
        public UUID getLeaderId() { return leaderId; }
        public Set<UUID> getMembers() { return Collections.unmodifiableSet(members); }

        public boolean isFull() {
            return members.size() >= MAX_PARTY_SIZE;
        }

        public boolean addMember(UUID playerId) {
            if (isFull()) return false;
            return members.add(playerId);
        }

        public boolean removeMember(UUID playerId) {
            boolean removed = members.remove(playerId);
            if (removed && playerId.equals(leaderId) && !members.isEmpty()) {
                this.leaderId = members.iterator().next();
            }
            return removed;
        }

        public boolean isEmpty() {
            return members.isEmpty();
        }
    }

    private static final Map<UUID, CoopParty> PARTIES_BY_ID = new ConcurrentHashMap<>();
    private static final Map<UUID, CoopParty> PLAYER_PARTY_MAP = new ConcurrentHashMap<>();

    public static boolean addPlayerToParty(ServerPlayer inviter, ServerPlayer target) {
        CoopParty inviterParty = PLAYER_PARTY_MAP.get(inviter.getUUID());
        CoopParty targetParty = PLAYER_PARTY_MAP.get(target.getUUID());

        if (targetParty != null) {
            inviter.displayClientMessage(Component.translatable("coop.fun_with_friends.party_already_in_party", target.getDisplayName()), true);
            return false;
        }

        if (inviterParty == null) {
            CoopParty newParty = new CoopParty(inviter);
            newParty.addMember(target.getUUID());

            PARTIES_BY_ID.put(newParty.getPartyId(), newParty);
            PLAYER_PARTY_MAP.put(inviter.getUUID(), newParty);
            PLAYER_PARTY_MAP.put(target.getUUID(), newParty);

            broadcastToParty(newParty, Component.translatable("coop.fun_with_friends.party_created", inviter.getDisplayName(), target.getDisplayName()));

            ModTriggers.FRIEND_JOINED.get().trigger(inviter);
            ModTriggers.FRIEND_JOINED.get().trigger(target);

            return true;
        }

        if (inviterParty.isFull()) {
            inviter.displayClientMessage(Component.translatable("coop.fun_with_friends.party_full"), true);
            return false;
        }

        inviterParty.addMember(target.getUUID());
        PLAYER_PARTY_MAP.put(target.getUUID(), inviterParty);

        broadcastToParty(inviterParty, Component.translatable("coop.fun_with_friends.party_joined", target.getDisplayName()));

        ModTriggers.FRIEND_JOINED.get().trigger(target);

        if (inviterParty.getMembers().size() >= MAX_PARTY_SIZE) {
            var server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                for (UUID memberId : inviterParty.getMembers()) {
                    ServerPlayer member = server.getPlayerList().getPlayer(memberId);
                    if (member != null) {
                        ModTriggers.TRUE_FRIENDSHIP.get().trigger(member);
                    }
                }
            }
        }

        return true;
    }

    public static void leaveParty(ServerPlayer player) {
        UUID playerId = player.getUUID();
        CoopParty party = PLAYER_PARTY_MAP.remove(playerId);

        if (party != null) {
            party.removeMember(playerId);

            player.displayClientMessage(Component.translatable("coop.fun_with_friends.party_left_self"), true);
            broadcastToParty(party, Component.translatable("coop.fun_with_friends.party_member_left", player.getDisplayName()));

            if (party.getMembers().size() <= 1) {
                dissolveParty(party);
            }
        }
    }

    private static void dissolveParty(CoopParty party) {
        broadcastToParty(party, Component.translatable("coop.fun_with_friends.party_dissolved"));
        for (UUID memberId : party.getMembers()) {
            PLAYER_PARTY_MAP.remove(memberId);
        }
        PARTIES_BY_ID.remove(party.getPartyId());
    }

    public static boolean inSameParty(UUID p1, UUID p2) {
        CoopParty party1 = PLAYER_PARTY_MAP.get(p1);
        CoopParty party2 = PLAYER_PARTY_MAP.get(p2);
        return party1 != null && party1 == party2;
    }

    public static CoopParty getParty(UUID playerId) {
        return PLAYER_PARTY_MAP.get(playerId);
    }

    public static List<ServerPlayer> getNearbyPartyMembers(ServerPlayer player, double radius) {
        CoopParty party = PLAYER_PARTY_MAP.get(player.getUUID());
        if (party == null) return Collections.emptyList();

        List<ServerPlayer> nearbyMembers = new ArrayList<>();
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return nearbyMembers;

        double radiusSqr = radius * radius;

        for (UUID memberId : party.getMembers()) {
            if (memberId.equals(player.getUUID())) continue;

            ServerPlayer member = server.getPlayerList().getPlayer(memberId);
            if (member != null && member.level() == player.level() && player.distanceToSqr(member) <= radiusSqr) {
                nearbyMembers.add(member);
            }
        }

        return nearbyMembers;
    }

    public static void handlePlayerDisconnect(ServerPlayer disconnectedPlayer) {
        leaveParty(disconnectedPlayer);
    }

    public static void broadcastToParty(CoopParty party, Component message) {
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        for (UUID memberId : party.getMembers()) {
            ServerPlayer member = server.getPlayerList().getPlayer(memberId);
            if (member != null) {
                member.displayClientMessage(message, true);
            }
        }
    }
}