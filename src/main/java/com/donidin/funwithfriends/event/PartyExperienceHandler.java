package com.donidin.funwithfriends.event;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.advancement.ModTriggers;
import com.donidin.funwithfriends.coop.CoopPartyManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

import java.util.List;

@EventBusSubscriber(modid = FunWithFriends.MOD_ID)
public class PartyExperienceHandler {

    private static final float XP_BONUS_MULTIPLIER = 1.25F;

    @SubscribeEvent
    public static void onXpPickup(PlayerXpEvent.PickupXp event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        CoopPartyManager.CoopParty party = CoopPartyManager.getParty(player.getUUID());
        if (party != null) {
            List<ServerPlayer> nearbyCloseMembers = CoopPartyManager.getNearbyPartyMembers(player, 16.0);

            if (!nearbyCloseMembers.isEmpty()) {
                int originalXp = event.getOrb().getValue();
                int bonusXp = Math.round(originalXp * XP_BONUS_MULTIPLIER);

                event.getOrb().value = bonusXp;

                ModTriggers.SHARED_XP.get().trigger(player);
            }
        }
    }
}