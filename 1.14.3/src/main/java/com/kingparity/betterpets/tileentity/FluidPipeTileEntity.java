package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.util.FluidHelper;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.ArrayList;
import java.util.List;

public class FluidPipeTileEntity extends FluidTankTileEntity implements ITickableTileEntity
{
    private boolean north = false, east = false, south = false, west = false, up = false, down = false;
    
    public FluidPipeTileEntity()
    {
        super(ModTileEntities.FLUID_PIPE, 128, 128);
    }
    
    public void setNorth(boolean north)
    {
        this.north = north;
        markDirty();
    }
    
    public void setEast(boolean east)
    {
        this.east = east;
        markDirty();
    }
    
    public void setSouth(boolean south)
    {
        this.south = south;
        markDirty();
    }
    
    public void setWest(boolean west)
    {
        this.west = west;
        markDirty();
    }
    
    public void setUp(boolean up)
    {
        this.up = up;
        markDirty();
    }
    
    public void setDown(boolean down)
    {
        this.down = down;
        markDirty();
    }
    
    public boolean isNorth()
    {
        return north;
    }
    
    public boolean isEast()
    {
        return east;
    }
    
    public boolean isSouth()
    {
        return south;
    }
    
    public boolean isWest()
    {
        return west;
    }
    
    public boolean isUp()
    {
        return up;
    }
    
    public boolean isDown()
    {
        return down;
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putBoolean("North", north);
        compound.putBoolean("East", east);
        compound.putBoolean("South", south);
        compound.putBoolean("West", west);
        compound.putBoolean("Up", up);
        compound.putBoolean("Down", down);
        return compound;
    }
    
    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        north = compound.getBoolean("North");
        east = compound.getBoolean("East");
        south = compound.getBoolean("South");
        west = compound.getBoolean("West");
        up = compound.getBoolean("Up");
        down = compound.getBoolean("Down");
    }
    
    @Override
    public void tick()
    {
        if(this.getFluidAmount() > 0)
        {
            extractAnywhereConnected(this);
            syncToClient();
        }
    }
    
    public static Direction[] faces = {Direction.UP, Direction.DOWN, Direction.WEST, Direction.SOUTH, Direction.NORTH, Direction.EAST};
    public static int extractAnywhereConnected(FluidPipeTileEntity conduit)
    {
        List<BlockPos> conduits = new ArrayList<BlockPos>();
        conduits.add(conduit.getPos());
        vein(conduit.getWorld(), conduits, null);
        
        int given = 0;
        for(BlockPos bpos : conduits)
        {
            given += FluidHelper.giveEnergyAllSidesCond(conduit, conduit.getWorld(), bpos);
        }
        return given;
    }
    public static void vein(IBlockReader world, List<BlockPos> conduits, List<BlockPos> dconduits)
    {
        if(dconduits == null) dconduits = new ArrayList<BlockPos>();
        for(Direction face : faces)
        {
            List<BlockPos> tconduits = new ArrayList<BlockPos>();
            for(BlockPos bp : conduits.toArray(new BlockPos[] {}))
            {
                tconduits.add(bp);
            }
            for(BlockPos pos : tconduits)
            {
                if(world.getTileEntity(pos.offset(face)) instanceof FluidPipeTileEntity && !dconduits.contains(pos.offset(face)))
                {
                    conduits.add(pos.offset(face));
                    dconduits.add(pos.offset(face));
                    vein(world, conduits, dconduits);
                }
            }
        }
    }
    
    @Override
    public CompoundNBT getUpdateTag()
    {
        return write(new CompoundNBT());
    }
    
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        read(pkt.getNbtCompound());
        notifyBlockUpdate();
    }
    
    private void notifyBlockUpdate()
    {
        final BlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }
    
    @Override
    public void markDirty()
    {
        super.markDirty();
        notifyBlockUpdate();
    }
}