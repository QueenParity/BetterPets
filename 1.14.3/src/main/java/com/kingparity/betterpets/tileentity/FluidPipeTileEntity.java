package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.block.FluidPipeBlock;
import com.kingparity.betterpets.core.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class FluidPipeTileEntity extends TileEntity implements ITickableTileEntity
{
    private float capacity, allowedTransferAmount;
    private boolean[] disabledConnections;
    
    private float fluidAmount = 0.0F;
    
    public FluidPipeTileEntity()
    {
        super(ModTileEntities.FLUID_PIPE);
        capacity = 500.0F;
        allowedTransferAmount = 20.0F;
        disabledConnections = new boolean[Direction.values().length];
    }
    
    public static boolean[] getDisabledConnections(FluidPipeTileEntity fluidPipe)
    {
        return fluidPipe != null ? fluidPipe.getDisabledConnections() : new boolean[Direction.values().length];
    }
    
    public boolean[] getDisabledConnections()
    {
        return disabledConnections;
    }
    
    public boolean isConnectionDisabled(int index)
    {
        return disabledConnections[index];
    }
    
    public boolean isConnectionDisabled(Direction direction)
    {
        return disabledConnections[direction.getIndex()];
    }
    
    public void setConnectionDisabled(int index, boolean disabled)
    {
        disabledConnections[index] = disabled;
        syncToClient();
    }
    
    public void setConnectionDisabled(Direction direction, boolean disabled)
    {
        setConnectionDisabled(direction.getIndex(), disabled);
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
    
    public float getCapacity()
    {
        return capacity;
    }
    
    public float getFluidAmount()
    {
        return fluidAmount;
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
    public void tick()
    {
        BlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof FluidPipeBlock)
        {
            int numConnectedPipes = 0;
            FluidPipeTileEntity fluidPipeNorth = null, fluidPipeEast = null, fluidPipeSouth = null, fluidPipeWest = null;
            FluidTankTileEntity fluidTankNorth = null, fluidTankEast = null, fluidTankSouth = null, fluidTankWest = null;
            if(state.get(FluidPipeBlock.NORTH))
            {
                TileEntity tileEntity = world.getTileEntity(pos.offset(Direction.NORTH));
                if(tileEntity instanceof FluidPipeTileEntity)
                {
                    fluidPipeNorth = (FluidPipeTileEntity)tileEntity;
                }
                else if(tileEntity instanceof FluidTankTileEntity)
                {
                    fluidTankNorth = (FluidTankTileEntity)tileEntity;
                }
                numConnectedPipes++;
            }
            if(state.get(FluidPipeBlock.EAST))
            {
                TileEntity tileEntity = world.getTileEntity(pos.offset(Direction.EAST));
                if(tileEntity instanceof FluidPipeTileEntity)
                {
                    fluidPipeEast = (FluidPipeTileEntity)tileEntity;
                }
                else if(tileEntity instanceof FluidTankTileEntity)
                {
                    fluidTankEast = (FluidTankTileEntity)tileEntity;
                }
                numConnectedPipes++;
            }
            if(state.get(FluidPipeBlock.SOUTH))
            {
                TileEntity tileEntity = world.getTileEntity(pos.offset(Direction.SOUTH));
                if(tileEntity instanceof FluidPipeTileEntity)
                {
                    fluidPipeSouth = (FluidPipeTileEntity)tileEntity;
                }
                else if(tileEntity instanceof FluidTankTileEntity)
                {
                    fluidTankSouth = (FluidTankTileEntity)tileEntity;
                }
                numConnectedPipes++;
            }
            if(state.get(FluidPipeBlock.WEST))
            {
                TileEntity tileEntity = world.getTileEntity(pos.offset(Direction.WEST));
                if(tileEntity instanceof FluidPipeTileEntity)
                {
                    fluidPipeWest = (FluidPipeTileEntity)tileEntity;
                }
                else if(tileEntity instanceof FluidTankTileEntity)
                {
                    fluidTankWest = (FluidTankTileEntity)tileEntity;
                }
                numConnectedPipes++;
            }
            if(fluidAmount != 0)
            {
                float transferAmount;
                if(fluidAmount >= 20)
                {
                    transferAmount = allowedTransferAmount / numConnectedPipes;
                }
                else
                {
                    transferAmount = fluidAmount / numConnectedPipes;
                }
                if(fluidPipeNorth != null || fluidPipeEast != null || fluidPipeSouth != null || fluidPipeWest != null)
                {
                    if(fluidPipeNorth != null)
                    {
                        if(fluidPipeNorth.getFluidAmount() < fluidAmount && fluidPipeNorth.getFluidAmount() + 10 < fluidAmount)
                        {
                            fluidAmount -= transferAmount;
                            transferAmount += (fluidPipeNorth.addFluid(transferAmount) / 3);
                        }
                    }
                    if(fluidPipeEast != null)
                    {
                        if(fluidPipeEast.getFluidAmount() < fluidAmount && fluidPipeEast.getFluidAmount() + 10 < fluidAmount)
                        {
                            fluidAmount -= transferAmount;
                            transferAmount += (fluidPipeEast.addFluid(transferAmount) / 3);
                        }
                    }
                    if(fluidPipeSouth != null)
                    {
                        if(fluidPipeSouth.getFluidAmount() < fluidAmount && fluidPipeSouth.getFluidAmount() + 10 < fluidAmount)
                        {
                            fluidAmount -= transferAmount;
                            transferAmount += (fluidPipeSouth.addFluid(transferAmount) / 3);
                        }
                    }
                    if(fluidPipeWest != null)
                    {
                        if(fluidPipeWest.getFluidAmount() < fluidAmount && fluidPipeWest.getFluidAmount() + 10 < fluidAmount)
                        {
                            fluidAmount -= transferAmount;
                            transferAmount += (fluidPipeWest.addFluid(transferAmount));
                        }
                    }
                    syncToClient();
                }
            }
    
            if(fluidTankNorth != null || fluidTankEast != null || fluidTankSouth != null || fluidTankWest != null)
            {
                if(fluidAmount != capacity)
                {
                    float fluidRemaining = capacity - fluidAmount;
                    if(fluidRemaining > 20)
                    {
                        fluidRemaining = 20;
                    }
                    float getEach;
                    int nonNull = 0;
                    if(fluidTankNorth != null)
                    {
                        nonNull++;
                    }
                    if(fluidTankEast != null)
                    {
                        nonNull++;
                    }
                    if(fluidTankSouth != null)
                    {
                        nonNull++;
                    }
                    if(fluidTankWest != null)
                    {
                        nonNull++;
                    }
                    getEach = fluidRemaining / (float)nonNull;
                    if(fluidTankNorth != null)
                    {
                        nonNull--;
                        float removedFluid = fluidTankNorth.removeFluid(getEach);
                        addFluid(getEach - removedFluid);
                        if(removedFluid != 0.0F)
                        {
                            getEach += removedFluid / nonNull;
                        }
                    }
                    if(fluidTankEast != null)
                    {
                        nonNull--;
                        float removedFluid = fluidTankEast.removeFluid(getEach);
                        addFluid(getEach - removedFluid);
                        if(removedFluid != 0.0F)
                        {
                            getEach += removedFluid / nonNull;
                        }
                    }
                    if(fluidTankSouth != null)
                    {
                        nonNull--;
                        float removedFluid = fluidTankSouth.removeFluid(getEach);
                        addFluid(getEach - removedFluid);
                        if(removedFluid != 0.0F)
                        {
                            getEach += removedFluid / nonNull;
                        }
                    }
                    if(fluidTankWest != null)
                    {
                        nonNull--;
                        float removedFluid = fluidTankWest.removeFluid(getEach);
                        addFluid(getEach - removedFluid);
                        if(removedFluid != 0.0F)
                        {
                            getEach += removedFluid / nonNull;
                        }
                    }
                    syncToClient();
                }
            }
        }
        else
        {
            System.out.println(state.getBlock().getRegistryName());
        }
    }
    
    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        if(compound.contains("FluidAmount", Constants.NBT.TAG_FLOAT))
        {
            this.fluidAmount = compound.getFloat("FluidAmount");
        }
        byte[] byteArr = compound.getByteArray("disabledConnections");
        for(int i = 0; i < byteArr.length; i++)
        {
            disabledConnections[i] = byteArr[i] == (byte)1;
        }
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putFloat("FluidAmount", fluidAmount);
        byte[] byteArr = new byte[disabledConnections.length];
        for(int i = 0; i < byteArr.length; i++)
        {
            byteArr[i] = (byte)(disabledConnections[i] ? 1 : 0);
        }
        compound.putByteArray("disabledConnections", byteArr);
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
                if(world.getTileEntity(this.getPos()) instanceof FluidPipeTileEntity)
                {
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