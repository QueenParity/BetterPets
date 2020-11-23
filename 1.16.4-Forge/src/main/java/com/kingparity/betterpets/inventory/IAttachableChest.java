package com.kingparity.betterpets.inventory;

import net.minecraft.item.ItemStack;

public interface IAttachableChest extends IPetChest
{
    boolean hasChest();
    
    void attachChest(ItemStack stack);
    
    void removeChest();
}