package com.kingparity.betterpets.inventory;

import com.kingparity.betterpets.inventory.container.WolfChestContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public interface IWolfChest extends IInventory
{
    boolean hasChest();
    
    void attachChest(ItemStack stack);

    void removeChest();
    
    WolfChestInventory getInventory();
    
    @Override
    default int getSizeInventory()
    {
        return this.getInventory().getSizeInventory();
    }
    
    @Override
    default boolean isEmpty()
    {
        return this.getInventory().isEmpty();
    }
    
    @Override
    default ItemStack getStackInSlot(int index)
    {
        return this.getInventory().getStackInSlot(index);
    }
    
    @Override
    default ItemStack decrStackSize(int index, int count)
    {
        return this.getInventory().decrStackSize(index, count);
    }
    
    @Override
    default ItemStack removeStackFromSlot(int index)
    {
        return this.getInventory().removeStackFromSlot(index);
    }
    
    @Override
    default void setInventorySlotContents(int index, ItemStack stack)
    {
        this.getInventory().setInventorySlotContents(index, stack);
    }
    
    @Override
    default int getInventoryStackLimit()
    {
        return this.getInventory().getInventoryStackLimit();
    }
    
    @Override
    default void markDirty()
    {
        this.getInventory().markDirty();
    }
    
    @Override
    default boolean isUsableByPlayer(PlayerEntity player)
    {
        return this.getInventory().isUsableByPlayer(player);
    }
    
    @Override
    default void openInventory(PlayerEntity player)
    {
        this.getInventory().openInventory(player);
    }
    
    @Override
    default void closeInventory(PlayerEntity player)
    {
        this.getInventory().openInventory(player);
    }
    
    @Override
    default boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return getInventory().isItemValidForSlot(index, stack);
    }
    
    @Override
    default void clear()
    {
        getInventory().clear();
    }
    
    ITextComponent getWolfChestName();
    
    default INamedContainerProvider getWolfChestContainerProvider()
    {
        return new SimpleNamedContainerProvider((windowId, playerInventory, playerEntity) -> new WolfChestContainer(windowId, playerInventory, this, playerEntity), this.getWolfChestName());
    }
}