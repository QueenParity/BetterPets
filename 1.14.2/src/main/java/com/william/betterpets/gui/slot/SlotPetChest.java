package com.william.betterpets.gui.slot;

import com.william.betterpets.entity.BetterWolfEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class SlotPetChest extends Slot
{
    private BetterWolfEntity betterWolf;
    
    public SlotPetChest(BetterWolfEntity betterWolf, IInventory inventory, int index, int xPosition, int yPosition)
    {
        super(inventory, index, xPosition, yPosition);
        this.betterWolf = betterWolf;
    }
    
    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return betterWolf.hasChest();
    }
    
    @Override
    public boolean isEnabled()
    {
        return betterWolf.hasChest();
    }
}
