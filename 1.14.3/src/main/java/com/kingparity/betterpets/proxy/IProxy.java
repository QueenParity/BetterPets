package com.kingparity.betterpets.proxy;

import com.kingparity.betterpets.ThirstStats;
import com.kingparity.betterpets.network.message.MessagePetWindow;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.UUID;

public interface IProxy
{
    public HashMap<UUID, ThirstStats> loadedPlayers = new HashMap<>();
    
    default ThirstStats getClientStats()
    {
        return null;
    }
    
    default ThirstStats getStatsByUUID(UUID uuid)
    {
        ThirstStats stats = loadedPlayers.get(uuid);
        if(stats == null)
        {
            System.out.println("[Better Pets] Error: Attempted to access non-existent player with UUID: " + uuid);
            return null;
        }
        return stats;
    }
    
    default void registerPlayer(PlayerEntity player, ThirstStats stats)
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
