package com.kingparity.betterpets;

import com.kingparity.betterpets.client.ClientHandler;
import com.kingparity.betterpets.init.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BetterPets.ID)
public class BetterPets
{
    public static final String ID = "betterpets";
    public static final CreativeModeTab TAB = new CreativeModeTab("tabBetterPets")
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(Blocks.ACACIA_DOOR/*ModBlocks.WATER_COLLECTOR.get()*/);
        }
    };
    
    public BetterPets()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModEntities.ENTITY_TYPES.register(eventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(eventBus);
        ModMenus.MENU_TYPES.register(eventBus);
        ModFluids.FLUIDS.register(eventBus);
        eventBus.addListener(this::onCommonSetup);
        eventBus.addListener(this::onClientSetup);
    }
    
    private void onCommonSetup(final FMLCommonSetupEvent event)
    {
    
    }
    
    private void onClientSetup(final FMLClientSetupEvent event)
    {
        ClientHandler.setup();
    }
}