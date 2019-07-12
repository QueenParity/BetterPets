package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.core.ModTileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class FluidPipeTileEntity extends TickableFluidHolderTileEntity
{
    public FluidPipeTileEntity()
    {
        super(ModTileEntities.FLUID_PIPE, 1000, 500, 500);
    }
    
    @Override
    public void fluidTick()
    {
        if(this.getFluidAmount() > 0)
        {
            for(Direction direction : Direction.values())
            {
                if(this.getFluidAmount() < 0)
                {
                    break;
                }
                TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(direction));
                if(tileEntity == null)
                {
                    continue;
                }
                
                if(tileEntity instanceof FluidHolderTileEntity)
                {
                    FluidHolderTileEntity holder = (FluidHolderTileEntity)tileEntity;
    
                    if(tileEntity instanceof FluidPipeTileEntity && ((FluidPipeTileEntity)tileEntity).getFluidAmount() > this.getFluidAmount())
                    {
                        continue;
                    }
                    else if(tileEntity instanceof FluidPumpTileEntity && ((FluidPumpTileEntity)tileEntity).getFluidAmount() > this.getFluidAmount())
                    {
                        continue;
                    }
    
                    this.extractFluid(holder.receiveFluid(Math.min(this.getMaxExtract(), this.getFluidAmount()), true), true);
                }
                else if(tileEntity instanceof WaterFilterTileEntity)
                {
                    WaterFilterTileEntity filter = (WaterFilterTileEntity)tileEntity;
                    
                    if(((WaterFilterTileEntity)tileEntity).getFluidAmount(1) > this.getFluidAmount())
                    {
                        continue;
                    }
                    
                    this.extractFluid(filter.receiveFluid(Math.min(this.getMaxExtract(), this.getFluidAmount()), true, 1), true);
                }
            }
        }
    }
}