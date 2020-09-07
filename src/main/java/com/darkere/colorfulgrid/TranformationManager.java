package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.GridNetworkNode;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

public class TranformationManager {
    private Map<Integer, ItemStack> matrix;
    private Map<Integer, ItemStack> filter;
    private Map<Integer, ItemStack> processingMatrix;
    private Map<Integer, FluidStack> processingMatrixFluids;

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
        Runnable run = ()->{};
        if (state.getBlock() instanceof ColoredGridBlock) {
            newState = state;
            if (state.get(ColorfulGrid.COLOR) == color) return;
        } else if (state.getBlock() instanceof GridBlock) {
            newState = transformToColoredGrid(state);
            world.destroyBlock(pos, false);
            getContainedItems(world,pos);
            run = ()-> reInsertItems(world,pos);
        } else if (state.getBlock() instanceof ColoredCraftingMonitorBlock) {
            newState = state;
            if (state.get(ColorfulGrid.COLOR) == color) return;
        } else if (state.getBlock() instanceof CraftingMonitorBlock) {
            newState = BlocksAndItems.COLORED_CRAFTING_MONITOR.get().getDefaultState().with(BlockDirection.ANY.getProperty(), state.get(BlockDirection.HORIZONTAL.getProperty())).with(NetworkNodeBlock.CONNECTED, state.get(NetworkNodeBlock.CONNECTED));
        } else if (state.getBlock() instanceof ColoredCrafterManagerBlock) {
            newState = state;
            if (state.get(ColorfulGrid.COLOR) == color) return;
        } else if (state.getBlock() instanceof CrafterManagerBlock) {
            newState = BlocksAndItems.COLORED_CRAFTER_MANAGER.get().getDefaultState().with(BlockDirection.ANY.getProperty(), state.get(BlockDirection.HORIZONTAL.getProperty())).with(NetworkNodeBlock.CONNECTED, state.get(NetworkNodeBlock.CONNECTED));
        } else {
            return;
        }

        if (newState == null) return;

        world.setBlockState(pos, newState.with(ColorfulGrid.COLOR, color));
        ForgeEventFactory.onBlockPlace(event.getEntity(), BlockSnapshot.create(world.func_234923_W_(), world, pos), event.getFace());
        run.run();
        event.setCanceled(true);
    }

    private void reInsertItems(ServerWorld world, BlockPos pos) {
        INetworkNode node = API.instance().getNetworkNodeManager(world).getNode(pos);
        if (!(node instanceof GridNetworkNode)) return;
        GridNetworkNode grid = (GridNetworkNode) node;
        refillSlots(grid.getFilter(), filter);
        filter = null;
        if (grid.getGridType() == GridType.CRAFTING) {
            for (int i = 0; i < grid.getCraftingMatrix().getSizeInventory(); i++) {
                grid.getCraftingMatrix().setInventorySlotContents(i, matrix.get(i));
            }
            matrix = null;
        }
        if (grid.getGridType() == GridType.PATTERN) {
            refillSlots(grid.getProcessingMatrix(), processingMatrix);
            for (int i = 0; i < grid.getProcessingMatrixFluids().getSlots(); i++) {
                grid.getProcessingMatrixFluids().setFluid(i, processingMatrixFluids.get(i));
            }
            processingMatrix = null;
            processingMatrixFluids = null;
        }
    }

    private void refillSlots(IItemHandlerModifiable empty, Map<Integer, ItemStack> filled) {
        for (int i = 0; i < empty.getSlots(); i++) {
            empty.setStackInSlot(i, filled.get(i));
        }
    }

    private void getContainedItems(ServerWorld world, BlockPos pos) {
        INetworkNode node = API.instance().getNetworkNodeManager(world).getNode(pos);
        if (!(node instanceof GridNetworkNode)) return;
        GridNetworkNode grid = (GridNetworkNode) node;
        fillMap(grid.getFilter().getSlots(), x -> grid.getFilter().getStackInSlot(x), filter);
        clearItemHandler(grid.getFilter());
        if (grid.getGridType() == GridType.CRAFTING) {
            fillMap(grid.getCraftingMatrix().getSizeInventory(), x -> grid.getCraftingMatrix().getStackInSlot(x), matrix);
            for (int i = 0; i < grid.getCraftingMatrix().getSizeInventory(); i++) {
                grid.getCraftingMatrix().setInventorySlotContents(i, ItemStack.EMPTY);
            }
        }
        if (grid.getGridType() == GridType.PATTERN) {
            fillMap(grid.getProcessingMatrix().getSlots(), x -> grid.getProcessingMatrix().getStackInSlot(x), processingMatrix);
            for (int i = 0; i < grid.getProcessingMatrixFluids().getSlots(); i++) {
                processingMatrixFluids.put(i, grid.getProcessingMatrixFluids().getFluid(i).copy());
            }
        }

    }

    private void clearItemHandler(IItemHandlerModifiable filter) {
        for (int i = 0; i < filter.getSlots(); i++) {
            filter.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    private void fillMap(int size, Function<Integer, ItemStack> getStack, Map<Integer, ItemStack> map) {
        for (int i = 0; i < size; i++) {
            map.put(i, getStack.apply(i).copy());
        }
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
