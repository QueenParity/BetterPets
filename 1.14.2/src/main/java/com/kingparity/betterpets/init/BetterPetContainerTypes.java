package com.kingparity.betterpets.init;

import com.kingparity.betterpets.gui.container.BetterWolfContainer;
import com.kingparity.betterpets.gui.container.PetFoodMakerContainer;
import com.kingparity.betterpets.gui.screen.BetterWolfScreen;
import com.kingparity.betterpets.gui.screen.PetFoodMakerScreen;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterPetContainerTypes
{
    private static final List<ContainerType<?>> CONTAINER_TYPES = new ArrayList<>();
    
    public static ContainerType<PetFoodMakerContainer> PET_FOOD_MAKER_CONTAINER;
    public static ContainerType<BetterWolfContainer> BETTER_WOLF_CONTAINER;
    
    public static List<ContainerType<?>> getContainerTypes()
    {
        return Collections.unmodifiableList(CONTAINER_TYPES);
    }
    
    @SubscribeEvent
    public static void addContainerTypes(final RegistryEvent.Register<ContainerType<?>> event)
    {
        PET_FOOD_MAKER_CONTAINER = register("pet_food_maker_container", PetFoodMakerContainer::new);
        BETTER_WOLF_CONTAINER = register("better_wolf_container", BetterWolfContainer::new);
        
        CONTAINER_TYPES.forEach(container_type -> event.getRegistry().register(container_type));
    }
    
    public static void bindScreens(FMLClientSetupEvent event)
    {
        bindScreen(PET_FOOD_MAKER_CONTAINER, PetFoodMakerScreen::new);
        bindScreen(BETTER_WOLF_CONTAINER, BetterWolfScreen::new);
    }
    
    private static <T extends Container> ContainerType<T> register(String name, IContainerFactory<T> container)
    {
        ContainerType<T> type = IForgeContainerType.create(container);
        type.setRegistryName(name);
        CONTAINER_TYPES.add(type);
        return type;
    }
    
    private static <M extends Container, U extends Screen & IHasContainer<M>> void bindScreen(ContainerType<M> container, ScreenManager.IScreenFactory<M, U> screen)
    {
        ScreenManager.registerFactory(container, screen);
    }
}