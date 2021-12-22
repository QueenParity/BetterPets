package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.FluidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidPipeBlockEntity extends FluidHandlerSyncedBlockEntity
{
    protected final int[] links = new int[6];
    
    public FluidPipeBlockEntity(BlockPos pos, BlockState state)
    {
        this(ModBlockEntities.FLUID_PIPE.get(), pos, state);
    }
    
    public FluidPipeBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state)
    {
        super(blockEntityType, pos, state, 696);
    }
    
    public static void updateLinks(LevelAccessor level, BlockPos pos, BlockState state, FluidPipeBlockEntity blockEntity)
    {
        int capacity = 216;
        for(Direction direction : Direction.values())
        {
            blockEntity.links[direction.get3DDataValue()] = 0;
            BlockEntity fluidReceiver = level.getBlockEntity(pos.relative(direction));
            if(fluidReceiver != null)
            {
                IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).orElse(null);
                if(fluidHandler != null)
                {
                    capacity += 80;
                    blockEntity.links[direction.get3DDataValue()] = 1;
                }
            }
        }
        blockEntity.tank.setCapacity(capacity);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, FluidPipeBlockEntity blockEntity)
    {
        if(pos.getX() == -12 && pos.getY() == 64 && pos.getZ() == -4)
        {
            System.out.println(blockEntity.getCapacity());
        }
        if(!level.isClientSide)
        {
            updateLinks(level, pos, state, blockEntity);
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
                                
                                int otherPipe = fluidPipe.getFluidLevel(), thisPipe = blockEntity.getFluidLevel();
                                if(fluidPipe.getLinkBoolean(0))
                                {
                                    otherPipe -= 80;
                                }
                                if(blockEntity.getLinkBoolean(0))
                                {
                                    thisPipe -= 80;
                                }
                                
                                if(fluidPipe.getLinkBoolean(1))
                                {
                                    otherPipe -= 80;
                                }
                                if(blockEntity.getLinkBoolean(1))
                                {
                                    thisPipe -= 80;
                                }
                                
                                if(otherPipe >= thisPipe)
                                {
                                    doTransfer = false;
                                }
                                if(thisPipe - otherPipe > 2)
                                {
                                    transferAmount = 2;
                                }
                                if(otherPipe < Mth.clamp(175, 0, thisPipe / 2))
                                {
                                    transferAmount = 50;
                                }
                            }
                            if(fluidReceiver instanceof WaterCollectorBlockEntity || fluidReceiver instanceof WaterFilterBlockEntity)
                            {
                                transferAmount = 500;
                            }
                            if(fluidReceiver instanceof FluidPumpBlockEntity)
                            {
                                transferAmount = 0;
                            }
                            //transferAmount = 1;
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
    
    public int[] getLinks()
    {
        return this.links;
    }
    
    public int getLink(int index)
    {
        return this.links[index];
    }
    
    public boolean getLinkBoolean(int index)
    {
        return this.links[index] != 0;
    }
    
    @Override
    public void load(CompoundTag compound)
    {
        super.load(compound);
        int[] aint = compound.getIntArray("Links");
        System.arraycopy(aint, 0, this.links, 0, aint.length);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound)
    {
        super.saveAdditional(compound);
        compound.putIntArray("Links", this.links);
    }
}
