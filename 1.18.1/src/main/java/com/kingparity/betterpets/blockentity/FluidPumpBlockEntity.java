package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.block.FluidPumpBlock;
import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.BlockEntityUtil;
import com.kingparity.betterpets.util.FluidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
    
    public static void updateLinks(LevelAccessor level, BlockPos pos, BlockState state, FluidPipeBlockEntity blockEntity)
    {
        for(Direction direction : Direction.values())
        {
            Direction facing = state.getValue(FluidPumpBlock.DIRECTION);
            blockEntity.links[direction.get3DDataValue()] = 0;
            if(direction != facing.getOpposite())
            {
                BlockEntity fluidReceiver = level.getBlockEntity(pos.relative(direction));
                if(fluidReceiver != null)
                {
                    IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).orElse(null);
                    if(fluidHandler != null)
                    {
                        blockEntity.links[direction.get3DDataValue()] = 1;
                    }
                }
                if(blockEntity.links[direction.get3DDataValue()] == 0)
                {
                    blockEntity.tankSides[direction.get3DDataValue()].drain(500, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, FluidPumpBlockEntity blockEntity)
    {
        if(!level.isClientSide)
        {
            updateLinks(level, pos, state, blockEntity);
            blockEntity.syncToClient();
    
            Direction facing = state.getValue(FluidPumpBlock.DIRECTION);
            BlockEntity fluidSender = level.getBlockEntity(pos.relative(facing.getOpposite()));
            
            if(blockEntity.getFluidLevel() > 0)
            {
                int remaining = 500;
                if(blockEntity.getLinkBoolean(0))
                {
                    remaining = remaining - FluidUtils.transferFluid(blockEntity.tankCenter, blockEntity.tankSides[0], remaining);
                }
                if(remaining > 0)
                {
                    int horizontal = 0;
                    for(int i = 2; i < 6; i++)
                    {
                        if(blockEntity.getLinkBoolean(i))
                        {
                            horizontal++;
                        }
                    }
            
                    for(int i = 2; i < 6; i++)
                    {
                        if(blockEntity.getLinkBoolean(i))
                        {
                            remaining = remaining - FluidUtils.transferFluid(blockEntity.tankCenter, blockEntity.tankSides[i], remaining / horizontal);
                            horizontal--;
                        }
                    }
                }
                if(remaining > 0)
                {
                    if(blockEntity.getLinkBoolean(1))
                    {
                        FluidUtils.transferFluid(blockEntity.tankCenter, blockEntity.tankSides[1], remaining);
                    }
                }
        
                for(Direction direction : Direction.values())
                {
                    if(blockEntity.getLinkBoolean(direction.get3DDataValue()))
                    {
                        if(direction != facing.getOpposite())
                        {
                            BlockEntity fluidReceiver = level.getBlockEntity(pos.relative(direction));
                            if(fluidReceiver != null)
                            {
                                IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).orElse(null);
                                if(fluidHandler != null)
                                {
                                    int transferAmount = 10;
                                    boolean doTransfer = true;
                                    if(fluidReceiver instanceof FluidPipeBlockEntity)
                                    {
                                        transferAmount = 10;
                                    }
                                    if(doTransfer)
                                    {
                                        FluidUtils.transferFluid(blockEntity.tankSides[direction.get3DDataValue()], fluidHandler, transferAmount / 2);
                                        FluidUtils.transferFluid(blockEntity.tankSides[direction.get3DDataValue()], blockEntity.tankCenter, transferAmount / 2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            if(fluidSender != null)
            {
                IFluidHandler fluidHandler = fluidSender.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).orElse(null);
                if(fluidHandler != null)
                {
                    FluidUtils.transferFluid(fluidHandler, blockEntity.tankCenter, 100);
                    BlockEntityUtil.sendUpdatePacket(fluidSender);
                }
            }
        }
    }
}
