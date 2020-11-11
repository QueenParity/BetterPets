package com.kingparity.betterpets;

import com.kingparity.betterpets.client.render.blockentity.WaterCollectorRenderer;
import com.kingparity.betterpets.client.render.blockentity.WaterFilterRenderer;
import com.kingparity.betterpets.core.ModBlockEntityTypes;
import com.kingparity.betterpets.core.ModBlocks;
import com.kingparity.betterpets.core.ModFluids;
import com.kingparity.betterpets.names.FluidNames;
import com.kingparity.betterpets.util.Reference;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;

import java.util.function.Function;

public class ModClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        setupFluidRendering(ModFluids.STILL_FILTERED_WATER, ModFluids.FLOWING_FILTERED_WATER, new Identifier(Reference.ID, FluidNames.FILTERED_WATER), 0x00C8FF);
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ModFluids.STILL_FILTERED_WATER, ModFluids.FLOWING_FILTERED_WATER);
        
        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityTypes.WATER_COLLECTOR_BLOCK_ENTITY, WaterCollectorRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityTypes.WATER_FILTER_BLOCK_ENTITY, WaterFilterRenderer::new);
        
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WATER_FILTER_OAK, RenderLayer.getCutout());
    }
    
    public static void setupFluidRendering(final Fluid still, final Fluid flowing, final Identifier textureFluidId, final int color)
    {
        final Identifier stillSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_still");
        final Identifier flowingSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_flow");
    
        // If they're not already present, add the sprites to the block atlas
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) ->
        {
            registry.register(stillSpriteId);
            registry.register(flowingSpriteId);
        });
    
        final Identifier fluidId = Registry.FLUID.getId(still);
        final Identifier listenerId = new Identifier(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");
    
        final Sprite[] fluidSprites = { null, null };
    
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener()
        {
            @Override
            public Identifier getFabricId()
            {
                return listenerId;
            }
        
            /**
             * Get the sprites from the block atlas when resources are reloaded
             */
            @Override
            public void apply(ResourceManager resourceManager)
            {
                final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
                fluidSprites[0] = atlas.apply(stillSpriteId);
                fluidSprites[1] = atlas.apply(flowingSpriteId);
            }
        });
    
        // The FluidRenderer gets the sprites and color from a FluidRenderHandler during rendering
        final FluidRenderHandler renderHandler = new FluidRenderHandler()
        {
            @Override
            public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state)
            {
                return fluidSprites;
            }
        
            @Override
            public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state)
            {
                return color;
            }
        };
    
        FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
        FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
    }
}
