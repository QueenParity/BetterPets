package com.kingparity.betterpets.proxy;

import com.kingparity.betterpets.client.render.tileentity.WaterCollectorRenderer;
import com.kingparity.betterpets.init.ModBlocks;
import com.kingparity.betterpets.init.ModTileEntities;
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
    }
    
    private void setupRenderLayers()
    {
        RenderTypeLookup.setRenderLayer(ModBlocks.OAK_WATER_COLLECTOR.get(), RenderType.getCutout());
    }
    
    private void bindTileEntityRenders()
    {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.WATER_COLLECTOR.get(), WaterCollectorRenderer::new);
    }
}