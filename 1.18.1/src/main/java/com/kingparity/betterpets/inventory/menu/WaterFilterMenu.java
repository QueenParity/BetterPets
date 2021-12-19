package com.kingparity.betterpets.inventory.menu;

import com.kingparity.betterpets.blockentity.WaterFilterBlockEntity;
import com.kingparity.betterpets.init.ModMenus;
import com.kingparity.betterpets.inventory.menu.slot.FuelSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;

public class WaterFilterMenu extends AbstractContainerMenu
{
    private int fluidLevel;
    
    private WaterFilterBlockEntity waterFilter;
    
    public WaterFilterMenu(int windowId, Inventory playerInventory, WaterFilterBlockEntity waterFilter)
    {
        super(ModMenus.WATER_FILTER.get(), windowId);
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
        
        this.addDataSlots(waterFilter.getWaterFilterData());
    }
    
    public WaterFilterBlockEntity getWaterFilter()
    {
        return waterFilter;
    }
    
    @Override
    public boolean stillValid(Player player)
    {
        return true;
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        if(slot != null && slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            
            if(index == 0)
            {
                if(!this.moveItemStackTo(slotStack, 1, 37, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if(ForgeHooks.getBurnTime(slotStack, RecipeType.SMELTING) > 0)
                {
                    if(!this.moveItemStackTo(slotStack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index < 28)
                {
                    if(!this.moveItemStackTo(slotStack, 28, 37, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index < 37 && !this.moveItemStackTo(slotStack, 1, 28, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            
            if(slotStack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
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