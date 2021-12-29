package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.block.FluidPumpBlock;
import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.FluidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
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
                this.sections.get(Parts.fromFacing(direction)).drain(500, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, FluidPumpBlockEntity blockEntity)
    {
        blockEntity.doTick();
    }
    
    @Override
    public void doTick()
    {
        if(!this.level.isClientSide)
        {
            //System.out.println(this.sections.get(Parts.CENTER).ticksInDirection);
            this.updateLinks(this.level, this.worldPosition);
    
            Direction facing = this.getBlockState().getValue(FluidPumpBlock.DIRECTION);
            BlockEntity fluidReceiver = this.level.getBlockEntity(this.worldPosition.relative(facing.getOpposite()));
            if(fluidReceiver != null)
            {
                IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).orElse(null);
                if(fluidHandler != null)
                {
                    /*if(this.sections.get(Parts.CENTER).getCurrentFlowDirection().canInput())
                    {
                        FluidUtils.transferFluid(fluidHandler, this.sections.get(Parts.CENTER), 20);
                    }
                    if(this.sections.get(Parts.CENTER).ticksInDirection == -1)
                    {
                        this.sections.get(Parts.CENTER).ticksInDirection = COOLDOWN_OUTPUT / 2;
                        System.out.println("hahahahahfgjgfhdhghjfjhgjfhgjfhgjfglhjkl");
                    }
                    else if(this.sections.get(Parts.CENTER).ticksInDirection == 0)
                    {
                        this.sections.get(Parts.CENTER).ticksInDirection = COOLDOWN_INPUT / 2;
                        System.out.println("fegfdddddddddddddddddddddddddddjghffjhjfjfhgjhfg");
                    }*/
                    //setFluid(this.sections.get(Parts.CENTER).getFluid());
                    if(this.level.getBlockState(this.worldPosition.relative(Direction.NORTH)).getBlock() == Blocks.REDSTONE_BLOCK)
                    {
                        if(this.level.getGameTime() % 50 < 25)
                        {
                            FluidUtils.transferFluid(fluidHandler, this.sections.get(Parts.CENTER), 500);
                        }
                    }
                }
            }
        }
        super.doTick();
    }
}
