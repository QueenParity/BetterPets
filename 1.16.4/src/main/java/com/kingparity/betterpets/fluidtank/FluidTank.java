package com.kingparity.betterpets.fluidtank;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Clearable;

import java.util.Set;

public interface FluidTank extends Clearable
{
    int size();
    
    boolean isEmpty();
    
    FluidStack getStack(int slot);
    
    FluidStack removeStack(int slot, int buckets);
    
    FluidStack removeStack(int slot);
    
    void setStack(int slot, FluidStack stack);
    
    default int getMaxCountPerStack()
    {
        return 64;
    }
    
    void markDirty();
    
    boolean canPlayerUse(PlayerEntity player);
    
    default void onOpen(PlayerEntity player) {}
    
    default void onClose(PlayerEntity player) {}
    
    default boolean isValid(int slot, FluidStack stack)
    {
        return true;
    }
    
    default int count(Fluid fluid)
    {
        int i = 0;
        
        for(int j = 0; j < this.size(); ++j)
        {
            FluidStack fluidStack = this.getStack(j);
            if(fluidStack.getFluid().equals(fluid))
            {
                i += fluidStack.getBuckets();
            }
        }
        
        return i;
    }
    
    default boolean containsAny(Set<Fluid> fluids)
    {
        for(int i = 0; i < this.size(); ++i)
        {
            FluidStack fluidStack = this.getStack(i);
            if(fluids.contains(fluidStack.getFluid()) && fluidStack.getBuckets() > 0)
            {
                return true;
            }
        }
        
        return false;
    }
}
