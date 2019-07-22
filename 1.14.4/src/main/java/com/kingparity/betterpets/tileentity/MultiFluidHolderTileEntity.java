package com.kingparity.betterpets.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public abstract class MultiFluidHolderTileEntity extends TileEntity
{
    private int capacity;
    private int maxReceive;
    private int maxExtract;
    private final int[] fluidAmount;
    
    public MultiFluidHolderTileEntity(TileEntityType<?> tileEntityType)
    {
        this(tileEntityType, 2);
    }
    
    public MultiFluidHolderTileEntity(TileEntityType<?> tileEntityType, int tankNum)
    {
        this(tileEntityType, 12000, tankNum);
    }
    
    public MultiFluidHolderTileEntity(TileEntityType<?> tileEntityType, int capacity, int tankNum)
    {
        this(tileEntityType, capacity, capacity, tankNum);
    }
    
    public MultiFluidHolderTileEntity(TileEntityType<?> tileEntityType, int capacity, int maxTransfer, int tankNum)
    {
        this(tileEntityType, capacity, maxTransfer, maxTransfer, tankNum);
    }
    
    public MultiFluidHolderTileEntity(TileEntityType<?> tileEntityType, int capacity, int maxReceive, int maxExtract, int tankNum)
    {
        super(tileEntityType);
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.fluidAmount = new int[tankNum];
        for(int i : fluidAmount)
        {
            this.fluidAmount[i] = 0;
        }
    }
    
    public void fillTank(int tankId)
    {
        fluidAmount[tankId] = capacity;
        syncToClient();
    }
    
    public void emptyTank(int tankId)
    {
        fluidAmount[tankId] = 0;
        syncToClient();
    }
    
    public int receiveFluid(int maxReceive, boolean doDrain, int tankId)
    {
        if(!canReceive())
        {
            return 0;
        }
        int fluidReceived = Math.min(capacity - fluidAmount[tankId], Math.min(this.maxReceive, maxReceive));
        if(doDrain)
        {
            fluidAmount[tankId] += fluidReceived;
            syncToClient();
        }
        return fluidReceived;
    }
    
    public int extractFluid(int maxExtract, boolean doDrain, int tankId)
    {
        if(!canExtract())
        {
            return 0;
        }
        
        int fluidExtracted = Math.min(fluidAmount[tankId], Math.min(this.maxExtract, maxExtract));
        if (doDrain)
        {
            fluidAmount[tankId] -= fluidExtracted;
            syncToClient();
        }
        return fluidExtracted;
    }
    
    public int getCapacity()
    {
        return capacity;
    }
    
    public int getMaxReceive()
    {
        return maxReceive;
    }
    
    public int getMaxExtract()
    {
        return maxExtract;
    }
    
    public int getFluidAmount()
    {
        int r = 0;
        for(int i : fluidAmount)
        {
            r += i;
        }
        return r;
    }
    
    public int getFluidAmount(int tankId)
    {
        return fluidAmount[tankId];
    }
    
    public boolean canExtract()
    {
        return this.maxExtract > 0;
    }
    
    public boolean canReceive()
    {
        return this.maxReceive > 0;
    }
    
    public boolean hasFluid()
    {
        for(int i : fluidAmount)
        {
            if(i != 0)
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasFluid(int tankId)
    {
        return fluidAmount[tankId] != 0;
    }
    
    public ResourceLocation getStill()
    {
        return new ResourceLocation("block/water_still");
    }
    
    public ResourceLocation getFlowing()
    {
        return new ResourceLocation("block/water_flow");
    }
    
    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        if(compound.contains("FluidAmount", Constants.NBT.TAG_INT_ARRAY))
        {
            int[] aint = compound.getIntArray("FluidAmount");
            System.arraycopy(aint, 0, this.fluidAmount, 0, Math.min(this.fluidAmount.length, aint.length));
        }
        if(compound.contains("Capacity", Constants.NBT.TAG_INT))
        {
            this.capacity = compound.getInt("Capacity");
        }
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putIntArray("FluidAmount", fluidAmount);
        compound.putInt("Capacity", capacity);
        return compound;
    }
    
    @Override
    public CompoundNBT getUpdateTag()
    {
        return write(new CompoundNBT());
    }
    
    public void syncToClient()
    {
        this.markDirty();
        if(!world.isRemote)
        {
            if(world instanceof ServerWorld)
            {
                ServerWorld server = (ServerWorld)world;
                ChunkPos chunkPos = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
                ChunkManager manager = (server.getChunkProvider()).chunkManager;
                if(manager != null)
                {
                    SUpdateTileEntityPacket packet = getUpdatePacket();
                    if(packet != null)
                    {
                        manager.getTrackingPlayers(chunkPos, false).forEach(e -> e.connection.sendPacket(packet));
                    }
                }
            }
        }
    }
    
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
    {
        read(packet.getNbtCompound());
    }
}
