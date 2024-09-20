package net.pitan76.bew76;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
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

        InteractionEvent.RIGHT_CLICK_BLOCK.register(((p, hand, pos, direction) -> {
            if (!BEWConfig.rotateFeature) return EventResult.pass();

            Player player = new Player(p);
            ItemStack stack = player.getStackInHand(hand);

            if (player.isSneaking()) return EventResult.pass();
            if (!(stack.getItem() instanceof WrenchItem)) return EventResult.pass();
            if (player.isClient()) return EventResult.interruptTrue();

            World world = player.getWorld();
            BlockState state = WorldUtil.getBlockState(world, pos);

            WorldUtil.setBlockState(world, pos, state.rotate(BlockRotation.CLOCKWISE_90));

            return EventResult.interruptTrue();
        }));
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

//    @Override
//    public TypedActionResult<ItemStack> onRightClick(ItemUseEvent e) {
//        return super.onRightClick(e);
//    }
}
