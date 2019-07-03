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
    private float capacity;       // 12 buckets
    
    private float fluidAmount = 0.0F;
    
    public FluidTankTileEntity(TileEntityType<?> tileEntityType)
    {
        this(tileEntityType, 12000.0F);
    }
    
    public FluidTankTileEntity(TileEntityType<?> tileEntityType, float capacity)
    {
        super(tileEntityType);
        this.capacity = capacity;
    }
    
    public void fillTank()
    {
        fluidAmount = capacity;
        syncToClient();
    }
    
    public void emptyTank()
    {
        fluidAmount = 0.0F;
        syncToClient();
    }
    
    public float addFluid(float amount)
    {
        float r = (fluidAmount + amount) - capacity;
        if(fluidAmount + amount <= capacity)
        {
            fluidAmount += amount;
            syncToClient();
            return 0.0F;
        }
        else
        {
            fillTank();
            return r;
        }
    }
    
    public float removeFluid(float amount)
    {
        float r = (fluidAmount - amount) * -1;
        if(fluidAmount >= amount)
        {
            fluidAmount -= amount;
            syncToClient();
            return 0.0F;
        }
        else
        {
            emptyTank();
            return r;
        }
    }
    
    public float getFluidAmount()
    {
        return this.fluidAmount;
    }
    
    public float getCapacity()
    {
        return capacity;
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
        if(compound.contains("FluidAmount", Constants.NBT.TAG_FLOAT))
        {
            this.fluidAmount = compound.getFloat("FluidAmount");
        }
        if(compound.contains("Capacity", Constants.NBT.TAG_FLOAT))
        {
            this.capacity = compound.getFloat("Capacity");
        }
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putFloat("FluidAmount", fluidAmount);
        compound.putFloat("Capacity", capacity);
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
