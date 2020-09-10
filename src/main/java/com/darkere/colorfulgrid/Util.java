package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.tile.NetworkNodeTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Util {
    public static JigsawOrientation cycleOrientation(JigsawOrientation ori) {
        int orig = ori.ordinal();
        if (JigsawOrientation.values().length == orig + 1) {
            return JigsawOrientation.values()[0];
        }
        return JigsawOrientation.values()[orig + 1];
    }

    public static void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof NetworkNodeTile) {
                IItemHandler handler = ((NetworkNodeTile) tile).getNode().getDrops();
                if (handler != null) {
                    NonNullList<ItemStack> drops = NonNullList.create();

                    for (int i = 0; i < handler.getSlots(); ++i) {
                        drops.add(handler.getStackInSlot(i));
                    }

                    InventoryHelper.dropItems(worldIn, pos, drops);
                }
            }
        }
        if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
            worldIn.removeTileEntity(pos);
        }
    }

    public static BlockState getStateForPlacement(BlockItemUseContext context, Block block) {
        Direction dir1;
        Direction dir2;
        Direction[] direction = context.getNearestLookingDirections();
        if (context.getPlayer().getPitchYaw().x < -45) {
            dir1 = Direction.DOWN;
            dir2 = getHorizontalLookDirection(direction, true, false);
        } else if (context.getPlayer().getPitchYaw().x > 45) {
                dir1 = Direction.UP;
                dir2 = getHorizontalLookDirection(direction, false, true);
        } else {
            dir1 = context.getNearestLookingDirection();
            if (dir1.getAxis() == Direction.Axis.Z) dir1 = dir1.getOpposite();
            if (dir1.getAxis() == Direction.Axis.Y) {
                dir1 = dir1.getOpposite();
                dir2 = getHorizontalLookDirection(direction, false, false);
            } else {
                dir2 = Direction.UP;
            }
        }

        return block.getDefaultState().with(BlockStateProperties.field_235907_P_, JigsawOrientation.func_239641_a_(dir1, dir2));
    }

    private static Direction getHorizontalLookDirection(Direction[] direction, boolean opposite, boolean revertZ) {
        for (int i = 0; i < direction.length; i++) {
            if (direction[i].getAxis() != Direction.Axis.Y) {
                if (revertZ && direction[i].getAxis() == Direction.Axis.Z) direction[i] = direction[i].getOpposite();
                return opposite ? direction[i].getOpposite() : direction[i];
            }

        }
        return Direction.NORTH;
    }

    public static List<ItemStack> getDrops(BlockState state, String name) {
        return Collections.singletonList(BlocksAndItems.blockItems.get(name).get(state.get(ColorfulGrid.COLOR)).getDefaultInstance());
    }
}
