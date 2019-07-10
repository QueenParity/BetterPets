package com.kingparity.betterpets.gui.container;

import com.kingparity.betterpets.core.ModContainers;
import com.kingparity.betterpets.gui.slot.BetterPetResultSlot;
import com.kingparity.betterpets.gui.slot.PetResourcesInputSlot;
import com.kingparity.betterpets.tileentity.PetResourcesCrafterTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class PetResourcesCrafterContainer extends Container
{
    private final IInventory inventory;
    private BlockPos pos;
    
    public PetResourcesCrafterContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData)
    {
        this(windowId, playerInventory, new Inventory(PetResourcesCrafterTileEntity.slotNum), extraData.readBlockPos());
    }
    
    public PetResourcesCrafterContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, BlockPos pos)
    {
        super(ModContainers.PET_RESOURCES_CRAFTER, windowId);
        assertInventorySize(inventory, PetResourcesCrafterTileEntity.slotNum);
        this.inventory = inventory;
        this.pos = pos;
        inventory.openInventory(playerInventory.player);
    
        this.addSlot(new BetterPetResultSlot(inventory, 0, 145, 39));
        
        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 2; ++j)
            {
                this.addSlot(new PetResourcesInputSlot(inventory, 1 + j + i * 2, 81 + j * 18, 21 + i * 18));
            }
        }
        
        for(int k = 0; k < 3; ++k)
        {
            for(int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 98 + k * 18));
            }
        }
        
        for(int l = 0; l < 9; ++l)
        {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 156));
        }
    }
    
    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return this.inventory.isUsableByPlayer(playerIn);
    }
    
    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(index < PetResourcesCrafterTileEntity.slotNum)
            {
                if(!this.mergeItemStack(itemstack1, PetResourcesCrafterTileEntity.slotNum, 45, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(itemstack1, 0, PetResourcesCrafterTileEntity.slotNum, false))
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
            
            slot.onTake(playerIn, itemstack1);
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
