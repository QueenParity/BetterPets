package com.kingparity.betterpets.util;

import net.minecraft.item.ItemStack;

public interface IAttachableChest extends IPetContainer
{
    boolean hasChest();
    
    void attachChest(ItemStack stack);
    
    void removeChest();
}
