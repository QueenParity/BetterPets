package com.kingparity.betterpets.fluidtank;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.function.Predicate;

public class FluidTanks
{
    public static FluidStack splitStack(List<FluidStack> stacks, int slot, int buckets)
    {
        return slot >= 0 && slot < stacks.size() && !(stacks.get(slot)).isEmpty() && buckets > 0 ? (stacks.get(slot)).split(buckets) : FluidStack.EMPTY;
    }
    
    public static FluidStack removeStack(List<FluidStack> stacks, int slot)
    {
        return slot >= 0 && slot < stacks.size() ? stacks.set(slot, FluidStack.EMPTY) : FluidStack.EMPTY;
    }
    
    public static CompoundTag toTag(CompoundTag tag, DefaultedList<FluidStack> stacks)
    {
        return toTag(tag, stacks, true);
    }
    
    public static CompoundTag toTag(CompoundTag tag, DefaultedList<FluidStack> stacks, boolean setIfEmpty)
    {
        ListTag listTag = new ListTag();
    
        for(int i = 0; i < stacks.size(); i++)
        {
            FluidStack fluidStack = stacks.get(i);
            if(!fluidStack.isEmpty())
            {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putByte("Slot", (byte)i);
                fluidStack.toTag(compoundTag);
                listTag.add(compoundTag);
            }
        }
        
        if(!listTag.isEmpty() || setIfEmpty)
        {
            tag.put("Fluids", listTag);
        }
        
        return tag;
    }
    
    public static void fromTag(CompoundTag tag, DefaultedList<FluidStack> stacks)
    {
        ListTag listTag = tag.getList("Fluids", 10);
        
        for(int i = 0; i < listTag.size(); i++)
        {
            CompoundTag compoundTag = listTag.getCompound(i);
            int j = compoundTag.getByte("Slot") & 255;
            if(j >= 0 && j < stacks.size())
            {
                stacks.set(j, FluidStack.fromTag(compoundTag));
            }
        }
    }
    
    public static int remove(FluidTank fluidTank, Predicate<FluidStack> shouldRemove, int maxBuckets, boolean dryRun)
    {
        int i = 0;
        
        for(int j = 0; j < fluidTank.size(); j++)
        {
            FluidStack fluidStack = fluidTank.getStack(j);
            int k = remove(fluidStack, shouldRemove, maxBuckets - i, dryRun);
            if(k > 0 && !dryRun && fluidStack.isEmpty())
            {
                fluidTank.setStack(j, FluidStack.EMPTY);
            }
            
            i += k;
        }
        
        return i;
    }
    
    public static int remove(FluidStack stack, Predicate<FluidStack> shouldRemove, int maxBuckets, boolean dryRun)
    {
        if(!stack.isEmpty() && shouldRemove.test(stack))
        {
            if(dryRun)
            {
                return stack.getBuckets();
            }
            else
            {
                int i = maxBuckets < 0 ? stack.getBuckets() : Math.min(maxBuckets, stack.getBuckets());
                stack.decrement(i);
                return i;
            }
        }
        else
        {
            return 0;
        }
    }
}