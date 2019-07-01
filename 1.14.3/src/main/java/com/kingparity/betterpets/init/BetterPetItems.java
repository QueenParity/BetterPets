package com.kingparity.betterpets.init;

import com.kingparity.betterpets.item.CanteenItem;
import com.kingparity.betterpets.item.PetFoodItem;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterPetItems
{
    public static Item PET_CHEST, PET_BIRTHDAY_HAT;
    
    public static PetFoodItem PET_FOOD;
    
    public static CanteenItem CANTEEN;
    
    public static Item WATER_FILTER_FABRIC;
    
    public static Item BETTER_WOLF_SPAWN_EGG;
    
    public static Item FLUID_TUBE, FLUID_TUBE_CONNECTING;
    
    public static Item.Properties tabMisc = new Item.Properties().group(ItemGroup.MISC);
    
    private static final List<Item> ITEMS = new LinkedList<>();
    
    public static void add(Item item)
    {
        ITEMS.add(item);
    }
    
    public static List<Item> getItems()
    {
        return Collections.unmodifiableList(ITEMS);
    }
    
    static
    {
        PET_CHEST = register("pet_chest", tabMisc);
        PET_BIRTHDAY_HAT = register("pet_birthday_hat", tabMisc);
        
        PET_FOOD = registerPetFood("pet_food", new PetFoodItem(new Item.Properties()));
    
        CANTEEN = registerCup("canteen", new CanteenItem(new Item.Properties()));
    
        WATER_FILTER_FABRIC = register("water_filter_fabric", tabMisc);
    
        BETTER_WOLF_SPAWN_EGG = registerSpawnEgg("better_wolf_spawn_egg", BetterPetEntities.BETTER_WOLF, 14144467, 13545366, tabMisc);
        
        FLUID_TUBE = register("fluid_tube", tabMisc);
        FLUID_TUBE_CONNECTING = register("fluid_tube_connecting", new Item.Properties());
    }
    
    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event)
    {
        ITEMS.forEach(item -> event.getRegistry().register(item));
    }
    
    private static Item register(String name, Item.Properties builder)
    {
        return register(name, new Item(builder));
    }
    
    private static CanteenItem registerCup(String name, CanteenItem item)
    {
        item.setRegistryName(new ResourceLocation(Reference.ID, name));
        ITEMS.add(item);
        return item;
    }
    
    private static Item registerSpawnEgg(String name, EntityType entity, int primary, int secondary, Item.Properties builder)
    {
        return register(name, new SpawnEggItem(entity, primary, secondary, builder));
    }
    
    private static PetFoodItem registerPetFood(String name, PetFoodItem item)
    {
        item.setRegistryName(new ResourceLocation(Reference.ID, name));
        ITEMS.add(item);
        return item;
    }
    
    private static Item register(String name, Item item)
    {
        item.setRegistryName(new ResourceLocation(Reference.ID, name));
        ITEMS.add(item);
        return item;
    }
}
