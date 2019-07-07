package com.kingparity.betterpets.core;

import com.kingparity.betterpets.BetterPetMod;
import com.kingparity.betterpets.item.CanteenItem;
import com.kingparity.betterpets.item.PetFoodItem;
import com.kingparity.betterpets.names.ItemNames;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems
{
    @ObjectHolder(ItemNames.PET_CHEST)
    public static final Item PET_CHEST = null;
    
    @ObjectHolder(ItemNames.PET_BIRTHDAY_HAT)
    public static final Item PET_BIRTHDAY_HAT = null;
    
    @ObjectHolder(ItemNames.PET_FOOD)
    public static final PetFoodItem PET_FOOD = null;
    
    @ObjectHolder(ItemNames.CANTEEN)
    public static final CanteenItem CANTEEN = null;
    
    @ObjectHolder(ItemNames.WATER_FILTER_FABRIC)
    public static final Item WATER_FILTER_FABRIC = null;
    
    @ObjectHolder(ItemNames.BETTER_WOLF_SPAWN_EGG)
    public static final Item BETTER_WOLF_SPAWN_EGG = null;
    
    private static final List<Item> ITEMS = new LinkedList<>();
    
    public static void add(Item item)
    {
        ITEMS.add(item);
    }
    
    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event)
    {
        register(ItemNames.PET_CHEST);
        register(ItemNames.PET_BIRTHDAY_HAT);
        
        registerPetFood(ItemNames.PET_FOOD, new PetFoodItem(new Item.Properties()));
        
        registerCup(ItemNames.CANTEEN, new CanteenItem(new Item.Properties()));
        
        register(ItemNames.WATER_FILTER_FABRIC);
        
        registerSpawnEgg(ItemNames.BETTER_WOLF_SPAWN_EGG, ModEntities.BETTER_WOLF, 14144467, 13545366);
        
        ITEMS.forEach(item -> event.getRegistry().register(item));
    }
    
    private static void register(String name)
    {
        register(name, new Item(new Item.Properties().group(BetterPetMod.GROUP)));
    }
    
    private static void registerCup(String name, CanteenItem item)
    {
        item.setRegistryName(name);
        ITEMS.add(item);
    }
    
    private static void registerSpawnEgg(String name, EntityType entity, int primary, int secondary)
    {
        register(name, new SpawnEggItem(entity, primary, secondary, new Item.Properties().group(BetterPetMod.GROUP)));
    }
    
    private static void registerPetFood(String name, PetFoodItem item)
    {
        item.setRegistryName(name);
        ITEMS.add(item);
    }
    
    private static void register(String name, Item item)
    {
        item.setRegistryName(name);
        ITEMS.add(item);
    }
}
