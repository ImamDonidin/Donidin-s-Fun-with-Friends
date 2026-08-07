package com.donidin.funwithfriends.datagen;

import com.donidin.funwithfriends.FunWithFriends;
import com.donidin.funwithfriends.advancement.CoopTrigger;
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
import java.util.Optional;
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
                            true,
                            true,
                            false
                    )
                    .addCriterion("joined", PlayerTrigger.TriggerInstance.tick())
                    .save(saver, ResourceLocation.fromNamespaceAndPath(FunWithFriends.MOD_ID, "coop/root"), existingFileHelper);

            AdvancementHolder friendNow = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            Items.PLAYER_HEAD,
                            Component.translatable("advancements.fun_with_friends.you_my_friend_now.title"),
                            Component.translatable("advancements.fun_with_friends.you_my_friend_now.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("friend_joined", ModTriggers.FRIEND_JOINED.get().createCriterion(new CoopTrigger.TriggerInstance(Optional.empty())))
                    .save(saver, ResourceLocation.fromNamespaceAndPath(FunWithFriends.MOD_ID, "coop/you_my_friend_now"), existingFileHelper);

            AdvancementHolder warAndPeace = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            Items.WRITTEN_BOOK,
                            Component.translatable("advancements.fun_with_friends.war_and_peace.title"),
                            Component.translatable("advancements.fun_with_friends.war_and_peace.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("long_typing", ModTriggers.WAR_AND_PEACE.get().createCriterion(new CoopTrigger.TriggerInstance(Optional.empty())))
                    .save(saver, ResourceLocation.fromNamespaceAndPath(FunWithFriends.MOD_ID, "coop/war_and_peace"), existingFileHelper);

            AdvancementHolder medic = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            Items.GLISTERING_MELON_SLICE,
                            Component.translatable("advancements.fun_with_friends.medic.title"),
                            Component.translatable("advancements.fun_with_friends.medic.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("give_health", ModTriggers.MEDIC.get().createCriterion(new CoopTrigger.TriggerInstance(Optional.empty())))
                    .save(saver, ResourceLocation.fromNamespaceAndPath(FunWithFriends.MOD_ID, "coop/medic"), existingFileHelper);

            AdvancementHolder boo = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            Items.POISONOUS_POTATO,
                            Component.translatable("advancements.fun_with_friends.boo.title"),
                            Component.translatable("advancements.fun_with_friends.boo.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("throw_potato", ModTriggers.BOO.get().createCriterion(new CoopTrigger.TriggerInstance(Optional.empty())))
                    .save(saver, ResourceLocation.fromNamespaceAndPath(FunWithFriends.MOD_ID, "coop/boo"), existingFileHelper);

            AdvancementHolder shinyQuarter = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            Items.GOLD_NUGGET,
                            Component.translatable("advancements.fun_with_friends.shiny_quarter.title"),
                            Component.translatable("advancements.fun_with_friends.shiny_quarter.description"),
                            null,
                            AdvancementType.TASK,
                            true, true, false
                    )
                    .addCriterion("give_item", ModTriggers.SHINY_QUARTER.get().createCriterion(new CoopTrigger.TriggerInstance(Optional.empty())))
                    .save(saver, ResourceLocation.fromNamespaceAndPath(FunWithFriends.MOD_ID, "coop/shiny_quarter"), existingFileHelper);

            AdvancementHolder hotPotato = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            Items.TNT,
                            Component.translatable("advancements.fun_with_friends.hot_potato.title"),
                            Component.translatable("advancements.fun_with_friends.hot_potato.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("throw_tnt", ModTriggers.HOT_POTATO.get().createCriterion(new CoopTrigger.TriggerInstance(Optional.empty())))
                    .save(saver, ResourceLocation.fromNamespaceAndPath(FunWithFriends.MOD_ID, "coop/hot_potato"), existingFileHelper);
        }
    }
}