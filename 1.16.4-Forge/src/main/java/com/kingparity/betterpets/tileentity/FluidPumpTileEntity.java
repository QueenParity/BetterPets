package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.block.FluidPumpBlock;
import com.kingparity.betterpets.init.ModTileEntities;
import com.kingparity.betterpets.util.FluidUtils;
import com.kingparity.betterpets.util.TileEntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidPumpTileEntity extends FluidPipeTileEntity
{
    public FluidPumpTileEntity()
    {
        super(ModTileEntities.FLUID_PUMP.get());
    }
    
    @Override
    public void tick()
    {
        if(!this.world.isRemote)
        {
            BlockState state = this.world.getBlockState(this.pos);
            Direction facing = state.get(FluidPumpBlock.DIRECTION);
            TileEntity fluidSender = this.world.getTileEntity(this.pos.offset(facing.getOpposite()));
            
            for(Direction direction : Direction.values())
            {
                if(direction != facing.getOpposite())
                {
                    TileEntity fluidReceiver = this.world.getTileEntity(this.pos.offset(direction));
                    if(fluidReceiver != null)
                    {
                        IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).orElse(null);
                        if(fluidHandler != null)
                        {
                            FluidUtils.transferFluid(this.tank, fluidHandler, 50);
                            TileEntityUtil.sendUpdatePacket(fluidReceiver);
                        }
                    }
                }
            }
            
            if(fluidSender != null)
            {
                IFluidHandler fluidHandler = fluidSender.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).orElse(null);
                if(fluidHandler != null)
                {
                    FluidUtils.transferFluid(fluidHandler, this.tank, 100);
                    TileEntityUtil.sendUpdatePacket(fluidSender);
                }
            }
            
            /*TileEntity fluidReceiver = this.world.getTileEntity(this.pos.offset(facing));
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