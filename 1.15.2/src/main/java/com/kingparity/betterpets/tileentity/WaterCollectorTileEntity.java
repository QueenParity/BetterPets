package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.core.ModTileEntities;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class WaterCollectorTileEntity extends TileFluidHandlerSynced implements ITickableTileEntity
{
    public WaterCollectorTileEntity()
    {
        super(ModTileEntities.WATER_COLLECTOR.get(), 12000, 500, 500);
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
                            this.tank.fill(new FluidStack(Fluids.WATER.getFluid(), Math.round(500 * temperature)), IFluidHandler.FluidAction.EXECUTE);
                            //this.receiveFluid(Math.round(500 * temperature), true, false);
                        }
                    }
                }
            }
        }
    }
}