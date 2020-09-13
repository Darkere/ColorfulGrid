package com.darkere.colorfulgrid;

import com.darkere.colorfulgrid.datagen.Datagen;
import net.minecraft.item.DyeColor;
import net.minecraft.state.EnumProperty;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
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
        ModelLoader loader = new ModelLoader();
        loader.setup();
        modEventBus.register(loader);
        modEventBus.register(new Datagen());
        modEventBus.register(new BlocksAndItems());
        MinecraftForge.EVENT_BUS.register(new TransformationManager());
    }

}
