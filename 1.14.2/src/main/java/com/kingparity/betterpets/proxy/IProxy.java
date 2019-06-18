package com.kingparity.betterpets.proxy;

import com.kingparity.betterpets.network.message.MessagePetWindow;

public interface IProxy
{
    default void setup()
    {
    }
    
    default void clientRegistries()
    {
    }
    
    default void openPetWindow(MessagePetWindow pkt)
    {
    }
    
    boolean isSinglePlayer();
    
    boolean isDedicatedServer();
}
