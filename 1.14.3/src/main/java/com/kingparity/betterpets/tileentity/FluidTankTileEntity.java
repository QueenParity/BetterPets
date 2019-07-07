package com.kingparity.betterpets.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class FluidTankTileEntity extends TileEntity
{
    private int capacity;
    private int maxReceive;
    private int maxExtract;
    private int fluidAmount = 0;
    
    public FluidTankTileEntity(TileEntityType<?> tileEntityType)
    {
        this(tileEntityType, 12000);
    }
    
    public FluidTankTileEntity(TileEntityType<?> tileEntityType, int capacity)
    {
        this(tileEntityType, capacity, capacity);
    }
    
    public FluidTankTileEntity(TileEntityType<?> tileEntityType, int capacity, int maxTransfer)
    {
        this(tileEntityType, capacity, maxTransfer, maxTransfer);
    }
    
    public FluidTankTileEntity(TileEntityType<?> tileEntityType, int capacity, int maxReceive, int maxExtract)
    {
        super(tileEntityType);
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }
    
    public void fillTank()
    {
        fluidAmount = capacity;
        syncToClient();
    }
    
    public void emptyTank()
    {
        fluidAmount = 0;
        syncToClient();
    }
    
    public int receiveFluid(int maxReceive)
    {
        return receiveFluid(maxReceive, false);
    }
    
    public int receiveFluid(int maxReceive, boolean simulate)
    {
        if(!canReceive())
        {
            return 0;
        }
        int fluidReceived = Math.min(capacity - fluidAmount, Math.min(this.maxReceive, maxReceive));
        if(!simulate)
        {
            fluidAmount += fluidReceived;
            syncToClient();
        }
        //System.out.println("hai: " + fluidReceived);
        return fluidReceived;
    }
    
    public int extractFluid(int maxExtract)
    {
        return extractFluid(maxExtract, true);
    }
    
    public int extractFluid(int maxExtract, boolean simulate)
    {
        if(!canExtract())
        {
            return 0;
        }
        
        int fluidExtracted = Math.min(fluidAmount, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
        {
            fluidAmount -= fluidExtracted;
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
        return fluidAmount;
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
        return fluidAmount != 0;
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
        if(compound.contains("FluidAmount", Constants.NBT.TAG_INT))
        {
            this.fluidAmount = compound.getInt("FluidAmount");
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
        compound.putInt("FluidAmount", fluidAmount);
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
