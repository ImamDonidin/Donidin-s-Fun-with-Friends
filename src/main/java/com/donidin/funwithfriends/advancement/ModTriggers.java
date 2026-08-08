package com.donidin.funwithfriends.advancement;

import com.donidin.funwithfriends.FunWithFriends;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, FunWithFriends.MOD_ID);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> FRIEND_JOINED =
            TRIGGERS.register("friend_joined", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> WAR_AND_PEACE =
            TRIGGERS.register("war_and_peace", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> SHARED_XP =
            TRIGGERS.register("shared_xp", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> BOO =
            TRIGGERS.register("boo", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> SHINY_QUARTER =
            TRIGGERS.register("shiny_quarter", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> HOT_POTATO =
            TRIGGERS.register("hot_potato", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> HIGH_FIVE =
            TRIGGERS.register("high_five", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> SAVIOR =
            TRIGGERS.register("savior", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> GOOD_NIGHT =
            TRIGGERS.register("good_night", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> TRADE_DEAL =
            TRIGGERS.register("trade_deal", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> OOPS_MY_BAD =
            TRIGGERS.register("oops_my_bad", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> BANQUET =
            TRIGGERS.register("banquet", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> ROAD_TRIP =
            TRIGGERS.register("road_trip", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> TEAM_WORK =
            TRIGGERS.register("team_work", CoopTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> TRUE_FRIENDSHIP =
            TRIGGERS.register("true_friendship", CoopTrigger::new);
}