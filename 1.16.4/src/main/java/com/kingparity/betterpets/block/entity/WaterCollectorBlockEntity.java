package com.kingparity.betterpets.block.entity;

import com.kingparity.betterpets.core.ModBlockEntityTypes;
import com.kingparity.betterpets.fluidtank.FluidStack;
import com.kingparity.betterpets.fluidtank.FluidTanks;
import com.kingparity.betterpets.fluidtank.ImplementedFluidTank;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public class WaterCollectorBlockEntity extends BlockEntity implements ImplementedFluidTank, BlockEntityClientSerializable//, Tickable
{
    private final DefaultedList<FluidStack> fluids = DefaultedList.ofSize(2, FluidStack.EMPTY);
    
    public WaterCollectorBlockEntity()
    {
        super(ModBlockEntityTypes.WATER_COLLECTOR_BLOCK_ENTITY);
    }
    
    /*@Override
    public void tick()
    {
        if(!world.isClient)
        {
            this.sync();
            System.out.println("SERVER: " + this.getStack(0).getFluid());
            System.out.println("SERVER: " + this.getStack(0).getBuckets());
        }
        else
        {
            System.out.println("CLIENT: " + this.getStack(0).getFluid());
            System.out.println("CLIENT: " + this.getStack(0).getBuckets());
        }
    }*/
    
    @Override
    public DefaultedList<FluidStack> getFluids()
    {
        return fluids;
    }
    
    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        FluidTanks.fromTag(tag,fluids);
    }
    
    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        FluidTanks.toTag(tag,fluids);
        return super.toTag(tag);
    }
    
    @Override
    public void fromClientTag(CompoundTag tag)
    {
        super.fromTag(getCachedState(), tag);
        FluidTanks.fromTag(tag,fluids);
    }
    
    @Override
    public CompoundTag toClientTag(CompoundTag tag)
    {
        FluidTanks.toTag(tag,fluids);
        return super.toTag(tag);
    }
    
    @Override
    public void onContentsChanged()
    {
        if(!world.isClient)
        {
            this.toClientTag(new CompoundTag());
            this.sync();
        }
    }
}