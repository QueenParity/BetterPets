package com.william.betterpets.testGui;

import com.william.betterpets.init.BetterPetTileEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class TestGuiTileEntity extends LockableLootTileEntity implements IInventory
{
    protected NonNullList<ItemStack> inventory = NonNullList.withSize(15, ItemStack.EMPTY);
    protected ITextComponent customName;
    public static int slotNum = 15;
    
    public TestGuiTileEntity()
    {
        super(BetterPetTileEntities.TEST_GUI_TILE_ENTITY);
    }
    
    @Override
    public ITextComponent getName()
    {
        return (this.customName != null ? this.customName : this.getDefaultName());
    }
    
    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("container.typecraft_block");
    }
    
    @Override
    public boolean hasCustomName()
    {
        return this.customName != null;
    }
    
    @Override
    public ITextComponent getCustomName()
    {
        return customName;
    }
    
    public void setCustomName(@Nullable ITextComponent customName)
    {
        this.customName = customName;
    }
    
    
    @Override
    public int getSizeInventory()
    {
        return 15;
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
        return (ItemStack) this.inventory.get(index);
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
        
        if (compound.contains("CustomName", 8))
        {
            this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
        }
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        
        ItemStackHelper.saveAllItems(compound, this.inventory);
        
        if (this.hasCustomName())
        {
            compound.putString("CustomName", ITextComponent.Serializer.toJson(customName));
        }
        
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
        return new SUpdateTileEntityPacket(pos, 15, this.write(new CompoundNBT()));
    }
    
    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }
    
    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return inventory;
    }
    
    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn)
    {
        this.inventory = itemsIn;
    }
    
    @Override
    protected Container createMenu(int id, PlayerInventory inventory)
    {
        return new TestGuiContainer(id, inventory, this);
    }
}
