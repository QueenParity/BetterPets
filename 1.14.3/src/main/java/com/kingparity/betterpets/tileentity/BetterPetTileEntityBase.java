package com.kingparity.betterpets.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public abstract class BetterPetTileEntityBase extends LockableTileEntity implements IInventory
{
    private final String ID;
    protected NonNullList<ItemStack> inventory;
    
    public BetterPetTileEntityBase(TileEntityType<?> tileEntityType, String id, int inventorySize)
    {
        super(tileEntityType);
        this.ID = id;
        this.inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    }
    
    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("container." + ID);
    }
    
    @Override
    public int getSizeInventory()
    {
        return inventory.size();
    }
    
    @Override
    public boolean isEmpty()
    {
        for(ItemStack itemstack : this.inventory)
        {
            if(!itemstack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.inventory.get(index);
    }
    
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventory, index, count);
        
        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }
        
        return itemstack;
    }
    
    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }
    
    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        this.inventory.set(index, stack);
        
        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
        
        this.markDirty();
    }
    
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }
    
    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        if(this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return !(player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
        }
    }
    
    @Override
    public void openInventory(PlayerEntity player) {}
    
    @Override
    public void closeInventory(PlayerEntity player) {}
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }
    
    @Override
    public void clear()
    {
        this.inventory.clear();
    }
    
    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        
        ItemStackHelper.loadAllItems(compound, this.inventory);
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        
        ItemStackHelper.saveAllItems(compound, this.inventory);
        
        return compound;
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        this.read(pkt.getNbtCompound());
    }
    
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(pos, inventory.size(), this.write(new CompoundNBT()));
    }
    
    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }
}
