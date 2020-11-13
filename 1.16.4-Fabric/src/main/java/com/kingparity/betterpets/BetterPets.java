package com.kingparity.betterpets;

import com.kingparity.betterpets.core.ModItems;
import com.kingparity.betterpets.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class BetterPets implements ModInitializer
{
    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(
        new Identifier(Reference.ID, "betterpet_group"),
        () -> new ItemStack(ModItems.WATER_FILTER_FABRIC)
    );
    
    @Override
    public void onInitialize()
    {
        
    }
}