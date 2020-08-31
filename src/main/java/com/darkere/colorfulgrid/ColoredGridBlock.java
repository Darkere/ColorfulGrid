package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.GridBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColoredGridBlock extends GridBlock {
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public ColoredGridBlock(GridType type) {
        super(type);
    }

    @Override
    public BlockDirection getDirection() {
        return BlockDirection.ANY;
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(COLOR);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
