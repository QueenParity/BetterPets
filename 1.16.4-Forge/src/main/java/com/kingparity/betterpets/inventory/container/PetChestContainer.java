package com.kingparity.betterpets.inventory.container;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.init.ModContainers;
import com.kingparity.betterpets.inventory.IPetChest;
import com.kingparity.betterpets.inventory.container.slot.PetChestSlot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class PetChestContainer extends Container
{
    private final BetterWolfEntity betterWolf;
    private final IPetChest petChestInventory;
    private final int numRows;
    
    public PetChestContainer(int windowId, PlayerInventory playerInventory, IPetChest petChestInventory, final BetterWolfEntity betterWolf)
    {
        super(ModContainers.BETTER_WOLF.get(), windowId);
        this.petChestInventory = petChestInventory;
        this.betterWolf = betterWolf;
        this.numRows = petChestInventory.getSizeInventory() / 5;
        petChestInventory.openInventory(playerInventory.player);
        int yOffset = (this.numRows - 4) * 18;
        
        for(int i = 0; i < this.numRows; i++)
        {
            for(int j = 0; j < 5; j++)
            {
                this.addSlot(new PetChestSlot(betterWolf, petChestInventory.getInventory(), j + i * 5, 80 + j * 18, 18 + i * 18));
            }
        }
        
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18 + yOffset));
            }
        }
        
        for(int i = 0; i < 9; i++)
        {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 161 + yOffset));
        }
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity player)
    {
        if(this.petChestInventory instanceof Entity)
        {
            Entity entity = (Entity) this.petChestInventory;
            if(!entity.isAlive())
            {
                return false;
            }
        }
        return this.petChestInventory.isUsableByPlayer(player);
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
    
        if(slot != null && slot.getHasStack())
        {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
        
            if(index < this.numRows * 5)
            {
                if(!this.mergeItemStack(stack1, this.numRows * 5, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(stack1, 0, this.numRows * 5, false))
            {
                return ItemStack.EMPTY;
            }
        
            if(stack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
    
        return stack;
    }
    
    @Override
    public void onContainerClosed(PlayerEntity player)
    {
        super.onContainerClosed(player);
        this.petChestInventory.closeInventory(player);
    }
    
    public IInventory getPetChestInventory()
    {
        return this.petChestInventory;
    }
    
    public BetterWolfEntity getBetterWolf()
    {
        return betterWolf;
    }
}
