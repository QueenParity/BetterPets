package com.kingparity.betterpets.proxy;

import com.kingparity.betterpets.client.render.tileentity.WaterCollectorRenderer;
import com.kingparity.betterpets.client.screen.WaterFilterScreen;
import com.kingparity.betterpets.core.ModContainers;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.util.FluidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.concurrent.CompletableFuture;

public class ClientProxy extends CommonProxy
{
    @Override
    public void onSetupClient()
    {
        //RenderTypeLookup.setRenderLayer(ModFluids.FILTERED_WATER, RenderType.getTranslucent());
        //RenderTypeLookup.setRenderLayer(ModFluids.FLOWING_FILTERED_WATER, RenderType.getTranslucent());

        MinecraftForge.EVENT_BUS.register(this);

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.WATER_COLLECTOR, WaterCollectorRenderer::new);

        ScreenManager.registerFactory(ModContainers.WATER_FILTER, WaterFilterScreen::new);

        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener((stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> CompletableFuture.runAsync(() ->
        {
            FluidUtils.clearCacheFluidColor();
            //EntityRaytracer.clearDataForReregistration();
        }));
    }
}