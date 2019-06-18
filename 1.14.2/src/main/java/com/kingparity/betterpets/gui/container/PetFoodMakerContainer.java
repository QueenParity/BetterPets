package com.kingparity.betterpets.gui.container;

import com.kingparity.betterpets.gui.slot.PetFoodMakerInputSlot;
import com.kingparity.betterpets.gui.slot.PetFoodMakerResultSlot;
import com.kingparity.betterpets.init.BetterPetContainerTypes;
import com.kingparity.betterpets.tileentity.PetFoodMakerTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class PetFoodMakerContainer extends Container
{
    private final IInventory inventory;
    private BlockPos pos;
    
    public PetFoodMakerContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData)
    {
        this(windowId, playerInventory, new Inventory(PetFoodMakerTileEntity.slotNum), extraData.readBlockPos());
    }
    
    public PetFoodMakerContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, BlockPos pos)
    {
        super(BetterPetContainerTypes.PET_FOOD_MAKER_CONTAINER, windowId);
        assertInventorySize(inventory, PetFoodMakerTileEntity.slotNum);
        this.inventory = inventory;
        this.pos = pos;
        inventory.openInventory(playerInventory.player);
    
        this.addSlot(new PetFoodMakerResultSlot(inventory, 0, 145, 39));
        
        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 2; ++j)
            {
                this.addSlot(new PetFoodMakerInputSlot(inventory, 1 + j + i * 2, 81 + j * 18, 21 + i * 18));
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
            if(index < PetFoodMakerTileEntity.slotNum)
            {
                if(!this.mergeItemStack(itemstack1, PetFoodMakerTileEntity.slotNum, 45, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(itemstack1, 0, PetFoodMakerTileEntity.slotNum, false))
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
