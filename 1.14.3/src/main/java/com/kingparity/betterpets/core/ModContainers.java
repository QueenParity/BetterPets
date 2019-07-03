package com.kingparity.betterpets.core;

import com.kingparity.betterpets.gui.container.BetterWolfContainer;
import com.kingparity.betterpets.gui.container.PetResourcesCrafterContainer;
import com.kingparity.betterpets.gui.container.WaterFilterContainer;
import com.kingparity.betterpets.gui.screen.BetterWolfScreen;
import com.kingparity.betterpets.gui.screen.PetResourcesCrafterScreen;
import com.kingparity.betterpets.gui.screen.WaterFilterScreen;
import com.kingparity.betterpets.util.ContainerNames;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers
{
    @ObjectHolder(ContainerNames.PET_RESOURCES_CRAFTER)
    public static final ContainerType<PetResourcesCrafterContainer> PET_RESOURCES_CRAFTER = null;
    
    @ObjectHolder(ContainerNames.WATER_FILTER)
    public static final ContainerType<WaterFilterContainer> WATER_FILTER = null;
    
    @ObjectHolder(ContainerNames.BETTER_WOLF)
    public static final ContainerType<BetterWolfContainer> BETTER_WOLF = null;
    
    private static final List<ContainerType<?>> CONTAINER_TYPES = new LinkedList<>();
    
    @SubscribeEvent
    public static void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> event)
    {
        register(ContainerNames.PET_RESOURCES_CRAFTER, PetResourcesCrafterContainer::new);
        register(ContainerNames.WATER_FILTER, WaterFilterContainer::new);
        register(ContainerNames.BETTER_WOLF, BetterWolfContainer::new);
        
        CONTAINER_TYPES.forEach(container_type -> event.getRegistry().register(container_type));
    }
    
    public static void bindScreens(FMLClientSetupEvent event)
    {
        bindScreen(PET_RESOURCES_CRAFTER, PetResourcesCrafterScreen::new);
        bindScreen(WATER_FILTER, WaterFilterScreen::new);
        bindScreen(BETTER_WOLF, BetterWolfScreen::new);
    }
    
    private static <T extends Container> void register(String name, IContainerFactory<T> container)
    {
        ContainerType<T> type = IForgeContainerType.create(container);
        type.setRegistryName(name);
        CONTAINER_TYPES.add(type);
    }
    
    private static <M extends Container, U extends Screen & IHasContainer<M>> void bindScreen(ContainerType<M> container, ScreenManager.IScreenFactory<M, U> screen)
    {
        ScreenManager.registerFactory(container, screen);
    }
}