package com.kingparity.betterpets;

import com.kingparity.betterpets.init.*;
import com.kingparity.betterpets.network.PacketHandler;
import com.kingparity.betterpets.proxy.ClientProxy;
import com.kingparity.betterpets.proxy.Proxy;
import com.kingparity.betterpets.proxy.ServerProxy;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BetterPets.ID)
public class BetterPets
{
    public static final String ID = "betterpets";
    public static final Proxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static final Logger LOGGER = LogManager.getLogger(BetterPets.ID);
    public static final ItemGroup TAB = new ItemGroup("tabBetterPets")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModBlocks.WATER_COLLECTOR.get());
        }
    };
    
    public BetterPets()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModEntities.ENTITY_TYPES.register(eventBus);
        ModTileEntities.TILE_ENTITY_TYPES.register(eventBus);
        ModContainers.CONTAINER_TYPES.register(eventBus);
        ModFluids.FLUIDS.register(eventBus);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }
    
    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        PacketHandler.register();
        ModEntities.registerEntityTypeAttributes();
    }
    
    private void onClientSetup(FMLClientSetupEvent event)
    {
        PROXY.setupClient();
    }
}