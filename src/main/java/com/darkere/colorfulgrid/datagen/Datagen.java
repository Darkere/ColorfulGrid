package com.darkere.colorfulgrid.datagen;

import com.darkere.colorfulgrid.ColorfulGrid;
import net.minecraft.data.RecipeProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class Datagen {
    @SubscribeEvent
    public void datagen(GatherDataEvent event) {
        if(event.includeClient()){
            event.getGenerator().addProvider(new BlockStateGenerator(event.getGenerator(), ColorfulGrid.MODID, event.getExistingFileHelper()));
            event.getGenerator().addProvider(new ItemModelGenerator(event.getGenerator(), ColorfulGrid.MODID, event.getExistingFileHelper()));
        }
        if(event.includeServer()){
            event.getGenerator().addProvider(new RecipeGenerator(event.getGenerator()));
        }

    }
}
