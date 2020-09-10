package com.darkere.colorfulgrid.Blocks;

import com.darkere.colorfulgrid.ColorfulGrid;
import com.darkere.colorfulgrid.Util;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.apiimpl.network.node.SecurityManagerNetworkNode;
import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import com.refinedmods.refinedstorage.container.SecurityManagerContainer;
import com.refinedmods.refinedstorage.container.factory.PositionalTileContainerProvider;
import com.refinedmods.refinedstorage.tile.SecurityManagerTile;
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

public class ColoredSecurityManagerBlock extends NetworkNodeBlock {
    public ColoredSecurityManagerBlock(Properties props) {
        super(props);
    }
    public BlockDirection getDirection() {
        return BlockDirection.HORIZONTAL;
    }

    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!world.isRemote) {
            Runnable action = () -> {
                NetworkHooks.openGui((ServerPlayerEntity)player, new PositionalTileContainerProvider(new TranslationTextComponent("gui.refinedstorage.security_manager"), (tile, windowId, inventory, p) -> {
                    return new SecurityManagerContainer((SecurityManagerTile) tile, player, windowId);
                }, pos), pos);
            };
            if (!player.getGameProfile().getId().equals(((SecurityManagerNetworkNode)((SecurityManagerTile)world.getTileEntity(pos)).getNode()).getOwner())) {
                return NetworkUtils.attempt(world, pos, hit.getFace(), player, action, new Permission[]{Permission.MODIFY, Permission.SECURITY});
            }

            action.run();
        }

        return ActionResultType.SUCCESS;
    }

    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SecurityManagerTile();
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
        return Util.getDrops(state,"security");
    }
}
