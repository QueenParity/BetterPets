package com.kingparity.betterpets.fluidtank;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluidTank
{
    private Fluid fluid;
    private int buckets;
    private int capacity;
    
    public FluidTank(Fluid fluid, int buckets, int capacity)
    {
        this.fluid = fluid;
        this.buckets = buckets;
        this.capacity = capacity;
    }
    
    public void fromTag(CompoundTag tag)
    {
        this.fluid = Registry.FLUID.get(new Identifier(tag.getString("id")));
        this.buckets = tag.getInt("Buckets");
        this.capacity = tag.getInt("Capacity");
    }
    
    public CompoundTag toTag(CompoundTag tag)
    {
        Identifier identifier = Registry.FLUID.getId(this.fluid);
        tag.putString("id", identifier == null ? "minecraft:empty" : identifier.toString());
        tag.putInt("Buckets", this.buckets);
        tag.putInt("Capacity", this.capacity);
        
        return tag;
    }
    
    public int drain(boolean dryRun)
    {
        return drain(this.capacity, dryRun);
    }
    
    public int drain(int amount, boolean dryRun)
    {
        int amountToDrain = amount;
        if(this.buckets - amount < 0)
        {
            amountToDrain = this.buckets;
        }
        
        if(!dryRun)
        {
            this.buckets -= amountToDrain;
        }
        
        onContentsChanged();
        
        return amountToDrain;
    }
    
    public int fill(boolean dryRun)
    {
        return fill(this.capacity, dryRun);
    }
    
    public int fill(int amount, boolean dryRun)
    {
        int amountToFill = amount;
        if(this.buckets + amount > this.capacity)
        {
            amountToFill = this.capacity - this.buckets;
        }

        if(!dryRun)
        {
            this.buckets += amountToFill;
        }
        
        onContentsChanged();
        
        return amountToFill;
    }
    
    public boolean isEmpty()
    {
        return this.buckets == 0;
    }
    
    protected void onContentsChanged()
    {
    
    }
    
    public void setFluid(Fluid fluid)
    {
        this.fluid = fluid;
    }
    
    public void setBuckets(int buckets)
    {
        this.buckets = buckets;
    }
    
    public void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }
    
    public Fluid getFluid()
    {
        return fluid;
    }
    
    public int getBuckets()
    {
        return buckets;
    }
    
    public int getCapacity()
    {
        return capacity;
    }
}