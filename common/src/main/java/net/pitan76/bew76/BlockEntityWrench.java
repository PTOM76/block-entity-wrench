package net.pitan76.bew76;

import net.pitan76.bew76.config.BEWConfig;
import net.pitan76.mcpitanlib.api.CommonModInitializer;
import net.pitan76.mcpitanlib.api.registry.v2.CompatRegistryV2;
import net.pitan76.mcpitanlib.api.util.*;

public class BlockEntityWrench extends CommonModInitializer {
    public static final String MOD_ID = "bew76";
    public static final String MOD_NAME = "BlockEntityWrench";

    public static BlockEntityWrench INSTANCE;
    public static CompatRegistryV2 registry;

    @Override
    public void init() {
        INSTANCE = this;
        registry = super.registry;

        BEWConfig.init();
        registry.registerExtendItem(_id("wrench"), WrenchItem::new);
    }

    public static CompatIdentifier _id(String path) {
        return CompatIdentifier.of(MOD_ID, path);
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    @Override
    public String getName() {
        return MOD_NAME;
    }
}