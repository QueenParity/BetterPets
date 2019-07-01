package com.kingparity.betterpets.proxy;

import com.kingparity.betterpets.ThirstStats;
import com.kingparity.betterpets.client.ClientEvents;
import com.kingparity.betterpets.client.KeyBinds;
import com.kingparity.betterpets.client.renderer.entity.BetterWolfRenderer;
import com.kingparity.betterpets.client.renderer.tileentity.WaterCollectorTESR;
import com.kingparity.betterpets.client.renderer.tileentity.WaterFilterTESR;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.gui.container.BetterWolfContainer;
import com.kingparity.betterpets.gui.screen.BetterWolfScreen;
import com.kingparity.betterpets.network.message.MessagePetWindow;
import com.kingparity.betterpets.tileentity.WaterCollectorTileEntity;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.IPetContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy
{
    public ThirstStats clientStats = new ThirstStats();
    
    @Override
    public ThirstStats getClientStats()
    {
        return clientStats;
    }
    
    @Override
    public void clientRegistries()
    {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        
        RenderingRegistry.registerEntityRenderingHandler(BetterWolfEntity.class, BetterWolfRenderer::new);
    
        ClientRegistry.bindTileEntitySpecialRenderer(WaterCollectorTileEntity.class, new WaterCollectorTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(WaterFilterTileEntity.class, new WaterFilterTESR());
        
        KeyBinds.register();
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
