package com.darkere.colorfulgrid;

import com.darkere.colorfulgrid.Blocks.*;
import com.refinedmods.refinedstorage.api.network.NetworkType;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.block.BaseBlock;
import com.refinedmods.refinedstorage.util.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.*;

public class BlocksAndItems {
    //TODO move everything to RegisterEvent cause Raoul hasn't moved on yet :(
    static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ColorfulGrid.MODID);
    public static final RegistryObject<ColoredCrafterManagerBlock> COLORED_CRAFTER_MANAGER = BLOCKS.register("colored_craftermanager", () -> new ColoredCrafterManagerBlock(BlockUtils.DEFAULT_ROCK_PROPERTIES));
    public static final RegistryObject<ColoredCraftingMonitorBlock> COLORED_CRAFTING_MONITOR = BLOCKS.register("colored_craftingmonitor", () -> new ColoredCraftingMonitorBlock(BlockUtils.DEFAULT_ROCK_PROPERTIES));
    public static final RegistryObject<ColoredRelayBlock> COLORED_RELAY = BLOCKS.register("colored_relay", () -> new ColoredRelayBlock(BlockUtils.DEFAULT_ROCK_PROPERTIES));
    public static final RegistryObject<ColoredTransmitterBlock> COLORED_TRANSMITTER = BLOCKS.register("colored_transmitter", () -> new ColoredTransmitterBlock(BlockUtils.DEFAULT_ROCK_PROPERTIES));
    public static final RegistryObject<ColoredReceiverBlock> COLORED_RECEIVER = BLOCKS.register("colored_receiver", () -> new ColoredReceiverBlock(BlockUtils.DEFAULT_ROCK_PROPERTIES));
    public static final RegistryObject<ColoredCrafter> COLORED_CRAFTER = BLOCKS.register("colored_crafter", () -> new ColoredCrafter(BlockUtils.DEFAULT_ROCK_PROPERTIES));
    public static final RegistryObject<ColoredSecurityManagerBlock> COLORED_SECURITY = BLOCKS.register("colored_security", () -> new ColoredSecurityManagerBlock(BlockUtils.DEFAULT_ROCK_PROPERTIES));
    public static final RegistryObject<ColoredController> COLORED_CONTROLLER = BLOCKS.register("colored_controller", () -> new ColoredController(BlockUtils.DEFAULT_ROCK_PROPERTIES, NetworkType.NORMAL));
    public static final RegistryObject<ColoredController> COLORED_CONTROLLER_CREATIVE = BLOCKS.register("colored_controller_creative", () -> new ColoredController(BlockUtils.DEFAULT_ROCK_PROPERTIES, NetworkType.CREATIVE));
    public static final RegistryObject<ColoredDiskManipulatorBlock> COLORED_DISKMANIPULATOR = BLOCKS.register("colored_diskmanipulator", () -> new ColoredDiskManipulatorBlock(BlockUtils.DEFAULT_ROCK_PROPERTIES));


    @ObjectHolder(ColorfulGrid.MODID + ":coloredgrid_normal")
    public static ColoredGridBlock COLORED_GRID;
    @ObjectHolder(ColorfulGrid.MODID + ":coloredgrid_crafting")
    public static ColoredGridBlock COLORED_CRAFTING_GRID;
    @ObjectHolder(ColorfulGrid.MODID + ":coloredgrid_pattern")
    public static ColoredGridBlock COLORED_PATTERN_GRID;
    @ObjectHolder(ColorfulGrid.MODID + ":coloredgrid_fluid")
    public static ColoredGridBlock COLORED_FLUID_GRID;
    public static final Map<String, Map<DyeColor, Item>> blockItems = new HashMap<>();

    public static void register() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
    }



    public static List<Block> getBlocks() {
        return Arrays.asList(COLORED_CRAFTER_MANAGER.get(), COLORED_CRAFTING_MONITOR.get(), COLORED_RELAY.get(), COLORED_TRANSMITTER.get(), COLORED_RECEIVER.get(),
            COLORED_CRAFTER.get(), COLORED_SECURITY.get(), COLORED_CONTROLLER.get(), COLORED_CONTROLLER_CREATIVE.get(), COLORED_DISKMANIPULATOR.get(),
            COLORED_GRID, COLORED_CRAFTING_GRID, COLORED_PATTERN_GRID, COLORED_FLUID_GRID);
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new ColoredGridBlock(GridType.NORMAL, new ResourceLocation(ColorfulGrid.MODID, "coloredgrid_normal")));
        event.getRegistry().register(new ColoredGridBlock(GridType.CRAFTING, new ResourceLocation(ColorfulGrid.MODID, "coloredgrid_crafting")));
        event.getRegistry().register(new ColoredGridBlock(GridType.PATTERN, new ResourceLocation(ColorfulGrid.MODID, "coloredgrid_pattern")));
        event.getRegistry().register(new ColoredGridBlock(GridType.FLUID, new ResourceLocation(ColorfulGrid.MODID, "coloredgrid_fluid")));
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        makeBlockItems();
        blockItems.forEach((name, itemmap) -> itemmap.forEach((color, item) -> {
            event.getRegistry().register(item);
        }));
    }


    private static void makeBlockItems() {
        blockItems.put("craftermanager", makeBlockItemWithColors(COLORED_CRAFTER_MANAGER.get()));
        blockItems.put("craftingmonitor", makeBlockItemWithColors(COLORED_CRAFTING_MONITOR.get()));
        blockItems.put("relay", makeBlockItemWithColors(COLORED_RELAY.get()));
        blockItems.put("transmitter", makeBlockItemWithColors(COLORED_TRANSMITTER.get()));
        blockItems.put("receiver", makeBlockItemWithColors(COLORED_RECEIVER.get()));
        blockItems.put("crafter", makeBlockItemWithColors(COLORED_CRAFTER.get()));
        blockItems.put("security", makeBlockItemWithColors(COLORED_SECURITY.get()));
        blockItems.put("controller", makeBlockItemWithColors(COLORED_CONTROLLER.get()));
        blockItems.put("controller_creative", makeBlockItemWithColors(COLORED_CONTROLLER_CREATIVE.get()));
        blockItems.put("diskmanipulator", makeBlockItemWithColors(COLORED_DISKMANIPULATOR.get()));
        blockItems.put("normal",makeBlockItemWithColors(COLORED_GRID));
        blockItems.put("crafting",makeBlockItemWithColors(COLORED_CRAFTING_GRID));
        blockItems.put("pattern",makeBlockItemWithColors(COLORED_PATTERN_GRID));
        blockItems.put("fluid",makeBlockItemWithColors(COLORED_FLUID_GRID));
        setRegistryNames();
    }

    private static void setRegistryNames() {
        blockItems.forEach((name,itemmap)-> itemmap.forEach((color, item)->
            item.setRegistryName(ColorfulGrid.MODID,"colored_"+ name + "_" + color)));
    }

    private static Map<DyeColor, Item> makeBlockItemWithColors(BaseBlock block) {
        Map<DyeColor, Item> map = new HashMap<>();
        ItemGroup group = new ItemGroup(ColorfulGrid.MODID){
            @Override
            public ItemStack createIcon() {
                return blockItems.get("normal").get(DyeColor.PURPLE).getDefaultInstance();
            }
        };
        for (DyeColor color : DyeColor.values()) {
                map.put(color, new ColoredBlockItem(block, new Item.Properties().group(group),color));
        }
        return map;
    }
}
