package com.kingparity.betterpets.inventory.container;

import com.kingparity.betterpets.init.ModContainers;
import com.kingparity.betterpets.inventory.container.slot.FuelSlot;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public class WaterFilterContainer extends Container
{
    private int fluidLevel;
    
    private WaterFilterTileEntity waterFilter;
    
    public WaterFilterContainer(int windowId, IInventory playerInventory, WaterFilterTileEntity waterFilter)
    {
        super(ModContainers.WATER_FILTER.get(), windowId);
        this.waterFilter = waterFilter;
        
        this.addSlot(new FuelSlot(waterFilter, 0, 29, 54));
        
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
    public boolean canInteractWith(PlayerEntity player)
    {
        return true;
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
    
        if(slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
        
            if(index == 0)
            {
                if(!this.mergeItemStack(slotStack, 1, 37, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if(ForgeHooks.getBurnTime(slotStack) > 0)
                {
                    if(!this.mergeItemStack(slotStack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index < 28)
                {
                    if(!this.mergeItemStack(slotStack, 28, 37, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index < 37 && !this.mergeItemStack(slotStack, 1, 28, false))
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
        
            slot.onTake(player, slotStack);
        }
    
        return stack;
    }
}