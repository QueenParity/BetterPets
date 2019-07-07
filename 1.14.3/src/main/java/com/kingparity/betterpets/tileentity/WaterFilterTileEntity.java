package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.util.FluidHelper;
import net.minecraft.tileentity.ITickableTileEntity;

public class WaterFilterTileEntity extends FluidTankTileEntity implements ITickableTileEntity
{
    public WaterFilterTileEntity()
    {
        super(ModTileEntities.WATER_FILTER, 12000, 500, 0);
    }
    
    @Override
    public void tick()
    {
        FluidHelper.retractEnergyAllSides(this, getWorld(), getPos());
    }
}