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
    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> MEDIC =
            TRIGGERS.register("medic", CoopTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> BOO =
            TRIGGERS.register("boo", CoopTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, CoopTrigger> SHINY_QUARTER =
            TRIGGERS.register("shiny_quarter", CoopTrigger::new);
}