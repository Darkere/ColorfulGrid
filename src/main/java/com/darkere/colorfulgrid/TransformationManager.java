package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.api.network.NetworkType;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.apiimpl.network.node.CrafterNetworkNode;
import com.refinedmods.refinedstorage.apiimpl.network.node.GridNetworkNode;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkTransmitterNetworkNode;
import com.refinedmods.refinedstorage.apiimpl.network.node.SecurityManagerNetworkNode;
import com.refinedmods.refinedstorage.apiimpl.network.node.diskmanipulator.DiskManipulatorNetworkNode;
import com.refinedmods.refinedstorage.block.*;
import com.refinedmods.refinedstorage.screen.DiskManipulatorScreen;
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
import net.minecraftforge.items.IItemHandlerModifiable;

import java.lang.reflect.Field;
import java.util.*;

public class TransformationManager {

    StateTransformationHandler stateHandler = new StateTransformationHandler();

    public TransformationManager(){
        stateHandler.initializeTransformer();
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) return;
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof DyeItem)) return;
        ServerWorld world = (ServerWorld) event.getWorld();
        BlockPos pos = event.getPos();
        DyeColor color = DyeColor.getColor(stack);
        if (color == null) return;
        StateTransformationHandler.Transform transform = new StateTransformationHandler.Transform(world.getBlockState(pos),pos,world);
        StateTransformationHandler.TransformResult result = stateHandler.transform(transform);
        world.setBlockState(pos, result.state.with(ColorfulGrid.COLOR, color));
        ForgeEventFactory.onBlockPlace(event.getEntity(), BlockSnapshot.create(world.func_234923_W_(), world, pos), event.getFace());
        result.toRun.forEach(Runnable::run);
        event.setCanceled(true);
    }

}
