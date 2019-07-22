package com.kingparity.betterpets.gui.container;

import com.kingparity.betterpets.core.ModContainers;
import com.kingparity.betterpets.gui.slot.WaterFilterFabricSlot;
import com.kingparity.betterpets.gui.slot.WaterFilterFuelSlot;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class WaterFilterContainer extends Container
{
    private final IInventory inventory;
    private BlockPos pos;
    
    public WaterFilterContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData)
    {
        this(windowId, playerInventory, new Inventory(WaterFilterTileEntity.inventorySize), extraData.readBlockPos());
    }
    
    public WaterFilterContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, BlockPos pos)
    {
        super(ModContainers.WATER_FILTER, windowId);
        assertInventorySize(inventory, WaterFilterTileEntity.inventorySize);
        this.inventory = inventory;
        this.pos = pos;
        inventory.openInventory(playerInventory.player);
    
        this.addSlot(new WaterFilterFabricSlot(inventory, 0, 92, 39));
        this.addSlot(new WaterFilterFuelSlot(inventory, 1, 29, 52));
        
        for(int k = 0; k < 3; ++k)
        {
            for(int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 102 + k * 18 + -9));
            }
        }
    
        for(int l = 0; l < 9; ++l)
        {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 151));
        }
    }
    
    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(PlayerEntity player)
    {
        return this.inventory.isUsableByPlayer(player);
    }
    
    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(index < WaterFilterTileEntity.inventorySize)
            {
                if(!this.mergeItemStack(itemstack1, WaterFilterTileEntity.inventorySize, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(itemstack1, 0, WaterFilterTileEntity.inventorySize, false))
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
            
            if(itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, itemstack1);
        }
        
        return itemstack;
    }
    
    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(PlayerEntity playerIn)
    {
        super.onContainerClosed(playerIn);
        this.inventory.closeInventory(playerIn);
    }
    
    public BlockPos getPos()
    {
        return pos;
    }
}
