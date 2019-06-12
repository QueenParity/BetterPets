package com.william.betterpets.init;

import com.william.betterpets.util.Reference;
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

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterPetItems
{
    private static final List<Item> ITEMS = new LinkedList<>();
    
    public static Item.Properties tabMisc = new Item.Properties().group(ItemGroup.MISC);
    
    public static Item PET_CHEST, PET_BIRTHDAY_HAT;
    
    public static Item BETTER_WOLF_SPAWN_EGG;
    
    public static List<Item> getItems()
    {
        return Collections.unmodifiableList(ITEMS);
    }
    
    @SubscribeEvent
    public static void addItems(final RegistryEvent.Register<Item> event)
    {
        PET_CHEST = register("pet_chest", tabMisc);
        PET_BIRTHDAY_HAT = register("pet_birthday_hat", tabMisc);
        
        BETTER_WOLF_SPAWN_EGG = registerSpawnEgg("better_wolf_spawn_egg", BetterPetEntities.BETTER_WOLF, 14144467, 13545366, tabMisc);
        
        ITEMS.stream().forEach(item -> event.getRegistry().register(item));
    }
    
    private static Item register(String name, Item.Properties builder)
    {
        return register(name, new Item(builder));
    }
    
    private static Item registerSpawnEgg(String name, EntityType entity, int primary, int secondary, Item.Properties builder)
    {
        return register(name, new SpawnEggItem(entity, primary, secondary, builder));
    }
    
    private static Item register(String name, Item item)
    {
        item.setRegistryName(new ResourceLocation(Reference.ID, name));
        ITEMS.add(item);
        return item;
    }
    
    /*@Mod.EventBusSubscriber(modid = Reference.ID, value = Side.CLIENT)
    public static class Models
    {
        @SubscribeEvent
        public static void register(ModelRegistryEvent event)
        {
            ITEMS.forEach(item -> ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.ID + ":" + item.getUnlocalizedName().substring(5), "inventory")));
        }
    }*/
}
