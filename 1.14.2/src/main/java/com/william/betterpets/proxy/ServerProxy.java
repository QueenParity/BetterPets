package com.william.betterpets.proxy;

public class ServerProxy implements IProxy
{
    @Override
    public boolean isSinglePlayer()
    {
        return false;
    }
    
    @Override
    public boolean isDedicatedServer()
    {
        return true;
    }
}
