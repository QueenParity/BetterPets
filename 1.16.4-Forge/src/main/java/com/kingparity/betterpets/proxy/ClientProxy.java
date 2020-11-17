package com.kingparity.betterpets.proxy;

import com.kingparity.betterpets.client.render.tileentity.*;
import com.kingparity.betterpets.client.screen.WaterFilterScreen;
import com.kingparity.betterpets.init.ModBlocks;
import com.kingparity.betterpets.init.ModContainers;
import com.kingparity.betterpets.init.ModFluids;
import com.kingparity.betterpets.init.ModTileEntities;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements Proxy
{
    @Override
    public void setupClient()
    {
        MinecraftForge.EVENT_BUS.register(this);
        
        this.setupRenderLayers();
        this.bindTileEntityRenders();
        this.registerScreenFactories();
    }
    
    private void setupRenderLayers()
    {
        RenderTypeLookup.setRenderLayer(ModBlocks.WATER_COLLECTOR.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.WATER_FILTER.get(), RenderType.getCutout());
        
        RenderTypeLookup.setRenderLayer(ModBlocks.TANK.get(), RenderType.getCutout());
        
        RenderTypeLookup.setRenderLayer(ModFluids.FILTERED_WATER.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModFluids.FLOWING_FILTERED_WATER.get(), RenderType.getTranslucent());
    }
    
    private void bindTileEntityRenders()
    {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.WATER_COLLECTOR.get(), WaterCollectorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.WATER_FILTER.get(), WaterFilterRenderer::new);
    
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.FLUID_PIPE.get(), FluidPipeRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.FLUID_PUMP.get(), FluidPumpRenderer::new);
        
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.TANK.get(), TankRenderer::new);
    }
    
    private void registerScreenFactories()
    {
        ScreenManager.registerFactory(ModContainers.WATER_FILTER.get(), WaterFilterScreen::new);
    }
}