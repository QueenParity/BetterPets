package com.william.betterpets.gui.container;

import com.william.betterpets.entity.BetterWolfEntity;
import com.william.betterpets.init.BetterPetItems;
import com.william.betterpets.util.StorageInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class StorageContainer extends Container
{
    private StorageInventory wolfInventory;
    private BetterWolfEntity theWolf;
    
    public StorageContainer(int windowId, PlayerInventory playerInventory, final StorageInventory wolfInventory, final BetterWolfEntity theWolf)
    {
        super(null, windowId);
        this.wolfInventory = wolfInventory;
        this.theWolf = theWolf;
        
        wolfInventory.openInventory(playerInventory.player);
        
        this.inventorySlots.clear();
        this.addSlot(new Slot(wolfInventory, 0, 6, 18)
        {
            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }
            
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return theWolf.isHat(stack);
            }
        });
        
        this.addSlot(new Slot(wolfInventory, 1, 6, 36)
        {
            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }
            
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() == BetterPetItems.PET_CHEST;
            }
        });
        
        int x;
        int y;
        
        if(theWolf.hasChest())
        {
            for(y = 0; y < 3; ++y)
            {//row
                for(x = 0; x < 5; ++x)
                {//col
                    this.addSlot(new Slot(wolfInventory, 2 + x + y * 5, 80 + x * 18, 18 + y * 18));
                }
            }
        }
        
        for(y = 0; y < 3; ++y)
        {
            for(x = 0; x < 9; ++x)
            {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 102 + y * 18 + -18));
            }
        }
        
        for(x = 0; x < 9; ++x)
        {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }
    
    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(PlayerEntity player)
    {
        return this.wolfInventory.isUsableByPlayer(player) && this.theWolf.isAlive() && this.theWolf.getDistance(player) < 8;
    }
    
    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(index < this.wolfInventory.getSizeInventory())
            {
                if(!this.mergeItemStack(itemstack1, this.wolfInventory.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack())
            {
                if(!this.mergeItemStack(itemstack1, 1, 2, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.getSlot(0).isItemValid(itemstack1))
            {
                if (!this.mergeItemStack(itemstack1, 0, 1, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(this.wolfInventory.getSizeInventory() <= 2 || !this.mergeItemStack(itemstack1, 2, this.wolfInventory.getSizeInventory(), false))
            {
                return ItemStack.EMPTY;
            }
            
            if(itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        
        return itemstack;
    }
    
    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(PlayerEntity playerIn)
    {
        super.onContainerClosed(playerIn);
        this.wolfInventory.closeInventory(playerIn);
    }
}
