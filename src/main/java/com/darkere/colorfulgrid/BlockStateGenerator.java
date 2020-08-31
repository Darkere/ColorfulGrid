package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.block.BlockDirection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BlockStateGenerator extends BlockStateProvider {

    public BlockStateGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, new ExistingFileHelper(new ArrayList<>(),false));
    }

    @Override
    protected void registerStatesAndModels() {
        Map<GridType, Map<DyeColor, BlockModelBuilder>> texturemap = new HashMap<>();
        for (GridType type : GridType.values()) {
            if(type != GridType.CRAFTING)continue;
            ResourceLocation gridleft = new ResourceLocation(RS.ID, "block/grid/" + (type == GridType.FLUID ? "fluid_" : "") + "left");
            ResourceLocation gridright = new ResourceLocation(RS.ID, "block/grid/" + (type == GridType.FLUID ? "fluid_" : "") + "right");
            ResourceLocation gridfront = new ResourceLocation(RS.ID, "block/grid/" + (type == GridType.FLUID ? "fluid_" : "") + "front");
            ResourceLocation gridtop = new ResourceLocation(RS.ID, "block/grid/" + (type == GridType.FLUID ? "fluid_" : "") + "top");
            ResourceLocation gridback = new ResourceLocation(RS.ID, "block/grid/" + (type == GridType.FLUID ? "fluid_side" : "back"));
            ResourceLocation bottom = new ResourceLocation(RS.ID, "block/bottom");
            Map<DyeColor, BlockModelBuilder> modelMap = new HashMap<>();
            for (DyeColor color : DyeColor.values()) {

                ResourceLocation gridColor = new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type.func_176610_l() + "/" + color.func_176610_l());
                BlockModelBuilder model = createCutoutModel(type.func_176610_l() + "_" + color, bottom, gridtop, gridfront, gridback, gridleft, gridright, gridleft, gridColor);
                modelMap.put(color, model);
            }
            texturemap.put(type, modelMap);
        }
       // directionalBlock(ColorfulGrid.COLOREDGRID.get(), state -> texturemap.get(GridType.NORMAL).get(state.get(ColoredGridBlock.COLOR)));
      //  directionalBlock(ColorfulGrid.COLOREDPATTERNGRID.get(), state -> texturemap.get(GridType.PATTERN).get(state.get(ColoredGridBlock.COLOR)));
       // directionalBlock(ColorfulGrid.COLOREDFLUIDGRID.get(), state -> texturemap.get(GridType.FLUID).get(state.get(ColoredGridBlock.COLOR)));
        directionalBlock(ColorfulGrid.COLOREDCRAFTINGGRID.get(), state -> texturemap.get(GridType.CRAFTING).get(state.get(ColoredGridBlock.COLOR)));
    }

    private BlockModelBuilder createCutoutModel(String name, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west, ResourceLocation particle, ResourceLocation cutout) {
        return models().withExistingParent(name, new ResourceLocation(ColorfulGrid.MODID,"cutoutmodel"))
            .texture("particle", particle)
            .texture("east", east)
            .texture("south", south)
            .texture("west", west)
            .texture("up", up)
            .texture("down", down)
            .texture("north", north)
            .texture("cutout", cutout);
    }
    public void directionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
        getVariantBuilder(block)
            .forAllStates(state -> {
                Direction dir = state.get(BlockDirection.ANY.getProperty());
                return ConfiguredModel.builder()
                    .modelFile(modelFunc.apply(state))
                    .rotationX(dir.getAxis() == Direction.Axis.Y ?  dir.getAxisDirection().getOffset() * -90 : 0)
                    .rotationY(dir.getAxis() != Direction.Axis.Y ? ((dir.getHorizontalIndex() + 2) % 4) * 90 : 0)
                    .build();
            });
    }

}

