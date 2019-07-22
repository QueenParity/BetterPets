package com.kingparity.betterpets.proxy;

import com.kingparity.betterpets.client.renderer.entity.BetterWolfRenderer;
import com.kingparity.betterpets.client.renderer.tileentity.FluidPipeTESR;
import com.kingparity.betterpets.client.renderer.tileentity.FluidPumpTESR;
import com.kingparity.betterpets.client.renderer.tileentity.WaterCollectorTESR;
import com.kingparity.betterpets.client.renderer.tileentity.WaterFilterTESR;
import com.kingparity.betterpets.core.ModItems;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.gui.container.BetterWolfContainer;
import com.kingparity.betterpets.gui.screen.BetterWolfScreen;
import com.kingparity.betterpets.item.DrinkBottleItem;
import com.kingparity.betterpets.network.message.MessagePetWindow;
import com.kingparity.betterpets.network.message.MessageUpdateFoodStats;
import com.kingparity.betterpets.network.message.MessageUpdateThirstStats;
import com.kingparity.betterpets.stats.food.PetFoodStats;
import com.kingparity.betterpets.stats.thirst.PetThirstStats;
import com.kingparity.betterpets.tileentity.FluidPipeTileEntity;
import com.kingparity.betterpets.tileentity.FluidPumpTileEntity;
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

public class ClientProxy extends CommonProxy
{
    @Override
    public void onSetupClient()
    {
        MinecraftForge.EVENT_BUS.register(this);
        
        RenderingRegistry.registerEntityRenderingHandler(BetterWolfEntity.class, BetterWolfRenderer::new);
        
        ClientRegistry.bindTileEntitySpecialRenderer(WaterCollectorTileEntity.class, new WaterCollectorTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(WaterFilterTileEntity.class, new WaterFilterTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(FluidPipeTileEntity.class, new FluidPipeTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(FluidPumpTileEntity.class, new FluidPumpTESR());
        
        Minecraft.getInstance().getItemColors().register(new DrinkBottleItem.ColorHandler(), ModItems.DRINK_BOTTLE);
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
    
    @Override
    public void syncThirstStats(MessageUpdateThirstStats pkt)
    {
        ClientPlayerEntity playerEntity = Minecraft.getInstance().player;
        Entity entity = playerEntity.world.getEntityByID(pkt.getEntityId());
        if(entity instanceof BetterWolfEntity)
        {
            BetterWolfEntity betterWolf = (BetterWolfEntity)entity;
            PetThirstStats stats = betterWolf.getPetThirstStats();
            stats.thirstLevel = pkt.getThirstLevel();
            stats.thirstSaturationLevel = pkt.getSaturation();
            stats.thirstExhaustionLevel = pkt.getExhaustion();
            stats.poisoned = pkt.isPoisoned();
        }
        else
        {
            System.out.println(entity != null ? entity.getPosition() : "uhthirst");
        }
    }
    
    @Override
    public void syncFoodStats(MessageUpdateFoodStats pkt)
    {
        ClientPlayerEntity playerEntity = Minecraft.getInstance().player;
        Entity entity = playerEntity.world.getEntityByID(pkt.getEntityId());
        if(entity instanceof BetterWolfEntity)
        {
            BetterWolfEntity betterWolf = (BetterWolfEntity)entity;
            PetFoodStats stats = betterWolf.getPetFoodStats();
            stats.foodLevel = pkt.getFoodLevel();
            stats.foodSaturationLevel = pkt.getSaturation();
            stats.foodExhaustionLevel = pkt.getExhaustion();
        }
        else
        {
            System.out.println(entity != null ? entity.getPosition() : "uhfood");
        }
    }
}
