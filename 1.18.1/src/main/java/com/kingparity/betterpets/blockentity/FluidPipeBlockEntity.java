package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.FluidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidPipeBlockEntity extends FluidHandlerSyncedBlockEntity
{
    public FluidPipeBlockEntity(BlockPos pos, BlockState state)
    {
        this(ModBlockEntities.FLUID_PIPE.get(), pos, state);
    }
    
    public FluidPipeBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state)
    {
        super(blockEntityType, pos, state, FluidAttributes.BUCKET_VOLUME / 5);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, FluidPipeBlockEntity blockEntity)
    {
        if(!level.isClientSide)
        {
            blockEntity.syncFluidToClient();
            if(blockEntity.getFluidLevel() > 0)
            {
                for(Direction direction : Direction.values())
                {
                    BlockEntity fluidReceiver = level.getBlockEntity(pos.relative(direction));
                    if(fluidReceiver != null)
                    {
                        IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).orElse(null);
                        if(fluidHandler != null)
                        {
                            int transferAmount = 1;
                            boolean doTransfer = true;
                            if(fluidReceiver instanceof FluidPipeBlockEntity)
                            {
                                FluidPipeBlockEntity fluidPipe = (FluidPipeBlockEntity)fluidReceiver;
                                if(fluidPipe.getFluidLevel() >= blockEntity.getFluidLevel())
                                {
                                    doTransfer = false;
                                }
                                if(blockEntity.getFluidLevel() - fluidPipe.getFluidLevel() > 2)
                                {
                                    transferAmount = 2;
                                }
                                if(fluidPipe.getFluidLevel() < Mth.clamp(175, 0, blockEntity.getFluidLevel() / 2))
                                {
                                    transferAmount = 50;
                                }
                            }
                            if(fluidReceiver instanceof WaterCollectorBlockEntity || fluidReceiver instanceof WaterFilterBlockEntity)
                            {
                                transferAmount = 50;
                            }
                            if(doTransfer)
                            {
                                FluidUtils.transferFluid(blockEntity.tank, fluidHandler, transferAmount);
                            }
                        }
                    }
                }
            }
        }
    }
}
