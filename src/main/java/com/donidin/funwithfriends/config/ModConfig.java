package com.donidin.funwithfriends.config;

import com.google.gson.GsonBuilder;
import net.neoforged.fml.loading.FMLPaths;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ModConfig {
    private static final Path CONFIG_FILE = FMLPaths.CONFIGDIR.get().resolve("donidins_fun_with_friends.json");

    public static ModConfig INSTANCE = new ModConfig();

    public boolean enablePartySystem = true;
    public int maxPartySize = 4;
    public boolean enableSharedMountBoost = true;

    public boolean enableItemHanding = true;
    public double itemHandingReachDistance = 3.0;

    public boolean enablePullMechanic = true;
    public double pullReachDistance = 3.0;

    public boolean enableHealthSharing = false;
    public float healthShareAmount = 2.0F;
    public int healthShareCooldown = 20;

    public boolean enableHighFive = true;

    public boolean enablePotatoThrowing = true;
    public int potatoCooldown = 15;
    public boolean enableSlimeballThrowing = true;
    public int slimeballCooldown = 10;
    public boolean enableTntThrowing = true;
    public int tntThrowCooldown = 20;

    public boolean enableBossScaling = true;
    public float bossHealthScaleMultiplier = 0.5F;
    public float bossHealthMaxMultiplier = 3.0F;

    public boolean enableHatEquip = true;
    public boolean allowBannersOnHead = true;
    public boolean allowFeathersOnHead = true;
    public boolean allowBlocksOnHead = true;
    public boolean allowCandleStacking = true;

    public boolean enableTypingIndicator = true;
    public boolean enableCustomNickColors = true;
    public boolean enableChatEmojis = true;

    public static void load() {
        if (CONFIG_FILE.toFile().exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE.toFile())) {
                INSTANCE = new GsonBuilder().create().fromJson(reader, ModConfig.class);
                if (INSTANCE == null) {
                    INSTANCE = new ModConfig();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE.toFile())) {
            new GsonBuilder().setPrettyPrinting().create().toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}