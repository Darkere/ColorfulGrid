package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.GridBlock;
import com.refinedmods.refinedstorage.tile.NetworkNodeTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;
import net.minecraftforge.items.IItemHandler;

public class ColoredGridBlock extends GridBlock {

    public ColoredGridBlock(GridType type, ResourceLocation registryname) {
        super(type,registryname);
        setDefaultState(this.getDefaultState().with(ColorfulGrid.COLOR,DyeColor.LIGHT_BLUE));
    }

    @Override
    public BlockDirection getDirection() {
        return BlockDirection.ANY;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(BlockStateProperties.field_235907_P_, Util.cycleOrientation(state.get(BlockStateProperties.field_235907_P_)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
       return Util.getStateForPlacement(context,this);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ColorfulGrid.COLOR);
        builder.add(CONNECTED);
        builder.add(BlockStateProperties.field_235907_P_);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        Util.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}
