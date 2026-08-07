package com.donidin.funwithfriends.cosmetics;

import net.minecraft.resources.ResourceLocation;

public enum NickColor {
    EMERALD("emerald", 0x50C878, ResourceLocation.parse("minecraft:story/cure_zombie_villager")),
    PURPLE("purple", 0xA020F0, ResourceLocation.parse("minecraft:nether/return_to_sender")),
    NETHERITE_BLUE("netherite_blue", 0x26252D, ResourceLocation.parse("minecraft:nether/netherite_armor")),
    AQUAMARINE("aquamarine", 0x7FFFD4, ResourceLocation.parse("minecraft:nether/create_full_beacon")),
    PEBBLE_GRAY("pebble_gray", 0xB8B799, ResourceLocation.parse("minecraft:adventure/summon_iron_golem")),
    SIGNAL_ORANGE("signal_orange", 0xFF9900, ResourceLocation.parse("minecraft:husbandry/complete_catalogue")),
    AMETHYST("amethyst", 0x9966CC, ResourceLocation.parse("minecraft:adventure/spyglass_at_parrot")),
    CRIMSON("crimson", 0xDC143C, ResourceLocation.parse("minecraft:nether/find_bastion")),
    SCULK_TEAL("sculk_teal", 0x008080, ResourceLocation.parse("minecraft:adventure/avoid_vibration")),
    GOLDEN("golden", 0xFFD700, ResourceLocation.parse("minecraft:husbandry/make_a_sign_glow")),
    CHERRY_PINK("cherry_pink", 0xFFB7C5, ResourceLocation.parse("minecraft:husbandry/allay_deliver_cake_to_note_block")),
    DIAMOND_BLUE("diamond_blue", 0x1E90FF, ResourceLocation.parse("minecraft:story/mine_diamond")),
    HONEY_YELLOW("honey_yellow", 0xA98307, ResourceLocation.parse("minecraft:husbandry/wax_on")),
    ROYAL_BLUE("royal_blue", 0x4169E1, ResourceLocation.parse("minecraft:story/shiny_gear")),
    ENDPLE("endple", 0xFF00FF, ResourceLocation.parse("minecraft:end/enter_end_gateway")),
    SO_GREENISH("so_greenish", 0xBFFF00, ResourceLocation.parse("minecraft:husbandry/plant_seed")),
    GRAY_AS_ANDESITE("gray_as_andesite", 0x808080, ResourceLocation.parse("minecraft:adventure/minecraft_trials_edition")),
    NINNI_TEAL("ninni_teal", 0x1E5945, ResourceLocation.parse("minecraft:adventure/adventuring_time"));

    private final String id;
    private final int hexColor;
    private final ResourceLocation advancementId;

    NickColor(String id, int hexColor, ResourceLocation advancementId) {
        this.id = id;
        this.hexColor = hexColor;
        this.advancementId = advancementId;
    }

    public String getId() { return id; }
    public int getHexColor() { return hexColor; }
    public ResourceLocation getAdvancementId() { return advancementId; }

    public static NickColor fromId(String id) {
        for (NickColor color : values()) {
            if (color.id.equalsIgnoreCase(id)) return color;
        }
        return null;
    }
}