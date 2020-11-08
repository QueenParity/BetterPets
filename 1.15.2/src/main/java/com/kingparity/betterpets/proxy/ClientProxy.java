package com.kingparity.betterpets.proxy;

import com.kingparity.betterpets.client.render.entity.BetterWolfRenderer;
import com.kingparity.betterpets.client.render.tileentity.WaterCollectorRenderer;
import com.kingparity.betterpets.client.screen.WaterFilterScreen;
import com.kingparity.betterpets.client.screen.WolfChestScreen;
import com.kingparity.betterpets.core.ModContainers;
import com.kingparity.betterpets.core.ModEntities;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.util.FluidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.concurrent.CompletableFuture;

public class ClientProxy extends CommonProxy
{
    @Override
    public void onSetupClient()
    {
        //RenderTypeLookup.setRenderLayer(ModFluids.FILTERED_WATER, RenderType.getTranslucent());
        //RenderTypeLookup.setRenderLayer(ModFluids.FLOWING_FILTERED_WATER, RenderType.getTranslucent());
    
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BETTER_WOLF.get(), BetterWolfRenderer::new);
        
        MinecraftForge.EVENT_BUS.register(this);

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.WATER_COLLECTOR.get(), WaterCollectorRenderer::new);

        ScreenManager.registerFactory(ModContainers.WATER_FILTER.get(), WaterFilterScreen::new);
        ScreenManager.registerFactory(ModContainers.WOLF_CHEST.get(), WolfChestScreen::new);

        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener((stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> CompletableFuture.runAsync(() ->
        {
            FluidUtils.clearCacheFluidColor();
            //EntityRaytracer.clearDataForReregistration();
        }));
    }
}