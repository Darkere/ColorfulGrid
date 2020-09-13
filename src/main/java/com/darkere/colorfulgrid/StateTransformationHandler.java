package com.darkere.colorfulgrid;

import com.darkere.colorfulgrid.Blocks.*;
import com.refinedmods.refinedstorage.api.network.NetworkType;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.apiimpl.network.node.CrafterNetworkNode;
import com.refinedmods.refinedstorage.apiimpl.network.node.GridNetworkNode;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkTransmitterNetworkNode;
import com.refinedmods.refinedstorage.apiimpl.network.node.SecurityManagerNetworkNode;
import com.refinedmods.refinedstorage.apiimpl.network.node.diskmanipulator.DiskManipulatorNetworkNode;
import com.refinedmods.refinedstorage.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class StateTransformationHandler {
    Map<Class<? extends Block>, Function<Transform, TransformResult>> map = new HashMap<>();

    public void initializeTransformer() {
        map.put(ColoredGridBlock.class, this::getNonTransFormation);
        map.put(ColoredController.class, this::getNonTransFormation);
        map.put(ColoredCrafter.class, this::getNonTransFormation);
        map.put(ColoredCrafterManagerBlock.class, this::getNonTransFormation);
        map.put(ColoredCraftingMonitorBlock.class, this::getNonTransFormation);
        map.put(ColoredDiskManipulatorBlock.class, this::getNonTransFormation);
        map.put(ColoredReceiverBlock.class, this::getNonTransFormation);
        map.put(ColoredRelayBlock.class, this::getNonTransFormation);
        map.put(ColoredSecurityManagerBlock.class, this::getNonTransFormation);
        map.put(ColoredTransmitterBlock.class, this::getNonTransFormation);
        map.put(GridBlock.class, t -> new TransformResult(getStateForGridType(t.state, Objects.requireNonNull(getGridTypeViaReflection(t.state.getBlock()))), handleGridItems(t.world, t.pos)));
        map.put(CraftingMonitorBlock.class, t -> getNoRunTransform(getBlockStateWithJigOri(t.state, BlocksAndItems.COLORED_CRAFTING_MONITOR.get())));
        map.put(CrafterManagerBlock.class, t -> getNoRunTransform(getBlockStateWithJigOri(t.state, BlocksAndItems.COLORED_CRAFTER_MANAGER.get())));
        map.put(RelayBlock.class, t -> getNoRunTransform(BlocksAndItems.COLORED_RELAY.get().getDefaultState().with(NetworkNodeBlock.CONNECTED, t.state.get(NetworkNodeBlock.CONNECTED))));
        map.put(ControllerBlock.class, t -> {
            if (((ControllerBlock) t.state.getBlock()).getType() == NetworkType.CREATIVE) {
                return getNoRunTransform(BlocksAndItems.COLORED_CONTROLLER_CREATIVE.get().getDefaultState());
            } else {
                return getNoRunTransform(BlocksAndItems.COLORED_CONTROLLER.get().getDefaultState());
            }
        });
        map.put(SecurityManagerBlock.class, t -> new TransformResult(BlocksAndItems.COLORED_SECURITY.get().getDefaultState().with(NetworkNodeBlock.CONNECTED, t.state.get(NetworkNodeBlock.CONNECTED)).with(BlockDirection.HORIZONTAL.getProperty(), t.state.get(BlockDirection.HORIZONTAL.getProperty())), handleSecurityItems(t.world, t.pos)));
        map.put(CrafterBlock.class, t -> new TransformResult(BlocksAndItems.COLORED_CRAFTER.get().getDefaultState().with(NetworkNodeBlock.CONNECTED, t.state.get(NetworkNodeBlock.CONNECTED)).with(BlockDirection.ANY_FACE_PLAYER.getProperty(), t.state.get(BlockDirection.ANY_FACE_PLAYER.getProperty())),
            handleCrafterItems(t.world, t.pos)));
        map.put(DiskManipulatorBlock.class, t -> new TransformResult(
            BlocksAndItems.COLORED_DISKMANIPULATOR.get().getDefaultState().with(NetworkNodeBlock.CONNECTED, t.state.get(NetworkNodeBlock.CONNECTED)).with(BlockDirection.HORIZONTAL.getProperty(), t.state.get(BlockDirection.HORIZONTAL.getProperty())),
            handlerDiskManipItems(t.world, t.pos)
        ));
        map.put(NetworkTransmitterBlock.class, t -> new TransformResult(
            BlocksAndItems.COLORED_TRANSMITTER.get().getDefaultState().with(NetworkNodeBlock.CONNECTED, t.state.get(NetworkNodeBlock.CONNECTED)),
            handleTransmitterItems(t.world, t.pos)
        ));
        map.put(NetworkReceiverBlock.class, t -> getNoRunTransform(BlocksAndItems.COLORED_RECEIVER.get().getDefaultState().with(NetworkNodeBlock.CONNECTED, t.state.get(NetworkNodeBlock.CONNECTED))));
    }

    public TransformResult transform(Transform t){
        return map.get(t.state.getBlock().getClass()).apply(t);
    }

    private TransformResult getNonTransFormation(Transform transform) {
        return new TransformResult(transform.state, new ArrayList<>());
    }

    public TransformResult getNoRunTransform(BlockState state) {
        return new TransformResult(state, new ArrayList<>());
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
        return newBlock.getDefaultState().with(BlockStateProperties.ORIENTATION, orientation).with(NetworkNodeBlock.CONNECTED, state.get(NetworkNodeBlock.CONNECTED));
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

    private List<Runnable> handleTransmitterItems(ServerWorld world, BlockPos pos) {
        StackTransformationHandler<NetworkTransmitterNetworkNode> handler = new StackTransformationHandler<>(NetworkTransmitterNetworkNode.class, world, pos);
        handler.handleItems(NetworkTransmitterNetworkNode::getNetworkCard);
        return handler.getRunnable();
    }

    private List<Runnable> handlerDiskManipItems(ServerWorld world, BlockPos pos) {
        StackTransformationHandler<DiskManipulatorNetworkNode> handler = new StackTransformationHandler<>(DiskManipulatorNetworkNode.class, world, pos);
        handler.handleItems(node -> (IItemHandlerModifiable) node.getInputDisks());
        handler.handleItems(node -> (IItemHandlerModifiable) node.getOutputDisks());
        return handler.getRunnable();
    }

    private List<Runnable> handleCrafterItems(ServerWorld world, BlockPos pos) {
        StackTransformationHandler<CrafterNetworkNode> handler = new StackTransformationHandler<>(CrafterNetworkNode.class, world, pos);
        handler.handleItems(CrafterNetworkNode::getPatternInventory);
        return handler.getRunnable();
    }

    private List<Runnable> handleSecurityItems(ServerWorld world, BlockPos pos) {
        StackTransformationHandler<SecurityManagerNetworkNode> handler = new StackTransformationHandler<>(SecurityManagerNetworkNode.class, world, pos);
        handler.handleItems(SecurityManagerNetworkNode::getCardsItems);
        return handler.getRunnable();
    }

    private List<Runnable> handleGridItems(ServerWorld world, BlockPos pos) {
        StackTransformationHandler<GridNetworkNode> handler = new StackTransformationHandler<>(GridNetworkNode.class, world, pos);
        handler.handleItems(GridNetworkNode::getFilter);
        if (handler.get().getGridType() == GridType.CRAFTING) {
            handler.handleCraftingItems(GridNetworkNode::getCraftingMatrix);
        }
        if (handler.get().getGridType() == GridType.PATTERN) {
            handler.handleItems(GridNetworkNode::getProcessingMatrix);
            handler.handleFluids(GridNetworkNode::getProcessingMatrixFluids);
        }
        return handler.getRunnable();
    }

    public static class Transform {
        BlockState state;
        BlockPos pos;
        ServerWorld world;

        public Transform(BlockState state, BlockPos pos, ServerWorld world) {
            this.state = state;
            this.pos = pos;
            this.world = world;
        }
    }

    public static class TransformResult {
        BlockState state;
        List<Runnable> toRun;

        public TransformResult(BlockState state, List<Runnable> toRun) {
            this.state = state;
            this.toRun = toRun;
        }
    }
}
