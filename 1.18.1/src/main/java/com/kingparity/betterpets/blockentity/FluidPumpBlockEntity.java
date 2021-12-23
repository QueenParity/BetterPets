package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.block.FluidPumpBlock;
import com.kingparity.betterpets.init.ModBlockEntities;
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
    
    @Override
    public void updateLinks(LevelAccessor level, BlockPos pos)
    {
        Direction facing = this.getBlockState().getValue(FluidPumpBlock.DIRECTION);
        for(Direction direction : Direction.values())
        {
            this.links[direction.get3DDataValue()] = 0;
            if(direction != facing.getOpposite())
            {
                BlockEntity fluidReceiver = level.getBlockEntity(pos.relative(direction));
                if(fluidReceiver != null)
                {
                    IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).orElse(null);
                    if(fluidHandler != null)
                    {
                        this.links[direction.get3DDataValue()] = 1;
                    }
                }
            }
            if(this.links[direction.get3DDataValue()] == 0)
            {
                this.tankSides[direction.get3DDataValue()].drain(500, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, FluidPumpBlockEntity blockEntity)
    {
        blockEntity.doTick();
    }
    
    public void doTick()
    {
        if(!this.level.isClientSide)
        {
            this.updateLinks(this.level, this.worldPosition);
    
            Direction facing = this.getBlockState().getValue(FluidPumpBlock.DIRECTION);
            BlockEntity fluidReceiver = this.level.getBlockEntity(this.worldPosition.relative(facing.getOpposite()));
            if(fluidReceiver != null)
            {
                IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).orElse(null);
                if(fluidHandler != null)
                {
                    FluidUtils.transferFluid(fluidHandler, this.tankCenter, 20);
                }
            }
            
            this.updateAmounts();
            
            this.doCenterLogic();
            
            for(int i = 0; i < 6; i++)
            {
                if(this.getLinkBoolean(i))
                {
                    this.doSideLogic(Direction.from3DDataValue(i));
                }
            }
            
            this.updateAmounts();
            
            this.syncToClient();
        }
    }
}
