package com.kingparity.betterpets.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class WolfChestInventory extends Inventory implements INamedContainerProvider
{
    private IWolfChest wrapper;

    public WolfChestInventory(IWolfChest wrapper, int size)
    {
        super(size);
        this.wrapper = wrapper;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.wrapper.getWolfChestName();
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return this.wrapper.getWolfChestContainerProvider().createMenu(windowId, playerInventory, playerEntity);
    }

    public CompoundNBT write()
    {
        CompoundNBT compound = new CompoundNBT();
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
        compound.put("Inventory", tagList);
        return compound;
    }

    public void read(CompoundNBT tagCompound)
    {
        if(tagCompound.contains("Inventory", Constants.NBT.TAG_LIST))
        {
            this.clear();
            ListNBT tagList = tagCompound.getList("Inventory", Constants.NBT.TAG_COMPOUND);
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
}
