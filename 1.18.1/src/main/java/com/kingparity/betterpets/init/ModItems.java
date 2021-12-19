package com.kingparity.betterpets.init;

import com.kingparity.betterpets.BetterPets;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BetterPets.ID);
    
    public static final RegistryObject<Item> WATER_FILTER_FABRIC = register("water_filter_fabric", new Item(new Item.Properties().tab(BetterPets.TAB)));
    public static final RegistryObject<Item> PET_CHEST = register("pet_chest", new Item(new Item.Properties().tab(BetterPets.TAB)));
    public static final RegistryObject<BucketItem> FILTERED_WATER_BUCKET = register("filtered_water_bucket", new BucketItem(ModFluids.FILTERED_WATER, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(BetterPets.TAB)));
    
    private static <T extends Item> RegistryObject<T> register(String id, T item)
    {
        return ModItems.ITEMS.register(id, () -> item);
    }
}
