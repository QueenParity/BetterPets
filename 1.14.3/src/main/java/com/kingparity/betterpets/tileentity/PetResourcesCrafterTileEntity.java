package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.gui.container.PetResourcesCrafterContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class PetResourcesCrafterTileEntity extends BasicLootTileEntity
{
    public static final int inventorySize = 7;
    
    public PetResourcesCrafterTileEntity()
    {
        super(ModTileEntities.PET_RESOURCES_CRAFTER, "pet_resources_crafter");
    }
    
    @Override
    public int getSizeInventory()
    {
        return inventorySize;
    }
    
    @Override
    protected Container createMenu(int id, PlayerInventory inventory)
    {
        return new PetResourcesCrafterContainer(id, inventory, this, this.pos);
    }
}
