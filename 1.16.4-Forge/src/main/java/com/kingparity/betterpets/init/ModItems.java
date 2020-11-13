package com.kingparity.betterpets.init;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.ID);
    
    public static final RegistryObject<Item> WATER_FILTER_FABRIC = register("water_filter_fabric", new Item(new Item.Properties().group(BetterPets.TAB)));
    
    private static <T extends Item> RegistryObject<T> register(String id, T item)
    {
        return ModItems.ITEMS.register(id, () -> item);
    }
}
