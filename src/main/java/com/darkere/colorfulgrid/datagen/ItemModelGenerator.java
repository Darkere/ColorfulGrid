package com.darkere.colorfulgrid.datagen;

import com.darkere.colorfulgrid.BlocksAndItems;
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
        withExistingParent(BlocksAndItems.COLORED_CRAFTING_GRID_ITEM.getRegistryName().getPath(), new ResourceLocation(RS.ID, "block/grid/crafting/disconnected"));
        withExistingParent(BlocksAndItems.COLORED_GRID_ITEM.getRegistryName().getPath(), new ResourceLocation(RS.ID, "block/grid/normal/disconnected"));
        withExistingParent(BlocksAndItems.COLORED_PATTERN_GRID_ITEM.getRegistryName().getPath(), new ResourceLocation(RS.ID, "block/grid/pattern/disconnected"));
        withExistingParent(BlocksAndItems.COLORED_FLUID_GRID_ITEM.getRegistryName().getPath(), new ResourceLocation(RS.ID, "block/grid/fluid/disconnected"));
        withExistingParent(BlocksAndItems.COLORED_CRAFTING_MONITOR_ITEM.get().getRegistryName().getPath(), new ResourceLocation(RS.ID, "block/crafting_monitor_disconnected"));
        withExistingParent(BlocksAndItems.COLORED_CRAFTER_MANAGER_ITEM.get().getRegistryName().getPath(), new ResourceLocation(RS.ID, "block/crafter_manager_disconnected"));

    }
}
