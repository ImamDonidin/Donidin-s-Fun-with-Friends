package com.donidin.funwithfriends.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServerCacheManager {

    private static final Set<Item> ALLOWED_HAT_ITEMS = ConcurrentHashMap.newKeySet();
    private static final Set<Block> ALLOWED_HAT_BLOCKS = ConcurrentHashMap.newKeySet();

    public static boolean isHatCached(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        if (ALLOWED_HAT_ITEMS.contains(item)) return true;

        Block block = Block.byItem(item);
        return ALLOWED_HAT_BLOCKS.contains(block);
    }

    public static void registerHatItem(Item item) {
        ALLOWED_HAT_ITEMS.add(item);
    }

    public static void registerHatBlock(Block block) {
        ALLOWED_HAT_BLOCKS.add(block);
    }
}