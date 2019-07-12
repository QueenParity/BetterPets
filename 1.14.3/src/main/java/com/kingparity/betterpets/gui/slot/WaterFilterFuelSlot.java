package com.kingparity.betterpets.gui.slot;

import com.kingparity.betterpets.gui.container.WaterFilterContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;

public class WaterFilterFuelSlot extends Slot
{
    public WaterFilterFuelSlot(IInventory inventory, int index, int xPosition, int yPosition)
    {
        super(inventory, index, xPosition, yPosition);
    }
    
    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack)
    {
        return AbstractFurnaceTileEntity.isFuel(stack) || isBucket(stack);
    }
    
    public int getItemStackLimit(ItemStack stack)
    {
        return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }
    
    public static boolean isBucket(ItemStack stack)
    {
        return stack.getItem() == Items.BUCKET;
    }
}