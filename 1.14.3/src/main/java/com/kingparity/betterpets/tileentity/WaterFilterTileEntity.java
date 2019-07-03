package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.core.ModTileEntities;
import net.minecraft.tileentity.ITickableTileEntity;

public class WaterFilterTileEntity extends FluidTankTileEntity implements ITickableTileEntity
{
    public WaterFilterTileEntity()
    {
        super(ModTileEntities.WATER_FILTER);
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
                            this.addFluid(Math.round(500 * temperature));
                        }
                    }
                }
            }
        }
    }
}