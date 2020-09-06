package com.darkere.colorfulgrid;

import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.render.BakedModelOverrideRegistry;
import com.refinedmods.refinedstorage.render.model.FullbrightBakedModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModelLoader {
    private final BakedModelOverrideRegistry bakedModelOverrideRegistry = new BakedModelOverrideRegistry();

    public void setup(){
        for (GridType gridType : GridType.values()) {
            String type = gridType.func_176610_l();
            bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "coloredgrid_" + type), (base, registry) -> new FullbrightBakedModel(base, true,
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/black"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/blue"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/brown"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/cyan"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/gray"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/green"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/light_blue"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/light_gray"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/lime"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/magenta"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/orange"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/pink"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/purple"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/red"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/white"),
                new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/yellow")
            ));
        }
        String type = "craftingmonitor";
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_craftingmonitor"), (base, registry) -> new FullbrightBakedModel(base, true,
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/black"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/blue"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/brown"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/cyan"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/gray"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/green"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/light_blue"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/light_gray"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/lime"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/magenta"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/orange"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/pink"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/purple"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/red"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/white"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type + "/yellow")
        ));
        String type2 = "craftermanager";
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_craftermanager"), (base, registry) -> new FullbrightBakedModel(base, true,
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/black"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/blue"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/brown"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/cyan"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/gray"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/green"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/light_blue"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/light_gray"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/lime"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/magenta"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/orange"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/pink"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/purple"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/red"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/white"),
            new ResourceLocation(ColorfulGrid.MODID, "cutout/" + type2 + "/yellow")
        ));
    }
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent e) {
        for (ResourceLocation id : e.getModelRegistry().keySet()) {
            BakedModelOverrideRegistry.BakedModelOverrideFactory factory = this.bakedModelOverrideRegistry.get(new ResourceLocation(id.getNamespace(), id.getPath()));

            if (factory != null) {
                e.getModelRegistry().put(id, factory.create(e.getModelRegistry().get(id), e.getModelRegistry()));
            }
        }
    }
    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(BlocksAndItems.COLORED_CRAFTING_GRID, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksAndItems.COLORED_PATTERN_GRID, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksAndItems.COLORED_FLUID_GRID, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksAndItems.COLORED_GRID, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksAndItems.COLORED_CRAFTING_MONITOR.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksAndItems.COLORED_CRAFTER_MANAGER.get(), RenderType.getCutout());
    }

}
