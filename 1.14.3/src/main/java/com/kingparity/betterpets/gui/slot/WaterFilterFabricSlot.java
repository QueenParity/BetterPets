package com.kingparity.betterpets.gui.slot;

import com.kingparity.betterpets.core.ModItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class WaterFilterFabricSlot extends Slot
{
    public WaterFilterFabricSlot(IInventory inventory, int index, int xPosition, int yPosition)
    {
        super(inventory, index, xPosition, yPosition);
    }
    
    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() == ModItems.WATER_FILTER_FABRIC;
    }
}