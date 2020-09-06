package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import com.refinedmods.refinedstorage.container.factory.CrafterManagerContainerProvider;
import com.refinedmods.refinedstorage.tile.CrafterManagerTile;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;
import net.minecraftforge.fml.network.NetworkHooks;

public class ColoredCrafterManagerBlock extends NetworkNodeBlock {

    public ColoredCrafterManagerBlock(Properties props) {
        super(props);
        setDefaultState(this.getDefaultState().with(ColorfulGrid.COLOR, DyeColor.LIGHT_BLUE));
    }


    @Override
    public BlockDirection getDirection() {
        return BlockDirection.ANY;
    }

    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CrafterManagerTile();
    }

    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return !world.isRemote ? NetworkUtils.attempt(world, pos, hit.getFace(), player, () -> {
            NetworkHooks.openGui((ServerPlayerEntity)player, new CrafterManagerContainerProvider((CrafterManagerTile)world.getTileEntity(pos)), (buf) -> {
                CrafterManagerContainerProvider.writeToBuffer(buf, world, pos);
            });
        }, new Permission[]{Permission.MODIFY, Permission.AUTOCRAFTING}) : ActionResultType.SUCCESS;
    }

    public boolean hasConnectedState() {
        return true;
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