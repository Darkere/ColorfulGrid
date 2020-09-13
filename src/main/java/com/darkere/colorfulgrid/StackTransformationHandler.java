package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.inventory.fluid.FluidInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StackTransformationHandler<T> {
    Class<T> clazz;
    List<Runnable> runnable = new ArrayList<>();
    ServerWorld world;
    BlockPos pos;


    public StackTransformationHandler(Class<T> clazz, ServerWorld world, BlockPos pos) {
        this.clazz = clazz;
        this.world = world;
        this.pos = pos;
    }

    public void handleItems(Function<T, IItemHandlerModifiable> handlerProvider) {
        Map<Integer, ItemStack> stacks = getFromHandler(handlerProvider.apply(getNode()));
        runnable.add(() -> refillSlots(handlerProvider.apply(getNode()), stacks));
    }

    public void handleCraftingItems(Function<T, CraftingInventory> handlerProvider) {
        Map<Integer, ItemStack> stacks = getFromHandler(handlerProvider.apply(getNode()));
        runnable.add(() -> refillSlots(handlerProvider.apply(getNode()), stacks));
    }


    public void handleFluids(Function<T, FluidInventory> handlerProvider) {
        Map<Integer, FluidStack> stacks = getFromHandler(handlerProvider.apply(getNode()));
        runnable.add(() -> refillSlots(handlerProvider.apply(getNode()), stacks));
    }

    public T get() {
        return getNode();
    }

    public List<Runnable> getRunnable() {
        return runnable;
    }

    private Map<Integer, ItemStack> getFromHandler(IItemHandlerModifiable handler) {
        Map<Integer, ItemStack> stacks = new HashMap<>();
        for (int i = 0; i < handler.getSlots(); i++) {
            stacks.put(i, handler.getStackInSlot(i).copy());
            handler.setStackInSlot(i, ItemStack.EMPTY);
        }
        return stacks;
    }

    private Map<Integer, ItemStack> getFromHandler(CraftingInventory handler) {
        Map<Integer, ItemStack> stacks = new HashMap<>();
        for (int i = 0; i < handler.getSizeInventory(); i++) {
            stacks.put(i, handler.getStackInSlot(i).copy());
            handler.setInventorySlotContents(i, ItemStack.EMPTY);
        }
        return stacks;
    }

    private Map<Integer, FluidStack> getFromHandler(FluidInventory handler) {
        Map<Integer, FluidStack> stacks = new HashMap<>();
        for (int i = 0; i < handler.getSlots(); i++) {
            stacks.put(i, handler.getFluid(i).copy());
            handler.setFluid(i, FluidStack.EMPTY);
        }
        return stacks;
    }

    private T getNode() {
        INetworkNode node = API.instance().getNetworkNodeManager(world).getNode(pos);
        if (node == null) return null;
        return clazz.cast(node);
    }

    private void refillSlots(FluidInventory empty, Map<Integer, FluidStack> filled) {
        for (int i = 0; i < empty.getSlots(); i++) {
            if (filled.containsKey(i)) {
                empty.setFluid(i, filled.get(i));
            }
        }
    }

    private void refillSlots(IItemHandlerModifiable empty, Map<Integer, ItemStack> filled) {
        for (int i = 0; i < empty.getSlots(); i++) {
            if (filled.containsKey(i)) {
                empty.setStackInSlot(i, filled.get(i));
            }
        }
    }

    private void refillSlots(CraftingInventory empty, Map<Integer, ItemStack> filled) {
        for (int i = 0; i < empty.getSizeInventory(); i++) {
            if (filled.containsKey(i)) {
                empty.setInventorySlotContents(i, filled.get(i));
            }
        }
    }
}
