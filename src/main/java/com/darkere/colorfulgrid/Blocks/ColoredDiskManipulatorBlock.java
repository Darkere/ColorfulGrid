package com.darkere.colorfulgrid.Blocks;

import com.darkere.colorfulgrid.ColorfulGrid;
import com.darkere.colorfulgrid.Util;
import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import com.refinedmods.refinedstorage.container.DiskManipulatorContainer;
import com.refinedmods.refinedstorage.container.factory.PositionalTileContainerProvider;
import com.refinedmods.refinedstorage.tile.DiskManipulatorTile;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class ColoredDiskManipulatorBlock extends NetworkNodeBlock {
    public ColoredDiskManipulatorBlock(Properties props) {
        super(props);
    }

    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DiskManipulatorTile();
    }

    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        return !world.isRemote ? NetworkUtils.attemptModify(world, pos, rayTraceResult.getFace(), player, () -> {
            NetworkHooks.openGui((ServerPlayerEntity)player, new PositionalTileContainerProvider(new TranslationTextComponent("gui.refinedstorage.disk_manipulator"), (tile, windowId, inventory, p) -> {
                return new DiskManipulatorContainer((DiskManipulatorTile) tile, p, windowId);
            }, pos), pos);
        }) : ActionResultType.SUCCESS;
    }

    public BlockDirection getDirection() {
        return BlockDirection.HORIZONTAL;
    }

    public boolean hasConnectedState() {
        return true;
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(ColorfulGrid.COLOR);
    }
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Util.getDrops(state,"diskmanipulator");
    }
}
