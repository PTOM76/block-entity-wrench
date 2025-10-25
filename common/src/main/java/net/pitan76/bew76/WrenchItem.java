package net.pitan76.bew76;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.bew76.config.BEWConfig;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnEntityEvent;
import net.pitan76.mcpitanlib.api.event.result.EventResult;
import net.pitan76.mcpitanlib.api.event.v0.InteractionEventRegistry;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.block.BlockUtil;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import net.pitan76.mcpitanlib.api.util.math.BlockRotations;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import net.pitan76.mcpitanlib.midohra.item.ItemGroups;

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
            ItemStack stack = e.getStackInHand();

            if (player.isSneaking()) return EventResult.pass();
            if (!(stack.getItem() instanceof WrenchItem)) return EventResult.pass();
            if (player.isClient()) return EventResult.success();

            World world = e.getWorld();
            BlockPos pos = e.getPos();
            BlockState state = e.getBlockState();

            WorldUtil.setBlockState(world, pos, BlockStateUtil.rotate(state, BlockRotations.CLOCKWISE_90));

            return EventResult.success();
        });
    }

    @Override
    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        if (!BEWConfig.breakFeature) return e.pass();

        if (e.isClient()) return e.success();
        if (!e.hasBlockEntity() || !e.player.isSneaking()) return e.pass();

        if (BEWConfig.blacklistBlocks.contains(BlockUtil.toId(BlockStateUtil.getBlock(e.getBlockState())).toString()))
            return e.pass();

        World world = e.getWorld();
        BlockPos pos = e.getBlockPos();
        BlockState state = e.getBlockState();

        ItemStack dropStack = ItemStackUtil.create(state.getBlock());

        if (BEWConfig.saveBlockEntity) {
            BlockEntityUtil.writeToStack(dropStack, e.getBlockEntity(), RegistryLookupUtil.getRegistryLookup(e.getBlockEntity()));
        }

        ItemEntity itemEntity = ItemEntityUtil.create(world, PosUtil.x(pos) + 0.5, PosUtil.y(pos) + 0.5, PosUtil.z(pos) + 0.5, dropStack);
        ItemEntityUtil.setToDefaultPickupDelay(itemEntity);
        WorldUtil.spawnEntity(world, itemEntity);

        WorldUtil.removeBlockEntity(world, pos);
        WorldUtil.breakBlock(world, pos, false);

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
