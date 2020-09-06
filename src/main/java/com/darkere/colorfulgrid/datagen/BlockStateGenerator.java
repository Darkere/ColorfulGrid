package com.darkere.colorfulgrid.datagen;

import com.darkere.colorfulgrid.BlocksAndItems;
import com.darkere.colorfulgrid.ColorfulGrid;
import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;
import net.minecraftforge.client.model.generators.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BlockStateGenerator extends BlockStateProvider {

    public BlockStateGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, new ExistingFileHelper(new ArrayList<>(), false));
    }

    @Override
    protected void registerStatesAndModels() {
        generateGridModels();
        generateCraftingMonitorModels();
        generateCrafterManagerModels();
    }

    private void generateCrafterManagerModels() {
        Map<DyeColor, BlockModelBuilder> map = new HashMap<>();
        ResourceLocation particle = new ResourceLocation("refinedstorage:block/crafter_manager/front");
        ResourceLocation north = new ResourceLocation("refinedstorage:block/crafter_manager/front");
        ResourceLocation east = new ResourceLocation("refinedstorage:block/crafter_manager/left");
        ResourceLocation south = new ResourceLocation("refinedstorage:block/crafter_manager/back");
        ResourceLocation west = new ResourceLocation("refinedstorage:block/crafter_manager/right");
        ResourceLocation up = new ResourceLocation("refinedstorage:block/crafter_manager/top");
        ResourceLocation down = new ResourceLocation("refinedstorage:block/bottom");
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation cutout = new ResourceLocation(ColorfulGrid.MODID, "cutout/craftermanager/" + color);
            BlockModelBuilder model = createCutoutModel(color.func_176610_l() + "crafter_manager", down, up, north, south, east, west, particle, cutout);
            map.put(color, model);
        }
        BlockModelBuilder dcmodel = createCutoutModel("dc_craftermanager", down, up, north, south, east, west, particle, new ResourceLocation(RS.ID, "block/crafter_manager/cutouts/front_disconnected"));
        customDirectionalBlock(BlocksAndItems.COLORED_CRAFTER_MANAGER.get(), state -> {
            if (state.get(NetworkNodeBlock.CONNECTED)) {
                return map.get(state.get(ColorfulGrid.COLOR));
            } else {
                return dcmodel;
            }
        });
    }

    private void generateCraftingMonitorModels() {
        Map<DyeColor, BlockModelBuilder> map = new HashMap<>();
        ResourceLocation particle = new ResourceLocation("refinedstorage:block/crafting_monitor/front");
        ResourceLocation north = new ResourceLocation("refinedstorage:block/crafting_monitor/front");
        ResourceLocation east = new ResourceLocation("refinedstorage:block/crafting_monitor/left");
        ResourceLocation south = new ResourceLocation("refinedstorage:block/crafting_monitor/back");
        ResourceLocation west = new ResourceLocation("refinedstorage:block/crafting_monitor/right");
        ResourceLocation up = new ResourceLocation("refinedstorage:block/crafting_monitor/top");
        ResourceLocation down = new ResourceLocation("refinedstorage:block/bottom");
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation cutout = new ResourceLocation(ColorfulGrid.MODID, "cutout/craftingmonitor/" + color);
            BlockModelBuilder model = createCutoutModel(color.func_176610_l() + "_craftingmonitor", down, up, north, south, east, west, particle, cutout);
            map.put(color, model);
        }
        BlockModelBuilder dcmodel = createCutoutModel("dc_craftingmonitor", down, up, north, south, east, west, particle, new ResourceLocation(RS.ID, "block/crafting_monitor/cutouts/front_disconnected"));
        customDirectionalBlock(BlocksAndItems.COLORED_CRAFTING_MONITOR.get(), state -> {
            if (state.get(NetworkNodeBlock.CONNECTED)) {
                return map.get(state.get(ColorfulGrid.COLOR));
            } else {
                return dcmodel;
            }
        });

    }

    private void generateGridModels() {
        Map<GridType, Map<DyeColor, BlockModelBuilder>> texturemap = new HashMap<>();
        Map<GridType, BlockModelBuilder> dcModels = new HashMap<>();
        for (GridType type : GridType.values()) {
            ResourceLocation gridleft = new ResourceLocation(RS.ID, "block/grid/" + (type == GridType.FLUID ? "fluid_" : "") + "left");
            ResourceLocation gridright = new ResourceLocation(RS.ID, "block/grid/" + (type == GridType.FLUID ? "fluid_" : "") + "right");
            ResourceLocation gridfront = new ResourceLocation(RS.ID, "block/grid/" + (type == GridType.FLUID ? "fluid_" : "") + "front");
            ResourceLocation gridtop = new ResourceLocation(RS.ID, "block/grid/" + (type == GridType.FLUID ? "fluid_" : "") + "top");
            ResourceLocation gridback = new ResourceLocation(RS.ID, "block/grid/" + (type == GridType.FLUID ? "fluid_side" : "back"));
            ResourceLocation bottom = new ResourceLocation(RS.ID, "block/bottom");
            ResourceLocation dc = new ResourceLocation(RS.ID, "block/grid/cutouts/" + (type == GridType.NORMAL ? "" : type.func_176610_l() + "_") + "front_disconnected");
            Map<DyeColor, BlockModelBuilder> modelMap = new HashMap<>();
            dcModels.put(type, createCutoutModel(type.func_176610_l() + "_disconnected", bottom, gridtop, gridfront, gridback, gridleft, gridright, gridleft, dc));
            for (DyeColor color : DyeColor.values()) {

                ResourceLocation gridColor = new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type.func_176610_l() + "/" + color.func_176610_l());
                BlockModelBuilder model = createCutoutModel(type.func_176610_l() + "_" + color, bottom, gridtop, gridfront, gridback, gridleft, gridright, gridleft, gridColor);
                modelMap.put(color, model);
            }
            texturemap.put(type, modelMap);
        }
        customDirectionalBlock(BlocksAndItems.COLORED_GRID, state -> {
            if (state.get(NetworkNodeBlock.CONNECTED)) {
                return texturemap.get(GridType.NORMAL).get(state.get(ColorfulGrid.COLOR));
            } else {
                return dcModels.get(GridType.NORMAL);
            }

        });
        customDirectionalBlock(BlocksAndItems.COLORED_PATTERN_GRID, state -> {
            if (state.get(NetworkNodeBlock.CONNECTED)) {
                return texturemap.get(GridType.PATTERN).get(state.get(ColorfulGrid.COLOR));
            } else {
                return dcModels.get(GridType.PATTERN);
            }
        });
        customDirectionalBlock(BlocksAndItems.COLORED_FLUID_GRID, state -> {
            if (state.get(NetworkNodeBlock.CONNECTED)) {
                return texturemap.get(GridType.FLUID).get(state.get(ColorfulGrid.COLOR));
            } else {
                return dcModels.get(GridType.FLUID);
            }
        });
        customDirectionalBlock(BlocksAndItems.COLORED_CRAFTING_GRID, state -> {
            if (state.get(NetworkNodeBlock.CONNECTED)) {
                return texturemap.get(GridType.CRAFTING).get(state.get(ColorfulGrid.COLOR));
            } else {
                return dcModels.get(GridType.CRAFTING);
            }
        });
    }

    private BlockModelBuilder createCutoutModel(String name, ResourceLocation down, ResourceLocation
        up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation
                                                    west, ResourceLocation particle, ResourceLocation cutout) {
        return models().withExistingParent(name, new ResourceLocation(RS.ID, "cube_north_cutout"))
            .texture("particle", particle)
            .texture("east", east)
            .texture("south", south)
            .texture("west", west)
            .texture("up", up)
            .texture("down", down)
            .texture("north", north)
            .texture("cutout", cutout);
    }

    public void customDirectionalBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
            .forAllStates(state -> {
                JigsawOrientation ori = state.get(BlockStateProperties.field_235907_P_);
                Direction dir1 = ori.func_239642_b_();
                Direction dir2 = ori.func_239644_c_();
                return ConfiguredModel.builder()
                    .modelFile(modelFunc.apply(state))
                    .rotationX(dir1.getAxis() == Direction.Axis.Y ? dir1.getAxisDirection().getOffset() * -90 : 0)
                    .rotationY(getXRotation(dir1, dir2))
                    .build();
            });
    }

    private int getXRotation(Direction dir1, Direction dir2) {
        if (dir1 == Direction.DOWN) {
            if (dir2 == Direction.EAST) return 90;
            if (dir2 == Direction.WEST) return -90;
            if (dir2 == Direction.NORTH) return 0;
            if (dir2 == Direction.SOUTH) return 180;
        } else if(dir1 == Direction.UP){
            if (dir2 == Direction.EAST) return -90;
            if (dir2 == Direction.WEST) return 90;
            if (dir2 == Direction.NORTH) return 0;
            if (dir2 == Direction.SOUTH) return 180;
        } else {
            if (dir1 == Direction.EAST) return -90;
            if (dir1 == Direction.WEST) return 90;
            if (dir1 == Direction.NORTH) return 0;
            if (dir1== Direction.SOUTH) return 180;
        }
        return 0;
    }
}
//    DOWN_EAST("down_east", Direction.DOWN, Direction.EAST), +90
//    DOWN_NORTH("down_north", Direction.DOWN, Direction.NORTH), 0
//    DOWN_SOUTH("down_south", Direction.DOWN, Direction.SOUTH),+180
//    DOWN_WEST("down_west", Direction.DOWN, Direction.WEST),-90
//    UP_EAST("up_east", Direction.UP, Direction.EAST)
//    UP_NORTH("up_north", Direction.UP, Direction.NORTH),
//    UP_SOUTH("up_south", Direction.UP, Direction.SOUTH),
//    UP_WEST("up_west", Direction.UP, Direction.WEST),
//    WEST_UP("west_up", Direction.WEST, Direction.UP),
//    EAST_UP("east_up", Direction.EAST, Direction.UP),
//    NORTH_UP("north_up", Direction.NORTH, Direction.UP),
//    SOUTH_UP("south_up", Direction.SOUTH, Direction.UP);

