package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.gui.container.WaterFilterContainer;
import com.kingparity.betterpets.init.BetterPetTileEntities;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class WaterFilterTileEntity extends BetterPetTileEntityBase
{
    public static int slotNum = 3;
    
    public WaterFilterTileEntity()
    {
        super(BetterPetTileEntities.WATER_FILTER_TILE_ENTITY, "water_filter", slotNum);
    }
    
    @Override
    protected Container createMenu(int id, PlayerInventory inventory)
    {
        return new WaterFilterContainer(id, inventory, this, this.pos);
    }
}