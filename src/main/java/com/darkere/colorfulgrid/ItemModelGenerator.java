package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.RS;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;

public class ItemModelGenerator extends ItemModelProvider {

    public ItemModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(ColorfulGrid.COLOREDCRAFTINGGRIDITEM.get().getRegistryName().getPath(), new ResourceLocation(RS.ID, "block/grid/crafting/disconnected"));
    }
}
