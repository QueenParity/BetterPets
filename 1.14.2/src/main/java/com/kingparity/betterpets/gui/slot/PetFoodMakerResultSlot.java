package com.kingparity.betterpets.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class PetFoodMakerResultSlot extends Slot
{
    public PetFoodMakerResultSlot(IInventory inventory, int index, int xPosition, int yPosition)
    {
        super(inventory, index, xPosition, yPosition);
    }
    
    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }
}
