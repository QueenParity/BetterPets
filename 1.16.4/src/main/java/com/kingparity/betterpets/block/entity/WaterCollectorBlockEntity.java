package com.kingparity.betterpets.block.entity;

import com.kingparity.betterpets.core.ModBlockEntityTypes;
import com.kingparity.betterpets.fluidtank.FluidTank;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class WaterCollectorBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable
{
    private FluidTank tankWater;
    
    public WaterCollectorBlockEntity()
    {
        super(ModBlockEntityTypes.WATER_COLLECTOR_BLOCK_ENTITY);
        this.tankWater = new FluidTank(Fluids.WATER, 0, 13000)
        {
            @Override
            protected void onContentsChanged()
            {
                WaterCollectorBlockEntity.this.markDirty();
                WaterCollectorBlockEntity.this.sync();
            }
        };
    }
    
    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        CompoundTag tagTankWater = tag.getCompound("TankWater");
        this.tankWater.fromTag(tagTankWater);
    }
    
    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        super.toTag(tag);
        CompoundTag tagTankWater = new CompoundTag();
        this.tankWater.toTag(tagTankWater);
        tag.put("TankWater", tagTankWater);
        
        return tag;
    }
    
    @Override
    public void fromClientTag(CompoundTag tag)
    {
        super.fromTag(getCachedState(), tag);
        CompoundTag tagTankWater = tag.getCompound("TankWater");
        this.tankWater.fromTag(tagTankWater);
    }
    
    @Override
    public CompoundTag toClientTag(CompoundTag tag)
    {
        super.toTag(tag);
        CompoundTag tagTankWater = new CompoundTag();
        this.tankWater.toTag(tagTankWater);
        tag.put("TankWater", tagTankWater);
    
        return tag;
    }
    
    public FluidTank getTankWater()
    {
        return tankWater;
    }
    
    @Override
    public void tick()
    {
        if(!world.isClient)
        {
            if(this.tankWater.getBuckets() < this.tankWater.getCapacity())
            {
                if(world.isRaining())
                {
                    if(world.random.nextInt(100) == 1)
                    {
                        float temperature = world.getBiome(pos).getTemperature(pos);
                        if(!(temperature < 0.15F))
                        {
                            if(world.isThundering())
                            {
                                temperature += 1.0F;
                            }
                            this.tankWater.fill(Math.round(500 * temperature), false);
                        }
                    }
                }
            }
        }
    }
}