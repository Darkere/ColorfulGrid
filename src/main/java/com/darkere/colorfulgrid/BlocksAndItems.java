package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.util.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class BlocksAndItems {
    //TODO move everything to RegisterEvent cause Raoul hasn't moved on yet :(
    static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ColorfulGrid.MODID);
    static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ColorfulGrid.MODID);
    public static final RegistryObject<ColoredCrafterManagerBlock> COLORED_CRAFTER_MANAGER = BLOCKS.register("colored_craftermanager", () -> new ColoredCrafterManagerBlock(BlockUtils.DEFAULT_ROCK_PROPERTIES));
    public static final RegistryObject<ColoredCraftingMonitorBlock> COLORED_CRAFTING_MONITOR = BLOCKS.register("colored_craftingmonitor", () -> new ColoredCraftingMonitorBlock(BlockUtils.DEFAULT_ROCK_PROPERTIES));
    public static final RegistryObject<BlockItem> COLORED_CRAFTER_MANAGER_ITEM = ITEMS.register("colored_craftermanager", () -> new BlockItem(COLORED_CRAFTER_MANAGER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> COLORED_CRAFTING_MONITOR_ITEM = ITEMS.register("colored_craftingmonitor", () -> new BlockItem(COLORED_CRAFTING_MONITOR.get(), new Item.Properties()));
    @ObjectHolder(ColorfulGrid.MODID + ":coloredgrid_normal" )
    public static Block COLORED_GRID;
    @ObjectHolder(ColorfulGrid.MODID + ":coloredgrid_crafting" )
    public static Block COLORED_CRAFTING_GRID;
    @ObjectHolder(ColorfulGrid.MODID + ":coloredgrid_pattern" )
    public static Block COLORED_PATTERN_GRID;
    @ObjectHolder(ColorfulGrid.MODID + ":coloredgrid_fluid" )
    public static Block COLORED_FLUID_GRID;
    @ObjectHolder(ColorfulGrid.MODID + ":coloredgrid_normal" )
    public static Item COLORED_GRID_ITEM;
    @ObjectHolder(ColorfulGrid.MODID + ":coloredgrid_crafting" )
    public static Item COLORED_CRAFTING_GRID_ITEM;
    @ObjectHolder(ColorfulGrid.MODID + ":coloredgrid_pattern" )
    public static Item COLORED_PATTERN_GRID_ITEM;
    @ObjectHolder(ColorfulGrid.MODID + ":coloredgrid_fluid" )
    public static Item COLORED_FLUID_GRID_ITEM;

    public static void register(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
    }
}
