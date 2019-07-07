package com.kingparity.betterpets.util;

import com.kingparity.betterpets.tileentity.FluidPipeTileEntity;
import com.kingparity.betterpets.tileentity.FluidTankTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidHelper
{
    public static final Direction[] DU_IO_SIDES = {Direction.UP, Direction.DOWN, Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST};
    
    public static int giveEnergyAllSides(FluidTankTileEntity m, World w, BlockPos p)
    {
        return giveEnergyAllSides(m, w, p, false);
    }
    
    public static int giveEnergyAllSidesCond(FluidTankTileEntity m, World w, BlockPos p)
    {
        return giveEnergyAllSidesCond(m, w, p, false);
    }
    
    public static int giveEnergyAllSides(FluidTankTileEntity m, World w, BlockPos p, boolean calcOnly)
    {
        if(!m.canExtract())
        {
            return 0;
        }
        if(m.getFluidAmount() <= 0)
        {
            return 0;
        }
        if(m.getCapacity() <= 0)
        {
            return 0;
        }
        
        int fakeStored = Math.min(m.getFluidAmount(), m.getMaxExtract());
        int totalRemove = 0;
        
        for(Direction side : DU_IO_SIDES)
        {
            BlockPos op = p.offset(side);
            TileEntity te = w.getTileEntity(op);
    
            if(te == null)
            {
                continue;
            }
            if(!(te instanceof FluidTankTileEntity))
            {
                continue;
            }
            //if(!(te.getCapability(CapabilityEnergy.ENERGY, null) instanceof FluidTankTileEntity)) continue;
            FluidTankTileEntity oduh = (FluidTankTileEntity)te;
            
            if(!oduh.canReceive())
            {
                continue;
            }
            
            totalRemove += oduh.receiveFluid(fakeStored - totalRemove, calcOnly);
        }
        
        m.extractFluid(totalRemove, calcOnly);
        return totalRemove;
    }
    
    public static int giveEnergyAllSidesCond(FluidTankTileEntity m, World w, BlockPos p, boolean calcOnly)
    {
        if(!m.canExtract())
        {
            return 0;
        }
        if(m.getFluidAmount() <= 0)
        {
            return 0;
        }
        if(m.getCapacity() <= 0)
        {
            return 0;
        }
        
        int fakeStored = Math.min(m.getFluidAmount(), m.getMaxExtract());
        int totalRemove = 0;
        
        for(Direction side : DU_IO_SIDES)
        {
            BlockPos op = p.offset(side);
            TileEntity te = w.getTileEntity(op);
    
            if(te == null)
            {
                continue;
            }
            if(!(te instanceof FluidTankTileEntity))
            {
                continue;
            }
            //if(!(te.getCapability(CapabilityEnergy.ENERGY, null) instanceof FluidTankTileEntity)) continue;
            if(te instanceof FluidPipeTileEntity)
            {
                continue;
            }
            FluidTankTileEntity oduh = (FluidTankTileEntity)te;
            
            if(!oduh.canReceive())
            {
                continue;
            }
            
            totalRemove += oduh.receiveFluid(fakeStored - totalRemove, calcOnly);
        }
        
        m.extractFluid(totalRemove, calcOnly);
        return totalRemove;
    }
    
    public static int retractEnergyAllSides(FluidTankTileEntity m, World w, BlockPos p)
    {
        return retractEnergyAllSides(m, w, p, false);
    }
    
    public static int retractEnergyAllSides(FluidTankTileEntity m, World w, BlockPos p, boolean calcOnly)
    {
        if(!m.canReceive())
        {
            return 0;
        }
        if(m.getFluidAmount() >= m.getCapacity())
        {
            return 0;
        }
        if(m.getCapacity() <= 0)
        {
            return 0;
        }
        
        int fakeStored = Math.min(m.getFluidAmount(), m.getMaxReceive());
        int totalAdd = 0;
        
        for(Direction side : DU_IO_SIDES)
        {
            BlockPos op = p.offset(side);
            TileEntity te = w.getTileEntity(op);
    
            if(te == null)
            {
                continue;
            }
            if(!(te instanceof FluidTankTileEntity))
            {
                continue;
            }
            //if(!(te.getCapability(CapabilityEnergy.ENERGY, null) instanceof FluidTankTileEntity)) continue;
            FluidTankTileEntity oduh = (FluidTankTileEntity)te;
    
            if(!oduh.canExtract())
            {
                continue;
            }
            
            totalAdd += oduh.extractFluid(fakeStored - totalAdd, calcOnly);
        }
        
        m.receiveFluid(totalAdd, calcOnly);
        return totalAdd;
    }
}