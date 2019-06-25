package com.kingparity.betterpets.client;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.ThirstStats;
import com.kingparity.betterpets.gui.screen.BetterPetPlayerHUDOverlay;
import com.kingparity.betterpets.network.PacketHandler;
import com.kingparity.betterpets.network.message.MessageMovementSpeed;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.*;

public class ClientEvents
{
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderGameOverLayEvent(RenderGameOverlayEvent event)
    {
        BetterPetPlayerHUDOverlay.onRenderGameOverLayEvent(event);
    }
    
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(!event.player.world.isRemote)
        {
            ThirstStats stats = BetterPets.PROXY.getStatsByUUID(event.player.getUniqueID());
            if(stats != null)
            {
                stats.update(event.player);
            }
        }
        else
        {
            PacketHandler.sendToServer(new MessageMovementSpeed(event.player.getUniqueID(), BetterPets.PROXY.getClientStats().getMovementSpeed(event.player)));
        }
    }
    
    @SubscribeEvent
    public void onAttack(AttackEntityEvent attack)
    {
        if(!attack.getEntityPlayer().world.isRemote)
        {
            ThirstStats stats = BetterPets.PROXY.getStatsByUUID(attack.getEntityPlayer().getUniqueID());
            stats.addExhaustion(0.5F);
        }
        attack.setResult(Event.Result.DEFAULT);
    }
    
    @SubscribeEvent
    public void onHurt(LivingHurtEvent hurt)
    {
        if(hurt.getEntity() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity)hurt.getEntity();
            if(!player.world.isRemote)
            {
                ThirstStats stats = BetterPets.PROXY.getStatsByUUID(player.getUniqueID());
                stats.addExhaustion(0.4f);
            }
        }
        hurt.setResult(Event.Result.DEFAULT);
    }
    
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event)
    {
        PlayerEntity player = event.getPlayer();
        if(player != null)
        {
            if(!player.world.isRemote)
            {
                ThirstStats stats = BetterPets.PROXY.getStatsByUUID(player.getUniqueID());
                stats.addExhaustion(0.03f);
            }
        }
        event.setResult(Event.Result.DEFAULT);
    }
    
    public void playedCloned(net.minecraftforge.event.entity.player.PlayerEvent.Clone event)
    {
        if(!event.getEntityPlayer().world.isRemote)
        {
            if(event.isWasDeath())
            {
                ThirstStats stats = BetterPets.PROXY.getStatsByUUID(event.getEntityPlayer().getUniqueID());
                stats.resetStats();
            }
        }
    }
    
    @SubscribeEvent
    public void onLoadPlayerData(PlayerEvent.LoadFromFile event)
    {
        if(!event.getEntityPlayer().world.isRemote)
        {
            PlayerEntity player = event.getEntityPlayer();
            File saveFile = event.getPlayerFile("thirstmod");
            if(!saveFile.exists())
            {
                BetterPets.PROXY.registerPlayer(player, new ThirstStats());
            }
            else
            {
                try
                {
                    FileReader reader = new FileReader(saveFile);
                    ThirstStats stats = BetterPets.gsonInstance.fromJson(reader, ThirstStats.class);
                    if(stats == null)
                    {
                        BetterPets.PROXY.registerPlayer(player, new ThirstStats());
                    }
                    else
                    {
                        BetterPets.PROXY.registerPlayer(player, stats);
                    }
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onSavePlayerData(PlayerEvent.SaveToFile event)
    {
        if(!event.getEntityPlayer().world.isRemote)
        {
            ThirstStats stats = BetterPets.PROXY.getStatsByUUID(event.getEntityPlayer().getUniqueID());
            File saveFile = new File(event.getPlayerDirectory(), event.getPlayerUUID() + ".thirstmod");
            try
            {
                String write = BetterPets.gsonInstance.toJson(stats);
                saveFile.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
                writer.write(write);
                writer.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
