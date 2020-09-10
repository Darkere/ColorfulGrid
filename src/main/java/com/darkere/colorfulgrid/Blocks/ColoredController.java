package com.darkere.colorfulgrid.Blocks;

import com.darkere.colorfulgrid.ColorfulGrid;
import com.darkere.colorfulgrid.Util;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.NetworkType;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.Network;
import com.refinedmods.refinedstorage.block.BaseBlock;
import com.refinedmods.refinedstorage.block.ControllerBlock;
import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import com.refinedmods.refinedstorage.container.ControllerContainer;
import com.refinedmods.refinedstorage.tile.ControllerTile;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.naming.ldap.Control;
import java.util.List;

public class ColoredController extends BaseBlock {
    private final NetworkType type;


    public ColoredController(Properties props, NetworkType type) {
        super(props);
        this.type = type;
        this.setDefaultState((this.getStateContainer().getBaseState()).with(ControllerBlock.ENERGY_TYPE, ControllerBlock.EnergyType.OFF));

    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(new Property[]{ControllerBlock.ENERGY_TYPE});
        builder.add(ColorfulGrid.COLOR);
    }

    public NetworkType getType() {
        return this.type;
    }

    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ControllerTile(this.type);
    }

    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, entity, stack);
        if (!world.isRemote) {
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((energyFromStack) -> {
                TileEntity tile = world.getTileEntity(pos);
                if (tile != null) {
                    tile.getCapability(CapabilityEnergy.ENERGY).ifPresent((energyFromTile) -> {
                        energyFromTile.receiveEnergy(energyFromStack.getEnergyStored(), false);
                    });
                }

            });
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        if(getType() == NetworkType.CREATIVE){
            return Util.getDrops(state,"controller_creative");
        } else {
            return Util.getDrops(state,"controller");
        }

    }

    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
        if (!world.isRemote) {
            INetwork network = API.instance().getNetworkManager((ServerWorld)world).getNetwork(pos);
            if (network instanceof Network) {
                ((Network)network).setRedstonePowered(world.isBlockPowered(pos));
            }
        }

    }

    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        return !world.isRemote ? NetworkUtils.attemptModify(world, pos, hit.getFace(), player, () -> {
            NetworkHooks.openGui((ServerPlayerEntity)player, new INamedContainerProvider() {
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("gui.refinedstorage." + (getType() == NetworkType.CREATIVE ? "creative_" : "") + "controller");
                }

                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player) {
                    return new ControllerContainer((ControllerTile)world.getTileEntity(pos), player, i);
                }
            }, pos);
        }) : ActionResultType.SUCCESS;
    }
}
