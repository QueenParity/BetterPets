package com.kingparity.betterpets.inventory.container;

import com.kingparity.betterpets.core.ModContainers;
import com.kingparity.betterpets.inventory.container.slot.FuelSlot;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public class WaterFilterContainer extends Container
{
    private WaterFilterTileEntity waterFilter;

    public WaterFilterContainer(int windowId, PlayerInventory playerInventory, WaterFilterTileEntity waterFilter)
    {
        super(ModContainers.WATER_FILTER, windowId);
        this.waterFilter = waterFilter;

        this.addSlot(new FuelSlot(waterFilter, 0, 9, 50));
        this.addSlot(new Slot(waterFilter, 1, 92, 41));

        for(int x = 0; x < 3; x++)
        {
            for(int y = 0; y < 9; y++)
            {
                this.addSlot(new Slot(playerInventory, y + x * 9 + 9, 8 + y * 18, 98 + x * 18));
            }
        }

        for(int x = 0; x < 9; x++)
        {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 156));
        }

        this.trackIntArray(waterFilter.getWaterFilterData());
    }

    public WaterFilterTileEntity getWaterFilter()
    {
        return waterFilter;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

            if(index == 0 || index == 1)
            {
                if(!this.mergeItemStack(slotStack, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if(this.waterFilter.isItemValidForSlot(1, slotStack))
                {
                    if(!this.mergeItemStack(slotStack, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(ForgeHooks.getBurnTime(slotStack) > 0)
                {
                    if(!this.mergeItemStack(slotStack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index < 29)
                {
                    if(!this.mergeItemStack(slotStack, 29, 38, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index < 38 && !this.mergeItemStack(slotStack, 2, 29, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if(slotStack.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if(slotStack.getCount() == stack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }

        return stack;
    }
}