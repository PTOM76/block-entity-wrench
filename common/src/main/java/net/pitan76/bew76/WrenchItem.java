package net.pitan76.bew76;

import net.pitan76.bew76.config.BEWConfig;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnEntityEvent;
import net.pitan76.mcpitanlib.api.event.result.EventResult;
import net.pitan76.mcpitanlib.api.event.v0.InteractionEventRegistry;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import net.pitan76.mcpitanlib.api.util.math.BlockRotations;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.item.ItemGroups;
import net.pitan76.mcpitanlib.midohra.item.ItemStack;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.world.World;

import static net.pitan76.bew76.BlockEntityWrench._id;

public class WrenchItem extends CompatItem {
    public WrenchItem() {
        this(CompatibleItemSettings.of(_id("wrench")).maxCount(1).addGroup(ItemGroups.TOOLS));
    }

    public WrenchItem(CompatibleItemSettings settings) {
        super(settings);

        InteractionEventRegistry.registerRightClickBlock(e -> {
            if (!BEWConfig.rotateFeature) return EventResult.pass();

            Player player = e.getPlayer();
            ItemStack stack = ItemStack.of(e.getStackInHand());

            if (player.isSneaking()) return EventResult.pass();
            if (!(stack.instanceOf(WrenchItem.class))) return EventResult.pass();
            if (player.isClient()) return EventResult.success();

            World world = e.player.getMidohraWorld();
            BlockPos pos = BlockPos.of(e.getPos());
            BlockState state = BlockState.of(e.getBlockState());

            world.setBlockState(pos, BlockState.of(
                    BlockStateUtil.rotate(state.toMinecraft(), BlockRotations.CLOCKWISE_90)));

            return EventResult.success();
        });
    }

    @Override
    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        if (!BEWConfig.breakFeature) return e.pass();

        if (e.isClient()) return e.success();
        if (!e.hasBlockEntity() || !e.player.isSneaking()) return e.pass();

        BlockState state = e.getMidohraState();

        if (BEWConfig.blacklistBlocks.contains(state.getBlock().getId().toString()))
            return e.pass();

        World world = e.getMidohraWorld();
        BlockPos pos = e.getMidohraPos();

        ItemStack dropStack = state.getBlock().asItem().createStack();

        if (BEWConfig.saveBlockEntity) {
            BlockEntityUtil.writeToStack(dropStack.toMinecraft(), e.getBlockEntity(), RegistryLookupUtil.getRegistryLookup(e.getBlockEntity()));
        }

        ItemEntityUtil.createWithSpawn(world, dropStack, pos.toCenterVector3d());

        world.removeBlockEntity(pos);
        world.breakBlock(pos, false);

        return e.success();
    }

    @Override
    public CompatActionResult onRightClickOnEntity(ItemUseOnEntityEvent e) {
        if (!BEWConfig.rotateEntityFeature) return e.pass();
        if (e.isClient()) return e.success();

        EntityUtil.applyRotation(e.entity, BlockRotations.CLOCKWISE_90);

        return e.success();
    }
}
