package com.darkere.colorfulgrid.datagen;

import com.darkere.colorfulgrid.BlocksAndItems;
import com.darkere.colorfulgrid.ColorfulGrid;
import com.refinedmods.refinedstorage.RS;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;

import java.util.ArrayList;

public class ItemModelGenerator extends ItemModelProvider {

    public ItemModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, new ExistingFileHelper(new ArrayList<>(),false));
    }

    @Override
    protected void registerModels() {
       BlocksAndItems.blockItems.forEach((name,colormap)->
       colormap.forEach((color,item)->{
           String fullName = "colored_"+ name + "_"+ color;
           String blockName = fullName;
           if(name.contains("controller_creative")){
                  blockName = blockName.replace("_creative", "");
           }
           withExistingParent(new ResourceLocation(ColorfulGrid.MODID,fullName).getPath(),new ResourceLocation(ColorfulGrid.MODID,"block/" + blockName));
       }));
    }
}
