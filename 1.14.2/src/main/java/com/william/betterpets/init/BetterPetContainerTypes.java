package com.william.betterpets.init;

import com.william.betterpets.testGui.TestGuiContainer;
import com.william.betterpets.testGui.TestGuiScreen;
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
    
    public static ContainerType<TestGuiContainer> TEST_GUI_CONTAINER;
    
    public static List<ContainerType<?>> getContainerTypes()
    {
        return Collections.unmodifiableList(CONTAINER_TYPES);
    }
    
    @SubscribeEvent
    public static void addContainerTypes(final RegistryEvent.Register<ContainerType<?>> event)
    {
        TEST_GUI_CONTAINER = register("test_gui_container", TestGuiContainer::new);
    
        CONTAINER_TYPES.forEach(container_type -> event.getRegistry().register(container_type));
    }
    
    public static void bindScreens(FMLClientSetupEvent event)
    {
        bindScreen(TEST_GUI_CONTAINER, TestGuiScreen::new);
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