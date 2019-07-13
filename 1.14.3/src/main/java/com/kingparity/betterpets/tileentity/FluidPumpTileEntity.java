package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.block.FluidPumpBlock;
import com.kingparity.betterpets.core.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;

public class FluidPumpTileEntity extends TickableFluidHolderTileEntity
{
    public FluidPumpTileEntity()
    {
        super(ModTileEntities.FLUID_PUMP, 1000, 500, 500);
    }
    
    @Override
    public void fluidTick()
    {
        BlockState state = getWorld().getBlockState(getPos());
        if(state.getBlock() instanceof FluidPumpBlock)
        {
            TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(state.get(FluidPumpBlock.FACING).getOpposite()));
            if(tileEntity instanceof WaterCollectorTileEntity)
            {
                WaterCollectorTileEntity waterCollector = (WaterCollectorTileEntity)tileEntity;
                this.receiveFluid(waterCollector.extractFluid(Math.min(this.getMaxReceive(), this.getCapacity() - this.getFluidAmount()), true), true);
            }
            else if(tileEntity instanceof WaterFilterTileEntity)
            {
                WaterFilterTileEntity waterFilter = (WaterFilterTileEntity)tileEntity;
                this.receiveFluid(waterFilter.extractFluid(Math.min(this.getMaxReceive(), this.getCapacity() - this.getFluidAmount()), true, 0), true);
            }
            
            TileEntity tileEntityFront = getWorld().getTileEntity(getPos().offset(state.get(FluidPumpBlock.FACING)));
            FluidHolderTileEntity holder = tileEntityFront instanceof FluidHolderTileEntity ? (FluidHolderTileEntity)tileEntityFront : null;
            if(holder == null)
            {
                return;
            }
        
            if(tileEntityFront instanceof FluidPipeTileEntity && ((FluidPipeTileEntity)tileEntityFront).getFluidAmount() > this.getFluidAmount())
            {
                return;
            }
            else if(tileEntityFront instanceof FluidPumpTileEntity && ((FluidPumpTileEntity)tileEntityFront).getFluidAmount() > this.getFluidAmount())
            {
                return;
            }
            
            this.extractFluid(holder.receiveFluid(Math.min(this.getMaxExtract(), this.getFluidAmount()), true), true);
            syncToClient();
        }
        else
        {
            String out = state.getBlock().getRegistryName() != null ? state.getBlock().getRegistryName().toString() : "air";
            System.out.println(out);
        }
    }
}