package com.william.betterpets.proxy;

import com.william.betterpets.network.message.MessageStorageWindow;

public interface IProxy
{
    default void setup()
    {
    }
    
    default void clientRegistries()
    {
    }
    
    default void openStorageWindow(MessageStorageWindow pkt)
    {
    }
    
    boolean isSinglePlayer();
    
    boolean isDedicatedServer();
}
