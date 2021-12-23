package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.BlockEntityUtil;
import com.kingparity.betterpets.util.FluidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

public class FluidPipeBlockEntity extends BlockEntity implements IFluidTankWriter
{
    protected final int[] links = new int[6];
    
    protected FluidTank tankCenter = new FluidTank(80)
    {
        @Override
        protected void onContentsChanged()
        {
            FluidPipeBlockEntity.this.syncToClient();
        }
    };
    
    protected FluidTank[] tankSides = new FluidTank[6];
    
    public FluidPipeBlockEntity(BlockPos pos, BlockState state)
    {
        this(ModBlockEntities.FLUID_PIPE.get(), pos, state);
    }
    
    public FluidPipeBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state)
    {
        super(blockEntityType, pos, state);
        for(int i = 0; i < 6; i++)
        {
            this.tankSides[i] = new FluidTank(80)
            {
                @Override
                protected void onContentsChanged()
                {
                    FluidPipeBlockEntity.this.syncToClient();
                }
            };
        }
    }
    
    public void syncToClient()
    {
        this.setChanged();
        BlockEntityUtil.sendUpdatePacket(this, this.saveWithFullMetadata());
    }
    
    public static void updateLinks(LevelAccessor level, BlockPos pos, BlockState state, FluidPipeBlockEntity blockEntity)
    {
        for(Direction direction : Direction.values())
        {
            blockEntity.links[direction.get3DDataValue()] = 0;
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
    
    public static void tick(Level level, BlockPos pos, BlockState state, FluidPipeBlockEntity blockEntity)
    {
        if(!level.isClientSide)
        {
            updateLinks(level, pos, state, blockEntity);
            blockEntity.syncToClient();
            if(blockEntity.getFluidLevel() > 0)
            {
                int remaining = 50;
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
                        BlockEntity fluidReceiver = level.getBlockEntity(pos.relative(direction));
                        if(fluidReceiver != null)
                        {
                            IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).orElse(null);
                            if(fluidHandler != null)
                            {
                                int transferAmount = 10;
                                boolean doTransferCenter = true, doTransferOther = true;
                                if(fluidReceiver instanceof FluidPumpBlockEntity)
                                {
                                    doTransferOther = false;
                                }
                                if(fluidReceiver instanceof FluidPipeBlockEntity)
                                {
                                    int other = ((FluidPipeBlockEntity)fluidReceiver).getFluidLevelSide(direction.getOpposite().get3DDataValue());
                                    int here = blockEntity.getFluidLevelSide(direction.get3DDataValue());
                                    transferAmount = Mth.clamp((here - other) / 2, 0, 10);
                                    if(other > here)
                                    {
                                        doTransferOther = false;
                                    }
                                }
                                int fluidLevelCenter = blockEntity.getFluidLevelCenter();
                                int fluidLevelSide = blockEntity.getFluidLevelSide(direction.get3DDataValue());
                                if(fluidLevelCenter > fluidLevelSide)
                                {
                                    doTransferCenter = false;
                                }
                                if(doTransferOther)
                                {
                                    if(blockEntity.tankSides[direction.get3DDataValue()].getFluidAmount() > 20)
                                    {
                                        FluidUtils.transferFluid(blockEntity.tankSides[direction.get3DDataValue()], fluidHandler, transferAmount);
                                    }
                                }
                                if(doTransferCenter)
                                {
                                    FluidUtils.transferFluid(blockEntity.tankSides[direction.get3DDataValue()], blockEntity.tankCenter, Mth.clamp((fluidLevelSide - fluidLevelCenter) / 2, 0, 20));
                                }
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
    
    public int getFluidLevel()
    {
        int level = this.tankCenter.getFluidAmount();
        for(int i = 0; i < 6; i++)
        {
            level += this.tankSides[i].getFluidAmount();
        }
        return level;
    }
    
    public int getFluidLevelCenter()
    {
        return this.tankCenter.getFluidAmount();
    }
    
    public int getFluidLevelSide(int side)
    {
        return this.tankSides[side].getFluidAmount();
    }
    
    public FluidStack getFluidStack(int side)
    {
        if(side == -1)
        {
            return this.tankCenter.getFluid();
        }
        else
        {
            return this.tankSides[side].getFluid();
        }
    }
    
    @Override
    public void load(CompoundTag compound)
    {
        super.load(compound);
        
        this.tankCenter.readFromNBT(compound.getCompound("TankCenter"));
        
        for(int i = 0; i < 6; i++)
        {
            this.tankSides[i].readFromNBT(compound.getCompound("TankSide" + Direction.from3DDataValue(i).getName().substring(0,1).toUpperCase() + Direction.from3DDataValue(i).getName().substring(1)));
        }
        
        int[] aint = compound.getIntArray("Links");
        System.arraycopy(aint, 0, this.links, 0, aint.length);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound)
    {
        super.saveAdditional(compound);
        
        this.writeTanks(compound);
        
        compound.putIntArray("Links", this.links);
    }
    
    @Override
    public void writeTanks(CompoundTag compound)
    {
        CompoundTag tagTankCenter = new CompoundTag();
        this.tankCenter.writeToNBT(tagTankCenter);
        compound.put("TankCenter", tagTankCenter);
        
        CompoundTag[] tagTankSides = new CompoundTag[6];
        for(int i = 0; i < 6; i++)
        {
            tagTankSides[i] = new CompoundTag();
            this.tankSides[i].writeToNBT(tagTankSides[i]);
            compound.put("TankSide" + Direction.from3DDataValue(i).getName().substring(0,1).toUpperCase() + Direction.from3DDataValue(i).getName().substring(1), tagTankSides[i]);
        }
    }
    
    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        this.load(pkt.getTag());
    }
    
    @Override
    public boolean areTanksEmpty()
    {
        return false;
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction facing)
    {
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            BlockState state = this.level.getBlockState(this.worldPosition);
            if(facing == null)
            {
                return LazyOptional.of(() -> this.tankCenter).cast();
            }
            return LazyOptional.of(() -> this.tankSides[facing.get3DDataValue()]).cast();
        }
        return super.getCapability(cap, facing);
    }
}
