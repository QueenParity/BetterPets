package com.kingparity.betterpets.fluidtank;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.collection.DefaultedList;

public interface ImplementedFluidTank extends FluidTank
{
    DefaultedList<FluidStack> getFluids();
    
    static ImplementedFluidTank of(DefaultedList<FluidStack> fluids)
    {
        return () -> fluids;
    }
    
    static ImplementedFluidTank ofSize(int size)
    {
        return of(DefaultedList.ofSize(size, FluidStack.EMPTY));
    }
    
    @Override
    default int size()
    {
        return getFluids().size();
    }
    
    @Override
    default boolean isEmpty()
    {
        for(int i = 0; i < size(); i++)
        {
            FluidStack fluidStack = getStack(i);
            if(!fluidStack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }
    
    @Override
    default FluidStack getStack(int slot)
    {
        return getFluids().get(slot);
    }
    
    @Override
    default FluidStack removeStack(int slot, int buckets)
    {
        FluidStack result = FluidTanks.splitStack(getFluids(), slot, buckets);
        if(!result.isEmpty())
        {
            markDirty();
        }
        
        return result;
    }
    
    @Override
    default FluidStack removeStack(int slot)
    {
        return FluidTanks.removeStack(getFluids(), slot);
    }
    
    @Override
    default void setStack(int slot, FluidStack stack)
    {
        getFluids().set(slot, stack);
        if(stack.getBuckets() > getMaxCountPerStack())
        {
            stack.setBuckets(getMaxCountPerStack());
        }
    }
    
    @Override
    default void clear()
    {
        getFluids().clear();
    }
    
    @Override
    default void markDirty()
    {
    
    }
    
    @Override
    default boolean canPlayerUse(PlayerEntity player)
    {
        return true;
    }
}