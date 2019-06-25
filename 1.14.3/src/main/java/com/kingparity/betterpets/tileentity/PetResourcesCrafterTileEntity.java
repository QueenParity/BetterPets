package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.gui.container.PetResourcesCrafterContainer;
import com.kingparity.betterpets.init.BetterPetTileEntities;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class PetResourcesCrafterTileEntity extends BetterPetTileEntityBase
{
    public static int slotNum = 7;
    
    public PetResourcesCrafterTileEntity()
    {
        super(BetterPetTileEntities.PET_RESOURCES_CRAFTER_TILE_ENTITY, "pet_resources_crafter", slotNum);
    }
    
    @Override
    protected Container createMenu(int id, PlayerInventory inventory)
    {
        return new PetResourcesCrafterContainer(id, inventory, this, this.pos);
    }
}
