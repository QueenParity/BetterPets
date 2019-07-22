package com.kingparity.betterpets.core;

import com.kingparity.betterpets.drink.Drink;
import com.kingparity.betterpets.names.DrinkNames;
import com.kingparity.betterpets.util.Reference;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDrinks
{
    @ObjectHolder(DrinkNames.EMPTY)
    public static final Drink EMPTY = null;
    
    @ObjectHolder(DrinkNames.FRESH_WATER)
    public static final Drink FRESH_WATER = null;
    
    @ObjectHolder(DrinkNames.MILK)
    public static final Drink MILK = null;
    
    @ObjectHolder(DrinkNames.CHOCOLATE_MILK)
    public static final Drink CHOCOLATE_MILK = null;
    
    private static final List<Drink> DRINKS = new LinkedList<>();
    
    @SubscribeEvent
    public static void registerDrinks(final RegistryEvent.Register<Drink> event)
    {
        register(DrinkNames.EMPTY, new Drink(0, 0.0F, 0x385DC6));
        register(DrinkNames.FRESH_WATER, new Drink(7, 2.0F, 0x11DEF5));
        register(DrinkNames.MILK, new Drink(5, 1.8F, 0xF0E8DF));
        register(DrinkNames.CHOCOLATE_MILK, new Drink(7, 2.0F, 0x6E440D));
    
        DRINKS.forEach(drink -> event.getRegistry().register(drink));
    }
    
    private static void register(String name, Drink drink)
    {
        drink.setRegistryName(name);
        DRINKS.add(drink);
    }
}