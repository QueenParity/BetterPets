package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.block.FluidPumpBlock;
import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.BlockEntityUtil;
import com.kingparity.betterpets.util.FluidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidPumpBlockEntity extends FluidPipeBlockEntity
{
    public FluidPumpBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.FLUID_PUMP.get(), pos, state);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, FluidPumpBlockEntity blockEntity)
    {
        if(!level.isClientSide)
        {
            Direction facing = state.getValue(FluidPumpBlock.DIRECTION);
            BlockEntity fluidSender = level.getBlockEntity(pos.relative(facing.getOpposite()));
    
            for(Direction direction : Direction.values())
            {
                if(direction != facing.getOpposite())
                {
                    BlockEntity fluidReceiver = level.getBlockEntity(pos.relative(direction));
                    if(fluidReceiver != null)
                    {
                        IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).orElse(null);
                        if(fluidHandler != null)
                        {
                            FluidUtils.transferFluid(blockEntity.tank, fluidHandler, 50);
                            BlockEntityUtil.sendUpdatePacket(fluidReceiver);
                        }
                    }
                }
            }
    
            if(fluidSender != null)
            {
                IFluidHandler fluidHandler = fluidSender.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).orElse(null);
                if(fluidHandler != null)
                {
                    FluidUtils.transferFluid(fluidHandler, blockEntity.tank, 100);
                    BlockEntityUtil.sendUpdatePacket(fluidSender);
                }
            }
            
            blockEntity.syncFluidToClient();
        }
    }
}
