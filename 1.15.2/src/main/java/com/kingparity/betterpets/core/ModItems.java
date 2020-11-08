package com.kingparity.betterpets.core;

import com.kingparity.betterpets.BetterPetMod;
import com.kingparity.betterpets.names.ItemNames;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Reference.ID);

    public static final RegistryObject<Item> PET_CHEST = register(ItemNames.PET_CHEST);
    public static final RegistryObject<Item> PET_BIRTHDAY_HAT = register(ItemNames.PET_BIRTHDAY_HAT);

    /*@ObjectHolder(ItemNames.PET_FOOD)
    public static final PetFoodItem PET_FOOD = null;

    @ObjectHolder(ItemNames.CANTEEN)
    public static final CanteenItem CANTEEN = null;*/

    public static final RegistryObject<Item> WATER_FILTER_FABRIC = register(ItemNames.WATER_FILTER_FABRIC);

    //public static final RegistryObject<Item> BETTER_WOLF_SPAWN_EGG = null;

    /*@ObjectHolder(ItemNames.OAK_WATER_FILTER)
    public static final Item OAK_WATER_FILTER = null;*/

    //public static final RegistryObject<Item> DRINK_BOTTLE = null;

    public static final RegistryObject<BucketItem> FILTERED_WATER_BUCKET = register(ItemNames.FILTERED_WATER_BUCKET, new BucketItem(ModFluids.FILTERED_WATER, (new Item.Properties()).containerItem(Items.BUCKET).maxStackSize(1).group(BetterPetMod.GROUP)));

    private static RegistryObject<Item> register(String name)
    {
        return register(name, new Item(new Item.Properties().group(BetterPetMod.GROUP)));
    }

    private static <T extends Item> RegistryObject<T> register(String name, T item)
    {
        return ModItems.ITEMS.register(name, () -> item);
    }

    /*private static void register(String name)
    {
        register(name, new Item(new Item.Properties().group(BetterPetMod.GROUP)));
    }*/

    /*private static void registerCup(String name, CanteenItem item)
    {
        item.setRegistryName(name);
        ITEMS.add(item);
    }*/

    /*private static void registerSpawnEgg(String name, EntityType entity, int primary, int secondary)
    {
        register(name, new SpawnEggItem(entity, primary, secondary, new Item.Properties().group(BetterPetMod.GROUP)));
    }*/

    /*private static void registerPetFood(String name, PetFoodItem item)
    {
        item.setRegistryName(name);
        ITEMS.add(item);
    }*/

    /*private static void register(String name, Item item)
    {
        item.setRegistryName(name);
        ITEMS.add(item);
    }*/
}