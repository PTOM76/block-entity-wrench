package net.pitan76.bew76.fabric;

import net.pitan76.bew76.BlockEntityWrench;
import net.fabricmc.api.ModInitializer;

public class BlockEntityWrenchFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new BlockEntityWrench();
    }
}