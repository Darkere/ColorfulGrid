package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.api.network.NetworkType;
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
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

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

        BlockState newState;
        Runnable run = () -> {
        };
        if (state.getBlock().getRegistryName().getNamespace().equals(ColorfulGrid.MODID)) {
            newState = state;
            if (state.get(ColorfulGrid.COLOR) == color) return;
        } else if (state.getBlock() instanceof GridBlock) {
            newState = getStateForGridType(state, Objects.requireNonNull(getGridTypeViaReflection(state.getBlock())));
            world.destroyBlock(pos, false);
            getContainedItems(world, pos);
            run = () -> reInsertItems(world, pos);
        } else if (state.getBlock() instanceof CraftingMonitorBlock) {
            newState = getBlockStateWithJigOri(state, BlocksAndItems.COLORED_CRAFTING_MONITOR.get());
        } else if (state.getBlock() instanceof CrafterManagerBlock) {
            newState = getBlockStateWithJigOri(state, BlocksAndItems.COLORED_CRAFTER_MANAGER.get());
        } else if (state.getBlock() instanceof RelayBlock) {
            newState = BlocksAndItems.COLORED_RELAY.get().getDefaultState().with(NetworkNodeBlock.CONNECTED, state.get(NetworkNodeBlock.CONNECTED));
        } else if (state.getBlock() instanceof ControllerBlock) {
            if(((ControllerBlock) state.getBlock()).getType() == NetworkType.CREATIVE){
                newState = BlocksAndItems.COLORED_CONTROLLER_CREATIVE.get().getDefaultState();
            } else {
                newState = BlocksAndItems.COLORED_CONTROLLER.get().getDefaultState();
            }
        } else if (state.getBlock() instanceof SecurityManagerBlock) {
            newState = BlocksAndItems.COLORED_SECURITY.get().getDefaultState().with(NetworkNodeBlock.CONNECTED, state.get(NetworkNodeBlock.CONNECTED)).with(BlockDirection.HORIZONTAL.getProperty(),state.get(BlockDirection.HORIZONTAL.getProperty()));
        } else if (state.getBlock() instanceof CrafterBlock) {
            newState = BlocksAndItems.COLORED_CRAFTER.get().getDefaultState().with(NetworkNodeBlock.CONNECTED, state.get(NetworkNodeBlock.CONNECTED)).with(BlockDirection.ANY_FACE_PLAYER.getProperty(),state.get(BlockDirection.ANY_FACE_PLAYER.getProperty()));
        } else if (state.getBlock() instanceof DiskManipulatorBlock) {
            newState = BlocksAndItems.COLORED_DISKMANIPULATOR.get().getDefaultState().with(NetworkNodeBlock.CONNECTED, state.get(NetworkNodeBlock.CONNECTED)).with(BlockDirection.HORIZONTAL.getProperty(),state.get(BlockDirection.HORIZONTAL.getProperty()));
        } else if (state.getBlock() instanceof NetworkTransmitterBlock) {
            newState = BlocksAndItems.COLORED_TRANSMITTER.get().getDefaultState().with(NetworkNodeBlock.CONNECTED,state.get(NetworkNodeBlock.CONNECTED));
        } else if (state.getBlock() instanceof NetworkReceiverBlock) {
            newState = BlocksAndItems.COLORED_RECEIVER.get().getDefaultState().with(NetworkNodeBlock.CONNECTED,state.get(NetworkNodeBlock.CONNECTED));
        } else {

            return;
        }

        if (newState == null) return;

        world.setBlockState(pos, newState.with(ColorfulGrid.COLOR, color));
        ForgeEventFactory.onBlockPlace(event.getEntity(), BlockSnapshot.create(world.func_234923_W_(), world, pos), event.getFace());
        run.run();
        event.setCanceled(true);
    }

    private BlockState getBlockStateWithJigOri(BlockState state, Block newBlock) {
        Direction direction = state.get(BlockDirection.HORIZONTAL.getProperty());
        JigsawOrientation orientation; //TODO make the models have the proper orientation
        switch (direction) {
            case EAST:
                orientation = JigsawOrientation.WEST_UP;
                break;
            case SOUTH:
                orientation = JigsawOrientation.SOUTH_UP;
                break;
            case NORTH:
                orientation = JigsawOrientation.NORTH_UP;
                break;
            default:
                orientation = JigsawOrientation.EAST_UP;
        }
        return newBlock.getDefaultState().with(BlockStateProperties.field_235907_P_, orientation).with(NetworkNodeBlock.CONNECTED, state.get(NetworkNodeBlock.CONNECTED));
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
        //Get FilterItems
        for (int i = 0; i < grid.getFilter().getSlots(); i++) {
            filter.put(i, grid.getFilter().getStackInSlot(i).copy());
            grid.getFilter().setStackInSlot(i, ItemStack.EMPTY);
        }

        //Get Crafting Grid Items
        if (grid.getGridType() == GridType.CRAFTING) {
            for (int i = 0; i < grid.getCraftingMatrix().getSizeInventory(); i++) {
                matrix.put(i, grid.getCraftingMatrix().getStackInSlot(i));
                grid.getCraftingMatrix().setInventorySlotContents(i, ItemStack.EMPTY);
            }
        }
        //Get Pattern Grid Items
        if (grid.getGridType() == GridType.PATTERN) {
            for (int i = 0; i < grid.getProcessingMatrix().getSlots(); i++) {
                processingMatrix.put(i, grid.getProcessingMatrix().getStackInSlot(i).copy());
            }
            for (int i = 0; i < grid.getProcessingMatrixFluids().getSlots(); i++) {
                processingMatrixFluids.put(i, grid.getProcessingMatrixFluids().getFluid(i).copy());
            }
        }
    }


    private BlockState getStateForGridType(BlockState state, GridType type) {
        switch (type) {
            case NORMAL:
                return getBlockStateWithJigOri(state, BlocksAndItems.COLORED_GRID);
            case CRAFTING:
                return getBlockStateWithJigOri(state, BlocksAndItems.COLORED_CRAFTING_GRID);
            case PATTERN:
                return getBlockStateWithJigOri(state, BlocksAndItems.COLORED_PATTERN_GRID);
            case FLUID:
                return getBlockStateWithJigOri(state, BlocksAndItems.COLORED_FLUID_GRID);
        }
        return null;
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
