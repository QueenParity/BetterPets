package com.kingparity.betterpets.core;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.names.ItemNames;
import com.kingparity.betterpets.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems implements ModInitializer
{
    public static Item WATER_FILTER_FABRIC;
    public static Item WATER_FILTER_FABRIC_DISPLAY;
    
    private static Item register(String name)
    {
        return register(name, new Item(new FabricItemSettings().group(BetterPets.GROUP)));
    }
    
    private static <T extends Item> T register(String name, T item)
    {
        return Registry.register(Registry.ITEM, new Identifier(Reference.ID, name), item);
    }
    
    @Override
    public void onInitialize()
    {
        WATER_FILTER_FABRIC = register(ItemNames.WATER_FILTER_FABRIC);
        WATER_FILTER_FABRIC_DISPLAY = register(ItemNames.WATER_FILTER_FABRIC_DISPLAY);
    }
}