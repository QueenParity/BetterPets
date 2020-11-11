package com.kingparity.betterpets.block.entity;

import com.kingparity.betterpets.core.ModBlockEntityTypes;
import com.kingparity.betterpets.core.ModFluids;
import com.kingparity.betterpets.fluidtank.FluidTank;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class WaterFilterBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable
{
    private FluidTank tankWater, tankFilteredWater;
    
    public WaterFilterBlockEntity()
    {
        super(ModBlockEntityTypes.WATER_FILTER_BLOCK_ENTITY);
        this.tankWater = new FluidTank(Fluids.WATER, 0, 14000)
        {
            @Override
            protected void onContentsChanged()
            {
                WaterFilterBlockEntity.this.markDirty();
                if(!WaterFilterBlockEntity.this.world.isClient)
                {
                    WaterFilterBlockEntity.this.sync();
                }
            }
        };
        this.tankFilteredWater = new FluidTank(ModFluids.STILL_FILTERED_WATER, 0, 14000)
        {
            @Override
            protected void onContentsChanged()
            {
                WaterFilterBlockEntity.this.markDirty();
                if(!WaterFilterBlockEntity.this.world.isClient)
                {
                    WaterFilterBlockEntity.this.sync();
                }
            }
        };
    }
    
    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        CompoundTag tagTankWater = tag.getCompound("TankWater");
        CompoundTag tagTankFilteredWater = tag.getCompound("TankFilteredWater");
        this.tankWater.fromTag(tagTankWater);
        this.tankFilteredWater.fromTag(tagTankFilteredWater);
    }
    
    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        super.toTag(tag);
        CompoundTag tagTankWater = new CompoundTag();
        CompoundTag tagTankFilteredWater = new CompoundTag();
        this.tankWater.toTag(tagTankWater);
        this.tankFilteredWater.toTag(tagTankFilteredWater);
        tag.put("TankWater", tagTankWater);
        tag.put("TankFilteredWater", tagTankFilteredWater);
        
        return tag;
    }
    
    @Override
    public void fromClientTag(CompoundTag tag)
    {
        super.fromTag(getCachedState(), tag);
        CompoundTag tagTankWater = tag.getCompound("TankWater");
        CompoundTag tagTankFilteredWater = tag.getCompound("TankFilteredWater");
        this.tankWater.fromTag(tagTankWater);
        this.tankFilteredWater.fromTag(tagTankFilteredWater);
    }
    
    @Override
    public CompoundTag toClientTag(CompoundTag tag)
    {
        super.toTag(tag);
        CompoundTag tagTankWater = new CompoundTag();
        CompoundTag tagTankFilteredWater = new CompoundTag();
        this.tankWater.toTag(tagTankWater);
        this.tankFilteredWater.toTag(tagTankFilteredWater);
        tag.put("TankWater", tagTankWater);
        tag.put("TankFilteredWater", tagTankFilteredWater);
    
        return tag;
    }
    
    public FluidTank getTankWater()
    {
        return tankWater;
    }
    
    public FluidTank getTankFilteredWater()
    {
        return tankFilteredWater;
    }
    
    @Override
    public void tick()
    {
        if(this.tankWater.getBuckets() == this.tankWater.getCapacity())
        {
            this.tankWater.drain(false);
            this.tankFilteredWater.drain(false);
        }
        this.tankWater.fill(50, false);
        this.tankFilteredWater.fill(50, false);
    }
}