package com.kingparity.betterpets;

import com.kingparity.betterpets.core.ModBlocks;
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
        () -> new ItemStack(ModBlocks.WATER_COLLECTOR.asItem())
    );
    
    @Override
    public void onInitialize()
    {
        
    }
}