package com.william.betterpets.gui.container;

import com.william.betterpets.entity.BetterWolfEntity;
import com.william.betterpets.gui.slot.SlotPetChest;
import com.william.betterpets.init.BetterPetItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import javax.annotation.Nonnull;

/**
 * Author: MrCrayfish
 */
public class StorageContainer extends Container
{
    //region Fields
    
    private IInventory wolfInventory;
    private BetterWolfEntity theWolf;
    
    //endregion Fields
    
    //region Constructors
    
    /**
     * Creates a new wolf inventory container.
     *
     * @param playerInventory The player's  inventory
     * @param wolfInventory The wolf's inventory
     * @param theWolf The wolf
     */
    /*protected Container(@Nullable ContainerType<?> type, int id) {
        this.containerType = type;
        this.windowId = id;
    }*/
    public StorageContainer(int windowId, PlayerInventory playerInventory, IInventory wolfInventory, final BetterWolfEntity theWolf)
    {
        super((ContainerType<?>)null, windowId);
        this.wolfInventory = wolfInventory;
        this.theWolf = theWolf;
        
        wolfInventory.openInventory(playerInventory.player);
        
        //this.inventorySlots.clear();
        this.addSlot(new Slot(wolfInventory, 0, 6, 18)
        {
            
            /**
             * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1
             * in the case of armor slots)
             */
            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }
            
            /**
             * Returns whether or not an item is valid
             * @param stack The item stack
             * @return True if the item is valid in a slot and if the item is a wolf armor item
             */
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack)
            {
                //return theWolf.isArmor(stack);
                return theWolf.isHat(stack);
            }
            
            @Override
            public void onSlotChanged()
            {
                super.onSlotChanged();
                
                ItemStack stack = this.getStack();
                if(!theWolf.getEntityWorld().isRemote)
                {
                    //ItemWolfArmor wolfArmor = (ItemWolfArmor) stack.getItem();
                    //SoundEvent sound = wolfArmor.getMaterial().getEquipSound();
                    SoundEvent sound = SoundEvents.ITEM_ARMOR_EQUIP_GOLD;
                    theWolf.playSound(sound, 1, (theWolf.getRNG().nextFloat() - theWolf.getRNG().nextFloat()) * 0.2F + 1);
                }
            }
        });
        
        this.addSlot(new Slot(wolfInventory, 1, 6, 36)
        {
            
            /**
             * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1
             * in the case of armor slots)
             */
            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }
            
            /**
             * Returns whether or not an item is valid
             * @param stack The item stack
             * @return True if the item is valid in a slot and if the item is a wolf armor item
             */
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack)
            {
                return stack.getItem() == BetterPetItems.PET_CHEST;
            }
            
            @Override
            public void onSlotChanged()
            {
                super.onSlotChanged();
            }
        });
        
        //IWolfArmorCapability wolfArmor = theWolf.getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);
        
        int x;
        int y;
        
        /*if (/*wolfArmor != null && wolfArmor.getHasChest()*//*theWolf.hasChest()) {
            for (y = 0; y < 3; y++) {//row
                for (x = 0; x < 5; x++) {//col
                    this.addSlotToContainer(new SlotPetChest(theWolf, wolfInventory, 2 + x + y * 5, 80 + x * 18, 18 + y * 18));
                }
            }
        }*/
        
        for(y = 0; y < 3; y++)
        {//row
            for(x = 0; x < 5; x++)
            {//col
                this.addSlot(new SlotPetChest(theWolf, wolfInventory, 2 + x + y * 5, 80 + x * 18, 18 + y * 18));
            }
        }
        
        for(y = 0; y < 3; y++)
        {
            for(x = 0; x < 9; x++)
            {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
        
        for(x = 0; x < 9; x++)
        {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }
    
    //endregion Constructors
    
    //region Public / Protected Methods
    
    /**
     * Transfers an item stack.
     *
     * @param player The player entity.
     * @param index The index of the slot the player is interacting with.
     *
     * @return The item stack
     */
    @Override
    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(index < this.wolfInventory.getSizeInventory()) {
                if(!this.mergeItemStack(itemstack1, this.wolfInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if(this.getSlot(0).isItemValid(itemstack1) && !this.getSlot(0).getHasStack()) {
                if(!this.mergeItemStack(itemstack1, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if(this.wolfInventory.getSizeInventory() <= 1 || !this.mergeItemStack(itemstack1, 1, this.wolfInventory.getSizeInventory(), false)) {
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
        }
        
        return itemstack;
    }
    
    /**
     * Gets whether or not the container can be interacted with/
     *
     * @param player The player attempting to interact with this container
     *
     * @return <tt>true</tt> if the interaction can be initiated, <tt>false</tt> if not
     */
    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player)
    {
        return this.wolfInventory.isUsableByPlayer(player) && this.theWolf.isAlive() && this.theWolf.getDistance(player) < 8;
    }
    
    
    
    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(PlayerEntity playerIn)
    {
        super.onContainerClosed(playerIn);
        this.wolfInventory.closeInventory(playerIn);
    }
}
