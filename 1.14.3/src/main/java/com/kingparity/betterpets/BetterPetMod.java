package com.kingparity.betterpets;

import com.google.gson.Gson;
import com.kingparity.betterpets.common.CommonEvents;
import com.kingparity.betterpets.core.ModContainers;
import com.kingparity.betterpets.network.PacketHandler;
import com.kingparity.betterpets.proxy.ClientProxy;
import com.kingparity.betterpets.proxy.CommonProxy;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.ID)
public class BetterPetMod
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.ID);
    public static final ItemGroup GROUP = new BetterPetGroup(Reference.ID);
    public static final CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    
    public BetterPetMod()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public static Gson gsonInstance = new Gson();
    
    private void onCommonSetup(final FMLCommonSetupEvent event)
    {
        DeferredWorkQueue.runLater(() -> {
            PacketHandler.register();
            MinecraftForge.EVENT_BUS.register(new CommonEvents());
        });
        
        LOGGER.debug("Setup method registered");
        PROXY.onSetupCommon();
    }
    
    private void onClientSetup(final FMLClientSetupEvent event)
    {
        ModContainers.bindScreens(event);
        LOGGER.debug("clientRegistries method registered");
        PROXY.onSetupClient();
    }
}
