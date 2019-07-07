package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.util.FluidHelper;
import net.minecraft.tileentity.ITickableTileEntity;

public class WaterCollectorTileEntity extends FluidTankTileEntity implements ITickableTileEntity
{
    public WaterCollectorTileEntity()
    {
        super(ModTileEntities.WATER_COLLECTOR, 12000, 0, 500);
    }
    
    @Override
    public void tick()
    {
        if(!world.isRemote)
        {
            if(getFluidAmount() < getCapacity())
            {
                if(world.isRaining())
                {
                    if(world.rand.nextInt(20) == 1)
                    {
                        float temperature = world.getBiome(pos).getTemperature(pos);
                        if(!(temperature < 0.15F))
                        {
                            if(world.isThundering())
                            {
                                temperature += 1.0F;
                            }
                            this.receiveFluid(Math.round(500 * temperature), true);
                        }
                    }
                }
            }
        }
        FluidHelper.giveEnergyAllSides(this, getWorld(), getPos());
    }
}