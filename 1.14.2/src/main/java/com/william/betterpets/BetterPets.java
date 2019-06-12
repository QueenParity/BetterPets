package com.william.betterpets;

import com.william.betterpets.network.PacketHandler;
import com.william.betterpets.proxy.ClientProxy;
import com.william.betterpets.proxy.IProxy;
import com.william.betterpets.proxy.ServerProxy;
import com.william.betterpets.util.Reference;
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
    public static BetterPets instance;
    public static final Logger logger = LogManager.getLogger(Reference.ID);
    public static final IProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    
    public BetterPets()
    {
        instance = this;
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
        //ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY);
    
        /*ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> {
        
            return (OpenContainer) -> {
            
                ResourceLocation loc = OpenContainer.getId();
            
                if (loc.toString().equals(Reference.ID + ":typecraft_block"))
                {
                    EntityPlayerSP player = Minecraft.getInstance().player;
                    BlockPos pos = OpenContainer.getAdditionalData().readBlockPos();
                    TileEntity te = player.world.getTileEntity(pos);
                
                    if (te instanceof TileEntityTypecraftBlock)
                    {
                        return new GuiTypecraftBlock(player.inventory, (TileEntityTypecraftBlock) te);
                    }
                }
                return null;
            };
        });*/
        
        //MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void setup(final FMLCommonSetupEvent event)
    {
        DeferredWorkQueue.runLater(PacketHandler::register);
        
        logger.info("Setup method registered");
        PROXY.setup();
    }
    
    private void clientRegistries(final FMLClientSetupEvent event)
    {
        logger.info("clientRegistries method registered");
        PROXY.clientRegistries();
    }
}
