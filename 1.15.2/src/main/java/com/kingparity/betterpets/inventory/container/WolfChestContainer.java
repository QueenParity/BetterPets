package com.kingparity.betterpets.inventory.container;

import com.kingparity.betterpets.core.ModContainers;
import com.kingparity.betterpets.inventory.IWolfChest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class WolfChestContainer extends Container
{
    private final IWolfChest wolfChestInventory;
    private final int numRows;

    public WolfChestContainer(int windowId, IInventory playerInventory, IWolfChest wolfChestInventory, PlayerEntity player)
    {
        super(ModContainers.WOLF_CHEST.get(), windowId);
        this.wolfChestInventory = wolfChestInventory;
        this.numRows = wolfChestInventory.getSizeInventory() / 9;
        wolfChestInventory.openInventory(player);
        int yOffset = (this.numRows - 4) * 18;

        for(int i = 0; i < this.numRows; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                this.addSlot(new Slot(wolfChestInventory.getInventory(), j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 103 + i * 18 + yOffset));
            }
        }

        for(int i = 0; i < 9; i++)
        {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 161 + yOffset));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return this.wolfChestInventory.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(index < this.numRows * 9)
            {
                if(!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false))
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

    @Override
    public void onContainerClosed(PlayerEntity playerIn)
    {
        super.onContainerClosed(playerIn);
        this.wolfChestInventory.closeInventory(playerIn);
    }

    public IInventory getWolfChestInventory()
    {
        return this.wolfChestInventory;
    }
}
