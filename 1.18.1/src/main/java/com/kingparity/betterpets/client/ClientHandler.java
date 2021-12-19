package com.kingparity.betterpets.client;

import com.kingparity.betterpets.client.render.blockentity.TankRenderer;
import com.kingparity.betterpets.client.render.blockentity.WaterCollectorRenderer;
import com.kingparity.betterpets.client.render.blockentity.WaterFilterRenderer;
import com.kingparity.betterpets.client.render.entity.BetterWolfRenderer;
import com.kingparity.betterpets.client.render.entity.model.BetterWolfModel;
import com.kingparity.betterpets.client.render.entity.model.BetterWolfModelLayers;
import com.kingparity.betterpets.client.screen.WaterFilterScreen;
import com.kingparity.betterpets.init.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.client.ForgeHooksClient;

public class ClientHandler
{
    public static void setup()
    {
        registerBlockEntityRenderers();
        registerEntityRenders();
        registerScreenFactories();
        registerLayers();
    }
    
    private static void registerBlockEntityRenderers()
    {
        BlockEntityRenderers.register(ModBlockEntities.WATER_COLLECTOR.get(), WaterCollectorRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.WATER_FILTER.get(), WaterFilterRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.TANK.get(), TankRenderer::new);
    }
    
    private static void registerEntityRenders()
    {
        EntityRenderers.register(ModEntities.BETTER_WOLF.get(), BetterWolfRenderer::new);
    
        ForgeHooksClient.registerLayerDefinition(BetterWolfModelLayers.BETTER_WOLF, BetterWolfModel::createBodyLayer);
    }
    
    private static void registerScreenFactories()
    {
        MenuScreens.register(ModMenus.WATER_FILTER.get(), WaterFilterScreen::new);
    }
    
    private static void registerLayers()
    {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WATER_COLLECTOR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WATER_FILTER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.TANK.get(), RenderType.cutout());
        
        ItemBlockRenderTypes.setRenderLayer(ModFluids.FILTERED_WATER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_FILTERED_WATER.get(), RenderType.translucent());
    }
}
