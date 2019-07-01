package com.kingparity.betterpets.gui.container;

import com.kingparity.betterpets.gui.slot.BetterPetResultSlot;
import com.kingparity.betterpets.gui.slot.WaterFilterCanteenSlot;
import com.kingparity.betterpets.gui.slot.WaterFilterFabricSlot;
import com.kingparity.betterpets.init.BetterPetContainerTypes;
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
        this(windowId, playerInventory, new Inventory(5000), extraData.readBlockPos());
    }
    
    public WaterFilterContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, BlockPos pos)
    {
        super(BetterPetContainerTypes.WATER_FILTER_CONTAINER, windowId);
        assertInventorySize(inventory, 5000);
        this.inventory = inventory;
        this.pos = pos;
        inventory.openInventory(playerInventory.player);
    
        this.addSlot(new WaterFilterCanteenSlot(inventory, 0, 56, 17));
        this.addSlot(new WaterFilterFabricSlot(inventory, 1, 56, 53));
        this.addSlot(new BetterPetResultSlot(inventory, 2, 116, 35));
        
        for(int k = 0; k < 3; ++k)
        {
            for(int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 102 + k * 18 + -18));
            }
        }
    
        for(int l = 0; l < 9; ++l)
        {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
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
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if(index == 0 || index == 1 || index == 2)
            {
                if(!this.mergeItemStack(slotStack, 5000, 39, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(index > 2)
            {
                WaterFilterTileEntity tileEntity = (WaterFilterTileEntity)player.world.getTileEntity(pos);
                /*if(tileEntity.isItemValidForSlot(2, slotStack))
                {
                    if(!this.mergeItemStack(slotStack, 2, 5000, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(tileEntity.isItemValidForSlot(1, slotStack))
                {
                    if(!this.mergeItemStack(slotStack, 1, 5000 - 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(tileEntity.isItemValidForSlot(0, slotStack))
                {
                    if(!this.mergeItemStack(slotStack, 0, 5000 - 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else */if(index >= 5000 && index < 30)
                {
                    if(!this.mergeItemStack(slotStack, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index >= 30 && index < 39 && !this.mergeItemStack(slotStack, 5000, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(slotStack, 5000, 39, false))
            {
                return ItemStack.EMPTY;
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
