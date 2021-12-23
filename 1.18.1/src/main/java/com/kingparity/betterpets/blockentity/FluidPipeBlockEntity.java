package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.BlockEntityUtil;
import com.kingparity.betterpets.util.FluidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FluidPipeBlockEntity extends BlockEntity implements IFluidTankWriter
{
    protected final int[] links = new int[6];
    protected int centerAmountLastTick;
    protected int[] sideAmountLastTick = new int[6];
    
    public static final int CENTER_CAPACITY = 80;
    public static final int SIDE_CAPACITY = 80;
    
    protected FluidTank tankCenter = new FluidTank(SIDE_CAPACITY)
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
            this.tankSides[i] = new FluidTank(SIDE_CAPACITY)
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
        blockEntity.updateLinks(level, pos);
    }
    
    public void updateLinks(LevelAccessor level, BlockPos pos)
    {
        for(Direction direction : Direction.values())
        {
            this.links[direction.get3DDataValue()] = 0;
            BlockEntity fluidReceiver = level.getBlockEntity(pos.relative(direction));
            if(fluidReceiver != null)
            {
                IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).orElse(null);
                if(fluidHandler != null)
                {
                    this.links[direction.get3DDataValue()] = 1;
                }
            }
            if(this.links[direction.get3DDataValue()] == 0)
            {
                this.tankSides[direction.get3DDataValue()].drain(500, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, FluidPipeBlockEntity blockEntity)
    {
        blockEntity.doTick();
    }
    
    public void doTick()
    {
        if(!this.level.isClientSide)
        {
            this.updateLinks(this.level, this.worldPosition);
            
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
    
    protected void doCenterLogic()
    {
        if(this.tankCenter.isEmpty())
        {
            return;
        }
    
        List<Direction> sides = new ArrayList<>(6);
    
        for(Direction to : Direction.values())
        {
            if(canGoInDirection(null, to))
            {
                int movable = (centerAmountLastTick - sideAmountLastTick[to.get3DDataValue()]);
                if(movable > 0)
                {
                    sides.add(to);
                }
                else if(!canGoInDirection(to, null) && this.tankSides[to.get3DDataValue()].getFluidAmount() < SIDE_CAPACITY)
                {
                    sides.add(to);
                }
            }
        }
    
        Collections.shuffle(sides);
    
        for(Direction to : sides)
        {
            FluidTank other = this.tankSides[to.get3DDataValue()];
            int movable = (this.centerAmountLastTick - this.sideAmountLastTick[to.get3DDataValue()] + sides.size() - 1) / sides.size();
            if(!canGoInDirection(to, null))
            {
                movable = Math.min(this.tankCenter.getFluidAmount(), SIDE_CAPACITY - other.getFluidAmount());
            }
            if(movable < 1)
            {
                continue;
            }
            FluidUtils.transferFluid(this.tankCenter, this.tankSides[to.get3DDataValue()], movable / 2);
        }
    }
    
    protected void doSideLogic(Direction side)
    {
        if(this.tankSides[side.get3DDataValue()].isEmpty())
        {
            return;
        }
    
        List<Direction> sides = new ArrayList<>(2);
        if(canGoInDirection(side, null))
        {
            int movable = (sideAmountLastTick[side.get3DDataValue()] - centerAmountLastTick);
            if(movable > 0)
            {
                sides.add(null);
            }
            else if(!canGoInDirection(null, side) && this.tankCenter.getFluidAmount() < CENTER_CAPACITY)
            {
                sides.add(null);
            }
        }
        
        if(canGoInDirection(side, side))
        {
            BlockEntity blockEntityAtSide = this.level.getBlockEntity(this.worldPosition.relative(side));
            FluidPipeBlockEntity otherPipe;
            if(blockEntityAtSide instanceof FluidPipeBlockEntity)
            {
                otherPipe = (FluidPipeBlockEntity)blockEntityAtSide;
            }
            else
            {
                otherPipe = null;
            }
            
            if(otherPipe != null)
            {
                int movable = (sideAmountLastTick[side.get3DDataValue()] - otherPipe.sideAmountLastTick[side.getOpposite().get3DDataValue()]);
                if(movable > 0)
                {
                    sides.add(side);
                }
            }
            else
            {
                IFluidHandler fluidHandler = this.level.getBlockEntity(this.worldPosition.relative(side)).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()).orElse(null);
                FluidStack leftOver = FluidUtil.tryFluidTransfer(fluidHandler, this.tankSides[side.get3DDataValue()], 10, false);
                if(leftOver.getAmount() < this.tankSides[side.get3DDataValue()].getFluidAmount())
                {
                    sides.add(side);
                }
            }
        }
    
        Collections.shuffle(sides);
    
        for(Direction to : sides)
        {
            if(to == null)
            {
                FluidTank other = this.tankCenter;
                int movable = (this.sideAmountLastTick[side.get3DDataValue()] - this.centerAmountLastTick + sides.size() - 1) / sides.size();
                if(!canGoInDirection(null, side))
                {
                    movable = Math.min(this.tankSides[side.get3DDataValue()].getFluidAmount(), SIDE_CAPACITY - other.getFluidAmount());
                }
                if(movable < 1)
                {
                    continue;
                }
                FluidUtils.transferFluid(this.tankSides[side.get3DDataValue()], this.tankCenter, movable / 2);
            }
            else
            {
                BlockEntity blockEntityAtSide = this.level.getBlockEntity(this.worldPosition.relative(side));
                FluidPipeBlockEntity otherPipe;
                if(blockEntityAtSide instanceof FluidPipeBlockEntity)
                {
                    otherPipe = (FluidPipeBlockEntity)blockEntityAtSide;
                }
                else
                {
                    otherPipe = null;
                }
                if(otherPipe != null)
                {
                    int movable = (this.sideAmountLastTick[side.get3DDataValue()] - otherPipe.sideAmountLastTick[side.getOpposite().get3DDataValue()]);
                    if(movable < 1)
                    {
                        continue;
                    }
                    FluidUtils.transferFluid(this.tankSides[side.get3DDataValue()], otherPipe.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, to.getOpposite()).orElse(null), movable / 2);
                }
                else
                {
                    IFluidHandler fluidHandler = this.level.getBlockEntity(this.worldPosition.relative(side)).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()).orElse(null);
                    
                    int movable = (this.tankSides[side.get3DDataValue()].getFluidAmount() + 1) / 2;
                    if(movable < 0)
                    {
                        continue;
                    }
                    FluidUtils.transferFluid(this.tankSides[side.get3DDataValue()], fluidHandler, movable);
                }
            }
        }
    }
    
    public void updateAmounts()
    {
        this.centerAmountLastTick = tankCenter.getFluidAmount();
        for(int i = 0; i < 6; i++)
        {
            this.sideAmountLastTick[i] = tankSides[i].getFluidAmount();
        }
    }
    
    public boolean canGoInDirection(@Nullable Direction from, @Nullable Direction to)
    {
        if(from == null)
        {
            if(to == null)
            {
                throw new IllegalArgumentException("WAT 2");
            }
            if(!this.getLinkBoolean(to.get3DDataValue()))
            {
                return false;
            }
            return true;
        }
        if(to == null)
        {
            return true;
        }
        if(to != from)
        {
            throw new IllegalArgumentException("WAT 2");
        }
        if(!this.getLinkBoolean(to.get3DDataValue()))
        {
            return false;
        }
        return true;
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
