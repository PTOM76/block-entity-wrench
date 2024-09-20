package net.pitan76.bew76.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.pitan76.bew76.BlockEntityWrench;

@Mod(BlockEntityWrench.MOD_ID)
public class BlockEntityWrenchNeoForge {
    public BlockEntityWrenchNeoForge(ModContainer modContainer) {
        IEventBus bus = modContainer.getEventBus();

        new BlockEntityWrench();
    }
}