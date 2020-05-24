package com.kingparity.betterpets;

import com.kingparity.betterpets.core.ModLootFunctions;
import com.kingparity.betterpets.proxy.ClientProxy;
import com.kingparity.betterpets.proxy.CommonProxy;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.item.ItemGroup;
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
    }

    private void onCommonSetup(final FMLCommonSetupEvent event)
    {
        PROXY.onSetupCommon();
        LOGGER.debug("onCommonSetup method registered");

        ModLootFunctions.register();
    }

    private void onClientSetup(final FMLClientSetupEvent event)
    {
        PROXY.onSetupClient();
        LOGGER.debug("onClientSetup method registered");
    }
}