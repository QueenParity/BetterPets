package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.init.ModTileEntities;
import com.kingparity.betterpets.util.FluidUtils;
import com.kingparity.betterpets.util.TileEntityUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidPipeTileEntity extends TileFluidHandlerSynced implements ITickableTileEntity
{
    public FluidPipeTileEntity()
    {
        this(ModTileEntities.FLUID_PIPE.get());
    }
    
    public FluidPipeTileEntity(TileEntityType<?> tileEntityType)
    {
        super(tileEntityType, 200);
    }
    
    @Override
    public void tick()
    {
        if(!this.world.isRemote)
        {
            if(this.getFluidLevel() > 0)
            {
                for(Direction direction : Direction.values())
                {
                    TileEntity fluidReceiver = this.world.getTileEntity(this.pos.offset(direction));
                    if(fluidReceiver != null)
                    {
                        IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).orElse(null);
                        if(fluidHandler != null)
                        {
                            int transferAmount = 1;
                            boolean doTransfer = true;
                            if(fluidReceiver instanceof FluidPipeTileEntity)
                            {
                                FluidPipeTileEntity fluidPipe = (FluidPipeTileEntity)fluidReceiver;
                                if(fluidPipe.getFluidLevel() >= this.getFluidLevel())
                                {
                                    doTransfer = false;
                                }
                                if(this.getFluidLevel() - fluidPipe.getFluidLevel() > 2)
                                {
                                    transferAmount = 2;
                                }
                                if(fluidPipe.getFluidLevel() < MathHelper.clamp(175, 0, this.getFluidLevel() / 2))
                                {
                                    transferAmount = 50;
                                }
                            }
                            if(fluidReceiver instanceof WaterCollectorTileEntity || fluidReceiver instanceof WaterFilterTileEntity)
                            {
                                transferAmount = 50;
                            }
                            if(doTransfer)
                            {
                                FluidUtils.transferFluid(this.tank, fluidHandler, transferAmount);
                                TileEntityUtil.sendUpdatePacket(fluidReceiver);
                            }
                        }
                    }
                }
            }
            /*BlockState state = this.world.getBlockState(this.pos);
            Direction facing = state.get(FluidPumpBlock.DIRECTION);
            
            TileEntity fluidReceiver = this.world.getTileEntity(this.pos.offset(facing));
            if(fluidReceiver != null)
            {
                IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).orElse(null);
                if(fluidHandler != null)
                {
                    FluidUtils.transferFluid(this.tank, fluidHandler, 100);
                    TileEntityUtil.sendUpdatePacket(fluidReceiver);
                }
            }*/
        }
    }
}
