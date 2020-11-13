package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.init.ModTileEntities;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class WaterCollectorTileEntity extends TileFluidHandlerSynced implements ITickableTileEntity
{
    
    
    public WaterCollectorTileEntity()
    {
        super(ModTileEntities.WATER_COLLECTOR.get(), 14000);
    }
    
    @Override
    public void tick()
    {
        if(!this.world.isRemote)
        {
            if(this.tank.getFluidAmount() < this.tank.getCapacity())
            {
                if(this.world.isRaining())
                {
                    if(this.world.rand.nextInt(200) == 1)
                    {
                        float temperature = this.world.getBiome(this.pos).getTemperature(this.pos);
                        if(!(temperature < 0.15F))
                        {
                            if(this.world.isThundering())
                            {
                                temperature += 1.0F;
                            }
                            this.tank.fill(new FluidStack(Fluids.WATER, Math.round(500 * temperature)), IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
        }
    }
}