package com.kingparity.betterpets.proxy;

import com.kingparity.betterpets.network.message.MessageUpdateFoodStats;
import com.kingparity.betterpets.network.message.MessageUpdateThirstStats;

public interface Proxy
{
    default void setupClient() {}
    
    default void syncThirstStats(MessageUpdateThirstStats message) {}
    
    default void syncFoodStats(MessageUpdateFoodStats message) {}
}
