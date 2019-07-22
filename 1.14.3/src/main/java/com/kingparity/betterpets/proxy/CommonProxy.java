package com.kingparity.betterpets.proxy;

import com.kingparity.betterpets.network.message.MessagePetWindow;
import com.kingparity.betterpets.network.message.MessageUpdateFoodStats;
import com.kingparity.betterpets.network.message.MessageUpdateThirstStats;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public void onSetupCommon()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public void onSetupClient() {}
    
    public void openPetWindow(MessagePetWindow pkt) {}
    
    public void syncThirstStats(MessageUpdateThirstStats pkt) {}
    
    public void syncFoodStats(MessageUpdateFoodStats pkt) {}
}
