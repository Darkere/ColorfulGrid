package com.darkere.colorfulgrid.datagen;

import com.darkere.colorfulgrid.BlocksAndItems;
import com.darkere.colorfulgrid.ColorfulGrid;
import com.refinedmods.refinedstorage.RSBlocks;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.DyeColor;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider {
    public RecipeGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("crafter").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.CRAFTER)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("craftermanager").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.CRAFTER_MANAGER)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("craftingmonitor").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.CRAFTING_MONITOR)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("relay").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.RELAY)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("transmitter").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.NETWORK_TRANSMITTER)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("receiver").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.NETWORK_RECEIVER)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("controller").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.CONTROLLER)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("controller_creative").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.CREATIVE_CONTROLLER)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("diskmanipulator").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.DISK_MANIPULATOR)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("normal").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.GRID)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("crafting").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.CRAFTING_GRID)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("pattern").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.PATTERN_GRID)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlocksAndItems.blockItems.get("fluid").get(DyeColor.LIGHT_BLUE))
            .addIngredient(RSBlocks.FLUID_GRID)
            .setGroup(ColorfulGrid.MODID)
            .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
            .build(consumer);


    }
}
