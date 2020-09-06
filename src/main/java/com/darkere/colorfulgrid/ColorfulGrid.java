package com.darkere.colorfulgrid;

import com.darkere.colorfulgrid.datagen.Datagen;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ColorfulGrid.MODID)
public class ColorfulGrid {
    public static final String MODID = "colorfulgrid";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public ColorfulGrid() {
        BlocksAndItems.register();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);
        ModelLoader loader = new ModelLoader();
        loader.setup();
        modEventBus.register(loader);
        modEventBus.register(new Datagen());
        modEventBus.register(new TranformationManager());
    }


    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event){
        event.getRegistry().register(new ColoredGridBlock(GridType.NORMAL,new ResourceLocation(MODID,"coloredgrid_normal")));
        event.getRegistry().register(new ColoredGridBlock(GridType.CRAFTING,new ResourceLocation(MODID,"coloredgrid_crafting")));
        event.getRegistry().register(new ColoredGridBlock(GridType.PATTERN,new ResourceLocation(MODID,"coloredgrid_pattern")));
        event.getRegistry().register(new ColoredGridBlock(GridType.FLUID,new ResourceLocation(MODID,"coloredgrid_fluid")));
    }
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().register(new BlockItem(BlocksAndItems.COLORED_GRID, new Item.Properties()).setRegistryName(MODID,"coloredgrid_normal"));
        event.getRegistry().register(new BlockItem(BlocksAndItems.COLORED_CRAFTING_GRID, new Item.Properties()).setRegistryName(MODID,"coloredgrid_crafting"));
        event.getRegistry().register(new BlockItem(BlocksAndItems.COLORED_PATTERN_GRID, new Item.Properties()).setRegistryName(MODID,"coloredgrid_pattern"));
        event.getRegistry().register(new BlockItem(BlocksAndItems.COLORED_FLUID_GRID, new Item.Properties()).setRegistryName(MODID,"coloredgrid_fluid"));
    }

}
