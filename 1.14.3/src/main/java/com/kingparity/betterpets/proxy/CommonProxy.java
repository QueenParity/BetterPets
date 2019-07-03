package com.kingparity.betterpets.proxy;

import com.kingparity.betterpets.ThirstStats;
import com.kingparity.betterpets.network.message.MessagePetWindow;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.UUID;

public class CommonProxy
{
    public HashMap<UUID, ThirstStats> loadedPlayers = new HashMap<>();
    
    public ThirstStats getClientStats()
    {
        return null;
    }
    
    public ThirstStats getStatsByUUID(UUID uuid)
    {
        ThirstStats stats = loadedPlayers.get(uuid);
        if(stats == null)
        {
            System.out.println("[Better Pets] Error: Attempted to access non-existent player with UUID: " + uuid);
            return null;
        }
        return stats;
    }
    
    public void registerPlayer(PlayerEntity player, ThirstStats stats)
    {
        UUID uuid = player.getUniqueID();
        if(loadedPlayers.containsKey(uuid))
        {
            //Player already loaded from previous login session where the
            //Server was not closed since the players last login
            return;
        }
        loadedPlayers.put(uuid, stats);
    }
    
    public void onSetupCommon()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public void onSetupClient() {}
    
    public void openPetWindow(MessagePetWindow pkt) {}
}
