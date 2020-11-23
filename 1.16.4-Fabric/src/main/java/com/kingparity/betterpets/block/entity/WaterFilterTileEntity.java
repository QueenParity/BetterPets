package com.kingparity.betterpets.block.entity;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.kingparity.betterpets.core.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class WaterFilterTileEntity extends BaseTileEntity implements Tickable
{
    private static final int CAPACITY = 14 * FluidVolume.BUCKET;
    
    public final SimpleFixedFluidInv fluidInv;
    
    public WaterFilterTileEntity()
    {
        super(ModTileEntities.WATER_FILTER);
        fluidInv = new SimpleFixedFluidInv(1, CAPACITY);
    }
    
    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        tag = super.toTag(tag);
        
        FluidVolume invFluidWater = fluidInv.getInvFluid(0);
        if(!invFluidWater.isEmpty())
        {
            tag.put("fluidWater", invFluidWater.toTag());
        }
        
        FluidVolume invFluidFilteredWater = fluidInv.getInvFluid(1);
        if(!invFluidFilteredWater.isEmpty())
        {
            tag.put("fluidFilteredWater", invFluidFilteredWater.toTag());
        }
        
        return tag;
    }
    
    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        
        if(tag.contains("fluidWater"))
        {
            FluidVolume fluidWater = FluidVolume.fromTag(tag.getCompound("fluidWater"));
            fluidInv.setInvFluid(0, fluidWater, Simulation.ACTION);
        }
        
        if(tag.contains("fluidFilteredWater"))
        {
            FluidVolume fluidFilteredWater = FluidVolume.fromTag(tag.getCompound("fluidFilteredWater"));
            fluidInv.setInvFluid(1, fluidFilteredWater, Simulation.ACTION);
        }
    }
    
    @Override
    public void tick()
    {
        if(this.fluidInv.getInvFluid(0).getAmount() < this.fluidInv.getMaxAmount(0))
        {
            this.fluidInv.setInvFluid(0, FluidKeys.EMPTY.withAmount(0), Simulation.ACTION);
            this.fluidInv.setInvFluid(1, FluidKeys.EMPTY.withAmount(0), Simulation.ACTION);
        }
        this.fluidInv.insertFluid(0, FluidKeys.WATER.withAmount(50), Simulation.ACTION);
        this.fluidInv.insertFluid(1, FluidKeys.WATER.withAmount(50), Simulation.ACTION);
    }
}