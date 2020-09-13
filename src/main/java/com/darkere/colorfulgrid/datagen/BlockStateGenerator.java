package com.darkere.colorfulgrid.datagen;

import com.darkere.colorfulgrid.BlocksAndItems;
import com.darkere.colorfulgrid.ColorfulGrid;
import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.ControllerBlock;
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
    private static final ResourceLocation BOTTOM = new ResourceLocation("refinedstorage:block/bottom");

    public BlockStateGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, new ExistingFileHelper(new ArrayList<>(), false));
    }

    @Override
    protected void registerStatesAndModels() {
        generateGridModels();
        generateCraftingMonitorModels();
        generateCrafterManagerModels();
        generateRelayModels();
        generateReceiverTransmitterModels();
        generateCrafterModels();
        generateControllerModels();
        generateSecurityModels();
        generateDiskmanipulatorModels();
    }

    private void generateDiskmanipulatorModels() {
        Map<DyeColor, BlockModelBuilder> map = new HashMap<>();
        ResourceLocation side = new ResourceLocation(RS.ID, "block/side");
        ResourceLocation front = new ResourceLocation(RS.ID,"block/disk_manipulator/disk_manipulator");
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation cutout = new ResourceLocation(ColorfulGrid.MODID,"cutout/diskmanipulator/"+ color);
            BlockModelBuilder model = createCutoutModel("colored_diskmanipulator_" + color,BOTTOM,side,front,side,side,side,front,cutout);
            map.put(color,model);
        }
        BlockModelBuilder dcModel = createCutoutModel("diskmanipulator_dc",BOTTOM,side,front,side,side,side,front,new ResourceLocation(RS.ID,"block/disk_manipulator/cutouts/disconnected"));
        simpleBlockStateModel(BlocksAndItems.COLORED_DISKMANIPULATOR.get(), state -> {
            if(!state.get(NetworkNodeBlock.CONNECTED)){
                return dcModel;
            } else {
                return map.get(state.get(ColorfulGrid.COLOR));
            }
        });
    }

    private void generateSecurityModels() {
        Map<DyeColor, BlockModelBuilder> map = new HashMap<>();
        ResourceLocation top = new ResourceLocation(RS.ID, "block/security_manager/top");
        ResourceLocation left = new ResourceLocation(RS.ID, "block/security_manager/left");
        ResourceLocation right = new ResourceLocation(RS.ID, "block/security_manager/right");
        ResourceLocation front = new ResourceLocation(RS.ID, "block/security_manager/front");
        ResourceLocation back = new ResourceLocation(RS.ID, "block/security_manager/back");
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation cutoutTop = new ResourceLocation(ColorfulGrid.MODID, "cutout/security/" + color + "_top");
            ResourceLocation cutoutLeft = new ResourceLocation(ColorfulGrid.MODID, "cutout/security/" + color + "_left");
            ResourceLocation cutoutRight = new ResourceLocation(ColorfulGrid.MODID, "cutout/security/" + color + "_right");
            ResourceLocation cutoutFront = new ResourceLocation(ColorfulGrid.MODID, "cutout/security/" + color + "_front");
            ResourceLocation cutoutBack = new ResourceLocation(ColorfulGrid.MODID, "cutout/security/" + color + "_back");
            BlockModelBuilder model = createCubeCutoutModelEachSideUnique("colored_security_" +  color, BOTTOM, BOTTOM, top, cutoutTop, left, cutoutLeft, right, cutoutRight, back, cutoutBack, front, cutoutFront);
            map.put(color, model);
        }
        ResourceLocation topdc = new ResourceLocation(RS.ID, "block/security_manager/cutouts/top_disconnected");
        ResourceLocation leftdc = new ResourceLocation(RS.ID, "block/security_manager/cutouts/left_disconnected");
        ResourceLocation rightdc = new ResourceLocation(RS.ID, "block/security_manager/cutouts/right_disconnected");
        ResourceLocation frontdc = new ResourceLocation(RS.ID, "block/security_manager/cutouts/front_disconnected");
        ResourceLocation backdc = new ResourceLocation(RS.ID, "block/security_manager/cutouts/back_disconnected");
        BlockModelBuilder dcModel = createCubeCutoutModelEachSideUnique("security_dc", BOTTOM, BOTTOM, top, topdc,left ,leftdc , right, rightdc, back, backdc, front, frontdc);
        simpleBlockStateModel(BlocksAndItems.COLORED_SECURITY.get(), state -> {
            if (!state.get(NetworkNodeBlock.CONNECTED)) {
                return dcModel;
            } else {
                return map.get(state.get(ColorfulGrid.COLOR));
            }
        });
    }

    private void generateControllerModels() {
        Map<DyeColor, BlockModelBuilder> map = new HashMap<>();
        ResourceLocation controlleron = new ResourceLocation(RS.ID, "block/controller/controller");
        ResourceLocation controlleroff = new ResourceLocation(RS.ID, "block/controller/controller_off");
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation cutout = new ResourceLocation(ColorfulGrid.MODID, "cutout/controller/" + color);
            BlockModelBuilder model = createCutOutModelAllSides("colored_controller_" + color, controlleron, controlleron, cutout);
            map.put(color, model);
        }
        BlockModelBuilder dcModel = createCutOutModelAllSides("controller_dc", controlleroff, controlleroff, new ResourceLocation(RS.ID, "block/controller/cutouts/off"));
        simpleBlockStateModel(BlocksAndItems.COLORED_CONTROLLER.get(), state -> {
            if (state.get(ControllerBlock.ENERGY_TYPE) == ControllerBlock.EnergyType.OFF) {
                return dcModel;
            } else {
                return map.get(state.get(ColorfulGrid.COLOR));
            }
        });
        simpleBlockStateModel(BlocksAndItems.COLORED_CONTROLLER_CREATIVE.get(), state -> map.get(state.get(ColorfulGrid.COLOR)));
    }

    private void simpleBlockStateModel(Block block, Function<BlockState, ModelFile> model) {
        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(model.apply(state)).build());
    }

    private void generateCrafterModels() {
        Map<DyeColor, BlockModelBuilder> map = new HashMap<>();
        ResourceLocation front = new ResourceLocation("refinedstorage:block/crafter/front");
        ResourceLocation side = new ResourceLocation("refinedstorage:block/crafter/side");
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation cutout = new ResourceLocation(ColorfulGrid.MODID, "cutout/crafter/" + color + "_top");
            ResourceLocation sideCutout = new ResourceLocation(ColorfulGrid.MODID, "cutout/crafter/" + color + "_side_0");
            BlockModelBuilder model = createCubeCutoutModelWithSideTopAndBottom("colored_crafter_" + color, BOTTOM, side, front, sideCutout, cutout);
            map.put(color, model);
        }
        BlockModelBuilder dcModel = createCubeCutoutModelWithSideTopAndBottom("crafter_dc", BOTTOM, side, front, new ResourceLocation(RS.ID, "block/crafter/cutouts/side_disconnected"), new ResourceLocation(RS.ID, "block/crafter/cutouts/front_disconnected"));
        blockDirectionDirectionalBlock(BlocksAndItems.COLORED_CRAFTER.get(), state -> {
            if (!state.get(NetworkNodeBlock.CONNECTED)) {
                return dcModel;
            } else {
                return map.get(state.get(ColorfulGrid.COLOR));
            }
        });

    }

    private void generateReceiverTransmitterModels() {
        Map<DyeColor, BlockModelBuilder> mapR = new HashMap<>();
        Map<DyeColor, BlockModelBuilder> mapT = new HashMap<>();
        ResourceLocation rec = new ResourceLocation(RS.ID, "block/network_receiver/network_receiver");
        ResourceLocation trans = new ResourceLocation(RS.ID, "block/network_transmitter/network_transmitter");
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation cutoutT = new ResourceLocation(ColorfulGrid.MODID, "cutout/transmitter/" + color);
            ResourceLocation cutoutR = new ResourceLocation(ColorfulGrid.MODID, "cutout/receiver/" + color);
            BlockModelBuilder model = createCutOutModelAllSides("colored_transmitter_" + color, trans, trans, cutoutT);
            mapT.put(color, model);
            BlockModelBuilder model2 = createCutOutModelAllSides("colored_receiver_" + color, rec, rec, cutoutR);
            mapR.put(color, model2);
        }
        BlockModelBuilder dcModelR = createCutOutModelAllSides("receiver_dc", rec, rec, new ResourceLocation(RS.ID, "block/network_receiver/cutouts/disconnected"));
        BlockModelBuilder dcModelT = createCutOutModelAllSides("transmitter_dc", trans, trans, new ResourceLocation(RS.ID, "block/network_transmitter/cutouts/disconnected"));

        simpleBlockStateModel(BlocksAndItems.COLORED_TRANSMITTER.get(), state -> {
            if (!state.get(NetworkNodeBlock.CONNECTED)) {
                return dcModelT;
            } else {
                return mapT.get(state.get(ColorfulGrid.COLOR));
            }
        });
        simpleBlockStateModel(BlocksAndItems.COLORED_RECEIVER.get(), state -> {
            if (!state.get(NetworkNodeBlock.CONNECTED)) {
                return dcModelR;
            } else {
                return mapR.get(state.get(ColorfulGrid.COLOR));
            }
        });
    }

    private void generateRelayModels() {
        Map<DyeColor, BlockModelBuilder> map = new HashMap<>();
        ResourceLocation relay = new ResourceLocation(RS.ID, "block/relay/relay");
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation cutout = new ResourceLocation(ColorfulGrid.MODID, "cutout/relay/" + color);
            BlockModelBuilder model = createCutOutModelAllSides("colored_relay_" + color, relay, relay, cutout);
            map.put(color, model);
        }
        BlockModelBuilder dcModel = createCutOutModelAllSides("relay_dc", relay, relay, new ResourceLocation(RS.ID, "block/relay/cutouts/disconnected"));
        simpleBlockStateModel(BlocksAndItems.COLORED_RELAY.get(), state -> {
            if (!state.get(NetworkNodeBlock.CONNECTED)) {
                return dcModel;
            } else {
                return map.get(state.get(ColorfulGrid.COLOR));
            }
        });
    }

    private BlockModelBuilder createCutOutModelAllSides(String name, ResourceLocation all, ResourceLocation particle, ResourceLocation cutout) {
        return models().withExistingParent(name, new ResourceLocation(RS.ID, "block/cube_all_cutout"))
            .texture("particle", particle)
            .texture("all", all)
            .texture("cutout", cutout);
    }

    private void generateCrafterManagerModels() {
        Map<DyeColor, BlockModelBuilder> map = new HashMap<>();
        ResourceLocation particle = new ResourceLocation("refinedstorage:block/crafter_manager/front");
        ResourceLocation north = new ResourceLocation("refinedstorage:block/crafter_manager/front");
        ResourceLocation east = new ResourceLocation("refinedstorage:block/crafter_manager/left");
        ResourceLocation south = new ResourceLocation("refinedstorage:block/crafter_manager/back");
        ResourceLocation west = new ResourceLocation("refinedstorage:block/crafter_manager/right");
        ResourceLocation up = new ResourceLocation("refinedstorage:block/crafter_manager/top");

        for (DyeColor color : DyeColor.values()) {
            ResourceLocation cutout = new ResourceLocation(ColorfulGrid.MODID, "cutout/craftermanager/" + color);
            BlockModelBuilder model = createCutoutModel("colored_craftermanager_"+ color, BOTTOM, up, north, south, east, west, particle, cutout);
            map.put(color, model);
        }
        BlockModelBuilder dcmodel = createCutoutModel("craftermanager_dc", BOTTOM, up, north, south, east, west, particle, new ResourceLocation(RS.ID, "block/crafter_manager/cutouts/front_disconnected"));
        JigsawDirectionalBlock(BlocksAndItems.COLORED_CRAFTER_MANAGER.get(), state -> {
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
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation cutout = new ResourceLocation(ColorfulGrid.MODID, "cutout/craftingmonitor/" + color);
            BlockModelBuilder model = createCutoutModel("colored_craftingmonitor_" + color, BOTTOM, up, north, south, east, west, particle, cutout);
            map.put(color, model);
        }
        BlockModelBuilder dcmodel = createCutoutModel("craftingmonitor_dc", BOTTOM, up, north, south, east, west, particle, new ResourceLocation(RS.ID, "block/crafting_monitor/cutouts/front_disconnected"));
        JigsawDirectionalBlock(BlocksAndItems.COLORED_CRAFTING_MONITOR.get(), state -> {
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
            ResourceLocation dc = new ResourceLocation(RS.ID, "block/grid/cutouts/" + (type == GridType.NORMAL ? "" : type.getString() + "_") + "front_disconnected");
            Map<DyeColor, BlockModelBuilder> modelMap = new HashMap<>();
            dcModels.put(type, createCutoutModel(type.getString() + "_disconnected", BOTTOM, gridtop, gridfront, gridback, gridleft, gridright, gridleft, dc));
            for (DyeColor color : DyeColor.values()) {

                ResourceLocation gridColor = new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type.getString() + "/" + color.getString());
                BlockModelBuilder model = createCutoutModel("colored_" + type.getString() + "_" + color, BOTTOM, gridtop, gridfront, gridback, gridleft, gridright, gridleft, gridColor);
                modelMap.put(color, model);
            }
            texturemap.put(type, modelMap);
        }
        JigsawDirectionalBlock(BlocksAndItems.COLORED_GRID, state -> {
            if (state.get(NetworkNodeBlock.CONNECTED)) {
                return texturemap.get(GridType.NORMAL).get(state.get(ColorfulGrid.COLOR));
            } else {
                return dcModels.get(GridType.NORMAL);
            }

        });
        JigsawDirectionalBlock(BlocksAndItems.COLORED_PATTERN_GRID, state -> {
            if (state.get(NetworkNodeBlock.CONNECTED)) {
                return texturemap.get(GridType.PATTERN).get(state.get(ColorfulGrid.COLOR));
            } else {
                return dcModels.get(GridType.PATTERN);
            }
        });
        JigsawDirectionalBlock(BlocksAndItems.COLORED_FLUID_GRID, state -> {
            if (state.get(NetworkNodeBlock.CONNECTED)) {
                return texturemap.get(GridType.FLUID).get(state.get(ColorfulGrid.COLOR));
            } else {
                return dcModels.get(GridType.FLUID);
            }
        });
        JigsawDirectionalBlock(BlocksAndItems.COLORED_CRAFTING_GRID, state -> {
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

    private BlockModelBuilder createCubeCutoutModelWithSideTopAndBottom(String name, ResourceLocation down, ResourceLocation side, ResourceLocation front, ResourceLocation sideCutout, ResourceLocation cutout) {
        return models().withExistingParent(name, new ResourceLocation(RS.ID, "cube_cutout"))
            .texture("particle", side)
            .texture("east", side)
            .texture("south", side)
            .texture("west", side)
            .texture("up", front)
            .texture("down", down)
            .texture("north", side)
            .texture("cutout_down", down)
            .texture("cutout_east", sideCutout)
            .texture("cutout_west", sideCutout)
            .texture("cutout_south", sideCutout)
            .texture("cutout_north", sideCutout)
            .texture("cutout_up", cutout);
    }

    private BlockModelBuilder createCubeCutoutModelEachSideUnique(String name, ResourceLocation down, ResourceLocation downCutout, ResourceLocation up, ResourceLocation upCutout, ResourceLocation east, ResourceLocation eastCutout, ResourceLocation west, ResourceLocation westCutout, ResourceLocation south, ResourceLocation southCutout, ResourceLocation north, ResourceLocation northCutout) {
        return models().withExistingParent(name, new ResourceLocation(RS.ID, "cube_cutout"))
            .texture("particle", north)
            .texture("east", east)
            .texture("south", south)
            .texture("west", west)
            .texture("up", up)
            .texture("down", down)
            .texture("north", north)
            .texture("cutout_down", downCutout)
            .texture("cutout_east", eastCutout)
            .texture("cutout_west", westCutout)
            .texture("cutout_south", southCutout)
            .texture("cutout_north", northCutout)
            .texture("cutout_up", upCutout);
    }
    public void blockDirectionDirectionalBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
            .forAllStates(state -> {
                Direction dir = state.get(BlockDirection.ANY_FACE_PLAYER.getProperty());
                return ConfiguredModel.builder()
                    .modelFile(modelFunc.apply(state))
                    .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.getHorizontalAngle()) + 180) % 360)
                    .build();
            });
    }

    public void JigsawDirectionalBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
            .forAllStates(state -> {
                JigsawOrientation ori = state.get(BlockStateProperties.ORIENTATION);
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
        } else if (dir1 == Direction.UP) {
            if (dir2 == Direction.EAST) return -90;
            if (dir2 == Direction.WEST) return 90;
            if (dir2 == Direction.NORTH) return 0;
            if (dir2 == Direction.SOUTH) return 180;
        } else {
            if (dir1 == Direction.EAST) return -90;
            if (dir1 == Direction.WEST) return 90;
            if (dir1 == Direction.NORTH) return 0;
            if (dir1 == Direction.SOUTH) return 180;
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

