package com.kingparity.betterpets.block.entity;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.kingparity.betterpets.core.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.world.World;

public class WaterCollectorTileEntity extends BaseTileEntity implements Tickable
{
    private static final int CAPACITY = 13 * FluidVolume.BUCKET;
    
    public final SimpleFixedFluidInv fluidInv;
    
    public WaterCollectorTileEntity()
    {
        super(ModTileEntities.WATER_COLLECTOR);
        fluidInv = new SimpleFixedFluidInv(1, CAPACITY);
    }
    
    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        tag = super.toTag(tag);
        FluidVolume invFluid = fluidInv.getInvFluid(0);
        if(!invFluid.isEmpty())
        {
            tag.put("fluid", invFluid.toTag());
        }
        return tag;
    }
    
    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        if(tag.contains("fluid"))
        {
            FluidVolume fluid = FluidVolume.fromTag(tag.getCompound("fluid"));
            fluidInv.setInvFluid(0, fluid, Simulation.ACTION);
        }
    }
    
    @Override
    public void tick()
    {
        if(!world.isClient)
        {
            System.out.println(this.fluidInv.getInvFluid(0).getAmount());
            if(this.fluidInv.getInvFluid(0).getAmount() < this.fluidInv.getMaxAmount(0))
            {
                if(world.isRaining())
                {
                    if(world.random.nextInt(10) == 1)
                    {
                        float temperature = world.getBiome(pos).getTemperature(pos);
                        if(!(temperature < 0.15F))
                        {
                            if(world.isThundering())
                            {
                                temperature += 1.0F;
                            }
                            //this.fluidInv.insertFluid(0, FluidKeys.WATER.withAmount(Math.round(500 * temperature)), Simulation.ACTION);
                            System.out.println("aaaaaaaaa");
                            this.fluidInv.insert(FluidKeys.WATER.withAmount(FluidAmount.of1620(Math.round(500 * temperature))));
                        }
                    }
                }
            }
        }
    
        World w = getWorld();
        if(w instanceof ServerWorld)
        {
            sendPacket((ServerWorld) w, this.toUpdatePacket());
        }
        else if(w != null)
        {
            w.scheduleBlockRerenderIfNeeded(getPos(), Blocks.AIR.getDefaultState(), getCachedState());
        }
        else
        {
            System.out.println("no");
        }
    }
}