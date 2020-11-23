package com.kingparity.betterpets.inventory;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class BetterWolfInventory extends Inventory implements INamedContainerProvider
{
    private BetterWolfEntity betterWolf;
    
    public BetterWolfInventory(BetterWolfEntity betterWolf, int size)
    {
        super(size);
        this.betterWolf = betterWolf;
    }
    
    public boolean isPetChestItem(ItemStack stack)
    {
        return this.betterWolf.isPetChestItem(stack);
    }
    
    @Override
    public ITextComponent getDisplayName()
    {
        return this.betterWolf.getPetChestName();
    }
    
    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return this.betterWolf.getPetChestContainerProvider(this.betterWolf).createMenu(windowId, playerInventory, playerEntity);
    }
    
    @Override
    public ListNBT write()
    {
        ListNBT tagList = new ListNBT();
        for(int i = 0; i < this.getSizeInventory(); i++)
        {
            ItemStack stack = this.getStackInSlot(i);
            if(!stack.isEmpty())
            {
                CompoundNBT slotTag = new CompoundNBT();
                slotTag.putByte("Slot", (byte) i);
                stack.write(slotTag);
                tagList.add(slotTag);
            }
        }
        return tagList;
    }
    
    @Override
    public void read(ListNBT tagList)
    {
        this.clear();
        for(int i = 0; i < tagList.size(); i++)
        {
            CompoundNBT slotTag = tagList.getCompound(i);
            byte slot = slotTag.getByte("Slot");
            if(slot >= 0 && slot < this.getSizeInventory())
            {
                this.setInventorySlotContents(slot, ItemStack.read(slotTag));
            }
        }
    }
}
