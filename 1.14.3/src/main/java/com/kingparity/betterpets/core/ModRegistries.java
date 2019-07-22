package com.kingparity.betterpets.core;

import com.kingparity.betterpets.drink.Drink;
import com.kingparity.betterpets.names.RegistryNames;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistries
{
    public static IForgeRegistry<Drink> DRINKS;
    
    @SubscribeEvent
    public static void registerRegistries(final RegistryEvent.NewRegistry event)
    {
        DRINKS = makeRegistry(RegistryNames.DRINKS, Drink.class).create();
    }
    
    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(String name, Class<T> type)
    {
        return new RegistryBuilder<T>().setName(new ResourceLocation(name)).setType(type);
    }
}