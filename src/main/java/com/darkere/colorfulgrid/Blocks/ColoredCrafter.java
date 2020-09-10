package com.darkere.colorfulgrid.Blocks;

import com.darkere.colorfulgrid.ColorfulGrid;
import com.darkere.colorfulgrid.Util;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.apiimpl.network.node.CrafterNetworkNode;
import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import com.refinedmods.refinedstorage.container.CrafterContainer;
import com.refinedmods.refinedstorage.container.factory.PositionalTileContainerProvider;
import com.refinedmods.refinedstorage.tile.CrafterTile;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class ColoredCrafter extends NetworkNodeBlock {
    public ColoredCrafter(Properties props) {
        super(props);
    }

    public BlockDirection getDirection() {
        return BlockDirection.ANY_FACE_PLAYER;
    }

    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CrafterTile();
    }

    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof CrafterTile && stack.hasDisplayName()) {
                ((CrafterNetworkNode)((CrafterTile)tile).getNode()).setDisplayName(stack.getDisplayName());
                ((CrafterNetworkNode)((CrafterTile)tile).getNode()).markDirty();
            }
        }

    }

    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return !world.isRemote ? NetworkUtils.attempt(world, pos, hit.getFace(), player, () -> {
            NetworkHooks.openGui((ServerPlayerEntity)player, new PositionalTileContainerProvider(((CrafterNetworkNode)((CrafterTile)world.getTileEntity(pos)).getNode()).getName(), (tile, windowId, inventory, p) -> {
                return new CrafterContainer((CrafterTile) tile, player, windowId);
            }, pos), pos);
        }, new Permission[]{Permission.MODIFY, Permission.AUTOCRAFTING}) : ActionResultType.SUCCESS;
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
        return Util.getDrops(state,"crafter");
    }
}
