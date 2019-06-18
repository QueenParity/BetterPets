package com.kingparity.betterpets.proxy;

import com.kingparity.betterpets.client.renderer.BetterWolfRenderer;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.gui.container.BetterWolfContainer;
import com.kingparity.betterpets.gui.screen.BetterWolfScreen;
import com.kingparity.betterpets.network.message.MessagePetWindow;
import com.kingparity.betterpets.util.IPetContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy
{
    @Override
    public void clientRegistries()
    {
        MinecraftForge.EVENT_BUS.register(this);
        
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
    public void openPetWindow(MessagePetWindow pkt)
    {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        World world = player.getEntityWorld();
        Entity entity = world.getEntityByID(pkt.getEntityId());
        if(entity instanceof IPetContainer && entity instanceof BetterWolfEntity)
        {
            IPetContainer wrapper = (IPetContainer)entity;
            BetterWolfEntity betterWolf = (BetterWolfEntity)entity;
            BetterWolfContainer petContainer = new BetterWolfContainer(pkt.getWindowId(), player.inventory, wrapper.getInventory(), betterWolf);
            Minecraft.getInstance().displayGuiScreen(new BetterWolfScreen(petContainer, player.inventory, betterWolf.getDisplayName()));
        }
    }
}
