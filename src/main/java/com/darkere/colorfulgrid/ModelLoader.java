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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelLoader {
    private final BakedModelOverrideRegistry bakedModelOverrideRegistry = new BakedModelOverrideRegistry();

    public void setup() {
        for (GridType gridType : GridType.values()) {
            String type = gridType.getString();
            bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "coloredgrid_" + type), (base, registry) -> new FullbrightBakedModel(base, true, getColorArray("cutout/" + type + "/")));
        }
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_craftingmonitor"), (base, registry) -> new FullbrightBakedModel(base, true, getColorArray("cutout/craftingmonitor/")));
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_craftermanager"), (base, registry) -> new FullbrightBakedModel(base, true, getColorArray("cutout/craftermanager/")));
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_relay"), (base, registry) -> new FullbrightBakedModel(base, true, getColorArray("cutout/relay/")));
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_transmitter"), (base, registry) -> new FullbrightBakedModel(base, true, getColorArray("cutout/transmitter/")));
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_receiver"), (base, registry) -> new FullbrightBakedModel(base, true, getColorArray("cutout/receiver/")));
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_crafter"), (base, registry) -> new FullbrightBakedModel(base, true, getColorArray("cutout/crafter/")));
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_security"), (base, registry) -> new FullbrightBakedModel(base, true, getColorArraySecurity()));
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_controller"), (base, registry) -> new FullbrightBakedModel(base, true, getColorArray("cutout/controller/")));
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_controller_creative"), (base, registry) -> new FullbrightBakedModel(base, true, getColorArray("cutout/controller/")));
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_crafter"), (base, registry) -> new FullbrightBakedModel(base, true, getColorArrayCrafter()));
        bakedModelOverrideRegistry.add(new ResourceLocation(ColorfulGrid.MODID, "colored_diskmanipulator"), (base, registry) -> new FullbrightBakedModel(base, true, getColorArray("cutout/diskmanipulator/")));
    }

    private ResourceLocation[] getColorArraySecurity() {
        List<String> colors = Arrays.asList("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow");
        List<ResourceLocation> rls = new ArrayList<>();
        colors.forEach(color -> rls.add(new ResourceLocation(ColorfulGrid.MODID, "cutout/security/" + color + "_" + "front")));
        colors.forEach(color -> rls.add(new ResourceLocation(ColorfulGrid.MODID, "cutout/security/" + color + "_" + "left")));
        colors.forEach(color -> rls.add(new ResourceLocation(ColorfulGrid.MODID, "cutout/security/" + color + "_" + "right")));
        colors.forEach(color -> rls.add(new ResourceLocation(ColorfulGrid.MODID, "cutout/security/" + color + "_" + "back")));
        colors.forEach(color -> rls.add(new ResourceLocation(ColorfulGrid.MODID, "cutout/security/" + color + "_" + "top")));
        return rls.toArray(new ResourceLocation[0]);
    }

    private ResourceLocation[] getColorArray(String location) {
        List<String> colors = Arrays.asList("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow");
        List<ResourceLocation> rls = new ArrayList<>();
        colors.forEach(color -> rls.add(new ResourceLocation(ColorfulGrid.MODID, location + color)));
        return rls.toArray(new ResourceLocation[0]);
    }
    private ResourceLocation[] getColorArrayCrafter() {
        List<String> colors = Arrays.asList("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow");
        List<ResourceLocation> rls = new ArrayList<>();
        colors.forEach(color -> rls.add(new ResourceLocation(ColorfulGrid.MODID, "cutout/crafter/" + color + "_" + "top")));
        colors.forEach(color -> rls.add(new ResourceLocation(ColorfulGrid.MODID, "cutout/crafter/" + color + "_" + "side_0")));
        return rls.toArray(new ResourceLocation[0]);
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
        BlocksAndItems.getBlocks().forEach(block -> {
            RenderTypeLookup.setRenderLayer(block,RenderType.getCutout());
        });
    }

}
