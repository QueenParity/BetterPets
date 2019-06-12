package com.william.betterpets.proxy;

import com.william.betterpets.client.BetterWolfRenderer;
import com.william.betterpets.entity.BetterWolfEntity;
import com.william.betterpets.gui.StorageScreen;
import com.william.betterpets.gui.container.StorageContainer;
import com.william.betterpets.network.message.MessageStorageWindow;
import com.william.betterpets.util.IStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientProxy extends ServerProxy
{
    @Override
    public void clientRegistries()
    {
        //MinecraftForge.EVENT_BUS.register(this);
        
        RenderingRegistry.registerEntityRenderingHandler(BetterWolfEntity.class, BetterWolfRenderer::new);
    }
    
    @Override
    public boolean isSinglePlayer()
    {
        return Minecraft.getInstance().isSingleplayer();
    }
    
    @Override
    public boolean isDedicatedServer()
    {
        return false;
    }
    
    @Override
    public void openStorageWindow(MessageStorageWindow pkt)
    {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        World world = player.getEntityWorld();
        Entity entity = world.getEntityByID(pkt.getEntityId());
        if(entity instanceof IStorage && entity instanceof BetterWolfEntity)
        {
            IStorage wrapper = (IStorage)entity;
            BetterWolfEntity betterWolf = (BetterWolfEntity)entity;
            StorageContainer storageContainer = new StorageContainer(pkt.getWindowId(), player.inventory, wrapper.getInventory(), betterWolf);
            Minecraft.getInstance().displayGuiScreen(new StorageScreen(storageContainer, player.inventory, betterWolf));
        }
    }
}
