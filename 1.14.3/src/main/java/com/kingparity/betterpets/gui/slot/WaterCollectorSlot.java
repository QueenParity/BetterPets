package com.kingparity.betterpets.gui.slot;

import com.kingparity.betterpets.item.CanteenItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class WaterCollectorSlot extends Slot
{
    public WaterCollectorSlot(IInventory inventory, int index, int xPosition, int yPosition)
    {
        super(inventory, index, xPosition, yPosition);
    }
    
    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() instanceof CanteenItem;
    }
}