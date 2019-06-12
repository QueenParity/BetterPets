package com.william.betterpets.gui.slot;

import com.william.betterpets.util.StorageInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class SlotStorage extends Slot
{
    private StorageInventory storageInventory;
    
    public SlotStorage(StorageInventory storageInventory, int index, int xPosition, int yPosition)
    {
        super(storageInventory, index, xPosition, yPosition);
        this.storageInventory = storageInventory;
    }
    
    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return storageInventory.isStorageItem(stack);
    }
}
