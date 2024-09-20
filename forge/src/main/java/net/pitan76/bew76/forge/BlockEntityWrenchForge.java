package net.pitan76.bew76.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.pitan76.bew76.BlockEntityWrench;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BlockEntityWrench.MOD_ID)
public class BlockEntityWrenchForge {
    public BlockEntityWrenchForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        EventBuses.registerModEventBus(BlockEntityWrench.MOD_ID, bus);
        new BlockEntityWrench();
    }
}