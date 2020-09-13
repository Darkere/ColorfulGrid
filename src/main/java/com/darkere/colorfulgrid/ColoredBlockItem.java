package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.block.BaseBlock;
import com.refinedmods.refinedstorage.item.blockitem.BaseBlockItem;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;

public class ColoredBlockItem extends BaseBlockItem {
    DyeColor color;

    public ColoredBlockItem(BaseBlock block, Properties builder, DyeColor color) {
        super(block, builder);
        this.color = color;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        IFormattableTextComponent name = (IFormattableTextComponent) super.getDisplayName(stack);
        Style style = name.getStyle().applyFormatting(Util.getTextColor(color));
        name.setStyle(style);
        return name;
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        return super.placeBlock(context, state.with(ColorfulGrid.COLOR, color));
    }
}
