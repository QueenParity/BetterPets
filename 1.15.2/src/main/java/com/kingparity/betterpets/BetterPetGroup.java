package com.kingparity.betterpets;

import com.kingparity.betterpets.core.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BetterPetGroup extends ItemGroup
{
    public BetterPetGroup(String label)
    {
        super(label);
    }
    
    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ModBlocks.WATER_COLLECTOR_OAK);
    }
}