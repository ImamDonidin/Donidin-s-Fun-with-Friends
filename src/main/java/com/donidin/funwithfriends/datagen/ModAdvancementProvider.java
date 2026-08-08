package com.donidin.funwithfriends.datagen;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.advancement.ModTriggers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider {

    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper fileHelper) {
        super(output, registries, fileHelper, List.of(new ModAdvancements()));
    }

    private static class ModAdvancements implements AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {

            AdvancementHolder root = Advancement.Builder.advancement()
                    .display(
                            Items.HEART_OF_THE_SEA,
                            Component.translatable("advancements.fun_with_friends.root.title"),
                            Component.translatable("advancements.fun_with_friends.root.description"),
                            ResourceLocation.withDefaultNamespace("textures/block/blue_terracotta.png"),
                            AdvancementType.TASK,
                            true, true, false
                    )
                    .addCriterion("joined", PlayerTrigger.TriggerInstance.tick())
                    .save(saver, id("coop/root"), existingFileHelper);

            AdvancementHolder friendNow = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            Items.PLAYER_HEAD,
                            Component.translatable("advancements.fun_with_friends.you_my_friend_now.title"),
                            Component.translatable("advancements.fun_with_friends.you_my_friend_now.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .addCriterion("friend_joined", ModTriggers.FRIEND_JOINED.get().createCriterion())
                    .save(saver, id("coop/you_my_friend_now"), existingFileHelper);

            AdvancementHolder shinyQuarter = Advancement.Builder.advancement()
                    .parent(friendNow)
                    .display(
                            Items.GOLD_NUGGET,
                            Component.translatable("advancements.fun_with_friends.shiny_quarter.title"),
                            Component.translatable("advancements.fun_with_friends.shiny_quarter.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .addCriterion("give_item", ModTriggers.SHINY_QUARTER.get().createCriterion())
                    .save(saver, id("coop/shiny_quarter"), existingFileHelper);

            AdvancementHolder tradeDeal = Advancement.Builder.advancement()
                    .parent(shinyQuarter)
                    .display(
                            Items.EMERALD,
                            Component.translatable("advancements.fun_with_friends.trade_deal.title"),
                            Component.translatable("advancements.fun_with_friends.trade_deal.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .addCriterion("trade", ModTriggers.TRADE_DEAL.get().createCriterion())
                    .save(saver, id("coop/trade_deal"), existingFileHelper);

            AdvancementHolder highFive = Advancement.Builder.advancement()
                    .parent(friendNow)
                    .display(
                            Items.SLIME_BALL,
                            Component.translatable("advancements.fun_with_friends.high_five.title"),
                            Component.translatable("advancements.fun_with_friends.high_five.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .addCriterion("high_five", ModTriggers.HIGH_FIVE.get().createCriterion())
                    .save(saver, id("coop/high_five"), existingFileHelper);

            AdvancementHolder goodNight = Advancement.Builder.advancement()
                    .parent(friendNow)
                    .display(
                            Items.RED_BED,
                            Component.translatable("advancements.fun_with_friends.good_night.title"),
                            Component.translatable("advancements.fun_with_friends.good_night.description"),
                            null, AdvancementType.GOAL, true, true, false
                    )
                    .addCriterion("sleep", ModTriggers.GOOD_NIGHT.get().createCriterion())
                    .save(saver, id("coop/good_night"), existingFileHelper);

            AdvancementHolder roadTrip = Advancement.Builder.advancement()
                    .parent(friendNow)
                    .display(
                            Items.OAK_BOAT,
                            Component.translatable("advancements.fun_with_friends.road_trip.title"),
                            Component.translatable("advancements.fun_with_friends.road_trip.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .addCriterion("ride", ModTriggers.ROAD_TRIP.get().createCriterion())
                    .save(saver, id("coop/road_trip"), existingFileHelper);

            AdvancementHolder banquet = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            Items.COOKED_BEEF,
                            Component.translatable("advancements.fun_with_friends.banquet.title"),
                            Component.translatable("advancements.fun_with_friends.banquet.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .addCriterion("eat_together", ModTriggers.BANQUET.get().createCriterion())
                    .save(saver, id("coop/banquet"), existingFileHelper);

            AdvancementHolder sharedXp = Advancement.Builder.advancement()
                    .parent(banquet)
                    .display(
                            Items.EXPERIENCE_BOTTLE,
                            Component.translatable("advancements.fun_with_friends.shared_xp.title"),
                            Component.translatable("advancements.fun_with_friends.shared_xp.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .addCriterion("get_bonus_xp", ModTriggers.SHARED_XP.get().createCriterion())
                    .save(saver, id("coop/shared_xp"), existingFileHelper);

            AdvancementHolder teamWork = Advancement.Builder.advancement()
                    .parent(sharedXp)
                    .display(
                            Items.DIAMOND_SWORD,
                            Component.translatable("advancements.fun_with_friends.team_work.title"),
                            Component.translatable("advancements.fun_with_friends.team_work.description"),
                            null, AdvancementType.GOAL, true, true, false
                    )
                    .addCriterion("fight_together", ModTriggers.TEAM_WORK.get().createCriterion())
                    .save(saver, id("coop/team_work"), existingFileHelper);

            AdvancementHolder warAndPeace = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            Items.WRITTEN_BOOK,
                            Component.translatable("advancements.fun_with_friends.war_and_peace.title"),
                            Component.translatable("advancements.fun_with_friends.war_and_peace.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .addCriterion("long_typing", ModTriggers.WAR_AND_PEACE.get().createCriterion())
                    .save(saver, id("coop/war_and_peace"), existingFileHelper);

            AdvancementHolder boo = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            Items.POISONOUS_POTATO,
                            Component.translatable("advancements.fun_with_friends.boo.title"),
                            Component.translatable("advancements.fun_with_friends.boo.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .addCriterion("throw_potato", ModTriggers.BOO.get().createCriterion())
                    .save(saver, id("coop/boo"), existingFileHelper);

            AdvancementHolder hotPotato = Advancement.Builder.advancement()
                    .parent(boo)
                    .display(
                            Items.TNT,
                            Component.translatable("advancements.fun_with_friends.hot_potato.title"),
                            Component.translatable("advancements.fun_with_friends.hot_potato.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .addCriterion("throw_tnt", ModTriggers.HOT_POTATO.get().createCriterion())
                    .save(saver, id("coop/hot_potato"), existingFileHelper);

            AdvancementHolder oopsMyBad = Advancement.Builder.advancement()
                    .parent(boo)
                    .display(
                            Items.BOW,
                            Component.translatable("advancements.fun_with_friends.oops_my_bad.title"),
                            Component.translatable("advancements.fun_with_friends.oops_my_bad.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .addCriterion("friendly_fire", ModTriggers.OOPS_MY_BAD.get().createCriterion())
                    .save(saver, id("coop/oops_my_bad"), existingFileHelper);

            AdvancementHolder trueFriendship = Advancement.Builder.advancement()
                    .parent(teamWork)
                    .display(
                            Items.NETHER_STAR,
                            Component.translatable("advancements.fun_with_friends.true_friendship.title"),
                            Component.translatable("advancements.fun_with_friends.true_friendship.description"),
                            null, AdvancementType.CHALLENGE, true, true, false
                    )
                    .addCriterion("full_party", ModTriggers.TRUE_FRIENDSHIP.get().createCriterion())
                    .save(saver, id("coop/true_friendship"), existingFileHelper);
        }

        private ResourceLocation id(String path) {
            return ResourceLocation.fromNamespaceAndPath(FunWithFriends.MOD_ID, path);
        }
    }
}