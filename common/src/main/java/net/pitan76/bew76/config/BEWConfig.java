package net.pitan76.bew76.config;

import net.pitan76.easyapi.config.YamlConfig;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BEWConfig {
    private static final File file = new File(PlatformUtil.getConfigFolderAsFile(), "bew76.yml");
    private static final YamlConfig config = new YamlConfig(file);

    public static boolean rotateFeature = true;
    public static boolean breakFeature = true;
    public static boolean rotateEntityFeature = true;
    public static boolean saveBlockEntity = true;
    public static List<String> blacklistBlocks = new ArrayList<>();

    public static boolean changed = false;

    public static void init() {
        reload();
    }

    public static void reload() {
        if (file.exists() && file.isFile())
            config.load(file);

        setDefault();

        if (!file.exists() || !file.isFile())
            save();

    }

    public static void saveIfChanged() {
        if (isChanged()) {
            setChanged(false);
            save();
        }
    }

    public static boolean isChanged() {
        return changed;
    }

    public static void setChanged(boolean changed) {
        BEWConfig.changed = changed;
    }

    public static Boolean setDefault() {
        rotateFeature = config.getBooleanOrCreate("rotate_feature", true);
        breakFeature = config.getBooleanOrCreate("break_feature", true);
        saveBlockEntity = config.getBooleanOrCreate("save_block_entity", true);
        rotateEntityFeature = config.getBooleanOrCreate("rotate_entity_feature", true);
        blacklistBlocks = (List) config.getOrCreate("blacklist_blocks", getDefaultBlacklistBlocks());
        return true;
    }

    public static void save() {
        config.set("rotate_feature", rotateFeature);
        config.set("break_feature", breakFeature);
        config.set("save_block_entity", saveBlockEntity);
        config.set("rotate_entity_feature", rotateEntityFeature);
        config.set("blacklist_blocks", blacklistBlocks);
        saveOnly();
    }

    public static void saveOnly() {
        config.save(file, true);
    }

    public static List<String> getDefaultBlacklistBlocks() {
        List<String> blacklistBlocks = new ArrayList<>();

        // Bed
        blacklistBlocks.add("minecraft:white_bed");
        blacklistBlocks.add("minecraft:orange_bed");
        blacklistBlocks.add("minecraft:magenta_bed");
        blacklistBlocks.add("minecraft:light_blue_bed");
        blacklistBlocks.add("minecraft:yellow_bed");
        blacklistBlocks.add("minecraft:lime_bed");
        blacklistBlocks.add("minecraft:pink_bed");
        blacklistBlocks.add("minecraft:gray_bed");
        blacklistBlocks.add("minecraft:light_gray_bed");
        blacklistBlocks.add("minecraft:cyan_bed");
        blacklistBlocks.add("minecraft:purple_bed");
        blacklistBlocks.add("minecraft:blue_bed");
        blacklistBlocks.add("minecraft:brown_bed");
        blacklistBlocks.add("minecraft:green_bed");
        blacklistBlocks.add("minecraft:red_bed");
        blacklistBlocks.add("minecraft:black_bed");

        return blacklistBlocks;
    }

    public static File getFile() {
        return file;
    }

    public static YamlConfig getConfig() {
        return config;
    }
}
