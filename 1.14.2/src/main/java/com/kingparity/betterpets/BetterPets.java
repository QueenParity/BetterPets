package com.kingparity.betterpets;

import com.google.gson.Gson;
import com.kingparity.betterpets.init.BetterPetContainerTypes;
import com.kingparity.betterpets.network.PacketHandler;
import com.kingparity.betterpets.proxy.ClientProxy;
import com.kingparity.betterpets.proxy.IProxy;
import com.kingparity.betterpets.proxy.ServerProxy;
import com.kingparity.betterpets.util.Reference;
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
public class BetterPets
{
    private static final Logger logger = LogManager.getLogger();
    public static final IProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    
    public BetterPets()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
    
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public static Gson gsonInstance = new Gson();
    
    private void setup(final FMLCommonSetupEvent event)
    {
        DeferredWorkQueue.runLater(PacketHandler::register);
        
        logger.info("Setup method registered");
        PROXY.setup();
    }
    
    private void clientRegistries(final FMLClientSetupEvent event)
    {
        BetterPetContainerTypes.bindScreens(event);
        logger.info("clientRegistries method registered");
        PROXY.clientRegistries();
    }
}
