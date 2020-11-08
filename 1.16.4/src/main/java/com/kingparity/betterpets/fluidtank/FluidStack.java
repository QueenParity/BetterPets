package com.kingparity.betterpets.fluidtank;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FluidStack
{
    private static final Logger LOGGER = LogManager.getLogger();
    
    private int buckets;
    private int cooldown;
    private Fluid fluid;
    private CompoundTag tag;
    private boolean empty;
    
    public static final FluidStack EMPTY = new FluidStack((Fluid)null);
    
    public FluidStack(Fluid fluid)
    {
        this(fluid, 1);
    }
    
    public FluidStack(Fluid fluid, int buckets)
    {
        this.fluid = fluid;
        this.buckets = buckets;
        
        this.updateEmptyState();
    }
    
    private void updateEmptyState()
    {
        this.empty = false;
        this.empty = this.isEmpty();
    }
    
    private FluidStack(CompoundTag tag)
    {
        this.fluid = Registry.FLUID.get(new Identifier(tag.getString("id")));
        this.buckets = tag.getByte("Buckets");
        if(tag.contains("tag", 10))
        {
            this.tag = tag.getCompound("tag");
        }
        
        this.updateEmptyState();
    }
    
    public static FluidStack fromTag(CompoundTag tag)
    {
        try
        {
            return new FluidStack(tag);
        }
        catch(RuntimeException exception)
        {
            LOGGER.debug("Tried to load invalid fluid: {}", tag, exception);
            return EMPTY;
        }
    }
    
    public boolean isEmpty()
    {
        if(this == EMPTY)
        {
            return true;
        }
        else if(this.getFluid() != null && this.getFluid() != Fluids.EMPTY)
        {
            return this.buckets <= 0;
        }
        else
        {
            return true;
        }
    }
    
    public FluidStack split(int amount)
    {
        int i = Math.min(amount, this.buckets);
        FluidStack fluidStack = this.copy();
        fluidStack.setBuckets(i);
        this.decrement(i);
        return fluidStack;
    }
    
    public Fluid getFluid()
    {
        return fluid;
    }
    
    public CompoundTag toTag(CompoundTag tag)
    {
        Identifier identifier = Registry.FLUID.getId(this.getFluid());
        tag.putString("id", identifier == null ? "minecraft:empty" : identifier.toString());
        tag.putByte("Buckets", (byte)this.buckets);
        if(this.tag != null)
        {
            tag.put("tag", this.tag.copy());
        }
        
        return tag;
    }
    
    public FluidStack copy()
    {
        if(this.isEmpty())
        {
            return EMPTY;
        }
        else
        {
            FluidStack fluidStack = new FluidStack(this.getFluid(), this.buckets);
            fluidStack.setCooldown(this.getCooldown());
            if(this.tag != null)
            {
                fluidStack.tag = this.tag.copy();
            }
            
            return fluidStack;
        }
    }
    
    public int getCooldown()
    {
        return this.cooldown;
    }
    
    public void setCooldown(int cooldown)
    {
        this.cooldown = cooldown;
    }
    
    public int getBuckets()
    {
        return this.empty ? 0 : this.buckets;
    }
    
    public void setBuckets(int buckets)
    {
        this.buckets = buckets;
        this.updateEmptyState();
    }
    
    public void increment(int amount)
    {
        this.setBuckets(this.buckets + amount);
    }
    
    public void decrement(int amount)
    {
        this.increment(-amount);
    }
}
