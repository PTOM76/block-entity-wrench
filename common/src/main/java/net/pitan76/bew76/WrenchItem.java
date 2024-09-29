package net.pitan76.bew76;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.bew76.config.BEWConfig;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnEntityEvent;
import net.pitan76.mcpitanlib.api.event.result.EventResult;
import net.pitan76.mcpitanlib.api.event.v0.InteractionEventRegistry;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.DefaultItemGroups;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;

import static net.pitan76.bew76.BlockEntityWrench._id;

public class WrenchItem extends ExtendItem {
    public WrenchItem() {
        this(CompatibleItemSettings.of().maxCount(1).addGroup(DefaultItemGroups.TOOLS, _id("wrench")));
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

            WorldUtil.setBlockState(world, pos, state.rotate(BlockRotation.CLOCKWISE_90));

            return EventResult.success();
        });
    }

    @Override
    public ActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        if (!BEWConfig.breakFeature) return ActionResult.PASS;

        if (e.isClient()) return ActionResult.SUCCESS;
        if (!e.hasBlockEntity() || !e.player.isSneaking()) return ActionResult.PASS;

        if (BEWConfig.blacklistBlocks.contains(BlockUtil.toCompatID(BlockStateUtil.getBlock(e.getBlockState())).toString()))
            return ActionResult.PASS;

        World world = e.getWorld();
        BlockPos pos = e.getBlockPos();
        BlockState state = e.getBlockState();

        ItemStack dropStack = ItemStackUtil.create(state.getBlock());

        if (BEWConfig.saveBlockEntity) {
            NbtCompound nbt = BlockEntityUtil.getBlockEntityNbt(world, e.getBlockEntity());
            if (!NbtUtil.has(nbt, "id"))
                NbtUtil.putString(nbt, "id", BlockEntityTypeUtil.toID(BlockEntityUtil.getType(e.getBlockEntity())).toString());

            BlockEntityDataUtil.setBlockEntityNbt(dropStack, nbt);
        }

        ItemEntity itemEntity = ItemEntityUtil.create(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, dropStack);
        ItemEntityUtil.setToDefaultPickupDelay(itemEntity);
        WorldUtil.spawnEntity(world, itemEntity);

        WorldUtil.removeBlockEntity(world, pos);
        WorldUtil.breakBlock(world, pos, false);

        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult onRightClickOnEntity(ItemUseOnEntityEvent e) {
        if (!BEWConfig.rotateEntityFeature) return ActionResult.PASS;
        if (e.isClient()) return ActionResult.SUCCESS;

        e.entity.applyRotation(BlockRotation.CLOCKWISE_90);

        return ActionResult.SUCCESS;
    }
}
