package com.darkere.colorfulgrid.Blocks;

import com.darkere.colorfulgrid.ColorfulGrid;
import com.darkere.colorfulgrid.Util;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.GridBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ColoredGridBlock extends GridBlock {
    GridType type;

    public ColoredGridBlock(GridType type, ResourceLocation registryname) {
        super(type, registryname);
        this.type = type;
        setDefaultState(this.getDefaultState().with(ColorfulGrid.COLOR, DyeColor.LIGHT_BLUE));
    }

    @Override
    public BlockDirection getDirection() {
        return BlockDirection.NONE;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(BlockStateProperties.ORIENTATION, Util.cycleOrientation(state.get(BlockStateProperties.ORIENTATION)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return Util.getStateForPlacement(context, this);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ColorfulGrid.COLOR);
        builder.add(CONNECTED);
        builder.add(BlockStateProperties.ORIENTATION);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        Util.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Util.getDrops(state, type.getString());
    }
}
