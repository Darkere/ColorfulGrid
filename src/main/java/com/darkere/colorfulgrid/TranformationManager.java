package com.darkere.colorfulgrid;

import com.darkere.colorfulgrid.*;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.reflect.Field;

public class TranformationManager {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) return;
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof DyeItem)) return;
        ServerWorld world = (ServerWorld) event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        DyeColor color = DyeColor.getColor(stack);
        if (color == null) return;

        BlockState newState = null;
        if (state.getBlock() instanceof ColoredGridBlock) {
            newState = state;
            if (state.get(ColorfulGrid.COLOR) == color) return;
        } else if (state.getBlock() instanceof GridBlock) {
            newState = transformToColoredGrid(state);
            world.destroyBlock(pos, false);
        } else if (state.getBlock() instanceof ColoredCraftingMonitorBlock) {
            newState = state;
            if (state.get(ColorfulGrid.COLOR) == color) return;
        } else if (state.getBlock() instanceof CraftingMonitorBlock) {
            newState = BlocksAndItems.COLORED_CRAFTING_MONITOR.get().getDefaultState().with(BlockDirection.ANY.getProperty(), state.get(BlockDirection.HORIZONTAL.getProperty())).with(NetworkNodeBlock.CONNECTED,state.get(NetworkNodeBlock.CONNECTED));
        } else if (state.getBlock() instanceof ColoredCrafterManagerBlock){
            newState = state;
            if (state.get(ColorfulGrid.COLOR) == color) return;
        } else if(state.getBlock() instanceof CrafterManagerBlock){
            newState = BlocksAndItems.COLORED_CRAFTER_MANAGER.get().getDefaultState().with(BlockDirection.ANY.getProperty(), state.get(BlockDirection.HORIZONTAL.getProperty())).with(NetworkNodeBlock.CONNECTED,state.get(NetworkNodeBlock.CONNECTED));
        } else {
            return;
        }

        if (newState == null) return;

        world.setBlockState(pos, newState.with(ColorfulGrid.COLOR, color));
        ForgeEventFactory.onBlockPlace(event.getEntity(), BlockSnapshot.create(world.func_234923_W_(), world, pos), event.getFace());
        event.setCanceled(true);
    }

    private BlockState transformToColoredGrid(BlockState state) {
        GridType type = getGridTypeViaReflection(state.getBlock());
        if (type == null) return null;
        return getStateForType(state, type);

    }

    private BlockState getStateForType(BlockState state, GridType type) {
        BlockState newState = null;
        switch (type) {
            case NORMAL:
                newState = BlocksAndItems.COLORED_GRID.getDefaultState();
                break;
            case CRAFTING:
                newState = BlocksAndItems.COLORED_CRAFTING_GRID.getDefaultState();
                break;
            case PATTERN:
                newState = BlocksAndItems.COLORED_PATTERN_GRID.getDefaultState();
                break;
            case FLUID:
                newState = BlocksAndItems.COLORED_FLUID_GRID.getDefaultState();
                break;
        }
        Boolean connected = state.get(NetworkNodeBlock.CONNECTED);
        Direction dir = state.get(BlockDirection.HORIZONTAL.getProperty());
        newState = newState.with(NetworkNodeBlock.CONNECTED, connected);
        return newState.with(BlockDirection.ANY.getProperty(), dir);
    }

    private GridType getGridTypeViaReflection(Block block) {
        Field type = null;
        try {
            type = GridBlock.class.getDeclaredField("type");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (type == null) return null;
        type.setAccessible(true);
        try {
            return (GridType) type.get(block);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
